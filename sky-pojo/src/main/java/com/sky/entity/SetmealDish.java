package com.sky.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 套餐菜品关系
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("套餐内菜品")
public class SetmealDish implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    //套餐id
    private Long setmealId;

    //菜品id
    @ApiModelProperty(value = "菜品ID", required = true)
    @NotNull(message = "菜品ID不能为空")
    private Long dishId;

    //菜品名称 （冗余字段）
    private String name;

    //菜品原价
    @ApiModelProperty(value = "菜品原价", required = true)
    @Range(message = "菜品原价不合法")
    private BigDecimal price;

    //份数
    @ApiModelProperty(value = "菜品份数", required = true)
    @Range(min = 1L, message = "菜品份数不合法")
    private Integer copies;
}
