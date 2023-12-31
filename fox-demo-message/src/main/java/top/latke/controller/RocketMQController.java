package top.latke.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.latke.rocket.RocketMQProducer;
import top.latke.vo.QinyiMessage;

/**
 * SpringBoot 集成 RocketMQ
 * */
@Slf4j
@RestController
@RequestMapping("/rocket-mq")
public class RocketMQController {

    private static final QinyiMessage RocketMQMessage = new QinyiMessage(
            1,
            "Qinyi-Study-RocketMQ-In-SpringBoot"
    );

    private final RocketMQProducer rocketMQProducer;

    public RocketMQController(RocketMQProducer rocketMQProducer) {
        this.rocketMQProducer = rocketMQProducer;
    }

    @GetMapping("/message-with-value")
    public void sendMessageWithValue() {
        rocketMQProducer.sendMessageWithValue(JSON.toJSONString(RocketMQMessage));
    }

    @GetMapping("/message-with-key")
    public void sendMessageWithKey() {
        rocketMQProducer.sendMessageWithKey("Qinyi", JSON.toJSONString(RocketMQMessage));
    }

    @GetMapping("/message-with-tag")
    public void sendMessageWithTag() {
        rocketMQProducer.sendMessageWithTag("qinyi",
                JSON.toJSONString(RocketMQMessage));
    }

    @GetMapping("/message-with-all")
    public void sendMessageWithAll() {
        rocketMQProducer.sendMessageWithAll("Qinyi", "qinyi",
                JSON.toJSONString(RocketMQMessage));
    }
}
