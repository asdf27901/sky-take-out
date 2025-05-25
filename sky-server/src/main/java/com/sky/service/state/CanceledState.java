package com.sky.service.state;

import com.sky.entity.Orders;
import com.sky.enums.OrderEvent;
import com.sky.enums.OrderStatus;
import com.sky.exception.OrderBusinessException;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;

@Service
public class CanceledState implements IOrderState<OrderStatus, OrderEvent> {
    @Override
    public void pay(Orders order, StateMachine<OrderStatus, OrderEvent> stateMachine) {
        throw new OrderBusinessException("订单已取消，不要重复付款！");
    }

    @Override
    public void userCancel(Orders order, StateMachine<OrderStatus, OrderEvent> stateMachine) {
        throw new OrderBusinessException("订单已取消，不要重复取消！");
    }

    @Override
    public void confirmOrder(Orders order, StateMachine<OrderStatus, OrderEvent> stateMachine) {
        throw new OrderBusinessException("订单已取消，不要重复接单！");
    }

    @Override
    public void adminCancel(Orders order, StateMachine<OrderStatus, OrderEvent> stateMachine, String cancelReason) {
        throw new OrderBusinessException("订单已取消，不要重复操作！");
    }

    @Override
    public void delivery(Orders order, StateMachine<OrderStatus, OrderEvent> stateMachine) {
        throw new OrderBusinessException("订单已取消，不要重复操作！");
    }

    @Override
    public void complete(Orders order, StateMachine<OrderStatus, OrderEvent> stateMachine) {
        throw new OrderBusinessException("订单已取消，不要重复操作！");
    }
}
