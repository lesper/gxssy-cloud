package top.latke.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.latke.vo.CommonResponse;

/**
 * Sentinel 对 OpenFeign 接口的降级策略
 * */
@Slf4j
@Component
public class SentinelFeignClientFallback implements SentinelFeignClient {

    @Override
    public CommonResponse<String> getResultByFeign(Integer code) {

        log.error("request supply for test has some error: [{}]", code);
        return new CommonResponse<>(
                -1,
                "sentinel feign fallback",
                "input code: "+ code
        );
    }
}
