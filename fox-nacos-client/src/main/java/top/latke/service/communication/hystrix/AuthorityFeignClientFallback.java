package top.latke.service.communication.hystrix;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.latke.service.communication.AuthorityFeignClient;
import top.latke.vo.JwtToken;
import top.latke.vo.UsernameAndPassword;

/**
 *  AuthorityFeignClient 后备 Fallback
 */
@Slf4j
@Component
public class AuthorityFeignClientFallback implements AuthorityFeignClient {
    @Override
    public JwtToken getTokenByFeign(UsernameAndPassword usernameAndPassword) {
        log.info("authority feign client get token by feign request error(Hystrix Fallback): [{}]", JSON.toJSONString(usernameAndPassword));
        return new JwtToken("admin");
    }
}
