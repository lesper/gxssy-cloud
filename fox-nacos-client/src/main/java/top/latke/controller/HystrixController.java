package top.latke.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.latke.service.hystrix.UseHystrixCommandAndAnnotation;

import java.util.List;

/**
 * Hystrix Controller
 */
@Slf4j
@RestController
@RequestMapping("/hystrix")
public class HystrixController {

    private final UseHystrixCommandAndAnnotation useHystrixCommandAndAnnotation;

    public HystrixController(UseHystrixCommandAndAnnotation useHystrixCommandAndAnnotation) {
        this.useHystrixCommandAndAnnotation = useHystrixCommandAndAnnotation;
    }

    @GetMapping("hystrix-command-annotation")
    public List<ServiceInstance> get(@RequestParam String serviceId) {
        log.info("request nacos client info use annotation:[{}],[{}]",serviceId,Thread.currentThread().getName());
        return useHystrixCommandAndAnnotation.getNacosClientInfoFallback(serviceId);
    }
}
