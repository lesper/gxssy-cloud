package top.latke.service.communication;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import top.latke.constant.CommonConstant;
import top.latke.vo.JwtToken;
import top.latke.vo.UsernameAndPassword;

/**
 * 使用 RestTemplate 实现微服务通信
 */
@Slf4j
@Service
public class UseRestTemplateService {

    private final LoadBalancerClient loadBalancerClient;

    public UseRestTemplateService(LoadBalancerClient loadBalancerClient) {
        this.loadBalancerClient = loadBalancerClient;
    }

    /**
     * Example RestTemplate Authority
     * @param usernameAndPassword
     * @return
     */
    public JwtToken getTokenFromAuthorityService(UsernameAndPassword usernameAndPassword) {
        String requestUrl = "http://127.0.0.1:7000/fox-authority-center/authority/token";

        //first
        log.info("RestTemplate request url and body: [{}],[{}]",requestUrl, JSON.toJSONString(usernameAndPassword));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new RestTemplate().postForObject(
                requestUrl,
                new HttpEntity<>(JSON.toJSONString(usernameAndPassword),headers),
                JwtToken.class
        );
    }

    /**
     * 从授权服务中获取,并带有负载均衡
     * @param usernameAndPassword
     * @return
     */
    public JwtToken getTokenFromAuthorityServiceWithloadBalancer(UsernameAndPassword usernameAndPassword) {
        ServiceInstance serviceInstance = loadBalancerClient.choose(CommonConstant.AUTHORITY_CENTER_SERVICE_ID);
        log.info("Nacos Client info: [{}],[{}],[{}]",serviceInstance.getServiceId(),serviceInstance.getInstanceId(),JSON.toJSONString(serviceInstance.getMetadata()));

        String requestUrl = String.format("http://%s:%s/fox-authority-center/authority/token",serviceInstance.getHost(),serviceInstance.getPort());

        log.info("login request url and body: [{}],[{}]",requestUrl,JSON.toJSONString(usernameAndPassword));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new RestTemplate().postForObject(
                requestUrl,
                new HttpEntity<>(JSON.toJSONString(usernameAndPassword),headers),
                JwtToken.class
        );
    }



}
