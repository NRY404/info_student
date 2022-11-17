package com.example.vuemanage.domain;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "student")
public class Student implements Serializable {

    @Alias("学号")
    @TableField(value = "studentId")
    private String studentId;

    @Alias("姓名")
    private String name;
    @Alias("性别")
    private String gender;

    @Alias("班级号")
    @TableField(value = "classId")
    private String classId;
    @Alias("班级名")
    @TableField(exist = false)
    private String className;
    @Alias("宿舍号")
    @TableField(value = "dormId")
    private String dormId;
    @Alias("宿舍名")
    @TableField(exist = false)
    private String dormName;

    @Alias("电话")
    private String phone;
    @Alias("地址")
    private String address;
    @Alias("邮箱")
    private String email;
    @Alias("出生日期")
    private String birthday;
    @Alias("头像")
    private String avatar;

}
