package com.sky.mapper;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ShoppingCartMapper {

    ShoppingCart getShoppingCartItem(Long userId, ShoppingCartDTO shoppingCartDTO);

    @Update("update shopping_cart set number = #{number} where id = #{id}")
    int updateCartItem(ShoppingCart cartItem);

    @Insert("insert into shopping_cart (name, image, user_id, dish_id, setmeal_id, dish_flavor, number, amount, create_time) " +
            "values (#{name}, #{image}, #{userId}, #{dishId}, #{setmealId}, #{dishFlavor}, #{number}, #{amount}, #{createTime})")
    int saveItem(ShoppingCart cartItem);
}
