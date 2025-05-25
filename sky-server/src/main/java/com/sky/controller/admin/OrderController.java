package com.sky.controller.admin;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("AdminOrderController")
@Slf4j
@Api(tags = "订单管理相关接口")
@Validated
@RequestMapping("/admin/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/conditionSearch")
    @ApiOperation("订单搜素")
    public Result<PageResult<OrderVO>> getOrderListByCondition(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("后台订单管理分页查询，{}", ordersPageQueryDTO);
        PageResult<OrderVO> orderVOPageResult = orderService.getOrderListByCondition(ordersPageQueryDTO);
        return Result.success(orderVOPageResult);
    }
}
