package com.example.vuemanage.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//统一 包装类，用于 统一 接口的 返回类型
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {

    private String code;    // 告知是否 请求成功 或失败
    private String msg;     //如果请求失败，告知原因
    private Object data;    //后台所需要的 携带数据，可以为任何类型

//    返回成功 时的 方法
    public static Result success(){
        return new Result(Constants.CODE_200,"",null);
    }
//    返回成功 时的 有数据方法
    public static Result success(Object data){
        return new Result(Constants.CODE_200,"",data);
    }

//    返回失败 时 默认 的 方法
    public static Result error(){
        return new Result(Constants.CODE_500,"系统错误",null);
    }
//    返回失败 时的 方法
    public static Result error(String code,String msg){
        return new Result(code,msg,null);
    }


}
