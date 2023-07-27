package top.latke.feign.hystrix;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.latke.common.TableId;
import top.latke.feign.NotSecuredGoodsClient;
import top.latke.goods.DeductGoodsInventory;
import top.latke.goods.SimpleGoodsInfo;
import top.latke.vo.CommonResponse;

import java.util.List;

/**
 * open-feign 集成 Hystrix 时，且feign.hystrix.enabled: true, 则 Hystrix 超时时间仅为 1s,超过 1s 即报错 time-out.
 */
@Slf4j
@Component
public class FallBackFactory implements FallbackFactory<NotSecuredGoodsClient> {
    @Override
    public NotSecuredGoodsClient create(Throwable throwable) {
        log.warn("FallBackFactory : [{}]",throwable.getMessage(),throwable);
        return new NotSecuredGoodsClient() {
            @Override
            public CommonResponse<Boolean> deductGoodsInventory(List<DeductGoodsInventory> deductGoodsInventories) {
                return null;
            }

            @Override
            public CommonResponse<List<SimpleGoodsInfo>> getSimpleGoodsInfoByTableId(TableId tableId) {
                return null;
            }
        };
    }
}