package top.latke.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.latke.service.communication.UseRestTemplateService;
import top.latke.vo.JwtToken;
import top.latke.vo.UsernameAndPassword;

/**
 * 微服务通信
 */
@RestController
@RequestMapping("/communication")
public class CommunicationController {

    private final UseRestTemplateService useRestTemplateService;

    public CommunicationController(UseRestTemplateService useRestTemplateService) {
        this.useRestTemplateService = useRestTemplateService;
    }

    @PostMapping("/rest-template")
    public JwtToken getTokenFromAuthorityService(@RequestBody UsernameAndPassword usernameAndPassword) {
        return useRestTemplateService.getTokenFromAuthorityService(usernameAndPassword);
    }

    @PostMapping("/rest-template-load-balancer")
    public JwtToken getTokenFromAuthorityServiceWithloadBalancer(@RequestBody UsernameAndPassword usernameAndPassword) {
        return useRestTemplateService.getTokenFromAuthorityServiceWithloadBalancer(usernameAndPassword);
    }
}
