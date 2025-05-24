package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderMapper {

    @Insert("INSERT INTO orders (" +
            "number, user_id, address_book_id, order_time, checkout_time, pay_method, amount, remark, " +
            "user_name, phone, address, consignee, cancel_reason, rejection_reason, cancel_time, " +
            "estimated_delivery_time, delivery_status, delivery_time, pack_amount, tableware_number, tableware_status" +
            ") VALUES (" +
            "#{number}, #{userId}, #{addressBookId}, #{orderTime}, #{checkoutTime}, #{payMethod}, #{amount}, #{remark}, " +
            "#{userName}, #{phone}, #{address}, #{consignee}, #{cancelReason}, #{rejectionReason}, #{cancelTime}, " +
            "#{estimatedDeliveryTime}, #{deliveryStatus}, #{deliveryTime}, #{packAmount}, #{tablewareNumber}, #{tablewareStatus}" +
            ")")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Orders order);

    @Select("select * from orders where user_id = #{userId} and number = #{orderNumber}")
    Orders getOrderByOrderNumber(Long userId, String orderNumber);

    void update(Orders order);
}
