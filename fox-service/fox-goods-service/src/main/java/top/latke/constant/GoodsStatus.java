package top.latke.constant;

/**
 * 商品状态枚举类
 */

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum GoodsStatus {

    ONLINE(101, "上线"),
    OFFLINE(102, "下线"),
    STOCK_OUT(103, "缺货");

    /**
     * 状态码
     */
    private final Integer status;

    /**
     * 状态描述
     */
    private final String description;

    /**
     * 根据 status 获取 GoodsStatus
     *
     * @param status
     * @return
     */
    public static GoodsStatus of(Integer status) {
        Objects.requireNonNull(status);
        return Stream.of(values()).filter(
                        goodsStatus -> goodsStatus.status.equals(status)
                ).findAny()
                .orElseThrow(
                        () -> new IllegalArgumentException(status + "not exists")
                );
    }
}
