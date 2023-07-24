package top.latke.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.latke.stream.DefaultSendService;
import top.latke.stream.admin.AdminSendService;
import top.latke.vo.AdminMessage;

/**
 * 构建消息驱动
 */
@Slf4j
@RestController
@RequestMapping("/message")
public class MessageController {

    private final DefaultSendService defaultSendService;
    private final AdminSendService adminSendService;

    public MessageController(DefaultSendService defaultSendService, AdminSendService adminSendService) {
        this.defaultSendService = defaultSendService;
        this.adminSendService = adminSendService;
    }

    /**
     * 默认信道
     */
    @GetMapping("/default")
    public void defaultSend() {
        defaultSendService.sendMessage(AdminMessage.defaultMessage());
    }

    /**
     * 自定义信道
     */
    @GetMapping("/admin")
    public void adminSend() {
        adminSendService.sendMessage(AdminMessage.defaultMessage());
    }

}
