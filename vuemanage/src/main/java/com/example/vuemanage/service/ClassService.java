package com.example.vuemanage.service;


import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.vuemanage.common.Result;
import com.example.vuemanage.domain.Class;
import com.example.vuemanage.mapper.ClassMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.List;

@Service
public class ClassService extends ServiceImpl<ClassMapper, Class> {

    @Resource
    private ClassMapper clMapper;

    public Result addClass(Class cls) {

            if (clMapper.getClassByClassId(cls.getClassId()) == null){
                clMapper.addClass(cls);//用于插入数据
                return Result.success();
            }else {
                return Result.error("124","添加失败！");
            }

    }

    //  导入学生数据
    public boolean importClasss(MultipartFile file) throws Exception{
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        List<Class> list = reader.read(0, 1, Class.class);
        saveBatch(list);

        return true;
    }

}
