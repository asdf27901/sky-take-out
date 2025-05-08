package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DishMapper {

    // 根据分类ID查找菜品
    @Select("select count(1) from dish where category_id = #{id}")
    int getCountByCategoryId(Long id);
}
