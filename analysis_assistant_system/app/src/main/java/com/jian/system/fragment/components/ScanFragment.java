
package com.jian.system.fragment.components;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONObject;
import com.jian.system.Application;
import com.jian.system.R;
import com.jian.system.config.UrlConfig;
import com.jian.system.nfc.NfcActivity;
import com.jian.system.utils.HttpUtils;
import com.jian.system.utils.ThreadUtils;
import com.jian.system.utils.Utils;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.sonnyjack.library.qrcode.QrCodeUtils;
import com.sonnyjack.permission.IRequestPermissionCallBack;
import com.sonnyjack.permission.PermissionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ScanFragment extends QMUIFragment {

    private final static String TAG = ScanFragment.class.getSimpleName();

    @Override
    protected View onCreateView() {

        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.activity_empty, null);
        rootView.setVisibility(View.GONE);

        scanQrCode();

        return rootView;
    }

    /**
     * 扫描二维码（先请求权限，用第三方库）
     */
    private void scanQrCode() {
        ArrayList<String> permissionList = new ArrayList<>();
        permissionList.add(Manifest.permission.CAMERA);
        PermissionUtils.getInstances().requestPermission(getActivity(), permissionList, new IRequestPermissionCallBack() {
            @Override
            public void onGranted() {
                //扫描二维码/条形码
                //QrCodeUtils.startScan(getActivity(), Application.Scan_Search_Request_Code);
                Intent intent = QrCodeUtils.createScanQrCodeIntent(getActivity());
                startActivityForResult(intent, Application.Scan_Search_Request_Code);
            }

            @Override
            public void onDenied() {
                Toast.makeText(getActivity(), "请在应用管理中打开拍照权限", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case Application.Scan_Search_Request_Code:
                    String equipNO = QrCodeUtils.getScanResult(data);
                    Log.d("onActivityResult", equipNO);
                    //查询数据
                    scanFind(equipNO);
                    break;
            }
        }
    }


    private void scanFind(String sEquip_NO){
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<>();
                params.put("sEquip_NO", sEquip_NO);
                String res = HttpUtils.getInstance().sendPost(UrlConfig.equipQueryScanUrl, params);
                Message msg = mHandler.obtainMessage();
                msg.obj = res;
                mHandler.sendMessage(msg);
            }
        });
    }

    private void intoDetail(String sEquip_ID){
        Bundle bundle = new Bundle();
        bundle.putString("id", sEquip_ID);
        bundle.putString("from", "scan");
        EquipDetailFragment fragment = new EquipDetailFragment();
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .add(R.id.main, fragment, "EquipDetailFragment")
                .addToBackStack("EquipDetailFragment")
                .commit();
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
            Toast.makeText(getActivity(), "未查询到器材信息", Toast.LENGTH_SHORT).show();
            return;
        }
        //进入详情
        intoDetail(resData.getString("sEquip_ID"));
    }

    private void showToast(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}
