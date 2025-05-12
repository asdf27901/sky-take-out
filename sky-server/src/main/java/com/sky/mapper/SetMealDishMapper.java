package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetMealDishMapper {
    List<Long> getCountByDishIds(List<Long> ids);

    void saveSetmealDishBatch(List<SetmealDish> setmealDishes);
}
