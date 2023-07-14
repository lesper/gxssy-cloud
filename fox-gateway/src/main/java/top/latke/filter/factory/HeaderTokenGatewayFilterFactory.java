package top.latke.filter.factory;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import top.latke.filter.HeaderTokenGatewayFilter;

/**
 * 使 HeaderTokenGatewayFilterFactory 局部过滤器生效,命名必须是 prefix + GatewayFilterFactory，否则会发生找不到的情况
 * 自定义过滤器才需要经过 AbstractGatewayFilterFactory 进行注册生效，而全局过滤器 GlobalFilter 不需要，会直接生效
 * 	<blockquote><pre>"filters": [{
 * 	"name": "HeaderToken"
 * 	 }]
 * 	</pre></blockquote>
 */
@Component
public class HeaderTokenGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {
    @Override
    public GatewayFilter apply(Object config) {
        return new HeaderTokenGatewayFilter();
    }
}
