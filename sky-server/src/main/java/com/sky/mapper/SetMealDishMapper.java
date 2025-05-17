package com.sky.mapper;

import com.sky.entity.SetmealDish;
import com.sky.vo.DishItemVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetMealDishMapper {
    List<Long> getCountByDishIds(List<Long> ids);

    void saveSetmealDishBatch(List<SetmealDish> setmealDishes);

    @Delete("delete from setmeal_dish where setmeal_id = #{id}")
    void delSetMealDishById(Long id);

    void delSetMealDishByIds(List<Long> ids);

    List<DishItemVO> getDishListBySetMealId(Long id);
}
