package com.jian.system.dao;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.zxing.common.StringUtils;
import com.jian.system.db.BaseHelper;

import java.util.Arrays;
import java.util.stream.Collectors;

public class UserMapper {

    public static String tableName = "tBase_User";

    private BaseHelper baseHelper;

    public UserMapper(){
        baseHelper = BaseHelper.getInstance();
    }

    @SuppressLint("NewApi")
    public static String createTable(){

        String[] strs = {

        };

        return String.join(" ", strs);
    }

    private static String dropTable(){
        return "drop table if exists " +  tableName;
    }
}
