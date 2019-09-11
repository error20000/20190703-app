package com.jian.system.service;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.jian.system.dao.EquipAisMapper;
import com.jian.system.dao.SystemMapper;
import com.jian.system.dao.UserMapper;
import com.jian.system.entity.EquipAis;
import com.jian.system.entity.User;
import com.jian.system.utils.Utils;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class EquipAisService {

    private EquipAisMapper baseMapper;

    public EquipAisService(Context context){
        baseMapper = new EquipAisMapper(context);
    }


    public EquipAis selectOne(Map<String, Object> condition){
        return baseMapper.selectOne(condition);
    }

    public EquipAisMapper getMapper(){
        return baseMapper;
    }

    public void deleteAll(){
        baseMapper.deleteAll();
    }

    public void insert(List<EquipAis> data){
        baseMapper.insert(data);
    }
}
