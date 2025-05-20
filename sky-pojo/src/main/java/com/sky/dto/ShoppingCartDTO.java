package com.sky.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;

@Data
public class ShoppingCartDTO implements Serializable {

    @Range(message = "dishId错误")
    private Long dishId;
    @Range(message = "setmealId错误")
    private Long setmealId;
    private String dishFlavor;

}
