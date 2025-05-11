package com.sky.dto;

import com.sky.entity.DishFlavor;
import com.sky.valid.groups.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel("菜品模版")
public class DishDTO implements Serializable {

    @ApiModelProperty("菜品id")
    @NotNull(groups = Update.class, message = "菜品ID不能为空")
    private Long id;

    //菜品名称
    @ApiModelProperty("菜品名称")
    @NotBlank(message = "菜品名称不能为空")
    private String name;

    //菜品分类id
    @ApiModelProperty("菜品分类ID")
    @NotNull(message = "菜品分类ID不能为空")
    private Long categoryId;

    //菜品价格
    @ApiModelProperty("菜品价格")
    @NotNull(message = "菜品价格不能为空")
    @Range(message = "菜品价格不合法")
    private BigDecimal price;

    //图片
    @ApiModelProperty("菜品图片")
    @NotBlank(message = "菜品图片不能为空")
    private String image;

    //描述信息
    @ApiModelProperty("菜品描述")
    private String description;

    //0 停售 1 起售
    @ApiModelProperty(value = "菜品状态", allowableValues = "1,0")
    @Range(max = 1L, message = "菜品状态不合法")
    private Integer status;

    //口味
    @ApiModelProperty("菜品口味")
    @Valid
    private List<DishFlavor> flavors = new ArrayList<>();

}
