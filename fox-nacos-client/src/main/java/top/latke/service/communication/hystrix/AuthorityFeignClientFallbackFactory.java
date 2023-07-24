package top.latke.service.communication.hystrix;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.latke.service.communication.AuthorityFeignClient;
import top.latke.vo.JwtToken;
import top.latke.vo.UsernameAndPassword;

/**
 *  AuthorityFeignClient 后备 Fallback-Factory-OpenFeign 实现
 */
@Slf4j
@Component
public class AuthorityFeignClientFallbackFactory implements FallbackFactory<AuthorityFeignClient> {
    @Override
    public AuthorityFeignClient create(Throwable throwable) {
        log.warn("authority feign client get token by feign request error(Hystrix FallbackFactory: [{}]",throwable.getMessage(),throwable);
        return new AuthorityFeignClient() {
            @Override
            public JwtToken getTokenByFeign(UsernameAndPassword usernameAndPassword) {
                return new JwtToken("admin-factory");
            }
        };
    }
}
