package com.example.vuemanage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.vuemanage.common.Result;
import com.example.vuemanage.domain.Class;
import com.example.vuemanage.domain.Student;
import com.example.vuemanage.domain.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;



public interface ClassMapper extends BaseMapper<Class> {

    @Select("select classId,className,speciality,counsellor,phone,entranceYear from class")
    List<Class> getClList();

    @Select("select classId,className,speciality,counsellor,phone,entranceYear from class where classId = #{classId}")
    Class getClassByClassId(String classId);

    @Select("select count(classId) from class")
    Integer selectTotal();

    boolean addClass(Class cl);

    @Delete("delete from class where classId = #{classId}")
    boolean deleteById(@Param("classId") String classId);

    @Select("select * from class limit #{pageNum}, #{pageSize}")
    List<Class> findByPage(Integer pageNum, Integer pageSize);

    List<Class> findByPage3(Integer pageNum, Integer pageSize, String className);

    Integer selectTotalByClassName(String className);

    @Select("select studentId,name,classId from student where classId = #{classId} limit #{pageNumS} , #{pageSizeS}")
    List<Student> getStu(Integer pageNumS, Integer pageSizeS, String classId);

    @Select("select count(*) from student where classId = #{classId}")
    Integer selectTotalStu(String classId);
}
