package com.example.vuemanage.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.vuemanage.common.Constants;
import com.example.vuemanage.common.Result;
import com.example.vuemanage.domain.Student;
import com.example.vuemanage.domain.User;
import com.example.vuemanage.domain.dto.UserLogin;
import com.example.vuemanage.mapper.StudentMapper;
import com.example.vuemanage.mapper.UserMapper;
import com.example.vuemanage.service.StudentService;
import com.example.vuemanage.service.UserService;
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
@RequestMapping(value = "/student")
public class StudentController {

    @Resource
    private StudentMapper studentMapper;

    @Autowired
    private StudentService studentService;



    //    新增 或者 更新
//    @PutMapping("/add")
    @PostMapping("/add")
    public Result save(@RequestBody Student student){
        return studentService.saveStudent(student);
    }


//    返回所有用户
    @GetMapping("/")
    public List<Student> findAll(){
        return studentService.list();  // mybatis plus 提供 查询所有 方法
    }

//    分页查询
//  @RequestParam 用于接收 /user/findById?pageNum=1&pageSize=5
//    limit (pageNum-1) * pageSize , pageSize
//    第一页：limit (1-1)* 5 , 5
    @GetMapping("/page")
    public Map<String,Object> findByPage(@RequestParam Integer pageNum,@RequestParam Integer pageSize){

//        返回总条数
        Integer total = studentMapper.selectTotal();
//         使用map 集合 将分页相关 数据 封装
        Map<String,Object> map = new HashMap<>();
        map.put("第几页",pageNum);

        //        分页计算
        pageNum = (pageNum-1 ) * pageSize;
        List<Student> data = studentMapper.findByPage(pageNum, pageSize);
        map.put("data",data);
        map.put("total",total);
        map.put("分页条数",pageSize);

        return map;
    }

//    分页查询 -- mybatis plus 方式
    @GetMapping("/page2")
    public IPage<Student> findByPage1(@RequestParam Integer pageNum,
                                      @RequestParam Integer pageSize,
                                      @RequestParam(defaultValue = "") String studentId,
                                      @RequestParam(defaultValue = "") String name,
                                      @RequestParam(defaultValue = "") String email){

        IPage<Student> page = new Page<>(pageNum,pageSize);
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        if ( !"".equals(studentId)){
            wrapper.like("studentId",studentId);
        }
        if ( !"".equals(name)){
            wrapper.like("name",name);
        }
        if ( !"".equals(email)){
            wrapper.like("email",email);
        }

//        倒序
//        wrapper.orderByDesc("id");
        IPage<Student> studentIPage = studentService.page(page, wrapper);

        return studentIPage;
    }



    @GetMapping("/page3")
    public Map<String,Object> findByPage3(@RequestParam Integer pageNum,
                                          @RequestParam Integer pageSize,
                                          @RequestParam(defaultValue = "") String studentId,
                                          @RequestParam(defaultValue = "") String name,
                                          @RequestParam(defaultValue = "") String email) {


//         使用map 集合 将分页相关 数据 封装
        Map<String, Object> map = new HashMap<>();

        //        分页计算
        pageNum = (pageNum - 1) * pageSize;
        if ( !"".equals(studentId)) {
            studentId = "%" + studentId + "%";
        }
        if ( !"".equals(name)) {
            name = "%" + name + "%";
        }
        if ( !"".equals(email)) {
            email = "%" + email + "%";
        }

        List<Student> dataByCondition = studentMapper.findByPage3(pageNum, pageSize, studentId, name, email);

        //        返回总条数
        Integer total = studentMapper.selectTotal3(studentId, name, email);
        map.put("data", dataByCondition);
        map.put("total", total);

        return map;
    }
//    根据 id 删除
    @RequestMapping("/delete/{studentId}") //其中的id @PathVariable 声明的 id 对应
    public boolean delete(@PathVariable String studentId){
        return studentMapper.deleteById(studentId);  // mybatis plus 提供的 根据id 删除
    }

    //    根据 id 批量 删除
    @PostMapping("/deletes") //其中的id
    public boolean deleteByIds(@RequestBody List<String> studentIds){     //{1,2,3}
        return studentMapper.deleteByIds(studentIds);  // mybatis plus 提供的 根据id 删除
    }


    /**
     * 导出接口
     *
     **/
    @RequestMapping("/export")
    public void export(HttpServletResponse response) throws Exception{

        List<Student> list = studentMapper.getStuList();
        //通过工具类创建writer 写出到磁盘路径
        //ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        //在内存操作，写出到浏览器,请求下载
        ExcelWriter writer = ExcelUtil.getWriter(true);
//        自定义别名     这里使用 hutool 的@Alies 注解，在 实体类中 为 字段添加注解
//        writer.addHeaderAlias("studentId","学号");
//        writer.addHeaderAlias("name","姓名");
//        writer.addHeaderAlias("gender","性别");
//        writer.addHeaderAlias("phone","电话");
//        writer.addHeaderAlias("dormId","宿舍号");
//        writer.addHeaderAlias("classId","班级号");
//        writer.addHeaderAlias("birthday","出生日期");
//        writer.addHeaderAlias("address","地址");
//        writer.addHeaderAlias("avatar","头像");

//        一次性写出 list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list,true);

//        设置浏览器响应的 格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("学生信息","UTF-8");
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
        return studentService.importStudents(file);
    }

}
