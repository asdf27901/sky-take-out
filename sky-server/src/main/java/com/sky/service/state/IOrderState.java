package com.sky.service.state;

import com.sky.entity.Orders;
import org.springframework.statemachine.StateMachine;

public interface IOrderState<S, E> {
    void pay(Orders order, StateMachine<S, E> stateMachine);

    void userCancel(Orders order, StateMachine<S, E> stateMachine);

    void confirmOrder(Orders order, StateMachine<S, E> stateMachine);

    void adminCancel(Orders order, StateMachine<S, E> stateMachine, String cancelReason);

    void delivery(Orders order, StateMachine<S, E> stateMachine);

    void complete(Orders order, StateMachine<S, E> stateMachine);
}
