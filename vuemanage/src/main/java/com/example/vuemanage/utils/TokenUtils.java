package com.example.vuemanage.utils;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.vuemanage.domain.User;
import com.example.vuemanage.service.UserService;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 生成token
 * @return null
 **/
public class TokenUtils {

//    这边之所以要 引入两个 UserService 变量，是因为，下面的静态方法，不能使用非静态的 变量
    private static UserService staticUserService;

    @Resource
    private UserService userService;

    @PostConstruct  //在构造函数 执行完之后，就执行该方法
    public void setUserService(){
        staticUserService = userService;
    }

    public static String genToken(String userId,String sign){

        return JWT.create().withAudience(userId)   //将 userId 保存到 token 里面，作为 载荷
                .withExpiresAt(DateUtil.offsetHour(new Date(), 2)) //设置 2h 后过期
                .sign(Algorithm.HMAC256(sign)); //以 password 作为 token 的密钥

    }

    /**
     * 获取当前登录的用户信息
     * @return com.example.vuemanage.domain.User
     **/
    public static User getCurrentUser(){

        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader("token");

            if (StrUtil.isNotBlank(token)){
                String userId = JWT.decode(token).getAudience().get(0);
                return staticUserService.getById(Integer.valueOf(userId));
            }
        }catch (Exception e){
            return null;
        }

        return null;
    }
}
