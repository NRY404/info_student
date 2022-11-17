package com.example.vuemanage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.vuemanage.domain.Dorm;
import com.example.vuemanage.domain.Grade;
import com.example.vuemanage.domain.Student;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface GradeMapper extends BaseMapper<Grade> {


    List<Grade> getGradeList();

    @Select("select count(studentId) from grade")
    Integer selectTotal();

    List<Grade> findByPage(Integer pageNum, Integer pageSize);

    List<Grade> findByPage3(Integer pageNum, Integer pageSize, String studentId);

    @Select("select count(studentId) from grade where studentId like #{studentId}")
    Integer selectTotalByStudentId(String studentId);

}
