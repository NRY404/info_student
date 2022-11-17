package com.example.vuemanage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.vuemanage.domain.Student;
import com.example.vuemanage.domain.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface StudentMapper extends BaseMapper<Student> {


    boolean add(Student student);

    boolean update(Student student);

    @Delete("delete from student where studentId = #{studentId}")
    boolean deleteById(@Param("studentId") String studentId);

    boolean deleteByIds(List<String> studentIds);

    @Select("SELECT COUNT(dormId) AS dormNum FROM student WHERE dormId = #{dormId}")
    Integer getDormNum(String dormId);


    @Select("select studentId,name,gender,birthday,address,dormId,classId,email,avatar from student where studentId = #{studentId}")
    Student getStudentById(@Param("studentId") String studentId);

    //    简单分页查询
//    @Select("select * from student limit #{pageNum}, #{pageSize}")
    List<Student> findByPage(Integer pageNum, Integer pageSize);

    List<Student> getStuList();

    @Select("select count(studentId) from student")
    Integer selectTotal();

    List<Student> findByPage3(Integer pageNum, Integer pageSize,String studentId,String name,String email);

    Integer selectTotal3(String studentId,String name,String email);
}
