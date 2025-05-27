package com.sky.service.impl;

import com.sky.exception.BusinessException;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.StringJoiner;

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
}

