package top.latke.service;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import top.latke.dao.EcommerceLogisticsDao;
import top.latke.entity.EcommerceLogistics;
import top.latke.order.LogisticsMessage;
import top.latke.sink.LogisticsSink;

/**
 * 物流服务实现
 */
@Slf4j
@EnableBinding(LogisticsSink.class)
public class EcommerceLogisticsImpl {

    private final EcommerceLogisticsDao ecommerceLogisticsDao;

    public EcommerceLogisticsImpl(EcommerceLogisticsDao ecommerceLogisticsDao) {
        this.ecommerceLogisticsDao = ecommerceLogisticsDao;
    }

    /**
     * 订阅监听订单微服务发送的物流消息
     * */
    @StreamListener("logisticsInput")
    public void consumeLogisticsMessage(@Payload Object payload) {

        log.info("receive and consume logistics message: [{}]", payload.toString());
        LogisticsMessage logisticsMessage = JSON.parseObject(
                payload.toString(), LogisticsMessage.class
        );
        EcommerceLogistics ecommerceLogistics = ecommerceLogisticsDao.save(
                new EcommerceLogistics(
                        logisticsMessage.getUserId(),
                        logisticsMessage.getOrderId(),
                        logisticsMessage.getAddressId(),
                        logisticsMessage.getExtraInfo()
                )
        );
        log.info("consume logistics message success: [{}]", ecommerceLogistics.getId());
    }
}
