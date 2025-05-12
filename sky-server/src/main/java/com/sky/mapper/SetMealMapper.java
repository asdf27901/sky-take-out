package com.sky.mapper;

import com.sky.entity.Setmeal;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SetMealMapper {

    // 根据分类ID查找套餐
    @Select("select count(1) from setmeal where category_id = #{id}")
    int getCountByCategoryId(Long id);

    @Insert("insert into setmeal (category_id, name, price, status, description, image, create_time, update_time, create_user, update_user) " +
            "values (#{categoryId}, #{name}, #{price}, #{status}, #{description}, #{image}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int saveSetMeal(Setmeal setmeal);

    @Select("select count(1) from setmeal where name = #{namd}")
    int getByMealName(String name);
}
