package top.latke.constant;

/**
 * 商品类别枚举类
 */

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum GoodsCategory {

    DIAN_QI("10001", "电器"),
    JIA_JU("10002", "家居"),
    FU_SHI("10003", "服饰"),
    MU_YIN("10004", "母婴"),
    SHI_PIN("10005", "食品"),
    TU_SHU("10006", "图书"),;

    /**
     * 商品类别编码
     */
    private final String code;

    /**
     * 商品类别描述
     */
    private final String description;

    /**
     * 根据 code 获取 GoodsCategory
     *
     * @param code
     * @return
     */
    public static GoodsCategory of(String code) {
        Objects.requireNonNull(code);
        return Stream.of(values()).filter(
                        goodsCategory -> goodsCategory.code.equals(code)
                ).findAny()
                .orElseThrow(
                        () -> new IllegalArgumentException(code + "not exists")
                );
    }
}
