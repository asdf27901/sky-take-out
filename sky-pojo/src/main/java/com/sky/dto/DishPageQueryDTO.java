package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;

@Data
@ApiModel("菜品分页查询模版")
public class DishPageQueryDTO implements Serializable {

    @ApiModelProperty("页码")
    private int page = 1;

    @ApiModelProperty("每页数量")
    private int pageSize = 10;

    @ApiModelProperty("查询菜品名称")
    private String name;

    //分类id
    @ApiModelProperty("分类ID")
    private Integer categoryId;

    //状态 0表示禁用 1表示启用
    @ApiModelProperty("菜品状态")
    @Range(max = 1L, message = "菜品状态不合法")
    private Integer status;

}
