package top.latke.block_handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import top.latke.vo.CommonResponse;

/**
 * 自定义通用的限流处理逻辑
 */
@Slf4j
public class AdminBlockHandler {

    /**
     * 通用限流处理方法
     * @param exception
     * @return
     */
    public static CommonResponse<String> adminHandlerBlockException(BlockException exception) {
        log.error("trigger admin block handler: [{}], [{}]",
                JSON.toJSONString(exception.getRule()), exception.getRuleLimitApp());
        return new CommonResponse<>(
                -1,
                "flow rule trigger block exception",
                null
        );
    }
}
