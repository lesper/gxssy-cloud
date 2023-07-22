package top.latke.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.latke.service.communication.AuthorityFeignClient;
import top.latke.service.communication.UseRestTemplateService;
import top.latke.service.communication.UseRibbonService;
import top.latke.vo.JwtToken;
import top.latke.vo.UsernameAndPassword;

/**
 * 微服务通信
 */
@RestController
@RequestMapping("/communication")
public class CommunicationController {

    private final UseRestTemplateService useRestTemplateService;
    private final UseRibbonService useRibbonService;

    private final AuthorityFeignClient authorityFeignClient;

    public CommunicationController(UseRestTemplateService useRestTemplateService, UseRibbonService useRibbonService, AuthorityFeignClient authorityFeignClient) {
        this.useRestTemplateService = useRestTemplateService;
        this.useRibbonService = useRibbonService;
        this.authorityFeignClient = authorityFeignClient;
    }

    @PostMapping("/rest-template")
    public JwtToken getTokenFromAuthorityService(@RequestBody UsernameAndPassword usernameAndPassword) {
        return useRestTemplateService.getTokenFromAuthorityService(usernameAndPassword);
    }

    @PostMapping("/rest-template-load-balancer")
    public JwtToken getTokenFromAuthorityServiceWithloadBalancer(@RequestBody UsernameAndPassword usernameAndPassword) {
        return useRestTemplateService.getTokenFromAuthorityServiceWithloadBalancer(usernameAndPassword);
    }

    @PostMapping("/ribbon")
    public JwtToken getTokenFromAuthorityServiceByRibbon(@RequestBody UsernameAndPassword usernameAndPassword) {
        return useRibbonService.getTokenFromAuthorityServiceByRibbon(usernameAndPassword);
    }

    @PostMapping("/think-in-ribbon")
    public JwtToken thinkInRibbon(@RequestBody UsernameAndPassword usernameAndPassword) {
        return useRibbonService.thinkInRibbon(usernameAndPassword);
    }

    @PostMapping("/token-by-feign")
    public JwtToken getTokenByFeign(@RequestBody UsernameAndPassword usernameAndPassword) {
        return authorityFeignClient.getTokenByFeign(usernameAndPassword);
    }
}
