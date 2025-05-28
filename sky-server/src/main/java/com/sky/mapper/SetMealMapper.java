package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoSet;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealOverViewVO;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetMealMapper {

    // 根据分类ID查找套餐
    @Select("select count(1) from setmeal where category_id = #{id}")
    int getCountByCategoryId(Long id);

    @Insert("insert into setmeal (category_id, name, price, status, description, image, create_time, update_time, create_user, update_user) " +
            "values (#{categoryId}, #{name}, #{price}, #{status}, #{description}, #{image}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @AutoSet(OperationType.INSERT)
    int saveSetMeal(Setmeal setmeal);

    @Select("select count(1) from setmeal where name = #{namd}")
    int getByMealName(String name);

    Page<SetmealVO> listAllSetMeal(SetmealPageQueryDTO setmealPageQueryDTO);

    SetmealVO getSetMealById(Long id);

    @AutoSet(OperationType.UPDATE)
    int updateSetMeal(Setmeal setmeal);

    List<Long> getSellingSetMealByIds(List<Long> ids);

    int deleteSetMealByIds(List<Long> ids);

    List<String> getSetMealImagesByIds(List<Long> ids);

    @Select("select * from setmeal where status = 1 and category_id = #{categoryId}")
    List<Setmeal> getSetMealListByCategoryId(Long categoryId);

    SetmealOverViewVO getAllStatusSetMealCount();
}
