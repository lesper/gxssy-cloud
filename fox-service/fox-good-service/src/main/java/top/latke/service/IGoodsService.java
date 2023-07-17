package top.latke.service;

import top.latke.common.TableId;
import top.latke.goods.DeductGoodsInventory;
import top.latke.goods.GoodsInfo;
import top.latke.goods.SimpleGoodsInfo;
import top.latke.vo.PageSimpleGoodsInfo;

import java.util.List;

/**
 * 商品微服务相关服务接口定义
 */
public interface IGoodsService {

    /**
     * 根据 TableId 查询商品详细信息
     * @param tableId
     * @return
     */
    List<GoodsInfo> getGoodsInfoByTableId(TableId tableId);

    /**
     * 获取分页的商品信息
     * @param page
     * @return
     */
    PageSimpleGoodsInfo getSimpleGoodsInfoByPage(int page);

    /**
     * 根据 TableId 查询简单商品信息
     * @param tableId
     * @return
     */
    List<SimpleGoodsInfo> getSimpleGoodsInfoByTableId(TableId tableId);

    /**
     * 扣减商品库存
     * @param deductGoodsInventories
     * @return
     */
    Boolean deductGoodsInventory(List<DeductGoodsInventory> deductGoodsInventories);
}
