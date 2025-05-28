package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.service.impl.ReportServiceImpl;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

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

    Page<OrderVO> getHistoryOrders(OrdersPageQueryDTO ordersPageQueryDTO);

    @Select("select * from orders where user_id = #{userId} and id = #{id}")
    Orders getOrderByOrderIdAndUserId(Long userId, Long id);

    @Select("select * from orders where id = #{id}")
    Orders getOrderByOrderId(Long id);

    @Select("select" +
            " (select count(1) from orders where status = 2) as to_be_confirmed," +
            " (select count(1) from orders where status = 3) as confirmed," +
            " (select count(1) from orders where status = 4) as delivery_in_progress"
    )
    OrderStatisticsVO statistics();

    @Select("SELECT * FROM orders WHERE order_time <= NOW() - INTERVAL 15 MINUTE AND status = 1")
    List<Orders> findTimeoutOrders();

    void updateOrderBatch(List<Orders> orders);

    @Select("SELECT * FROM orders WHERE order_time <= NOW() - INTERVAL 1 HOUR AND status = 4")
    List<Orders> existsOrdersWithStatus();

//    @Select("select ifnull(sum(amount), 0) from orders where DATE(checkout_time) = #{begin} and status = 5")
    List<ReportServiceImpl.TurnoverDate> getTurnoverList(LocalDate begin, LocalDate end);

    List<ReportServiceImpl.OrderDate> getOrdersStatistics(LocalDate begin, LocalDate end);

    List<ReportServiceImpl.SalesTop10> getSaleDishTop10(LocalDate begin, LocalDate end);

    List<ReportServiceImpl.SalesTop10> getSaleSetMealTop10(LocalDate begin, LocalDate end);

    OrderOverViewVO getAllStatusOrderCount();

    BusinessDataVO getBusinessData(LocalDate begin, LocalDate end);

    List<BusinessDataVO> getBusinessDataList(LocalDate begin, LocalDate end);
}
