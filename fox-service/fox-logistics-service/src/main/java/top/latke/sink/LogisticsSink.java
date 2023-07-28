package top.latke.sink;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * 自定义物流信息接收器(Sink)
 * */
public interface LogisticsSink {

    /** 输入信道名称 */
    String INPUT = "logisticsInput";

    /**
     * 物流 Sink -> logisticsInput
     * */
    @Input(LogisticsSink.INPUT)
    SubscribableChannel logisticsInput();
}
