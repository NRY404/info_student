package com.example.vuemanage.domain;


import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.hpsf.Decimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "course")
public class Course {

    @Alias("课程号")
    @TableField(value = "courseId")
    private String courseId;

    @Alias("课程名")
    @TableField(value = "courseName")
    private String courseName;

    @Alias("课程类别")
    @TableField(value = "courseType")
    private String courseType;

    @Alias("课程学分")
    @TableField(value = "courseMark")
    private Integer courseMark;

    @Alias("课程学时")
    @TableField(value = "courseTime")
    private Decimal courseTime;

}
