package com.jian.system.nfc;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONObject;
import com.jian.system.Application;
import com.jian.system.R;
import com.jian.system.config.UrlConfig;
import com.jian.system.fragment.components.EquipDetailFragment;
import com.jian.system.utils.HttpUtils;
import com.jian.system.utils.NfcUtils;
import com.jian.system.utils.ThreadUtils;
import com.jian.system.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;

public class NfcActivity extends BaseNfcActivity {

    public static final String NFC_RESULT = "nfc_result";
    public static final String NFC_TYPE = "nfc_type";
    public static final String NFC_ID = "nfc_id";

    public static final String NFC_TYPE_SEARCH = "nfc_type_search";
    public static final String NFC_TYPE_ADD = "nfc_type_add";
    public static final String NFC_TYPE_SCAN = "nfc_type_scan";

    private String nfcType = "";
    private String nfcId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_read);

        Bundle bundle = getIntent().getExtras();
        nfcType = bundle.getString(NFC_TYPE);

        nfcId = NfcUtils.readNFCId(getIntent());
        if(!Utils.isNullOrEmpty(nfcId)){
            nfcFind(nfcId);
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        //查询nfcId
        String id = NfcUtils.readNFCId(intent);
        intent.putExtra(NFC_RESULT, id);
        setResult(RESULT_OK, intent);

        if(Utils.isNullOrEmpty(nfcType)){
            nfcType = NFC_TYPE_SCAN;
        }
        switch (nfcType){
            case NFC_TYPE_SEARCH:
            case NFC_TYPE_ADD:
                finish();
                break;
            case NFC_TYPE_SCAN:
                //查询NFC
                nfcFind(id);
                break;
        }
    }
    private void nfcFind(String sNfc_NO){
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<>();
                params.put("sNfc_NO", sNfc_NO);
                String res = HttpUtils.getInstance().sendPost(UrlConfig.equipQueryNfcUrl, params);
                Message msg = mHandler.obtainMessage();
                msg.obj = res;
                mHandler.sendMessage(msg);
            }
        });
    }

    //TODO ------------------------------------------------------------------------------ Handler
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            //处理结果
            String str =  (String) msg.obj;
            if(Utils.isNullOrEmpty(str)){
                showToast("网络异常，请检查网络。");
                return;
            }
            JSONObject resData = JSONObject.parseObject(str);
            //处理数据
            handleNFC(resData);
        }
    };

    private void handleNFC(JSONObject resObj){
        JSONObject resData = resObj.getJSONObject("data");

        if(resData == null){
            Toast.makeText(this, "未查询到器材信息", Toast.LENGTH_SHORT).show();
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString("id", resData.getString("sEquip_ID"));
        bundle.putString("from", "nfc");
        EquipDetailFragment fragment = new EquipDetailFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.nfc_container, fragment, "EquipDetailFragment")
                .addToBackStack("EquipDetailFragment")
                .commit();
    }

    private void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
