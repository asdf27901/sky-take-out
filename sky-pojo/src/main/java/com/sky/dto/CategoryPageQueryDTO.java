package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;

@Data
@ApiModel("分类查询模型")
public class CategoryPageQueryDTO implements Serializable {

    //页码
    @ApiModelProperty(value = "页码", required = true)
    private int page = 1;

    //每页记录数
    @ApiModelProperty(value = "每页记录数", required = true)
    private int pageSize = 10;

    //分类名称
    @ApiModelProperty("分类名称")
    private String name;

    //分类类型 1菜品分类  2套餐分类
    @ApiModelProperty(value = "分类类型", allowableValues = "1, 2")
    @Range(min = 1L, max = 2L, message = "分类类型错误")
    private Integer type;
}
