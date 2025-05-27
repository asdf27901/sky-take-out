package com.sky.service.impl;

import com.sky.exception.BusinessException;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        DateRange dateRange = checkDate(begin, end);
        begin = dateRange.getBegin();
        end = dateRange.getEnd();

        List<TurnoverDate> turnoverDates = orderMapper.getTurnoverList(begin, end);
        // 构建TurnoverReportVO中的dateList字符串
        StringJoiner dateList = new StringJoiner(",");
        // 构建TurnoverReportVO中的turnoverList字符串
        StringJoiner turnoverList = new StringJoiner(",");

        for (TurnoverDate turnover : turnoverDates) {
            dateList.add(turnover.date.toString());
            turnoverList.add(turnover.totalAmount.toString());
        }

        return TurnoverReportVO.builder()
                .dateList(dateList.toString())
                .turnoverList(turnoverList.toString())
                .build();
    }

    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        DateRange dateRange = checkDate(begin, end);
        begin = dateRange.getBegin();
        end = dateRange.getEnd();

        StringJoiner dateList = new StringJoiner(",");
        StringJoiner totalUserList = new StringJoiner(",");
        StringJoiner newUserList = new StringJoiner(",");

        List<UserDate> userDateList = userMapper.getUserStatistics(begin, end);
        for (UserDate userDate : userDateList) {
            dateList.add(userDate.date.toString());
            newUserList.add(userDate.dailyNewUsers.toString());
            totalUserList.add(userDate.cumulativeUsers.toString());
        }

        return UserReportVO.builder()
                .dateList(dateList.toString())
                .totalUserList(totalUserList.toString())
                .newUserList(newUserList.toString())
                .build();
    }

    @Override
    public OrderReportVO getOrdersStatistics(LocalDate begin, LocalDate end) {
        DateRange dateRange = checkDate(begin, end);
        begin = dateRange.getBegin();
        end = dateRange.getEnd();

        StringJoiner dateList = new StringJoiner(",");
        StringJoiner orderCountList = new StringJoiner(",");
        StringJoiner validOrderCountList = new StringJoiner(",");
        BigDecimal totalOrderCount = BigDecimal.ZERO;
        BigDecimal validOrderCount = BigDecimal.ZERO;

        List<OrderDate> orderDateList = orderMapper.getOrdersStatistics(begin, end);
        for (OrderDate orderDate : orderDateList) {
            totalOrderCount = totalOrderCount.add(new BigDecimal(orderDate.dailyNewOrders));
            validOrderCount = validOrderCount.add(new BigDecimal(orderDate.dailyEffectiveNewOrders));
            dateList.add(orderDate.date.toString());
            orderCountList.add(orderDate.dailyNewOrders.toString());
            validOrderCountList.add(orderDate.dailyEffectiveNewOrders.toString());
        }
        // 保留4位小数，四舍五入
        // 使用BigDecimal计算避免出现精度丢失的情况
        BigDecimal orderCompletionRate = validOrderCount.divide(totalOrderCount, 4, RoundingMode.HALF_UP);
        return OrderReportVO.builder()
                .dateList(dateList.toString())
                .totalOrderCount(totalOrderCount.intValue())
                .validOrderCount(validOrderCount.intValue())
                .orderCompletionRate(orderCompletionRate.doubleValue())
                .orderCountList(orderCountList.toString())
                .validOrderCountList(validOrderCountList.toString())
                .build();
    }

    @Override
    public SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end) {
        DateRange dateRange = checkDate(begin, end);
        begin = dateRange.getBegin();
        end = dateRange.getEnd();

        StringJoiner nameList = new StringJoiner(",");
        StringJoiner numberList = new StringJoiner(",");

        // 获取菜品前10
        List<SalesTop10> salesTop10DishList = orderMapper.getSaleDishTop10(begin, end);
        List<SalesTop10> salesTop10SetMealList = orderMapper.getSaleSetMealTop10(begin, end);

        Map<String, Integer> map = new HashMap<>();
        salesTop10DishList.forEach(item -> {
            map.put(item.name, item.count);
        });
        salesTop10SetMealList.forEach(item -> {
            map.put(item.name, item.count);
        });
        List<Map.Entry<String, Integer>> sorted10 = new ArrayList<>(map.entrySet());
        sorted10.sort((o1, o2) -> o2.getValue() - o1.getValue());

        for (int i = 0; i < sorted10.size(); i++) {
            if (i == 10) {
                break;
            }
            nameList.add(sorted10.get(i).getKey());
            numberList.add(sorted10.get(i).getValue().toString());
        }
        System.out.println(sorted10);

        return SalesTop10ReportVO.builder()
                .nameList(nameList.toString())
                .numberList(numberList.toString())
                .build();
    }

    private DateRange checkDate(LocalDate begin, LocalDate end) {
        if (begin == null && end == null) {
            throw new BusinessException("范围过大，请限定时间范围");
        }
        if (begin == null) {
            // 如果开始日期为空，默认按照end逆推30天
            begin = end.minusDays(29);
        }
        if (end == null) {
            // 如果开始日期为空，默认按照begin顺推30天
            end = begin.plusDays(29);
        }
        if (begin.isAfter(end)) {
            throw new BusinessException("时间选择错误");
        }
        if (ChronoUnit.DAYS.between(begin, end) > 30) {
            throw new BusinessException("时间超出30天，暂不支持查询");
        }
        return new DateRange(begin, end);
    }

    @Data
    static class DateRange {
        private final LocalDate begin;
        private final LocalDate end;
    }

    @Data
    public static class TurnoverDate {
        private LocalDate date;
        private Double totalAmount;
    }

    @Data
    public static class UserDate {
        private LocalDate date;
        private Integer dailyNewUsers;
        private Integer cumulativeUsers;
    }

    @Data
    public static class OrderDate {
        private LocalDate date;
        private Integer dailyNewOrders;
        private Integer dailyEffectiveNewOrders;
    }

    @Data
    public static class SalesTop10 {
        private String name;
        private Integer count;
    }
}

