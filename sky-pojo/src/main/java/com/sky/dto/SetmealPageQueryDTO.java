package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;

@Data
@ApiModel("套餐分页查询模版")
public class SetmealPageQueryDTO implements Serializable {

    @ApiModelProperty(value = "页码", required = true)
    private int page = 1;

    @ApiModelProperty(value = "每页记录数", required = true)
    private int pageSize = 10;

    @ApiModelProperty("套餐名称")
    private String name;

    //分类id
    @ApiModelProperty("分类ID")
    private Integer categoryId;

    //状态 0表示禁用 1表示启用
    @ApiModelProperty(value = "套餐起售状态", allowableValues = "0, 1")
    @Range(max = 1L, message = "套餐起售状态错误")
    private Integer status;

}
