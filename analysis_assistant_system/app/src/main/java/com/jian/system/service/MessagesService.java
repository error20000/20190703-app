package com.jian.system.service;

import android.content.Context;

import com.jian.system.config.Constant;
import com.jian.system.dao.AidMapper;
import com.jian.system.dao.MessagesMapper;
import com.jian.system.entity.Aid;
import com.jian.system.entity.Messages;
import com.jian.system.entity.User;
import com.jian.system.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessagesService {

    private MessagesMapper baseMapper;

    public MessagesService(Context context){
        baseMapper = new MessagesMapper(context);
    }

    public List<Messages> selectPage(Map<String, Object> condition, Date startDate, Date endDate, User user, int start, int rows) {
        if(user == null) {
            return new ArrayList<>();
        }
        if(Constant.superGroupId.equals(user.getsUser_GroupID())) { //超管组查询所有消息
            return baseMapper.selectPageByUser(condition, startDate, endDate, null, start, rows);
        }
        return baseMapper.selectPageByUser(condition, startDate, endDate, user.getsUser_ID(), start, rows);
    }

    public long size(Map<String, Object> condition, Date startDate, Date endDate, User user) {
        if(user == null) {
            return 0;
        }
        if(Constant.superGroupId.equals(user.getsUser_GroupID())) { //超管组查询所有消息
            return baseMapper.sizeByUser(condition, startDate, endDate, null);
        }
        return baseMapper.sizeByUser(condition, startDate, endDate, user.getsUser_ID());
    }


    public List<Messages> search(String keywords, User user) {
        if(user == null){
            return new ArrayList<>();
        }
        if(Constant.superGroupId.equals(user.getsUser_GroupID())) { //超管组搜索所有消息
            return baseMapper.search(keywords, null);
        }
        return baseMapper.search(keywords, user.getsUser_ID());
    }


    public Map<String, Object> view(String sMsg_ID, User user) {
        if(user == null){
            return new HashMap<>();
        }
        if(Utils.isNullOrEmpty(sMsg_ID)) {
            return new HashMap<>();
        }
        if(Constant.superGroupId.equals(user.getsUser_GroupID())) { //超管组搜索所有消息
            return baseMapper.view(sMsg_ID, null);
        }
        return baseMapper.view(sMsg_ID, user.getsUser_ID());
    }

}
