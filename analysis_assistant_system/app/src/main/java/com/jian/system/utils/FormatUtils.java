package com.jian.system.utils;

import com.alibaba.fastjson.JSONObject;
import com.jian.system.entity.Dict;
import com.jian.system.entity.Nfc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class FormatUtils {

    public static final String Dict_Name = "Name";
    public static final String Dict_Describe = "Describe";
    public static final String Dict_ID = "ID";
    public static final String Dict_Link = "Link";
    public static final String Dict_Picture = "Picture";
    public static final String Dict_Color = "Color";
    public static final String Dict_UserID = "UserID";
    public static final String Dict_UpdateUserID = "UpdateUserID";

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


    public static String formatDictCustom(String sDict_NO, List<Dict> data, String attr){
        if(Utils.isNullOrEmpty(sDict_NO) || Utils.isNullOrEmpty(attr)){
            return "";
        }
        if(data == null || data.size() == 0){
            return "";
        }
        String name = "";
        for(int i = 0; i < data.size(); i++){
            Dict node = data.get(i);
            if(node.getsDict_NO().equals(sDict_NO)){
                switch (attr){
                    case "Name":
                        name = node.getsDict_Name();
                        break;
                    case "Describe":
                        name = node.getsDict_Describe();
                        break;
                    case "ID":
                        name = node.getsDict_ID();
                        break;
                    case "Link":
                        name = node.getsDict_Link();
                        break;
                    case "Picture":
                        name = node.getsDict_Picture();
                        break;
                    case "Color":
                        name = node.getsDict_Color();
                        break;
                    case "UserID":
                        name = node.getsDict_UserID();
                        break;
                    case "UpdateUserID":
                        name = node.getsDict_UpdateUserID();
                        break;
                }
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

    public static Date formatDate(String formatStr, String strDate){
        if(Utils.isNullOrEmpty(strDate)){
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
        try {
            return sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formatDegree(double degree){
        return formatDegree(String.valueOf(degree));
    }

    public static String formatDegree(String degree){
        String[] array = degree.split("[.]");
        String degrees = array[0];//得到度

        Double m = Double.parseDouble("0." + array[1]) * 60;
        String[] array1 = m.toString().split("[.]");
        String minutes = array1[0];//得到分

        Double s = Double.parseDouble("0."+array1[1]) * 60;
        String[] array2 = s.toString().split("[.]");
        String seconds = array2[0];//得到秒
        return degrees+"°"+minutes+"'"+seconds+"\"";
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
