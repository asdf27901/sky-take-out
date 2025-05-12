package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoSet;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {

    // 根据分类ID查找菜品
    @Select("select count(1) from dish where category_id = #{id}")
    int getCountByCategoryId(Long id);

    Page<DishVO> getDishVoList(DishPageQueryDTO dishPageQueryDTO);

    @Select("select * from dish where name = #{dishName}")
    Dish getDishByDishName(String dishName);

    @AutoSet(OperationType.INSERT)
    @Insert("insert into dish (name, category_id, price, image, description, create_time, update_time, create_user, update_user) " +
            "values (#{name}, #{categoryId}, #{price}, #{image}, #{description}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int saveDish(Dish dish);

    List<Long> getSellingDishListByIds(List<Long> ids);

    int deleteByIds(List<Long> ids);

    DishVO getDishVOById(Long id);

    @Select("select * from dish where id = #{id}")
    Dish getDishById(Long id);

    @AutoSet(OperationType.UPDATE)
    int updateDish(Dish dish);

    @Select("select * from dish where category_id = #{categoryId} and status = 1")
    List<Dish> getDishListByCategoryId(Long categoryId);
}
