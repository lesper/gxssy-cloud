package top.latke.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import top.latke.vo.CommonResponse;

/**
 * 通过 Sentinel 对 OpenFeign 实现熔断降级
 * */
@FeignClient(
        value = "fox",
        fallback = SentinelFeignClientFallback.class
)
public interface SentinelFeignClient {

    @RequestMapping(value = "admin", method = RequestMethod.GET)
    CommonResponse<String> getResultByFeign(@RequestParam Integer code);
}
