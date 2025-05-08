package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper {

    Page<Category> getCategoryList(CategoryPageQueryDTO categoryPageQueryDTO);

    @Insert("insert into category (type, name, sort, status, create_time, update_time, create_user, update_user) " +
            "values (#{type}, #{name}, #{sort}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser});")
    int addCategory(Category category);

    @Select("select count(1) from category where name = #{name}")
    int getCategoryByName(String name);

    @Select("select * from category where id = #{id}")
    Category getCategoryById(Long id);

    int updateCategory(Category category);

    @Delete("delete from category where id = #{id}")
    int delCategoryById(Long id);

    @Select("select * from category where type = #{type} and status = 1")
    List<Category> listCategoryByType(Integer type);
}
