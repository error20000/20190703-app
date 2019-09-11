package com.jian.system.service;

import android.content.Context;

import com.jian.system.dao.DictMapper;
import com.jian.system.dao.SystemMapper;
import com.jian.system.entity.Dict;
import com.jian.system.entity.System;

import java.util.List;

public class DictService {

    private DictMapper baseMapper;

    public DictService(Context context){
        baseMapper = new DictMapper(context);
    }

    public DictMapper getMapper(){
        return baseMapper;
    }

    public void deleteAll(){
        baseMapper.deleteAll();
    }

    public void insert(List<Dict> data){
        baseMapper.insert(data);
    }
}
