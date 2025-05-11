package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SetMealDishMapper {
    Long[] getCountByDishIds(Long[] ids);
}
