package com.sky.service.impl;

import com.sky.exception.BusinessException;
import com.sky.mapper.ReportMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.StringJoiner;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportMapper reportMapper;

    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
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

        // 构建TurnoverReportVO中的dateList字符串
        StringJoiner dateList = new StringJoiner(",");
        StringJoiner turnoverList = new StringJoiner(",");
        while (begin.isBefore(end) || begin.equals(end)) {
            dateList.add(begin.toString());
            turnoverList.add(reportMapper.getTurnoverList(begin).toString());
            begin = begin.plusDays(1);
        }

        return TurnoverReportVO.builder()
                .dateList(dateList.toString())
                .turnoverList(turnoverList.toString())
                .build();
    }
}

