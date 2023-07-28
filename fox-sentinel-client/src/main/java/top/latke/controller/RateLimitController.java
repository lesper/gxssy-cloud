package top.latke.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.latke.block_handler.AdminBlockHandler;
import top.latke.vo.CommonResponse;

/**
 * 基于 Sentinel 控制台配置流控规则，Sentinel 是懒加载的，所以需要先访问一下 controller 方法
 */
@Slf4j
@RestController
@RequestMapping("/dashboard")
public class RateLimitController {

    /**
     * 在 dashboard 中 "流控规则" 中按照资源名称新增流控规则
     * */
    @GetMapping("/by-resource")
    @SentinelResource(
            value = "byResource",
            blockHandler = "adminHandlerBlockException",
            blockHandlerClass = AdminBlockHandler.class
    )
    public CommonResponse<String> byResource() {
        log.info("coming in rate limit controller by resource");
        return new CommonResponse<>(0, "", "byResource");
    }

    /**
     * 在 "簇点链路" 中给 url 添加流控规则
     * */
    @GetMapping("/by-url")
    @SentinelResource(value = "byUrl")
    public CommonResponse<String> byUrl() {
        log.info("coming in rate limit controller by url");
        return new CommonResponse<>(0, "", "byUrl");
    }
}
