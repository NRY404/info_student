package com.example.vuemanage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.vuemanage.domain.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Mapper   已由MybatisPlusConfig 配置类 通过@MapperScan 引入
public interface UserMapper extends BaseMapper<User> {


    int update(User user);

    @Delete("delete from sys_user where id = #{id}")
    Integer deleteById(@Param("id") Integer id);

//    简单分页查询
    @Select("select * from sys_user limit #{pageNum}, #{pageSize}")
    List<User> findByPage(Integer pageNum, Integer pageSize);

    @Select("select count(id) from sys_user")
    Integer selectTotal();


    List<User> findByPage3(Integer pageNum, Integer pageSize,String username,String email,String address);

    Integer selectTotal3(String username,String email,String address);
}
