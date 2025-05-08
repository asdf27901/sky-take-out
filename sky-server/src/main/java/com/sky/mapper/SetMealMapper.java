package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SetMealMapper {

    // 根据分类ID查找套餐
    @Select("select count(1) from setmeal where category_id = #{id}")
    int getCountByCategoryId(Long id);
}
