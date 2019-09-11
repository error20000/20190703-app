package com.jian.system.service;

import android.content.Context;

import com.jian.system.dao.UserAidMapper;
import com.jian.system.entity.UserAid;

import java.util.List;

public class UserAidService {

    private UserAidMapper baseMapper;

    public UserAidService(Context context){
        baseMapper = new UserAidMapper(context);
    }

    public UserAidMapper getMapper(){
        return baseMapper;
    }

    public void deleteAll(){
        baseMapper.deleteAll();
    }

    public void insert(List<UserAid> data){
        baseMapper.insert(data);
    }
}
