package com.sky.dto;

import com.sky.entity.SetmealDish;
import com.sky.valid.groups.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel("套餐模版")
public class SetmealDTO implements Serializable {

    @ApiModelProperty("套餐ID")
    @NotNull(groups = Update.class, message = "套餐ID不能为空")
    private Long id;

    //分类id
    @ApiModelProperty(value = "分类ID", required = true)
    @NotNull(message = "分类ID不能为空")
    private Long categoryId;

    //套餐名称
    @ApiModelProperty(value = "套餐名称", required = true)
    @NotBlank(message = "套餐名称不能为空")
    private String name;

    //套餐价格
    @ApiModelProperty(value = "套餐价格", required = true)
    @NotNull(message = "套餐价格不能为空")
    @Range(message = "套餐价格错误")
    private BigDecimal price;

    //状态 0:停用 1:启用
    @ApiModelProperty(value = "套餐状态", allowableValues = "0, 1", required = true)
    @NotNull(message = "状态不能为空")
    @Range(max = 1L, message = "状态错误")
    private Integer status;

    //描述信息
    @ApiModelProperty("套餐描述")
    private String description;

    //图片
    @ApiModelProperty("套餐图片")
    @NotBlank(message = "套餐图片不能为空")
    private String image;

    //套餐菜品关系
    @Valid
    @NotEmpty(message = "套餐菜品不能为空")
    private List<SetmealDish> setmealDishes;

}
