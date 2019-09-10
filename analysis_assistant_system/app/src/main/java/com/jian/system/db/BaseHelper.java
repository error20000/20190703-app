package com.jian.system.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jian.system.dao.DictMapper;
import com.jian.system.dao.EquipAisMapper;
import com.jian.system.dao.EquipBatteryMapper;
import com.jian.system.dao.EquipLampMapper;
import com.jian.system.dao.EquipLogMapper;
import com.jian.system.dao.EquipMapper;
import com.jian.system.dao.EquipRadarMapper;
import com.jian.system.dao.EquipSolarEnergyMapper;
import com.jian.system.dao.EquipSpareLampMapper;
import com.jian.system.dao.EquipTelemetryMapper;
import com.jian.system.dao.EquipViceLampMapper;
import com.jian.system.dao.NfcMapper;
import com.jian.system.dao.StoreMapper;
import com.jian.system.dao.StoreTypeMapper;
import com.jian.system.dao.UserMapper;

import java.util.HashMap;
import java.util.Map;

public class BaseHelper extends SQLiteOpenHelper{

    private static final String TAG = BaseHelper.class.getSimpleName();
    private static Map<String, BaseHelper> dbMaps = new HashMap<String, BaseHelper>();
    private static String dbName = "aasystem.db";
    private static int dbVersion = 2;

    private BaseHelper(Context context, String dbName, int dbVersion) {
        super(context, dbName, null, dbVersion);
    }

    public static BaseHelper getInstance(Context context, String dbName, int dbVersion) {
        BaseHelper baseHelper = dbMaps.get(dbName);
        if (baseHelper == null) {
            baseHelper = new BaseHelper(context, dbName, dbVersion);
            dbMaps.put(dbName, baseHelper);
        }
        return baseHelper;
    }

    public static BaseHelper getInstance(Context context) {
        BaseHelper baseHelper = dbMaps.get(dbName);
        if (baseHelper == null) {
            baseHelper = new BaseHelper(context, dbName, dbVersion);
            dbMaps.put(dbName, baseHelper);
        }
        return baseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(UserMapper.createTable());
        sqLiteDatabase.execSQL(DictMapper.createTable());

        sqLiteDatabase.execSQL(EquipMapper.createTable());
        sqLiteDatabase.execSQL(EquipLogMapper.createTable());
        sqLiteDatabase.execSQL(EquipAisMapper.createTable());
        sqLiteDatabase.execSQL(EquipBatteryMapper.createTable());
        sqLiteDatabase.execSQL(EquipLampMapper.createTable());
        sqLiteDatabase.execSQL(EquipRadarMapper.createTable());
        sqLiteDatabase.execSQL(EquipSolarEnergyMapper.createTable());
        sqLiteDatabase.execSQL(EquipSpareLampMapper.createTable());
        sqLiteDatabase.execSQL(EquipTelemetryMapper.createTable());
        sqLiteDatabase.execSQL(EquipViceLampMapper.createTable());

        sqLiteDatabase.execSQL(StoreMapper.createTable());
        sqLiteDatabase.execSQL(StoreTypeMapper.createTable());

        sqLiteDatabase.execSQL(NfcMapper.createTable());
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        try {
            sqLiteDatabase.execSQL(UserMapper.dropTable());
            sqLiteDatabase.execSQL(DictMapper.dropTable());

            sqLiteDatabase.execSQL(EquipMapper.dropTable());
            sqLiteDatabase.execSQL(EquipLogMapper.dropTable());
            sqLiteDatabase.execSQL(EquipAisMapper.dropTable());
            sqLiteDatabase.execSQL(EquipBatteryMapper.dropTable());
            sqLiteDatabase.execSQL(EquipLampMapper.dropTable());
            sqLiteDatabase.execSQL(EquipRadarMapper.dropTable());
            sqLiteDatabase.execSQL(EquipSolarEnergyMapper.dropTable());
            sqLiteDatabase.execSQL(EquipSpareLampMapper.dropTable());
            sqLiteDatabase.execSQL(EquipTelemetryMapper.dropTable());
            sqLiteDatabase.execSQL(EquipViceLampMapper.dropTable());

            sqLiteDatabase.execSQL(StoreMapper.dropTable());
            sqLiteDatabase.execSQL(StoreTypeMapper.dropTable());

            sqLiteDatabase.execSQL(NfcMapper.dropTable());
        } catch (Exception e){

        }
        Log.d(TAG, "onUpgrade");

        onCreate(sqLiteDatabase);
    }
}
