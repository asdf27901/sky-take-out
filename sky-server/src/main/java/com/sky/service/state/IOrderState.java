package com.sky.service.state;

import com.sky.entity.Orders;
import org.springframework.statemachine.StateMachine;

public interface IOrderState<S, E> {
    void pay(Orders order, StateMachine<S, E> stateMachine);

    void cancel(Orders order, StateMachine<S, E> stateMachine);
}
