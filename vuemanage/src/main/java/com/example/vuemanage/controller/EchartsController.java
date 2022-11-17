package com.example.vuemanage.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Quarter;
import com.example.vuemanage.common.Result;
import com.example.vuemanage.domain.Student;
import com.example.vuemanage.domain.User;
import com.example.vuemanage.service.StudentService;
import com.example.vuemanage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/echarts")
public class EchartsController {

    @Autowired
    private StudentService studentService;


    @GetMapping("/members")
    public Result members() {

        List<Student> list = studentService.list();
//        男女零
        int q1 = 0;
        int q2 = 0;
        int q3 = 0;
//        int q4 = 0;

        for (Student stu : list) {
            String gender = stu.getGender();
            switch (gender) {
                case "男": q1 += 1; break;
                case "女": q2 += 1; break;
                case "零": q3 += 1; break;
//                case Q4: q4 += 1; break;
                default: break;
            }
        }
        return Result.success(CollUtil.newArrayList(q1,q2,q3));
    }
}
