package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetMealDishMapper {
    Long[] getCountByDishIds(Long[] ids);

    void saveSetmealDish(List<SetmealDish> setmealDishes);
}
