package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel("订单支付请求模版")
public class OrdersPaymentDTO implements Serializable {

    //订单号
    @ApiModelProperty(value = "订单号", required = true)
    @NotBlank(message = "订单号不能为空")
    private String orderNumber;

    //付款方式
    @ApiModelProperty(value = "付款方式", required = true)
    @NotNull(message = "付款方式不能为空")
    @Range(min = 1L, max = 2L, message = "付款方式错误")
    private Integer payMethod = 1;

}
