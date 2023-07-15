package top.latke.filter;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.latke.constant.CommonConstant;
import top.latke.constant.GatewayConstant;
import top.latke.util.TokenParseUtil;
import top.latke.vo.JwtToken;
import top.latke.vo.LoginUserInfo;
import top.latke.vo.UsernameAndPassword;

import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;


/**
 * 全局登录鉴权过滤器
 */
@Slf4j
@Component
public class GlobalLoginOrRegisterFilter implements GlobalFilter, Ordered {

    /**
     * 注册中心客户端，可以从注册中心获取服务实例信息
     */
    private final LoadBalancerClient loadBalancerClient;
    private final RestTemplate restTemplate;

    public GlobalLoginOrRegisterFilter(LoadBalancerClient loadBalancerClient, RestTemplate restTemplate) {
        this.loadBalancerClient = loadBalancerClient;
        this.restTemplate = restTemplate;
    }


    /**
     * 登录、注册、鉴权
     * 1.如果是登录或注册，则去授权中心拿到 Token 并返回给客户端
     * 2.如果是访问其他服务，则鉴权，没有权限返回 401
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        ServerHttpResponse serverHttpResponse = exchange.getResponse();

        //如果是登录
        if (serverHttpRequest.getURI().getPath().contains(GatewayConstant.LOGIN_URI)) {
            //去授权中心拿 Token
            String token = getTokenFromAuthorityCenter(serverHttpRequest,GatewayConstant.AUTHORITY_CENTER_TOKEN_URI_FORMAT);
            //header 中不能设置 null
            serverHttpResponse.getHeaders().add(CommonConstant.JWT_USER_INFO_KEY,null == token ? "null" : token);
            serverHttpResponse.setStatusCode(HttpStatus.OK);
            return serverHttpResponse.setComplete();
        }

        //如果是注册
        if (serverHttpRequest.getURI().getPath().contains(GatewayConstant.REGISTER_URI)) {
            //去授权中心拿 Token,先创建用户，再返回 Token
            String token = getTokenFromAuthorityCenter(serverHttpRequest,GatewayConstant.AUTHORITY_CENTER_REGISTER_URI_FORMAT);
            //header 中不能设置 null
            serverHttpResponse.getHeaders().add(CommonConstant.JWT_USER_INFO_KEY,null == token ? "null" : token);
            serverHttpResponse.setStatusCode(HttpStatus.OK);
            return serverHttpResponse.setComplete();
        }

        //如果访问其他服务，则鉴权，校验是否能够从 Token 中解析出用户信息
        HttpHeaders headers = serverHttpRequest.getHeaders();
        String token = headers.getFirst(CommonConstant.JWT_USER_INFO_KEY);
        LoginUserInfo loginUserInfo = null;

        try {
            loginUserInfo = TokenParseUtil.parseUserInfoFromToken(token);
        }catch (Exception ex) {
            log.error("parse user info from token error: [{}]",ex.getMessage(),ex);
        }

        //鉴权失败
        if (null == loginUserInfo) {
            serverHttpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
            return serverHttpResponse.setComplete();
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 2;
    }

    /**
     * 从授权中心获取 Token
     * @param serverHttpRequest
     * @param uriFormat
     * @return
     */
    private String getTokenFromAuthorityCenter(ServerHttpRequest serverHttpRequest, String uriFormat) {
        //service id 就是服务名称，负载均衡
        ServiceInstance serviceInstance = loadBalancerClient.choose(CommonConstant.AUTHORITY_CENTER_SERVICE_ID);
        log.info("Nacos Client Info: [{}],[{}],[{}]",serviceInstance.getServiceId(),serviceInstance.getInstanceId(), JSON.toJSONString(serviceInstance.getMetadata()));
        String requestUrl = String.format(uriFormat,serviceInstance.getHost(),serviceInstance.getPort());
        UsernameAndPassword requestBody = JSON.parseObject(postBodyFromRequest(serverHttpRequest),UsernameAndPassword.class);
        log.info("login request url and body: [{}],[{}]",requestUrl,JSON.toJSONString(requestBody));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JwtToken token = restTemplate.postForObject(requestUrl,new HttpEntity<>(JSON.toJSONString(requestBody),headers), JwtToken.class);
        if (null != token) {
            return token.getToken();
        }
        return null;
    }

    /**
     * 从 Post 请求中获取到请求数据
     *
     * @param serverHttpRequest
     * @return
     */
    private String postBodyFromRequest(ServerHttpRequest serverHttpRequest) {
        //获取请求体
        Flux<DataBuffer> body = serverHttpRequest.getBody();
        AtomicReference<String> bodyRef = new AtomicReference<>();

        //订阅缓冲区去消费请求体中的数据
        body.subscribe(buffer -> {
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
            //一定要使用 DataBufferUtils.release 释放，否则会出现内存泄露
            DataBufferUtils.release(buffer);
            bodyRef.set(charBuffer.toString());
        });

        //获取 request body
        return bodyRef.get();
    }
}
