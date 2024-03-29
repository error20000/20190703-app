package com.jian.system.service;

import android.content.Context;

import com.jian.system.dao.EquipAisMapper;
import com.jian.system.dao.EquipLogMapper;
import com.jian.system.dao.SystemMapper;
import com.jian.system.entity.EquipAis;
import com.jian.system.entity.EquipLog;

import java.util.List;
import java.util.Map;

public class EquipLogService {

    private EquipLogMapper baseMapper;

    public EquipLogService(Context context){
        baseMapper = new EquipLogMapper(context);
    }

    public EquipLogMapper getMapper(){
        return baseMapper;
    }

    public List<EquipLog> selectList(Map<String, Object> condition){
        return baseMapper.selectList(condition);
    }

    public void deleteAll(){
        baseMapper.deleteAll();
    }

    public void insert(List<EquipLog> data){
        baseMapper.insert(data);
    }
}
