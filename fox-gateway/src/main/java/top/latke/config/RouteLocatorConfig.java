package top.latke.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置登录请求转发规则
 */

@Configuration
public class RouteLocatorConfig {

    /**
     * 使用代码定义路由规则，在网关层面拦截瞎登录和注册接口
     * @param routeLocatorBuilder
     * @return
     */
    @Bean
    public RouteLocator loginRouteLocator(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route("fox-authority",r -> r.path("/fox/login","/fox/register").uri("http://localhost:9001")
                ).build();
    }
}
