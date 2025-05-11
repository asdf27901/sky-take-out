package com.sky.entity;

import com.sky.valid.groups.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 菜品口味
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("菜品口味")
public class DishFlavor implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("口味ID")
    private Long id;

    //菜品id
    @ApiModelProperty("菜品ID")
    private Long dishId;

    //口味名称
    @ApiModelProperty("口味名称")
    @NotBlank(message = "口味名称不能为空")
    private String name;

    //口味数据list
    @ApiModelProperty("口味数据")
    @NotBlank(message = "口味数据不能为空")
    private String value;

}
