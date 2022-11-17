package com.example.vuemanage.service;


import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.vuemanage.common.Result;
import com.example.vuemanage.domain.Dorm;
import com.example.vuemanage.domain.Grade;
import com.example.vuemanage.mapper.DormMapper;
import com.example.vuemanage.mapper.GradeMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.List;

@Service
public class GradeService extends ServiceImpl<GradeMapper, Grade> {

    @Resource
    private GradeMapper gradeMapper;

    //  导入宿舍数据
    public boolean importDorms(MultipartFile file) throws Exception{
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        List<Grade> list = reader.read(0, 1, Grade.class);
        saveBatch(list);

        return true;
    }
}
