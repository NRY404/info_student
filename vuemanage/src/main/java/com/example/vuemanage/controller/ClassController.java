package com.example.vuemanage.controller;


import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.vuemanage.common.Result;
import com.example.vuemanage.domain.Class;
import com.example.vuemanage.domain.Student;
import com.example.vuemanage.mapper.ClassMapper;
import com.example.vuemanage.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/class")
public class ClassController {

    @Resource
    private ClassMapper clMapper;
    @Autowired
    private ClassService clService;

    @GetMapping("/")
    public List<Class> findAll(){
        return clMapper.getClList();
    }

//  分页查询
    @GetMapping("/page")
    public Map<String,Object> findByPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize){

//        返回总条数
        Integer total = clMapper.selectTotal();
//         使用map 集合 将分页相关 数据 封装
        Map<String,Object> map = new HashMap<>();
        map.put("第几页",pageNum);

        //        分页计算
        pageNum = (pageNum-1 ) * pageSize;
        List<Class> data = clMapper.findByPage(pageNum, pageSize);
        map.put("data",data);
        map.put("total",total);
        map.put("分页条数",pageSize);
        return map;
    }

    @GetMapping("/page3")
    public Map<String,Object> findByPage3(@RequestParam Integer pageNum,
                                          @RequestParam Integer pageSize,
                                          @RequestParam(defaultValue = "") String className) {


//         使用map 集合 将分页相关 数据 封装
        Map<String, Object> map = new HashMap<>();

        //        分页计算
        pageNum = (pageNum - 1) * pageSize;
        if ( !"".equals(className)) {
            className = "%" + className + "%";
        }
        List<Class> dataByCondition = clMapper.findByPage3(pageNum, pageSize, className);

        //        返回总条数
        Integer total = clMapper.selectTotalByClassName(className);
        map.put("data", dataByCondition);
        map.put("total", total);

        return map;
    }

    @PostMapping("/add")
    public Result save(@RequestBody Class cls){
        return clService.addClass(cls);
    }

    @GetMapping("/getStu")
    public Map<String,Object> getStu(@RequestParam Integer pageNumS,
                                          @RequestParam Integer pageSizeS,
                                          @RequestParam(defaultValue = "") String classId){

//         使用map 集合 将分页相关 数据 封装
        Map<String, Object> map = new HashMap<>();

        //        分页计算
        pageNumS = (pageNumS - 1) * pageSizeS;
        List<Student> stuData = clMapper.getStu(pageNumS, pageSizeS, classId);

        //        返回总条数
        Integer total = clMapper.selectTotalStu(classId);
        map.put("data", stuData);
        map.put("total", total);

        return map;
    }

    //    根据 id 删除
    @RequestMapping("/delete/{classId}") //其中的id @PathVariable 声明的 id 对应
    public boolean delete(@PathVariable String classId){
        return clMapper.deleteById(classId);  // mybatis plus 提供的 根据id 删除
    }


    /**
     * 导出接口
     *
     **/
    @RequestMapping("/export")
    public void export(HttpServletResponse response) throws Exception{

        List<Class> list = clMapper.getClList();
        //通过工具类创建writer 写出到磁盘路径
        //ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        //在内存操作，写出到浏览器,请求下载
        ExcelWriter writer = ExcelUtil.getWriter(true);
//        自定义别名     这里使用 hutool 的@Alies 注解，在 实体类中 为 字段添加注解
//        writer.addHeaderAlias("classId","班级号");
//        writer.addHeaderAlias("className","班级名");
//        writer.addHeaderAlias("speciality","专业");
//        writer.addHeaderAlias("counsellor","辅导员");
//        writer.addHeaderAlias("phone","联系电话");
//        writer.addHeaderAlias("entranceYear","入学年份");

//        一次性写出 list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list,true);

//        设置浏览器响应的 格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("班级信息","UTF-8");
        response.setHeader("Content-Disposition","attachment;filename=" + fileName + ".xlsx");

//      将writer 对象 刷新到 输出流中
        ServletOutputStream out = response.getOutputStream();
        writer.flush(out,true);
        out.close();
        writer.close();

    }

    /**
     * excel 导入
     *
     **/
    @PostMapping("/import")
    public boolean importUsers(MultipartFile file) throws Exception{
        return clService.importClasss(file);
    }
}
