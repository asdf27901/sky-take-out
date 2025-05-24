package com.sky.service.state;

import com.sky.entity.Orders;
import com.sky.enums.OrderEvent;
import com.sky.enums.OrderStatus;
import com.sky.exception.OrderBusinessException;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;

@Service
public class ToBeConfirmedState implements IOrderState<OrderStatus, OrderEvent>{

    @Override
    public void pay(Orders order, StateMachine<OrderStatus, OrderEvent> stateMachine) {
        throw new OrderBusinessException("订单已完成支付，不要重复付款");
    }
}
