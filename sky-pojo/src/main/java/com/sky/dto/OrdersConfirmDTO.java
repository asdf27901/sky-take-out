package com.sky.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class OrdersConfirmDTO implements Serializable {

    @ApiModelProperty("订单ID")
    @NotNull(message = "订单ID不能为空")
    private Long id;
    //订单状态 1待付款 2待接单 3 已接单 4 派送中 5 已完成 6 已取消 7 退款
    private Integer status;

}
