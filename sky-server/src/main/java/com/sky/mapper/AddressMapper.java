package com.sky.mapper;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AddressMapper {

    @Insert("insert into address_book (user_id, consignee, sex, phone, province_code, province_name, city_code, city_name, district_code, district_name, detail, label, is_default) " +
            "values (#{userId}, #{consignee}, #{sex}, #{phone}, #{provinceCode}, #{provinceName}, #{cityCode}, #{cityName}, #{districtCode}, #{districtName}, #{detail}, #{label}, #{isDefault})")
    int saveAddress(AddressBook addressBook);

    @Select("select * from address_book where user_id = #{userId}")
    List<AddressBook> getAllAddress(Long userId);

    @Select("select * from address_book where user_id = #{userId} and is_default = 1")
    AddressBook getDefaultAddress(Long userId);

    @Select("select * from address_book where id = #{id} and user_id = #{userId}")
    AddressBook getAddressById(Long userId, Long id);

    @Delete("delete from address_book where user_id = #{userId} and id = #{id}")
    int deleteAddressById(Long userId, Long id);

    int updateAddress(AddressBook address);
}
