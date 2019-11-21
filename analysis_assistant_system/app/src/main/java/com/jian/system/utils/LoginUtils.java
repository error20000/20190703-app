package com.jian.system.utils;


import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.jian.system.entity.User;
import com.jian.system.gesture.util.GestureUtils;

public class LoginUtils {



    public static User getLoginUser(Context context){
        User user = null;
        String userInfo = GestureUtils.get(context, GestureUtils.USER_INFO);
        if(!Utils.isNullOrEmpty(userInfo)){
            user = JSONObject.parseObject(userInfo, User.class);
        }
        return user;
    }

}
