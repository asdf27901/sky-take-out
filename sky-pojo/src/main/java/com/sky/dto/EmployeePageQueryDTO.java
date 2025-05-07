package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("员工列表查询对象")
public class EmployeePageQueryDTO implements Serializable {

    //员工姓名
    @ApiModelProperty("员工姓名")
    private String name;

    //页码
    @ApiModelProperty("页码")
    private int page = 1;

    //每页显示记录数
    @ApiModelProperty("每页数量")
    private int pageSize = 10;

}
