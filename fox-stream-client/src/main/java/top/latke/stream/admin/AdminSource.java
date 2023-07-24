package top.latke.stream.admin;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * 自定义输出信道
 */
public interface AdminSource {

    String OUTPUT = "adminOutput";

    /**
     * 输出信道的名称是 adminOutput，同时需要使用 Stream 绑定器在yml中声明
     * @return
     */
    @Output
    MessageChannel adminOutput();
}
