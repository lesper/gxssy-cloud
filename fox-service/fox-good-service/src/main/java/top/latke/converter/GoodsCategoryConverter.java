package top.latke.converter;

import top.latke.constant.GoodsCategory;

import javax.persistence.AttributeConverter;

/**
 * 商品类别枚举属性转换器
 */
public class GoodsCategoryConverter implements AttributeConverter<GoodsCategory, String> {


    /**
     * 转换成可以存入数据表的基本类型
     *
     * @param goodsCategory
     * @return
     */
    @Override
    public String convertToDatabaseColumn(GoodsCategory goodsCategory) {
        return goodsCategory.getCode();
    }

    /**
     * 还原数据表中的字段值到 Java 数据类型
     *
     * @param status
     * @return
     */
    @Override
    public GoodsCategory convertToEntityAttribute(String code) {
        return GoodsCategory.of(code);
    }
}
