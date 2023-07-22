package top.latke.service.communication;

import feign.Logger;
import feign.Request;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * open feign 配置类
 */
@Configuration
public class FeignConfig {

    /**
     * 开启 open feign 日志
     * @return
     */
    @Bean
    public Logger.Level feignLogger() {
        return Logger.Level.FULL;
    }

    /**
     * 开启 open feign 重试
     * @return
     */
    @Bean
    public Retryer feignRetryer() {
        return new Retryer.Default(
                100,
                SECONDS.toMillis(1),
                5
        );
    }

    public static final int CONNECT_TIMEOUT_MILLS = 5000;
    public static final int READ_TIMEOUT_MILLS = 5000;

    /**
     * 请求的链接和响应时间进行限制
     * @return
     */
    public Request.Options options() {
        return new Request.Options(CONNECT_TIMEOUT_MILLS,TimeUnit.MICROSECONDS,READ_TIMEOUT_MILLS,TimeUnit.MICROSECONDS,true);
    }

}
