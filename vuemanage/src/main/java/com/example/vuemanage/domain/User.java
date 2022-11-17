package com.example.vuemanage.domain;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "user")
//mybatis 提供的 用于识别 数据库中表名，如果不加此注解，则默认使用该实体类 名的小写 作为表名！
public class User {

    @TableId(value = "id",type = IdType.AUTO)    // 声明 表中 主键
    private Integer id;

    @Alias("用户名")
    private String username;

//    @JsonIgnore //忽视该字段 不对外展示
    @Alias("密码")
    private String password;
    @Alias("昵称")
    private String nickname;
    @Alias("邮箱")
    private String email;
    @Alias("电话")
    private String phone;
    @Alias("身份权限")
    private Integer identity;
    @Alias("创建时间")
    @TableField(value = "create_Time")  //数据库 表中 的原字段 名称
    private Date createTime;  // 重新自定义 取别名，
    // 注意 mybatis plus 框架 会将_后面字母自动大写，也可以识别，比如这里可以不用声明注解

    @Alias("头像")
    private String avatar;


}
