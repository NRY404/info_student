package com.example.vuemanage.service;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.vuemanage.common.Constants;
import com.example.vuemanage.domain.dto.UserLogin;
import com.example.vuemanage.domain.User;
import com.example.vuemanage.exception.ServiceException;
import com.example.vuemanage.mapper.UserMapper;
import com.example.vuemanage.utils.TokenUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@Service
public class UserService extends ServiceImpl<UserMapper,User> {

    private static final Log LOG = Log.get();

//    登录
    /**
     * 方法一：
     * @return UserLogin
     **/
    public UserLogin login(UserLogin userLogin) {

            /*
             * 方法二：
             *
             **/
    //        User one = getOne(queryWrapper);    //当数据中查出的数据 是重复的字段名时，这种方法不适用
    //            List<User> list = list(queryWrapper);
    //            return  list.size() != 0;

        User one = getUserInfo(userLogin);

        if (one != null){
                BeanUtil.copyProperties(one,userLogin,true);
                //设置 Token
            String token = TokenUtils.genToken(one.getId().toString(), one.getPassword());
            userLogin.setToken(token);
            return userLogin;
            } else {
                throw new  ServiceException(Constants.CODE_600,"用户名或密码错误");
            }
    }

    //  注册
    public User register(UserLogin userLogin) {

        User one = getUserInfo(userLogin);
        if (one == null){
            one = new User();
            BeanUtil.copyProperties(userLogin,one,true);
            save(one);  // 把 copy 完之后的用户对象存储到 数据库
        } else {
            throw new  ServiceException(Constants.CODE_600,"用户名已存在");
        }

        return one;
    }

//  添加用户
    public boolean saveUser(User user) {
        /*
        if (user.getId() == null){
            return save(user);//该save 方法为 Mybatis中 ServiceImpl 提供的，用于插入数据
        }else {
            return updateById(user);
        }
        */
        return saveOrUpdate(user);  // 相当于 以上判断的 方法
    }


    public boolean importUsers(MultipartFile file) throws Exception{
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
//        List<UserLogin> list = reader.readAll(UserLogin.class);
        List<User> list = reader.read(0, 1, User.class);

        saveBatch(list);

        return true;
    }


    private User getUserInfo(UserLogin userLogin){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userLogin.getUsername());
        queryWrapper.eq("password", userLogin.getPassword());

        User one;
        try {
            one = getOne(queryWrapper);    //从数据库中 查询用户信息
        } catch (Exception e) {
            LOG.error(e);
            throw new  ServiceException(Constants.CODE_500,"系统错误");
        }
        return one;
    }



//    @Resource
//    private UserMapper userMapper;

//    添加或更新
/*    public int save(UserLogin user){

        if (user.getId() == null){  //user没有id，则进行新增
            return userMapper.insert(user);
        }else {
            return userMapper.update(user);
        }
    }
*/
//    删除数据根据 id
//    public int deleteById(int id){
//        return userMapper.deleteById(id);
//    }

////    根据 id 分页查询
//    public int findById(Integer pageNum, Integer pageSize) {
//        pageNum = (pageNum - 1)* pageSize;
//        userMapper.findById(pageNum,pageSize);
//
//    }
}
