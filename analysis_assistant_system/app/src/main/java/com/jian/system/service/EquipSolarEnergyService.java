package com.jian.system.service;

import android.content.Context;

import com.jian.system.dao.EquipRadarMapper;
import com.jian.system.dao.EquipSolarEnergyMapper;
import com.jian.system.entity.EquipRadar;
import com.jian.system.entity.EquipSolarEnergy;

import java.util.List;
import java.util.Map;

public class EquipSolarEnergyService {

    private EquipSolarEnergyMapper baseMapper;

    public EquipSolarEnergyService(Context context){
        baseMapper = new EquipSolarEnergyMapper(context);
    }


    public EquipSolarEnergy selectOne(Map<String, Object> condition){
        return baseMapper.selectOne(condition);
    }

    public EquipSolarEnergyMapper getMapper(){
        return baseMapper;
    }

    public void deleteAll(){
        baseMapper.deleteAll();
    }

    public void insert(List<EquipSolarEnergy> data){
        baseMapper.insert(data);
    }
}
