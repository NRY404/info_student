package com.example.vuemanage.controller;


import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.vuemanage.common.Result;
import com.example.vuemanage.domain.Class;
import com.example.vuemanage.domain.Dorm;
import com.example.vuemanage.domain.Student;
import com.example.vuemanage.mapper.DormMapper;
import com.example.vuemanage.service.DormService;
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
@RequestMapping(value = "/dorm")
public class DormController {

    @Resource
    private DormMapper dormMapper;

    @Autowired
    private DormService dormService;

    @GetMapping("/")
    public List<Dorm> findAll(){
        return dormMapper.getDormList();
    }

    //  分页查询
    @GetMapping("/page")
    public Map<String,Object> findByPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize){

//        返回总条数
        Integer total = dormMapper.selectTotal();
//         使用map 集合 将分页相关 数据 封装
        Map<String,Object> map = new HashMap<>();
        map.put("第几页",pageNum);

        //        分页计算
        pageNum = (pageNum-1 ) * pageSize;
        List<Dorm> data = dormMapper.findByPage(pageNum, pageSize);
        map.put("data",data);
        map.put("total",total);
        map.put("分页条数",pageSize);
        return map;
    }

    @GetMapping("/page3")
    public Map<String,Object> findByPage3(@RequestParam Integer pageNum,
                                          @RequestParam Integer pageSize,
                                          @RequestParam(defaultValue = "") String dormName) {


//         使用map 集合 将分页相关 数据 封装
        Map<String, Object> map = new HashMap<>();

        //        分页计算
        pageNum = (pageNum - 1) * pageSize;
        if ( !"".equals(dormName)) {
            dormName = "%" + dormName + "%";
        }
        List<Dorm> dataByCondition = dormMapper.findByPage3(pageNum, pageSize, dormName);

        //        返回总条数
        Integer total = dormMapper.selectTotalByDormName(dormName);
        map.put("data", dataByCondition);
        map.put("total", total);

        return map;
    }

    @PostMapping("/add")
    public Result save(@RequestBody Dorm dorm){
        return dormService.addDorm(dorm);
    }

    @GetMapping("/getStu")
    public Map<String,Object> getStu(@RequestParam Integer pageNumS,
                                     @RequestParam Integer pageSizeS,
                                     @RequestParam(defaultValue = "") String dormId){

//         使用map 集合 将分页相关 数据 封装
        Map<String, Object> map = new HashMap<>();

        //        分页计算
        pageNumS = (pageNumS - 1) * pageSizeS;
        List<Student> stuData = dormMapper.getStu(pageNumS, pageSizeS, dormId);

        //        返回总条数
        Integer total = dormMapper.selectTotalStu(dormId);
        map.put("data", stuData);
        map.put("total", total);

        return map;
    }

    //    根据 id 删除
    @RequestMapping("/delete/{dormId}") //其中的id @PathVariable 声明的 id 对应
    public boolean delete(@PathVariable String dormId){
        return dormMapper.deleteById(dormId);
    }


    /**
     * 导出接口
     *
     **/
    @RequestMapping("/export")
    public void export(HttpServletResponse response) throws Exception{

        List<Dorm> list = dormMapper.getDormList();
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list,true);

//        设置浏览器响应的 格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("宿舍信息","UTF-8");
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
        return dormService.importDorms(file);
    }
}
