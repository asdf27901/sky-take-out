package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;

@Mapper
public interface ReportMapper {
    
    @Select("select ifnull(sum(amount), 0) from orders where DATE(checkout_time) = #{begin} and status = 5")
    Double getTurnoverList(LocalDate begin);
}
