package com.jian.system.service;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.jian.system.dao.SystemMapper;
import com.jian.system.dao.UserMapper;
import com.jian.system.entity.User;
import com.jian.system.utils.Utils;

import java.util.Date;
import java.util.List;

public class UserService {

    private UserMapper baseMapper;

    public UserService(Context context){
        baseMapper = new UserMapper(context);
    }

    public UserMapper getMapper(){
        return baseMapper;
    }

    public void deleteAll(){
        baseMapper.deleteAll();
    }

    public void insert(List<User> data){
        baseMapper.insert(data);
    }
}
