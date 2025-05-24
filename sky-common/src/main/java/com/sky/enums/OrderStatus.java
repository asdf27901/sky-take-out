package com.sky.enums;

import java.util.HashMap;
import java.util.Map;

public enum OrderStatus {
    PENDING_PAYMENT(1),  // 待付款
    TO_BE_CONFIRMED(2),  // 待接单
    CONFIRMED(3),  // 已接单
    DELIVERY_IN_PROGRESS(4),  // 派送中
    COMPLETED(5),  // 已完成
    CANCELLED(6);  // 已取消

    private static final Map<Integer, OrderStatus> STATE_MAP = new HashMap<>();

    static {
        for (OrderStatus orderStatus : OrderStatus.values()) {
            STATE_MAP.put(orderStatus.getState(), orderStatus);
        }
    }

    private final Integer state;

    OrderStatus(Integer state) {
        this.state = state;
    }

    public Integer getState() {
        return state;
    }

    public static OrderStatus fromState(Integer state) {
        return STATE_MAP.get(state);
    }
}
