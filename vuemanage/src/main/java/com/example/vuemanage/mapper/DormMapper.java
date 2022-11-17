package com.example.vuemanage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.vuemanage.domain.Class;
import com.example.vuemanage.domain.Dorm;
import com.example.vuemanage.domain.Student;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface DormMapper extends BaseMapper<Dorm> {

    @Select("select dormId,dormName,telephone from dorm")
    List<Dorm> getDormList();

    @Select("select dormId,dormName,telephone from dorm where dormId = #{dormId}")
    Dorm getDormByDormId(String dormId);

    @Select("select count(dormId) from dorm")
    Integer selectTotal();

    boolean addDorm(Dorm dorm);

    @Delete("delete from dorm where dormId = #{dormId}")
    boolean deleteById(@Param("dormId") String dormId);

    @Select("select * from dorm limit #{pageNum}, #{pageSize}")
    List<Dorm> findByPage(Integer pageNum, Integer pageSize);

    List<Dorm> findByPage3(Integer pageNum, Integer pageSize, String dormName);

    Integer selectTotalByDormName(String dormName);

    @Select("select studentId,name,dormId from student where dormId = #{dormId} limit #{pageNumS} , #{pageSizeS}")
    List<Student> getStu(Integer pageNumS, Integer pageSizeS, String dormId);

    @Select("select count(*) from student where dormId = #{dormId}")
    Integer selectTotalStu(String dormId);
}
