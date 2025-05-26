package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrderService {
    OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    void payment(OrdersPaymentDTO ordersPaymentDTO);

    PageResult<OrderVO> getHistoryOrders(OrdersPageQueryDTO ordersPageQueryDTO);

    OrderVO getUserOrderDetail(Long id);

    void repeatOrder(Long id);

    void userCancelOrder(Long id);

    PageResult<OrderVO> getOrderListByCondition(OrdersPageQueryDTO ordersPageQueryDTO);

    OrderVO getOrderDetail(Long id);

    void confirmOrder(Long id);

    OrderStatisticsVO statistics();

    void rejectOrder(OrdersRejectionDTO ordersRejectionDTO);

    void adminCancelOrder(OrdersCancelDTO ordersCancelDTO);

    void deliveryOrder(Long id);

    void completeOrder(Long id);

    void remindOrder(Long id);
}
