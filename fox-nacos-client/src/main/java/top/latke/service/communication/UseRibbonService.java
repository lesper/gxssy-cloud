package top.latke.service.communication;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import top.latke.constant.CommonConstant;
import top.latke.vo.JwtToken;
import top.latke.vo.UsernameAndPassword;

/**
 * 使用 Ribbon 实现微服务通信
 */
@Slf4j
@Service
public class UseRibbonService {

    public final RestTemplate restTemplate;

    public UseRibbonService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
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
}
