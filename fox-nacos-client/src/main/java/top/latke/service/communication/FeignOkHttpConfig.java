package top.latke.service.communication;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * open feign 使用 OkHttp 配置类
 */
@Configuration
@ConditionalOnClass(FeignConfig.class)
@AutoConfigureBefore(FeignAutoConfiguration.class)
public class FeignOkHttpConfig {

    @Bean
    public okhttp3.OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)    // 设置连接超时
                .readTimeout(5, TimeUnit.SECONDS)   // 设置读超时
                .writeTimeout(5, TimeUnit.SECONDS)  // 设置写超时
                .retryOnConnectionFailure(true)     // 是否自动重连
                // 配置连接池中的最大空闲线程个数为 10, 并保持 5 分钟
                .connectionPool(new ConnectionPool(
                        10, 5L, TimeUnit.MINUTES))
                .build();
    }

}
