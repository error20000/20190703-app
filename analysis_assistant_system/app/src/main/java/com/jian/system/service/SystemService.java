package com.jian.system.service;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.jian.system.dao.SystemMapper;
import com.jian.system.dao.UserMapper;
import com.jian.system.entity.User;
import com.jian.system.utils.Utils;

import java.util.Date;
import java.util.List;

public class SystemService {

    private SystemMapper baseMapper;

    public SystemService(Context context){
        baseMapper = new SystemMapper(context);
    }


    public void syncData(){
        //baseMapper.
    }
}
