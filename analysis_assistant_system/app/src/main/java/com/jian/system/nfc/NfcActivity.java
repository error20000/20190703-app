package com.jian.system.nfc;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.jian.system.Application;
import com.jian.system.R;
import com.jian.system.utils.NfcUtils;

public class NfcActivity extends BaseNfcActivity {

    public static final String NFC_RESULT = "nfc_result";
    public static final String NFC_TYPE = "nfc_type";

    public static final String NFC_TYPE_SEARCH = "nfc_type_search";
    public static final String NFC_TYPE_ADD = "nfc_type_add";
    public static final String NFC_TYPE_SCAN = "nfc_type_scan";

    private String nfcType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_nfc_read);

        Bundle bundle = getIntent().getExtras();
        nfcType = bundle.getString(NFC_TYPE);
        Log.e("ddddddddddddddd", nfcType);
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        //查询nfcId
        intent.putExtra(NFC_RESULT, NfcUtils.readNFCId(intent));
        setResult(RESULT_OK, intent);

        switch (nfcType){
            case NFC_TYPE_SEARCH:
            case NFC_TYPE_ADD:
                finish();
                break;
            case NFC_TYPE_SCAN:

                break;
        }
    }

}
