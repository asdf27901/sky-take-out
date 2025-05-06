package com.sky.dto;

import com.sky.valid.groups.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@ApiModel("员工模型")
public class EmployeeDTO implements Serializable {

    @ApiModelProperty(value = "ID", required = false)
    @NotNull(groups = Update.class, message = "员工ID不能为空")
    private Long id;

    @ApiModelProperty(value = "登录账号", required = true)
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 8, message = "用户名长度应在 3 到 8 个字符之间")
    private String username;

    @ApiModelProperty(value = "用户名", required = true)
    @NotBlank(message = "姓名不能为空")
    @Size(min = 2, max = 4, message = "姓名长度应在 2 到 4 个字符之间")
    private String name;

    @ApiModelProperty(value = "手机号码", required = true)
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1(3[0-9]|5[0-3,5-9]|7[1-3,5-8]|8[0-9])\\d{8}$", message = "手机号码格式不正确")
    private String phone;

    @ApiModelProperty(value = "性别", allowableValues = "0, 1")
    @NotBlank(message = "性别不能为空")
    @Pattern(regexp = "[0-1]", message = "性别输入错误")
    private String sex;

    @ApiModelProperty("身份证号")
    @Pattern(regexp = "^[1-9][0-9]{16}[0-9xX]$", message = "身份证号格式有误")
    @NotBlank(message = "身份证号不能为空")
    private String idNumber;

}
