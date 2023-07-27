package top.latke.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.latke.common.TableId;
import top.latke.constant.GoodsConstant;
import top.latke.dao.EcommerceGoodsDao;
import top.latke.entity.EcommerceGoods;
import top.latke.goods.DeductGoodsInventory;
import top.latke.goods.GoodsInfo;
import top.latke.goods.SimpleGoodsInfo;
import top.latke.service.IGoodsService;
import top.latke.vo.PageSimpleGoodsInfo;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 商品微服务相关服务接口实现
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class GoodsServiceImpl implements IGoodsService {

    private final StringRedisTemplate stringRedisTemplate;
    private final EcommerceGoodsDao ecommerceGoodsDao;

    public GoodsServiceImpl(StringRedisTemplate stringRedisTemplate, EcommerceGoodsDao ecommerceGoodsDao) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.ecommerceGoodsDao = ecommerceGoodsDao;
    }

    @Override
    public List<GoodsInfo> getGoodsInfoByTableId(TableId tableId) {
        List<Long> ids = tableId.getIds().stream().map(TableId.Id::getId).collect(Collectors.toList());
        log.info("get goods info by ids: [{}]", JSON.toJSONString(ids));

        List<EcommerceGoods> ecommerceGoods = IterableUtils.toList(ecommerceGoodsDao.findAllById(ids));

        return ecommerceGoods.stream().map(EcommerceGoods::toGoodsInfo).collect(Collectors.toList());
    }

    @Override
    public PageSimpleGoodsInfo getSimpleGoodsInfoByPage(int page) {
        if (page <= 1) {
            page = 1;
        }

        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by("id").descending());
        Page<EcommerceGoods> ecommerceGoodsPage = ecommerceGoodsDao.findAll(pageable);
        boolean hasMore = ecommerceGoodsPage.getTotalPages() > page;
        return new PageSimpleGoodsInfo(
                ecommerceGoodsPage.getContent()
                        .stream()
                        .map(EcommerceGoods::toSimple)
                        .collect(Collectors.toList()), hasMore
        );
    }

    @Override
    public List<SimpleGoodsInfo> getSimpleGoodsInfoByTableId(TableId tableId) {
        List<Object> goodIds = tableId.getIds().stream().map(id -> id.getId().toString()).collect(Collectors.toList());
        List<Object> cachedSimpleGoodsInfos = stringRedisTemplate.opsForHash()
                .multiGet(GoodsConstant.ECOMMERCE_GOODS_DICT_KEY, goodIds)
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
                ;
        if (CollectionUtils.isNotEmpty(cachedSimpleGoodsInfos)) {
            if (cachedSimpleGoodsInfos.size() == goodIds.size()) {
                log.info("get simple goods info by ids (from cache): [{}]", JSON.toJSONString(goodIds));
                return parseCachedGoodsInfo(cachedSimpleGoodsInfos);
            } else {
                List<SimpleGoodsInfo> left = parseCachedGoodsInfo(cachedSimpleGoodsInfos);
                //取差集
                Collection<Long> subtractIds = CollectionUtils.subtract(
                        goodIds.stream().map(g -> Long.valueOf(g.toString())).collect(Collectors.toList()),
                        left.stream().map(SimpleGoodsInfo::getId).collect(Collectors.toList())
                );
                //缓存中没有的去查询数据表
                List<SimpleGoodsInfo> right = queryGoodsFromDBAndCacheToRedis(
                        new TableId(
                                subtractIds.stream()
                                        .map(TableId.Id::new)
                                        .collect(Collectors.toList())
                        )
                );

                log.info("get simple goods ingo by ids (from db and cache): [{}]", JSON.toJSONString(subtractIds));
                return new ArrayList<>(CollectionUtils.union(left, right));
            }
        } else {
            //全量从数据库查询
            return queryGoodsFromDBAndCacheToRedis(tableId);
        }
    }

    /**
     * 将缓存中的数据反序列化成 Java Pojo 对象
     *
     * @param cacheSimpleGoodsInfo
     * @return
     */
    private List<SimpleGoodsInfo> parseCachedGoodsInfo(List<Object> cacheSimpleGoodsInfo) {
        return cacheSimpleGoodsInfo.stream()
                .map(object -> JSON.parseObject(object.toString(), SimpleGoodsInfo.class))
                .collect(Collectors.toList());
    }

    /**
     * 从数据表中查询数据，并缓存至 Redis 中
     *
     * @param tableId
     * @return
     */
    private List<SimpleGoodsInfo> queryGoodsFromDBAndCacheToRedis(TableId tableId) {
        List<Long> ids = tableId.getIds().stream().map(TableId.Id::getId).collect(Collectors.toList());
        log.info("get simple goods info by ids (from db): [{}]", JSON.toJSONString(ids));

        List<EcommerceGoods> ecommerceGoods = IterableUtils.toList(ecommerceGoodsDao.findAllById(ids));
        List<SimpleGoodsInfo> simpleGoodsInfos = ecommerceGoods.stream()
                .map(EcommerceGoods::toSimple)
                .collect(Collectors.toList());

        log.info("cache simple goods info by ids: [{}]", JSON.toJSONString(ids));

        Map<String, String> id2JsonObject = new HashMap<>(simpleGoodsInfos.size());
        simpleGoodsInfos.forEach(simpleGoodsInfo -> id2JsonObject.put(simpleGoodsInfo.getId().toString(), JSON.toJSONString(simpleGoodsInfo)));
        stringRedisTemplate.opsForHash().putAll(GoodsConstant.ECOMMERCE_GOODS_DICT_KEY, id2JsonObject);

        return simpleGoodsInfos;
    }

    @Override
    public Boolean deductGoodsInventory(List<DeductGoodsInventory> deductGoodsInventories) {
        deductGoodsInventories.forEach(deductGoodsInventory -> {
            if (deductGoodsInventory.getCount() <= 0) {
                throw new RuntimeException("purchase goods count need > 0");
            }
        });

        List<EcommerceGoods> ecommerceGoods = IterableUtils.toList(
                ecommerceGoodsDao.findAllById(
                        deductGoodsInventories.stream()
                                .map(DeductGoodsInventory::getGoodsId)
                                .collect(Collectors.toList())
                )
        );

        if (CollectionUtils.isEmpty(ecommerceGoods)) {
            throw new RuntimeException("can not found any goods by request");
        }

        if (ecommerceGoods.size() != deductGoodsInventories.size()) {
            throw new RuntimeException("request is not vaild");
        }

        Map<Long, DeductGoodsInventory> goodsInventoryMap = deductGoodsInventories.stream()
                .collect(Collectors.toMap(DeductGoodsInventory::getGoodsId, Function.identity()));

        ecommerceGoods.forEach(ecommerceGoods1 -> {
            Long currentInventory = ecommerceGoods1.getInventory();
            Integer needDeductInventory = goodsInventoryMap.get(ecommerceGoods1.getId()).getCount();

            if (currentInventory < needDeductInventory) {
                log.error("goods inventory is not enough: [{}],[{}]", currentInventory, needDeductInventory);
                throw new RuntimeException("goods inventory is not enough: " + ecommerceGoods1.getId());
            }

            ecommerceGoods1.setInventory(currentInventory - needDeductInventory);
            log.info("deduct goods inventory: [{}],[{}],[{}]", ecommerceGoods1.getId(), currentInventory, ecommerceGoods1.getInventory());
        });

        ecommerceGoodsDao.saveAll(ecommerceGoods);
        log.info("deduct goods inventory done");

        return true;
    }
}
