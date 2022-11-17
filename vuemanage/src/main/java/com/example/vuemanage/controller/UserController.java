package com.example.vuemanage.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.vuemanage.common.Constants;
import com.example.vuemanage.common.Result;
import com.example.vuemanage.domain.dto.UserLogin;
import com.example.vuemanage.domain.User;
import com.example.vuemanage.mapper.UserMapper;
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
@RequestMapping(value = "/user")
public class UserController {

    @Resource
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

//    登录
    @PostMapping("/login")
    public Result loginIn(@RequestBody UserLogin userLogin) {
//      RequestBody 将前端传来的 json 转为 java 对象
        String username = userLogin.getUsername();
        String password = userLogin.getPassword();

        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {   // hutool 提供工具类，判断是否为空
            return Result.error(Constants.CODE_400, "参数错误");
        } else {
            UserLogin login = userService.login(userLogin);
            return Result.success(login);
        }
    }

//    注册
    @PostMapping("/register")
    public Result register(@RequestBody UserLogin userLogin){
        String username = userLogin.getUsername();
        String password = userLogin.getPassword();

        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {   // hutool 提供工具类，判断是否为空
            return Result.error(Constants.CODE_400, "参数错误");
        }
        User register = userService.register(userLogin);

        return Result.success(register);
    }


    //    新增 或者 更新
//    @PutMapping("/add")
    @PostMapping("/add")
    public boolean save(@RequestBody User user){
        return userService.saveUser(user);
    }


//    返回所有用户
    @GetMapping("/")
    public List<User> findAll(){
        return userService.list();  // mybatis plus 提供 查询所有 方法
    }

//    分页查询
//  @RequestParam 用于接收 /user/findById?pageNum=1&pageSize=5
//    limit (pageNum-1) * pageSize , pageSize
//    第一页：limit (1-1)* 5 , 5
    @GetMapping("/page")
    public Map<String,Object> findByPage(@RequestParam Integer pageNum,@RequestParam Integer pageSize){

//        返回总条数
        Integer total = userMapper.selectTotal();
//         使用map 集合 将分页相关 数据 封装
        Map<String,Object> map = new HashMap<>();
        map.put("第几页",pageNum);

        //        分页计算
        pageNum = (pageNum-1 ) * pageSize;
        List<User> data = userMapper.findByPage(pageNum, pageSize);
        map.put("data",data);
        map.put("total",total);
        map.put("分页条数",pageSize);

        return map;
    }

//    分页查询 -- mybatis plus 方式
    @GetMapping("/page2")
    public IPage<User> findByPage1(@RequestParam Integer pageNum,
                                          @RequestParam Integer pageSize,
                                          @RequestParam(defaultValue = "") String username,
                                          @RequestParam(defaultValue = "") String email,
                                          @RequestParam(defaultValue = "") String address){

        IPage<User> page = new Page<>(pageNum,pageSize);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if ( !"".equals(username)){
            wrapper.like("username",username);
        }
        if ( !"".equals(email)){
            wrapper.like("email",email);
        }
        if ( !"".equals(address)){
            wrapper.like("address",address);
        }

//        倒序
//        wrapper.orderByDesc("id");
        IPage<User> userIPage = userService.page(page, wrapper);

        return userIPage;
    }



    @GetMapping("/page3")
    public Map<String,Object> findByPage3(@RequestParam Integer pageNum,
                                          @RequestParam Integer pageSize,
                                          @RequestParam(defaultValue = "") String username,
                                          @RequestParam(defaultValue = "") String email,
                                          @RequestParam(defaultValue = "") String address) {


//         使用map 集合 将分页相关 数据 封装
        Map<String, Object> map = new HashMap<>();

        //        分页计算
        pageNum = (pageNum - 1) * pageSize;
        if ( !"".equals(username)) {
            username = "%" + username + "%";
        }
        if ( !"".equals(email)) {
            email = "%" + email + "%";
        }
        if ( !"".equals(address)) {
            address = "%" + address + "%";
        }
        List<User> dataByCondition = userMapper.findByPage3(pageNum, pageSize, username, email, address);

        //        返回总条数
        Integer total = userMapper.selectTotal3(username, email, address);
        map.put("data", dataByCondition);
        map.put("total", total);

        return map;
    }
//    根据 id 删除
    @RequestMapping("/delete/{id}") //其中的id @PathVariable 声明的 id 对应
    public boolean delete(@PathVariable Integer id){
        return userService.removeById(id);  // mybatis plus 提供的 根据id 删除
    }

    //    根据 id 批量 删除
    @PostMapping("/deletes") //其中的id
    public boolean deleteByIds(@RequestBody List<Integer> id){     //{1,2,3}
        return userService.removeByIds(id);  // mybatis plus 提供的 根据id 删除
    }


    /**
     * 导出接口
     *
     **/
    @RequestMapping("/export")
    public void export(HttpServletResponse response) throws Exception{

        List<User> list = userService.list();
        //通过工具类创建writer 写出到磁盘路径
        //ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        //在内存操作，写出到浏览器,请求下载
        ExcelWriter writer = ExcelUtil.getWriter(true);
//        自定义别名     这里使用 hutool 的@Alies 注解，在 实体类中 为 字段添加注解
//        writer.addHeaderAlias("username","用户名");
//        writer.addHeaderAlias("password","密码");
//        writer.addHeaderAlias("nickname","昵称");
//        writer.addHeaderAlias("email","邮箱");
//        writer.addHeaderAlias("phone","电话");
//        writer.addHeaderAlias("address","地址");
//        writer.addHeaderAlias("createTime","创建时间");

//        一次性写出 list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list,true);

//        设置浏览器响应的 格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息","UTF-8");
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

        return userService.importUsers(file);
    }

}
