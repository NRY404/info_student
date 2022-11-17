package com.example.vuemanage.domain;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "class")
public class Class {

    @Alias("班级号")
    @TableId(value = "classId",type = IdType.AUTO)    // 声明 表中 主键
    private String classId;

    @Alias("班级名")
    @TableField(value = "className")
    private String className;
    @Alias("专业")
    private String speciality;
    @Alias("辅导员")
    private String counsellor;
    @Alias("联系电话")
    private String phone;
    @Alias("入学年份")
    @TableField(value = "entranceYear")
    private String entranceYear;

}
