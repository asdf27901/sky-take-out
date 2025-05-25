package com.sky.entity;

import com.sky.valid.groups.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 地址簿
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("收货地址")
public class AddressBook implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("收货地址ID")
    @NotNull(groups = Update.class, message = "地址ID不能为空")
    private Long id;

    //用户id
    @ApiModelProperty(value = "用户ID", hidden = true)
    private Long userId;

    //收货人
    @ApiModelProperty(value = "收货人姓名", required = true)
    @NotBlank(message = "收货人姓名不能为空")
    private String consignee;

    //手机号
    @ApiModelProperty(value = "手机号", required = true)
    @NotBlank(message = "手机号不能为空")
    private String phone;

    //性别 0 女 1 男
    @ApiModelProperty(value = "性别", required = true)
    @Pattern(regexp = "[0-1]", message = "性别不合法")
    private String sex = "1";

    //省级区划编号
    @ApiModelProperty(value = "省编号", required = true)
    @NotBlank(message = "provinceCode不能为空")
    private String provinceCode;

    //省级名称
    @ApiModelProperty(value = "省级名称", required = true)
    @NotBlank(message = "省级名称不能为空")
    private String provinceName;

    //市级区划编号
    @ApiModelProperty(value = "市编号", required = true)
    @NotBlank(message = "cityCode不能为空")
    private String cityCode;

    //市级名称
    @ApiModelProperty(value = "市级名称", required = true)
    @NotBlank(message = "市级名称不能为空")
    private String cityName;

    //区级区划编号
    @ApiModelProperty(value = "区编号", required = true)
    @NotBlank(message = "districtCode不能为空")
    private String districtCode;

    //区级名称
    @ApiModelProperty(value = "区级名称", required = true)
    @NotBlank(message = "区级名称不能为空")
    private String districtName;

    //详细地址
    @ApiModelProperty(value = "详细地址", required = true)
    @NotBlank(message = "详细地址不能为空")
    private String detail;

    //标签
    @ApiModelProperty("地址标签")
    private String label;

    //是否默认 0否 1是
    @ApiModelProperty(value = "是否为默认地址", required = true)
    @Range(max = 1L, message = "isDefault不合法")
    private Integer isDefault = 0;

    public String detailedAddress() {
        return provinceName + cityName + districtName + detail;
    }
}
