package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel("订单查询模型")
public class OrdersPageQueryDTO implements Serializable {

    @ApiModelProperty(value = "页码", required = true)
    @Range(min = 1, max = Integer.MAX_VALUE, message = "页码错误")
    private int page = 1;

    @ApiModelProperty(value = "分页数量", required = true)
    @Range(min = 1, max = Integer.MAX_VALUE, message = "分页数量错误")
    private int pageSize = 10;

    private String number;

    private  String phone;

    @ApiModelProperty(value = "订单状态", required = true)
    @Range(min = 1, max = 6, message = "订单状态错误")
    @NotNull(message = "订单状态不能为空")
    private Integer status;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime beginTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "用户ID", required = true)
    @NotNull(message = "用户ID不能为空")
    private Long userId;

}
