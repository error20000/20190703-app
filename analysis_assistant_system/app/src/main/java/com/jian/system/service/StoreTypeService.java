package com.jian.system.service;

import android.content.Context;

import com.jian.system.dao.StoreMapper;
import com.jian.system.dao.StoreTypeMapper;
import com.jian.system.entity.Equip;
import com.jian.system.entity.Store;
import com.jian.system.entity.StoreType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreTypeService {

    private StoreTypeMapper baseMapper;

    public StoreTypeService(Context context){
        baseMapper = new StoreTypeMapper(context);
    }

    public List<StoreType> selectAll() {
        return baseMapper.selectAll();
    }

    public List<Map<String, Object>> storeMap() {
        return baseMapper.storeMap();
    }

}
