package com.example.vuemanage.domain.dto;

import cn.hutool.core.annotation.Alias;
import lombok.Data;

/**
 * 接收前端 登录请求数据 
 * @return null
 **/
@Data
public class UserLogin {

    @Alias("用户名")
    private String username;
    @Alias("密码")
    private String password;
    @Alias("昵称")
    private String nickname;
    @Alias("邮箱")
    private String email;
    @Alias("电话")
    private String phone;
    @Alias("地址")
    private String address;
    @Alias("头像")
    private String avatar;

    private String token;

    
}
