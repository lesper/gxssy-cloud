package top.latke.conf;

import com.alibaba.cloud.sentinel.rest.SentinelClientHttpResponse;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import top.latke.vo.JwtToken;

/**
 * RestTemplate 在限流或异常时的兜底方法
 * */
@Slf4j
public class RestTemplateExceptionUtil {

    /**
     * 限流后的处理方法
     * */
    public static SentinelClientHttpResponse handleBlock(HttpRequest request,
                                                         byte[] body,
                                                         ClientHttpRequestExecution execution,
                                                         BlockException ex) {
        log.error("Handle RestTemplate Block Exception: [{}], [{}]",
                request.getURI().getPath(), ex.getClass().getCanonicalName());
        return new SentinelClientHttpResponse(
                JSON.toJSONString(new JwtToken("admin-block"))
        );
    }

    /**
     * 异常降级之后的处理方法
     * */
    public static SentinelClientHttpResponse handleFallback(HttpRequest request,
                                                            byte[] body,
                                                            ClientHttpRequestExecution execution,
                                                            BlockException ex) {
        log.error("Handle RestTemplate Fallback Exception: [{}], [{}]",
                request.getURI().getPath(), ex.getClass().getCanonicalName());
        return new SentinelClientHttpResponse(
                JSON.toJSONString(new JwtToken("admin-block"))
        );
    }
}
