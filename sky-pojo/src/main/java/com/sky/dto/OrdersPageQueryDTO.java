package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;
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

    @ApiModelProperty("订单号")
    private String number;

    @ApiModelProperty("手机号码")
    private String phone;

    @ApiModelProperty(value = "订单状态")
    @Range(min = 1, max = 6, message = "订单状态错误")
    private Integer status;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("下单时间")
    private LocalDateTime beginTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("下单时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

}
