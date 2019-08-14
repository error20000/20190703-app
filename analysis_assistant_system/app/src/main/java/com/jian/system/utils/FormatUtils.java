package com.jian.system.utils;

import com.alibaba.fastjson.JSONObject;
import com.jian.system.entity.Dict;
import com.jian.system.entity.Nfc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class FormatUtils {

    //TODO ----------------------------------------------------------------------------normal

    public static String formatDict(String sDict_NO, List<Dict> data){
        if(Utils.isNullOrEmpty(sDict_NO)){
            return "";
        }
        if(data == null || data.size() == 0){
            return "";
        }
        String name = "";
        for(int i = 0; i < data.size(); i++){
            Dict node = data.get(i);
            if(node.getsDict_NO().equals(sDict_NO)){
                name = node.getsDict_Name();
                break;
            }
        }
        return name;
    }

    public static String formatDictDesc(String sDict_NO, List<Dict> data){
        if(Utils.isNullOrEmpty(sDict_NO)){
            return "";
        }
        if(data == null || data.size() == 0){
            return "";
        }
        String name = "";
        for(int i = 0; i < data.size(); i++){
            Dict node = data.get(i);
            if(node.getsDict_NO().equals(sDict_NO)){
                name = node.getsDict_Describe();
                break;
            }
        }
        return name;
    }

    public static String formatDate(String formatStr, Date date){
        if(Utils.isNullOrEmpty(formatStr) || date == null){
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
        return sdf.format(date);
    }


    //TODO ----------------------------------------------------------------------------person

    public static String formatNFC(String sNfc_ID, List<Nfc> data){
        if(Utils.isNullOrEmpty(sNfc_ID)){
            return "";
        }
        if(data == null || data.size() == 0){
            return "";
        }
        String name = "";
        for(int i = 0; i < data.size(); i++){
            Nfc node = data.get(i);
            if(node.getsNfc_ID().equals(sNfc_ID)){
                name = node.getsNfc_Name();
                break;
            }
        }
        return name;
    }

    public static String formatAidJSONObject(String sAid_ID, List<JSONObject> data){
        if(Utils.isNullOrEmpty(sAid_ID)){
            return "";
        }
        if(data == null || data.size() == 0){
            return "";
        }
        String name = "";
        for(int i = 0; i < data.size(); i++){
            JSONObject node = data.get(i);
            if(sAid_ID.equals(node.getString("sAid_ID"))){
                name = node.getString("sAid_Name");
                break;
            }
        }
        return name;
    }
}
