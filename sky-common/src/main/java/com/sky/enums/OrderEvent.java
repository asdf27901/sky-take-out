package com.sky.enums;

public enum OrderEvent {
    PAY,  // 付款
    CONFIRMED,  // 接单
    DELIVERY,  // 派送
    RECEIVE,  // 确认收货
    USER_CANCEL,  // 用户取消
    ADMIN_CANCEL // 后台取消
}
