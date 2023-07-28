package top.latke.fallback_handler;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import top.latke.vo.JwtToken;
import top.latke.vo.UsernameAndPassword;

/**
 * Sentinel 回退降级的兜底策略
 * 都需要是静态方法
 * */
@Slf4j
public class AdminFallbackHandler {

    /**
     * getTokenFromAuthorityService 方法的 fallback
     * */
    public static JwtToken getTokenFromAuthorityServiceFallback(
            UsernameAndPassword usernameAndPassword
    ) {
        log.error("get token from authority service fallback: [{}]",
                JSON.toJSONString(usernameAndPassword));
        return new JwtToken("fox-fallback");
    }

    /**
     * ignoreException 方法的 fallback
     * */
    public static JwtToken ignoreExceptionFallback(Integer code) {
        log.error("ignore exception input code: [{}] has trigger exception", code);
        return new JwtToken("fox-fallback");
    }
}
