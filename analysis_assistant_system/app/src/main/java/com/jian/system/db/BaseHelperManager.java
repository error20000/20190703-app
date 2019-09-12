package com.jian.system.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jian.system.dao.AidEquipMapper;
import com.jian.system.dao.AidMapIconMapper;
import com.jian.system.dao.AidMapper;
import com.jian.system.dao.AidTypeMapIconMapper;
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
import com.jian.system.dao.MessagesMapper;
import com.jian.system.dao.NfcMapper;
import com.jian.system.dao.StoreMapper;
import com.jian.system.dao.StoreTypeMapper;
import com.jian.system.dao.SyncMapper;
import com.jian.system.dao.SystemMapper;
import com.jian.system.dao.UserAidMapper;
import com.jian.system.dao.UserMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class BaseHelperManager {

    private static final String TAG = BaseHelperManager.class.getSimpleName();
    private AtomicInteger counter = new AtomicInteger();
    private static BaseHelper baseHelper;
    private static BaseHelperManager instance;
    private static SQLiteDatabase db;

    public static BaseHelperManager getInstance(Context context, String dbName, int dbVersion) {
        if (instance == null) {
            instance = new BaseHelperManager();
            baseHelper = BaseHelper.getInstance(context, dbName,dbVersion);
        }
        return instance;
    }

    public static BaseHelperManager getInstance(Context context) {
        if (instance == null) {
            instance = new BaseHelperManager();
            baseHelper = BaseHelper.getInstance(context);
        }
        return instance;
    }

    public synchronized SQLiteDatabase getReadableDatabase(){
        if(counter.incrementAndGet() == 1){
            db = baseHelper.getReadableDatabase();
        }
        return db;
    }

    public synchronized SQLiteDatabase getWritableDatabase(){
        if(counter.incrementAndGet() == 1){
            db = baseHelper.getWritableDatabase();
        }
        return db;
    }

    public synchronized void close() {
        if(counter.decrementAndGet() == 0){
            baseHelper.close();
        }
    }
}
