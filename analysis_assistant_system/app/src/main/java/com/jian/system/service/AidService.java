package com.jian.system.service;

import android.content.Context;

import com.jian.system.config.Constant;
import com.jian.system.dao.AidMapper;
import com.jian.system.dao.DictMapper;
import com.jian.system.dao.EquipAisMapper;
import com.jian.system.entity.Aid;
import com.jian.system.entity.Dict;
import com.jian.system.entity.EquipAis;
import com.jian.system.entity.User;
import com.jian.system.utils.LoginUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AidService {

    private AidMapper baseMapper;

    public AidService(Context context){
        baseMapper = new AidMapper(context);
    }


    public List<Map<String, Object>> aidAll() {
        return baseMapper.aidAll();
    }

    public List<Aid> selectPage(Map<String, Object> condition, User user, int start, int rows) {
        if(user == null){
            return new ArrayList<Aid>();
        }
        if(Constant.superGroupId.equals(user.getsUser_GroupID())) { //超管组查询所有航标
            return baseMapper.selectPageByUser(condition, null, start, rows);
        }
        return baseMapper.selectPageByUser(condition, user.getsUser_ID(), start, rows);
    }

    public long size(Map<String, Object> condition, User user) {
        if(user == null) {
            return 0;
        }
        condition = condition.isEmpty() ? null : condition;
        if(Constant.superGroupId.equals(user.getsUser_GroupID())) { //超管组查询所有航标
            return baseMapper.sizeByUser(condition, null);
        }
        return baseMapper.sizeByUser(condition, user.getsUser_ID());
    }

    public List<Aid> search(String keywords, User user) {
        if(Constant.superGroupId.equals(user.getsUser_GroupID())) { //超管组搜索所有航标
            return baseMapper.search(keywords, null);
        }
        return baseMapper.search(keywords, user.getsUser_ID());
    }

    public List<Map<String, Object>> equip(String sAid_ID) {
        return baseMapper.equip(sAid_ID);
    }

    public List<Map<String, Object>> aidMap() {
        return baseMapper.aidMap();
    }


    public AidMapper getMapper(){
        return baseMapper;
    }

    public void deleteAll(){
        baseMapper.deleteAll();
    }

    public void insert(List<Aid> data){
        baseMapper.insert(data);
    }
}
