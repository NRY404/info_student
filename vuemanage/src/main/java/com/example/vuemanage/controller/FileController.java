package com.example.vuemanage.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.vuemanage.common.Result;
import com.example.vuemanage.domain.Files;
import com.example.vuemanage.mapper.FileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${files.upload.path}")
    private String fileUploadPth;

    @Resource
    private FileMapper fileMapper;


    //    分页查询 -- mybatis plus 方式
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String name){

        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        queryWrapper.eq("is_delete",0); //用于查询 是否被删除字段，0表示未删除，
        if (!"".equals(name)){
            queryWrapper.like("name",name);
        }

        return Result.success(fileMapper.selectPage(new Page<>(pageNum,pageSize),queryWrapper));
    }

    /**
     * 删除
 * @return boolean
     **/

    //    根据 id 删除
    @RequestMapping("/delete/{id}") //其中的id @PathVariable 声明的 id 对应
    public Result delete(@PathVariable Integer id){
//        fileMapper.deleteById(id); // 代替了下面两行代码
        Files files = fileMapper.selectById(id);
        files.setIsDelete(true);
        return Result.success(fileMapper.updateById(files)) ;  // mybatis plus 提供的 根据id 删除
    }

    //    根据 id 批量 删除
    @PostMapping("/deletes") //其中的id
    public Result deleteByIds(@RequestBody List<Integer> ids){     //{1,2,3}
//        select * from sys_file where id in(id,id,id..)
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id",ids);
        List<Files> files = fileMapper.selectList(queryWrapper);
        for (Files file : files){
            file.setIsDelete(true);
            fileMapper.updateById(file);
        }
        return Result.success();
    }


    /**
     * 文件上传接口
     *
     * @return 前端传过来的文件
     **/
    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();   //获取文件原名称
        String type = FileUtil.extName(originalFilename);   //获取文件类型
        long size = file.getSize();


//       定义一个文件 唯一的 标识码
        String uuid = IdUtil.fastSimpleUUID();
        String fileUuid = uuid + StrUtil.DOT + type;

        File uploadFile = new File(fileUploadPth + fileUuid);

//        判断父级文件夹是否存在
        File parentFile = uploadFile.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

//        获取文件 url
        String url;
//        上传文件到磁盘
        file.transferTo(uploadFile);

//        先判断文件是否存在，当文件存在的时候，再获取文件的md5
//        获取文件的 md5 格式
        String md5 = SecureUtil.md5(uploadFile);
//        查询数据库 是否有md5 重名的文件
        Files dbFiles = getFileByMd5(md5);
        if (dbFiles != null) {
            url = dbFiles.getUrl();
            uploadFile.delete();
        } else {
//              数据库若不存在重复文件，则不删除刚才上传的文件
            url = "http://localhost:9090/file/" + fileUuid;
        }


//        存储数据库
        Files saveFile = new Files();
        saveFile.setName(originalFilename);
        saveFile.setType(type);
        saveFile.setSize(size / 1024);    //从 b 转为 kb
        saveFile.setUrl(url);
        saveFile.setMd5(md5);
        fileMapper.insert(saveFile);

        return url;

    }

    /**
     * 文件下载路径  http://localhost:9090/file/{fileUuid}
     **/

    @GetMapping("/{fileUuid}")
    public void download(@PathVariable String fileUuid, HttpServletResponse response) throws IOException {
//        根据文件的唯一标识码，获取文件
        File uploadFile = new File(fileUploadPth + fileUuid);
//        设置输出流的格式
        ServletOutputStream os = response.getOutputStream();
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileUuid, "UTF-8"));
        response.setContentType("application/octet-stream");

//        读取文件的字节流
        os.write(FileUtil.readBytes(uploadFile));
        os.flush();
        os.close();
    }

    @PostMapping("/enable")
    public Result save(@RequestBody Files files){
        return Result.success(fileMapper.updateById(files));
    }

    /**
     * 通过文件的 md5 查询文件
     *
     * @return com.example.vuemanage.domain.Files
     **/
    private Files getFileByMd5(String md5) {
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5", md5);
        List<Files> files = fileMapper.selectList(queryWrapper);

        return files.size() == 0 ? null : files.get(0);
    }
}
