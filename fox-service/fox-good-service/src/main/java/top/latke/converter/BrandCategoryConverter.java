package top.latke.converter;

import top.latke.constant.BrandCategory;

import javax.persistence.AttributeConverter;

/**
 * 商品品牌枚举枚举属性转换器
 */
public class BrandCategoryConverter implements AttributeConverter<BrandCategory, String> {


    /**
     * 转换成可以存入数据表的基本类型
     *
     * @param brandCategory
     * @return
     */
    @Override
    public String convertToDatabaseColumn(BrandCategory brandCategory) {
        return brandCategory.getCode();
    }

    /**
     * 还原数据表中的字段值到 Java 数据类型
     *
     * @param status
     * @return
     */
    @Override
    public BrandCategory convertToEntityAttribute(String status) {
        return BrandCategory.of(status);
    }
}
