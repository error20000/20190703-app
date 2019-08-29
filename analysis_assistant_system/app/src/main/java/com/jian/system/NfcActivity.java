
package com.jian.system;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jian.system.fragment.components.EquipListFragment;
import com.jian.system.gesture.GesturePwdCheckActivity;
import com.jian.system.gesture.GesturePwdResetActivity;
import com.jian.system.gesture.GesturePwdSettingActivity;
import com.jian.system.utils.NfcUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;


public class NfcActivity extends Activity {

    private final static String TAG = NfcActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);

        findViewById(R.id.tv_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NfcActivity.this, GesturePwdSettingActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.tv_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NfcActivity.this, GesturePwdCheckActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.tv_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NfcActivity.this, GesturePwdResetActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        NfcAdapter mNfcAdapter= NfcAdapter.getDefaultAdapter(this);//设备的NfcAdapter对象
        if(mNfcAdapter==null){//判断设备是否支持NFC功能
            Toast.makeText(this,"设备不支持NFC功能!",Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (!mNfcAdapter.isEnabled()){//判断设备NFC功能是否打开
            /*Toast.makeText(this,"请到系统设置中打开NFC功能!",Toast.LENGTH_SHORT).show();
            finish();*/
            Intent setNfc= new Intent(Settings.ACTION_NFC_SETTINGS);
            startActivity(setNfc);
            return;
        }
        Toast.makeText(this,"支持NFC功能!",Toast.LENGTH_SHORT).show();
        NfcUtils.mPendingIntent = PendingIntent.getActivity(this,0,new Intent(this,getClass()),0);//创建PendingIntent对象,当检测到一个Tag标签就会执行此Intent
    }



    //在onResume中开启前台调度
    @Override
    protected void onResume() {
        super.onResume();
        //设定intentfilter和tech-list。如果两个都为null就代表优先接收任何形式的TAG action。也就是说系统会主动发TAG intent。
        if (NfcUtils.mNfcAdapter != null) {
            NfcUtils.mNfcAdapter.enableForegroundDispatch(this, NfcUtils.mPendingIntent, NfcUtils.mIntentFilter, NfcUtils.mTechList);
        }
    }


    //在onNewIntent中处理由NFC设备传递过来的intent
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e(TAG, "--------------NFC-------------" );
        Tag mTag=intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);//获取到Tag标签对象
        String[] techList=mTag.getTechList();
        System.out.println("标签支持的tachnology类型：");
        for (String tech:techList){
            System.out.println(tech);

        }

        processIntent(intent);
    }

    //  这块的processIntent() 就是处理卡中数据的方法
    public void processIntent(Intent intent) {
        Parcelable[] rawmsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage msg = (NdefMessage) rawmsgs[0];
        NdefRecord[] records = msg.getRecords();
        String resultStr = new String(records[0].getPayload());
        // 返回的是NFC检查到卡中的数据
        Log.e(TAG, "processIntent: "+resultStr );
        try {
            // 检测卡的id
            String id = NfcUtils.readNFCId(intent);
            Log.e(TAG, "processIntent--id: "+id );
            // NfcUtils中获取卡中数据的方法
            String result = NfcUtils.readNFCFromTag(intent);
            Log.e(TAG, "processIntent--result: "+result );
            // 往卡中写数据
            //ToastUtils.showLong(getActivity(),result);
            String data = "this.is.write";
            NfcUtils.writeNFCToTag(data,intent);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (NfcUtils.mNfcAdapter != null) {
            NfcUtils.mNfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NfcUtils.mNfcAdapter = null;
    }
}
