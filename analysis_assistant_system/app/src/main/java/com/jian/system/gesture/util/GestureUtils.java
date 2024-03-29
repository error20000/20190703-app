package com.jian.system.gesture.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class GestureUtils {

    private static final String DEFAULT_SP_NAME = "user_info";

    public static final String TEST_BASE_URL = "test_base_url";

    public static final String USER_GESTURE = "user_gesture_str";
    public static final String USER_TOEKN = "user_token_str";
    public static final String USER_INFO = "user_info_str";
    public static final String USER_USERNAME = "user_username";
    public static final String USER_PASSWORD = "user_password";
    public static final String USER_LOCK_SCREEN_TIME = "user_lock_screen_time";
    public static final String USER_LOCK_SCREEN_TIME_DEF = "60"; //默认60秒


    public static final String Gesture_Model_Type = "modelType";
    public static final String Gesture_Model_Type_Lock_Screen = "lock_screen";
    public static final String Gesture_Model_Type_Change_Pwd = "change_pwd";
    public static final String Gesture_Model_Type_Reset_Pwd = "reset_pwd";


    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(DEFAULT_SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.clear();
        edit.commit();
    }

    public static void remove(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(DEFAULT_SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.remove(key);
        edit.commit();
    }

    public static void set(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(DEFAULT_SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static String get(Context context, String key) {
        return get(context, key, null);
    }

    public static String get(Context context, String key, String defValue) {
        SharedPreferences sp = context.getSharedPreferences(DEFAULT_SP_NAME, Context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }

}
