
package com.jian.system;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.jian.system.nfc.NfcActivity;
import com.jian.system.utils.NfcUtils;
import com.jian.system.utils.Utils;


public class LauncherActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

        if(getIntent() != null){
            String nfcId = NfcUtils.readNFCId(getIntent());
            if(!Utils.isNullOrEmpty(nfcId)){
                Log.e("ffffffffffff", "dddddddddddd");
                //Toast.makeText(this, "ddddddddddddd", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, NfcActivity.class);
                intent.putExtra(NfcActivity.NFC_TYPE, NfcActivity.NFC_TYPE_SCAN);
                intent.putExtra(NfcActivity.NFC_ID, nfcId);
                startActivity(intent);
                finish();
                return;
            }
        }

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
