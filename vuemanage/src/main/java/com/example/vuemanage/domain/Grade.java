package com.example.vuemanage.domain;


import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "grade")
public class Grade {

    @Alias("学号")
    @TableField(value = "studentId")
    private String studentId;

    @Alias("姓名")
    @TableField(exist = false)
    private String name;

    @Alias("课程号")
    @TableField(value = "courseId")
    private String courseId;

    @Alias("课程名")
    @TableField(exist = false)
    private String courseName;

    @Alias("课程类别")
    @TableField(exist = false)
    private String courseType;

    @Alias("课程学分")
    @TableField(exist = false)
    private Integer courseMark;

    @Alias("成绩")
    private Float grade;
}
