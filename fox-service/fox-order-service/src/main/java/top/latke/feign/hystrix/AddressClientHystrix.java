package top.latke.feign.hystrix;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.latke.account.AddressInfo;
import top.latke.common.TableId;
import top.latke.feign.AddressClient;
import top.latke.vo.CommonResponse;

import java.util.Collections;

/**
 * 账户服务熔断降级兜底策略
 */
@Slf4j
@Component
public class AddressClientHystrix implements AddressClient {

    @Override
    public CommonResponse<AddressInfo> getAddressInfoByTableId(TableId tableId) {
        log.error("account client feign request error in order service get address info error: [{}]", JSON.toJSONString(tableId));
        return new CommonResponse<>(-1,"account client feign request error in order service",new AddressInfo(-1L, Collections.emptyList()));
    }
}
