package com.example.vuemanage.domain;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "dorm")
public class Dorm {
    @Alias("宿舍号")
    @TableId(value = "dormId",type = IdType.AUTO)    // 声明 表中 主键
    private String dormId;

    @Alias("宿舍名")
    private String dormName;
    @Alias("联系电话")
    private String telephone;
}
