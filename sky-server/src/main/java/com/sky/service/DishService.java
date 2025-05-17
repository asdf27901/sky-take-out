package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    PageResult<DishVO> getDishList(DishPageQueryDTO dishPageQueryDTO);

    boolean saveDish(DishDTO dishDTO);

    boolean deleteDishByIds(List<Long> ids);

    DishVO getDishVOById(Long id);

    boolean updateDish(DishDTO dishDTO);

    boolean updateDishStatus(Long id, Integer status);

    List<Dish> getDishListByCategoryId(Long categoryId);

    List<DishVO> getDishVoListByCategoryId(Long categoryId);
}
