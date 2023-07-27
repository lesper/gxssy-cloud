package top.latke.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import top.latke.common.TableId;
import top.latke.goods.DeductGoodsInventory;
import top.latke.goods.SimpleGoodsInfo;
import top.latke.vo.CommonResponse;

import java.util.List;

/**
 * 用户商品服务 Feign 接口
 */
@FeignClient(contextId = "NotSecuredGoodsClient",value = "fox-goods-service")
public interface NotSecuredGoodsClient {

    /**
     * 扣减商品库存
     * @param deductGoodsInventories
     * @return
     */
    @RequestMapping(value = "/fox-goods-service/goods/deduct-goods-inventory",method = RequestMethod.PUT)
    CommonResponse<Boolean> deductGoodsInventory(@RequestBody List<DeductGoodsInventory> deductGoodsInventories);

    /**
     * 根据 ids 查询简单商品信息
     * @param tableId
     * @return
     */
    @RequestMapping(value = "fox-goods-service/goods/simple-goods-info",method = RequestMethod.POST)
    CommonResponse<List<SimpleGoodsInfo>> getSimpleGoodsInfoByTableId(@RequestBody TableId tableId);
}
