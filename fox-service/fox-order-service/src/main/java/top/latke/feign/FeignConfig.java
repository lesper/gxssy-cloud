package top.latke.feign;

import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * Feign 调用时，把 Header 传递到服务提供方
 */
@Slf4j
@Configuration
public class FeignConfig {

    /**
     * 给 Feign 配置请求拦截器，RequestInterceptor 是提供给 openFeifn 的请求拦截器，透传 Header
     * @return
     */
    @Bean
    public RequestInterceptor headerInterceptor() {
        return template -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (null != attributes) {
                HttpServletRequest request = attributes.getRequest();
                Enumeration<String> headerNames = request.getHeaderNames();
                if (null != headerNames) {
                    while (headerNames.hasMoreElements()) {
                        String name = headerNames.nextElement();
                        String values = request.getHeader(name);
                        if (!name.equalsIgnoreCase("content-length")) {
                            template.header(name,values);
                        }
                    }
                }
            }
        };
    }

}
