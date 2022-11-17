package com.example.vuemanage.service;


import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.vuemanage.common.Result;
import com.example.vuemanage.domain.Student;
import com.example.vuemanage.mapper.StudentMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.List;

@Service
public class StudentService extends ServiceImpl<StudentMapper, Student> {


    @Resource
    private StudentMapper studentMapper;

//  添加用户
    public Result saveStudent(Student student) {

        Integer dormNum = studentMapper.getDormNum(student.getDormId());
        if ((4-dormNum)>0){
            if (studentMapper.getStudentById(student.getStudentId()) == null){
                studentMapper.add(student);//用于插入数据
                return Result.success();
            }else {
                studentMapper.update(student);//用于更新数据
                return Result.success();
            }
        }else {
            return Result.error("123","所选宿舍已住满！");
        }
    }

//  导入学生数据
    public boolean importStudents(MultipartFile file) throws Exception{
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        List<Student> list = reader.read(0, 1, Student.class);

        saveBatch(list);

        return true;
    }

}
