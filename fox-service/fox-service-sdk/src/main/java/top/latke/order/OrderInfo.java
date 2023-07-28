package top.latke.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.latke.goods.DeductGoodsInventory;

import java.util.List;

/**
 * 订单信息
 * */
@ApiModel(description = "用户发起购买订单")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfo {

    @ApiModelProperty(value = "用户地址表主键 id")
    private Long userAddress;

    @ApiModelProperty(value = "订单中的商品信息")
    private List<OrderItem> orderItems;

    /**
     * 订单中的商品信息
     * */
    @ApiModel(description = "订单中的单项商品信息")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItem {

        @ApiModelProperty(value = "商品表主键 id")
        private Long goodsId;

        @ApiModelProperty(value = "购买商品个数")
        private Integer count;

        public DeductGoodsInventory toDeductGoodsInventory() {
            return new DeductGoodsInventory(this.goodsId, this.count);
        }
    }
}
