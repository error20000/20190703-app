package com.jian.system.service;

import android.content.Context;

import com.jian.system.dao.EquipAisMapper;
import com.jian.system.dao.EquipLogMapper;
import com.jian.system.entity.EquipAis;
import com.jian.system.entity.EquipLog;

import java.util.List;
import java.util.Map;

public class EquipLogService {

    private EquipLogMapper baseMapper;

    public EquipLogService(Context context){
        baseMapper = new EquipLogMapper(context);
    }


    public List<EquipLog> selectList(Map<String, Object> condition){
        return baseMapper.selectList(condition);
    }
}
