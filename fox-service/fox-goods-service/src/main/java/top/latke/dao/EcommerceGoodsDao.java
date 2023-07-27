package top.latke.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import top.latke.constant.BrandCategory;
import top.latke.constant.GoodsCategory;
import top.latke.entity.EcommerceGoods;

import java.util.Optional;

/**
 * EcommerceGoods Dao 接口定义
 */
public interface EcommerceGoodsDao extends PagingAndSortingRepository<EcommerceGoods, Long> {

    /**
     * 根据查询条件查询商品表,并限制返回结果
     * @param goodsCategory
     * @param brandCategory
     * @param goodsName
     * @return
     */
    Optional<EcommerceGoods> findFirst1ByGoodsCategoryAndBrandCategoryAndGoodsName(GoodsCategory goodsCategory, BrandCategory brandCategory,String goodsName);

}
