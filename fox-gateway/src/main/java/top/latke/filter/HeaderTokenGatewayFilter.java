package top.latke.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Http 请求头部携带 Token 验证过滤器,局部(GatewayFilter),全局(GlobalFilter)
 */
public class HeaderTokenGatewayFilter implements GatewayFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 从 Http Header 中寻找指定 Token
        String name = exchange.getRequest().getHeaders().getFirst("token");
        if ("fox".equals(name)) {
            return chain.filter(exchange);
        }

        // 标记此次请求无权限，并结束请求
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
