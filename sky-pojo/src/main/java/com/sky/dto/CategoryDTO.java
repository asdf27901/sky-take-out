package com.sky.dto;

import com.sky.valid.groups.Add;
import com.sky.valid.groups.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel("分类模版")
public class CategoryDTO implements Serializable {

    //主键
    @ApiModelProperty("分类ID")
    @NotNull(groups = Update.class, message = "分类ID不能为空")
    private Long id;

    //类型 1 菜品分类 2 套餐分类
    @ApiModelProperty(value = "类型", allowableValues = "1, 2", required = true)
    @Range(min = 1L, max = 2L, message = "分类类型有误，请输入1或2")
    @NotNull(groups = Add.class, message = "类型不能为空")
    private Integer type;

    //分类名称
    @ApiModelProperty(value = "分类名称", required = true)
    @NotBlank(message = "分类名称不能为空")
    private String name;

    //排序
    @ApiModelProperty(value = "排序值", required = true)
    @NotNull(message = "排序值不能为空")
    private Integer sort;

}
