package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;

@Mapper
public interface UserMapper {

    @Select("select * from user where openid = #{openid}")
    User getUserByOpenid(String openid);

    @Insert("insert into user (openid, name, phone, sex, id_number, avatar, create_time) " +
            "values (#{openid}, #{name}, #{phone}, #{sex}, #{idNumber}, #{avatar}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void saveUser(User user);

    @Select("select * from user where DATE(create_time) <= #{begin}")
    Integer countTotalUserByDate(LocalDate begin);

    @Select("select count(1) from user where DATE(create_time) = #{begin}")
    CharSequence countNewUserByDate(LocalDate begin);
}
