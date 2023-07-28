package top.latke.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.latke.feign.SentinelFeignClient;
import top.latke.vo.CommonResponse;

/**
 * OpenFeign 集成 Sentinel 实现熔断降级
 * */
@Slf4j
@RestController
@RequestMapping("/sentinel-feign")
public class SentinelFeignController {

    private final SentinelFeignClient sentinelFeignClient;

    public SentinelFeignController(SentinelFeignClient sentinelFeignClient) {
        this.sentinelFeignClient = sentinelFeignClient;
    }

    /**
     * 通过 Feign 接口去获取结果
     * */
    @GetMapping("/result-by-feign")
    public CommonResponse<String> getResultByFeign(@RequestParam Integer code) {
        log.info("coming in get result by feign: [{}]", code);
        return sentinelFeignClient.getResultByFeign(code);
    }
}
