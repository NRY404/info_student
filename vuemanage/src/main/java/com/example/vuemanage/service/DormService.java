package com.example.vuemanage.service;


import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.vuemanage.common.Result;
import com.example.vuemanage.domain.Class;
import com.example.vuemanage.domain.Dorm;
import com.example.vuemanage.mapper.ClassMapper;
import com.example.vuemanage.mapper.DormMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.List;

@Service
public class DormService extends ServiceImpl<DormMapper, Dorm> {

    @Resource
    private DormMapper dormMapper;

    public Result addDorm(Dorm dorm) {

        if (dormMapper.getDormByDormId(dorm.getDormId()) == null){
            dormMapper.addDorm(dorm);//用于插入数据
            return Result.success();
        }else {
            return Result.error("124","添加失败！");
        }

    }

    //  导入宿舍数据
    public boolean importDorms(MultipartFile file) throws Exception{
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        List<Dorm> list = reader.read(0, 1, Dorm.class);
        saveBatch(list);

        return true;
    }
}
