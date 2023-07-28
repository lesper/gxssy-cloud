package top.latke.service.impl;

import com.alibaba.fastjson.JSON;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.latke.account.AddressInfo;
import top.latke.account.BalanceInfo;
import top.latke.common.TableId;
import top.latke.dao.EcommerceOrderDao;
import top.latke.entity.EcommerceOrder;
import top.latke.feign.AddressClient;
import top.latke.feign.NotSecuredBalanceClient;
import top.latke.feign.NotSecuredGoodsClient;
import top.latke.feign.SecuredGoodsClient;
import top.latke.filter.AccessContext;
import top.latke.goods.DeductGoodsInventory;
import top.latke.goods.SimpleGoodsInfo;
import top.latke.order.LogisticsMessage;
import top.latke.order.OrderInfo;
import top.latke.service.IOrderService;
import top.latke.source.LogisticsSource;
import top.latke.vo.PageSimpleOrderDetail;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 订单相关服务接口实现
 */
@Slf4j
@Service
@EnableBinding(LogisticsSource.class)
public class OrderviceImpl implements IOrderService {

    private final EcommerceOrderDao ecommerceOrderDao;
    private final AddressClient addressClient;
    private final SecuredGoodsClient securedGoodsClient;
    private final NotSecuredGoodsClient notSecuredGoodsClient;
    private final NotSecuredBalanceClient notSecuredBalanceClient;
    private final LogisticsSource logisticsSource;

    public OrderviceImpl(EcommerceOrderDao ecommerceOrderDao,
                         AddressClient addressClient,
                         SecuredGoodsClient securedGoodsClient,
                         NotSecuredGoodsClient notSecuredGoodsClient,
                         NotSecuredBalanceClient notSecuredBalanceClient,
                         LogisticsSource logisticsSource) {
        this.ecommerceOrderDao = ecommerceOrderDao;
        this.addressClient = addressClient;
        this.securedGoodsClient = securedGoodsClient;
        this.notSecuredGoodsClient = notSecuredGoodsClient;
        this.notSecuredBalanceClient = notSecuredBalanceClient;
        this.logisticsSource = logisticsSource;
    }

    /**
     * 创建订单: 这里会涉及到分布式事务
     * 创建订单会涉及到多个步骤和校验, 当不满足情况时直接抛出异常;
     * 1. 校验请求对象是否合法
     * 2. 创建订单
     * 3. 扣减商品库存
     * 4. 扣减用户余额
     * 5. 发送订单物流消息 SpringCloud Stream + Kafka
     */
    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public TableId createOrder(OrderInfo orderInfo) {
        //获取地址信息
        AddressInfo addressInfo = addressClient.getAddressInfoByTableId(
                new TableId(Collections.singletonList(new TableId.Id(orderInfo.getUserAddress())))
        ).getData();
        //校验请求对象是否合法（商品信息不会校验，扣减库存时才校验）
        if (CollectionUtils.isEmpty(addressInfo.getAddressItems())) {
            throw new RuntimeException("user address is not exist: " + orderInfo.getUserAddress());
        }
        //创建订单
        EcommerceOrder newOrder = ecommerceOrderDao.save(
                new EcommerceOrder(
                        AccessContext.getLoginUserInfo().getId(), orderInfo.getUserAddress(), JSON.toJSONString(orderInfo.getOrderItems())
                )
        );
        log.info("create order success: [{}],[{}]",AccessContext.getLoginUserInfo().getId(),newOrder.getId());
        //扣减商品库存
        if (!notSecuredGoodsClient.deductGoodsInventory(
                orderInfo.getOrderItems()
                        .stream()
                        .map(OrderInfo.OrderItem::toDeductGoodsInventory)
                        .collect(Collectors.toList())).getData()
        ){
            throw new RuntimeException("deduct goods inventory failure");
        }
        //扣减用户账户余额
        //获取商品信息，计算总价
        List<SimpleGoodsInfo> goodsInfos = notSecuredGoodsClient.getSimpleGoodsInfoByTableId(
                new TableId(
                        orderInfo.getOrderItems()
                                .stream()
                                .map(orderItem -> new TableId.Id((orderItem.getGoodsId())))
                                .collect(Collectors.toList()))
        ).getData();
        Map<Long,SimpleGoodsInfo> goodsId2GoodsInfo = goodsInfos.stream()
                .collect(Collectors.toMap(SimpleGoodsInfo::getId, Function.identity()));
        //计算价格
        long balance = 0;
        for (OrderInfo.OrderItem orderItem : orderInfo.getOrderItems()) {
            balance += goodsId2GoodsInfo.get(orderItem.getGoodsId()).getPrice() * orderItem.getCount();
        }
        assert balance > 0;
        //填写总价，扣减账户余额
        BalanceInfo balanceInfo = notSecuredBalanceClient.deductBalance(new BalanceInfo(AccessContext.getLoginUserInfo().getId(), balance)).getData();
        if (null == balanceInfo) {
            throw new RuntimeException("deduct user balance failure");
        }
        log.info("deduct user balance: [{}],[{}]",newOrder.getId(),JSON.toJSONString(balanceInfo));
        //发送物流订单信息
        LogisticsMessage logisticsMessage = new LogisticsMessage(AccessContext.getLoginUserInfo().getId(), newOrder.getId(),orderInfo.getUserAddress(),null);
        if (!logisticsSource.logisticsOutput().send(MessageBuilder.withPayload(JSON.toJSONString(logisticsMessage)).build())) {
            throw new RuntimeException("send logistics message failure");
        }
        log.info("send create order message to kafka with stream: [{}]",JSON.toJSONString(logisticsMessage));
        //返回订单id
        return new TableId(Collections.singletonList(new TableId.Id(newOrder.getId())));
    }

    @Override
    public PageSimpleOrderDetail getSimpleOrderDetailByPage(int page) {
        if (page <= 0) {
            page = 1;
        }
        Pageable pageable = PageRequest.of(page - 1,10, Sort.by("id").descending());
        Page<EcommerceOrder> orderPage = ecommerceOrderDao.findAllByUserId(AccessContext.getLoginUserInfo().getId(), pageable);
        List<EcommerceOrder> orders = orderPage.getContent();
        if (CollectionUtils.isEmpty(orders)) {
            return new PageSimpleOrderDetail(Collections.emptyList(),false);
        }

        //获取订单中所有的 goodsId
        Set<Long> goodsInOrders = new HashSet<>();
        orders.forEach(order -> {
            List<DeductGoodsInventory> goodsAndCount = JSON.parseArray(order.getOrderDetail(), DeductGoodsInventory.class);
            goodsInOrders.addAll(goodsAndCount.stream().map(DeductGoodsInventory::getGoodsId).collect(Collectors.toList()));
        });

        assert !CollectionUtils.isEmpty(goodsInOrders);
        boolean hasMore = orderPage.getTotalPages() > page;

        //获取商品信息
        List<SimpleGoodsInfo> goodsInfos = securedGoodsClient.getSimpleGoodsInfoByTableId(
                new TableId(
                        goodsInOrders.stream()
                                .map(TableId.Id::new)
                                .collect(Collectors.toList())
                )
        ).getData();
        //获取地址信息
        AddressInfo addressInfo = addressClient.getAddressInfoByTableId(
                new TableId(
                        orders.stream()
                                .map(order -> new TableId.Id(order.getAddressId()))
                                .distinct()
                                .collect(Collectors.toList())
                )
        ).getData();
        //组装订单中的商品，地址信息 -> 订单信息并返回
        return new PageSimpleOrderDetail(assembleSimpleOrderDetail(orders,goodsInfos,addressInfo),hasMore);
    }

    /**
     * 组装订单详情
     * @return
     */
    private List<PageSimpleOrderDetail.SingleOrderItem> assembleSimpleOrderDetail(
            List<EcommerceOrder> orders,
            List<SimpleGoodsInfo> goodsInfos,
            AddressInfo addressInfo) {

        Map<Long,SimpleGoodsInfo> id2GoodsInfo = goodsInfos.stream()
                .collect(
                        Collectors.toMap(SimpleGoodsInfo::getId,Function.identity())
                );
        Map<Long,AddressInfo.AddressItem> id2AddressItem = addressInfo.getAddressItems().stream()
                .collect(
                        Collectors.toMap(AddressInfo.AddressItem::getId,Function.identity())
                );

        List<PageSimpleOrderDetail.SingleOrderItem> result = new ArrayList<>(orders.size());
        orders.forEach(order -> {
            PageSimpleOrderDetail.SingleOrderItem orderItem = new PageSimpleOrderDetail.SingleOrderItem();
            orderItem.setId(order.getId());
            orderItem.setUserAddress(id2AddressItem.getOrDefault(order.getAddressId(),new AddressInfo.AddressItem(-1L)).toUserAddress());
            orderItem.setGoodsItems(buildOrderGoodsItem(order,id2GoodsInfo));
            result.add(orderItem);
        });
        return result;
    }

    /**
     * 构造订单中的商品信息
     * @param order
     * @param id2GoodsInfo
     * @return
     */
    private List<PageSimpleOrderDetail.SingleOrderGoodsItem> buildOrderGoodsItem(EcommerceOrder order,Map<Long,SimpleGoodsInfo> id2GoodsInfo) {
        List<PageSimpleOrderDetail.SingleOrderGoodsItem> goodsItems = new ArrayList<>();
        List<DeductGoodsInventory> goodsAndCount = JSON.parseArray(order.getOrderDetail(), DeductGoodsInventory.class);
        goodsAndCount.forEach(gc -> {
            PageSimpleOrderDetail.SingleOrderGoodsItem goodsItem = new PageSimpleOrderDetail.SingleOrderGoodsItem();
            goodsItem.setCount(gc.getCount());
            goodsItem.setSimpleGoodsInfo(id2GoodsInfo.getOrDefault(gc.getGoodsId(),new SimpleGoodsInfo(-1L)));
            goodsItems.add(goodsItem);
        });
        return goodsItems;
    }

}
