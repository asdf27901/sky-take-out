package com.sky.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class OrdersRejectionDTO implements Serializable {

    @ApiModelProperty(value = "订单ID", required = true)
    @NotNull(message = "订单ID不能为空")
    private Long id;

    //订单拒绝原因
    @ApiModelProperty(value = "拒绝原因", required = true)
    @NotNull(message = "拒绝原因不能为空")
    private String rejectionReason;

}
