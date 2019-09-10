package com.jian.system.service;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.jian.system.dao.SystemMapper;
import com.jian.system.dao.UserMapper;
import com.jian.system.entity.System;
import com.jian.system.entity.User;
import com.jian.system.utils.Utils;

import java.util.Date;
import java.util.List;

public class SystemService {

    private SystemMapper baseMapper;

    public SystemService(Context context){
        baseMapper = new SystemMapper(context);
    }

    public SystemMapper getMapper(){
        return baseMapper;
    }

    public List<System> selectAll(){
        return baseMapper.selectAll();
    }

    public void deleteAll(){
        baseMapper.deleteAll();
    }

    public void insert(List<System> data){
        baseMapper.insert(data);
    }
}
