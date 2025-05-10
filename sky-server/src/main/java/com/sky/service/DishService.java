package com.sky.service;

import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;

public interface DishService {
    PageResult<Dish> getDishList(DishPageQueryDTO dishPageQueryDTO);
}
