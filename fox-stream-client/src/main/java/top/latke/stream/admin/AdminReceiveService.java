package top.latke.stream.admin;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import top.latke.vo.AdminMessage;

/**
 * 使用自定义的通入信道 AdminSink实现消息的发送
 */
@Slf4j
@EnableBinding(AdminSink.class)
public class AdminReceiveService {

    /**
     * 使用自定义的输入信道接收消息
     * @param payload
     */
    @StreamListener(AdminSink.INPUT)
    public void receiveMessage(Object payload) {
        log.info("in AdminReceiveService consume message start");
        AdminMessage adminMessage = JSON.parseObject(payload.toString(),AdminMessage.class);
        log.info("in AdminReceiveService consume message success: [{}]",JSON.toJSONString(adminMessage));
    }
}
