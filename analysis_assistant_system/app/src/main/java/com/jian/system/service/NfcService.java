package com.jian.system.service;

import android.content.Context;

import com.jian.system.dao.EquipViceLampMapper;
import com.jian.system.dao.NfcMapper;
import com.jian.system.entity.EquipViceLamp;
import com.jian.system.entity.Nfc;

import java.util.List;
import java.util.Map;

public class NfcService {

    private NfcMapper baseMapper;

    public NfcService(Context context){
        baseMapper = new NfcMapper(context);
    }


    public NfcMapper getMapper(){
        return baseMapper;
    }

    public void deleteAll(){
        baseMapper.deleteAll();
    }

    public void insert(List<Nfc> data){
        baseMapper.insert(data);
    }
}
