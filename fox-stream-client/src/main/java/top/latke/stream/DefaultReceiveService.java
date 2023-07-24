package top.latke.stream;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import top.latke.vo.AdminMessage;

/**
 * 使用默认的通信信道实现消息的接收
 */
@Slf4j
@EnableBinding(Sink.class)
public class DefaultReceiveService {

    /**
     * 使用默认的输入信道接收消息
     * @param payload
     */
    @StreamListener(Sink.INPUT)
    public void receiveMessage(Object payload) {
        log.info("in DefaultReceiveService consume message start");
        AdminMessage adminMessage = JSON.parseObject(payload.toString(),AdminMessage.class);
        log.info("in DefaultReceiveService consume message success: [{}]",JSON.toJSONString(adminMessage));
    }
}
