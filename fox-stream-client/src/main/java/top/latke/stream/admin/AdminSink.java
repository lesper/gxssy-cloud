package top.latke.stream.admin;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * 自定义输入信道
 */
public interface AdminSink {

    String INPUT = "adminInput";

    /**
     * 输入信道的名称是 adminInput，同时需要使用 Stream 绑定器在yml中声明
     * @return
     */
    @Input(AdminSink.INPUT)
    SubscribableChannel adminInput();
}
