package top.latke.stream;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import top.latke.vo.AdminMessage;

/**
 * 使用默认的通信信道实现消息的发送
 */
@Slf4j
@EnableBinding(Source.class)
public class DefaultSendService {

    private final Source source;

    public DefaultSendService(Source source) {
        this.source = source;
    }

    /**
     * 使用默认的输出信道发送消息
     * @param adminMessage
     */
    public void sendMessage(AdminMessage adminMessage) {
        String _message = JSON.toJSONString(adminMessage);
        log.info("in DefaultSendService send message: [{}]",_message);
        //统一消息编程模型，是 Stream 组件的重要组成部分之一
        source.output().send(MessageBuilder.withPayload(_message).build());
    }
}
