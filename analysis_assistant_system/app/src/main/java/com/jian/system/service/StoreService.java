package com.jian.system.service;

import android.content.Context;

import com.jian.system.dao.StoreMapper;
import com.jian.system.dao.StoreTypeMapper;
import com.jian.system.entity.Equip;
import com.jian.system.entity.Store;
import com.jian.system.entity.StoreType;
import com.jian.system.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreService {

    private StoreMapper baseMapper;
    private StoreTypeService typeService;
    private EquipService equipService;

    public StoreService(Context context){
        baseMapper = new StoreMapper(context);
        typeService = new StoreTypeService(context);
        equipService = new EquipService(context);
    }

    public List<Store> storeList(String parent) {

        List<Store> list = new ArrayList<>();

        if(Utils.isNullOrEmpty(parent)) {
            list = baseMapper.selectAll();
        }else {
            Map<String, Object> condition = new HashMap<String, Object>();
            condition.put("sStore_Parent", parent);
            list = baseMapper.selectList(condition);
        }
        return list;
    }

    public List<StoreType> selectAllType() {
        return typeService.selectAll();
    }

    public List<Map<String, Object>> storeMap() {
        return typeService.storeMap();
    }

    public List<Equip> appEquip(String sEquip_StoreLv1, String sEquip_StoreLv2, String sEquip_StoreLv3, String sEquip_StoreLv4) {
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("sEquip_StoreLv1", sEquip_StoreLv1);
        condition.put("sEquip_StoreLv2", sEquip_StoreLv2);
        condition.put("sEquip_StoreLv3", sEquip_StoreLv3);
        condition.put("sEquip_StoreLv4", sEquip_StoreLv4);
        return equipService.selectList(condition);
    }
    public StoreMapper getMapper(){
        return baseMapper;
    }

    public void deleteAll(){
        baseMapper.deleteAll();
    }

    public void insert(List<Store> data){
        baseMapper.insert(data);
    }
}
