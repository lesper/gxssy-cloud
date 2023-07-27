package top.latke.source;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * 自定义物流消息通信信道
 */
public interface LogisticsSource {

    String OUTPUT = "logisticsOutput";

    @Output(LogisticsSource.OUTPUT)
    MessageChannel logisticsOutput();
}
