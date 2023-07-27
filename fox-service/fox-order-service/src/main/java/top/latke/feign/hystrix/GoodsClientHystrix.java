package top.latke.feign.hystrix;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.latke.common.TableId;
import top.latke.feign.SecuredGoodsClient;
import top.latke.goods.SimpleGoodsInfo;
import top.latke.vo.CommonResponse;

import java.util.Collections;
import java.util.List;

/**
 * 商品服务熔断降级兜底策略
 */
@Slf4j
@Component
public class GoodsClientHystrix implements SecuredGoodsClient {
    @Override
    public CommonResponse<List<SimpleGoodsInfo>> getSimpleGoodsInfoByTableId(TableId tableId) {
        log.error("goods client feign request error in order service get simple goods error: [{}]", JSON.toJSONString(tableId));
        return new CommonResponse<>(-1,"goods client feign request error in order service", Collections.emptyList());
    }
}
