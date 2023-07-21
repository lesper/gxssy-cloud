package top.latke.service.communication;

import com.alibaba.fastjson.JSON;
import com.netflix.loadbalancer.*;
import com.netflix.loadbalancer.reactive.LoadBalancerCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rx.Observable;
import top.latke.constant.CommonConstant;
import top.latke.vo.JwtToken;
import top.latke.vo.UsernameAndPassword;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用 Ribbon 实现微服务通信
 */
@Slf4j
@Service
public class UseRibbonService {

    public final RestTemplate restTemplate;
    private final DiscoveryClient discoveryClient;

    public UseRibbonService(RestTemplate restTemplate, DiscoveryClient discoveryClient) {
        this.restTemplate = restTemplate;
        this.discoveryClient = discoveryClient;
    }


    /**
     * 通过 Ribbon 调用服务
     * @param usernameAndPassword
     * @return
     */
    public JwtToken getTokenFromAuthorityServiceByRibbon(UsernameAndPassword usernameAndPassword) {
        String requestUrl = String.format("http://%s/fox-authority-center/authority/token", CommonConstant.AUTHORITY_CENTER_SERVICE_ID);
        log.info("login request url and body: [{}],[{}]",requestUrl, JSON.toJSONString(usernameAndPassword));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return restTemplate.postForObject(
                requestUrl,
                new HttpEntity<>(JSON.toJSONString(usernameAndPassword),headers),
                JwtToken.class
        );
    }

    /**
     * 原生 Ribbon 示例
     * @param usernameAndPassword
     * @return
     */
    public JwtToken thinkInRibbon(UsernameAndPassword usernameAndPassword) {
        String urlFormat = "http://%s/fox-authority-center/authority/token";
        List<ServiceInstance> targetInstances = discoveryClient.getInstances(CommonConstant.AUTHORITY_CENTER_SERVICE_ID);
        List<Server> servers = new ArrayList<>(targetInstances.size());
        targetInstances.forEach(i -> {
            servers.add(new Server(i.getHost(),i.getPort()));
            log.info("found target instance:[{}],[{}]",i.getHost(),i.getPort());
        });
        BaseLoadBalancer baseLoadBalancer = LoadBalancerBuilder.newBuilder()
                .buildFixedServerListLoadBalancer(servers);
        baseLoadBalancer.setRule(new RetryRule(new RandomRule(),300));
        String result = LoadBalancerCommand.builder()
                .withLoadBalancer(baseLoadBalancer)
                .build()
                .submit(server -> {
                    String targetUrl = String.format(urlFormat,String.format("%s:%s",server.getHost(),server.getPort()));
                    log.info("target request url:[{}]",targetUrl);
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    String tokenStr = new RestTemplate().postForObject(
                            targetUrl,
                            new HttpEntity<>(JSON.toJSONString(usernameAndPassword),headers),
                            String.class
                    );
                    return Observable.just(tokenStr);
                }).toBlocking().first().toString();
        return JSON.parseObject(result, JwtToken.class);
    }
}
