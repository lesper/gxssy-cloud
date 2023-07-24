package top.latke.stream.admin;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import top.latke.vo.AdminMessage;

/**
 * 使用自定义的通信信道 AdminSource 实现消息的发送
 */
@Slf4j
@EnableBinding(AdminSource.class)
public class AdminSendService {

    private final AdminSource adminSource;

    public AdminSendService(AdminSource adminSource) {
        this.adminSource = adminSource;
    }

    /**
     * 使用自定义的输出信道发送消息
     * @param adminMessage
     */
    public void sendMessage(AdminMessage adminMessage) {
        String _message = JSON.toJSONString(adminMessage);
        log.info("in AdminSendService send message: [{}]",_message);
        //统一消息编程模型，是 Stream 组件的重要组成部分之一
        adminSource.adminOutput().send(MessageBuilder.withPayload(_message).build());
    }
}
