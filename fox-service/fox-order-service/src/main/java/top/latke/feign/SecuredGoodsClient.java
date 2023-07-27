package top.latke.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import top.latke.common.TableId;
import top.latke.feign.hystrix.GoodsClientHystrix;
import top.latke.goods.SimpleGoodsInfo;
import top.latke.vo.CommonResponse;

import java.util.List;

/**
 * 安全的商品服务 Fiegn 接口定义
 */
@FeignClient(contextId = "SecuredGoodsClients", value = "fox-goods-service", fallbackFactory = GoodsClientHystrix.class)
public interface SecuredGoodsClient {

    /**
     * 根据 ids 查询简单商品信息
     *
     * @param tableId
     * @return
     */
    @RequestMapping(value = "/fox-goods-service/goods/simple-goods-info", method = RequestMethod.POST)
    CommonResponse<List<SimpleGoodsInfo>> getSimpleGoodsInfoByTableId(@RequestBody TableId tableId);

}
