package top.latke.service;

import top.latke.common.TableId;
import top.latke.order.OrderInfo;
import top.latke.vo.PageSimpleOrderDetail;

/**
 * 订单相关服务接口定义
 */
public interface IOrderService {

    /**
     * 下单（分布式事务），创建订单 -> 扣减库存 -> 扣减余额 -> 创建物流信息（Stream + Kafka）
     * @param orderInfo
     * @return
     */
    TableId createOrder(OrderInfo orderInfo);

    /**
     * 获取当前用户的订单信息，带有分页
     * @param page
     * @return
     */
    PageSimpleOrderDetail getPageSimpleOrderDetailByPage(int page);
}
