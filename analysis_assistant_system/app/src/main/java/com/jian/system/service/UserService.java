package com.jian.system.service;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.jian.system.dao.UserMapper;
import com.jian.system.entity.User;
import com.jian.system.utils.Utils;

import java.util.Date;
import java.util.List;

public class UserService {

    private UserMapper userMapper;

    public UserService(Context context){
        userMapper = new UserMapper(context);
    }

    public void insert(){
        User test = new User();
        test.setdUser_CreateDate(new Date());
        test.setsUser_ID(Utils.newSnowflakeIdStr());
        test.setlUser_StatusFlag(1);
        test.setsUser_PassWord(Utils.md5("123321"));
        test.setsUser_Nick("test");
        userMapper.insert(test);
    }

    public void select(){
        List<User> res = userMapper.selectAll();
        System.out.println(JSON.toJSON(res));
    }
}
