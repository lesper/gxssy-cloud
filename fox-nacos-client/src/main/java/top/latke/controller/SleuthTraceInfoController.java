package top.latke.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.latke.service.SleuthTraceInfoService;

@Slf4j
@RestController
@RequestMapping("/sleuth")
public class SleuthTraceInfoController {

    private final SleuthTraceInfoService sleuthTraceInfoService;

    public SleuthTraceInfoController(SleuthTraceInfoService sleuthTraceInfoService) {
        this.sleuthTraceInfoService = sleuthTraceInfoService;
    }

    /**
     * 打印日志跟踪信息
     */
    @GetMapping("trace-info")
    public void logCurrentTraceInfo() {
        sleuthTraceInfoService.logCurrentTraceInfo();
    }
}
