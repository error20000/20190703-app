
package com.jian.system;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class LauncherActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        Intent intent = new Intent(this, TestReadNfcActivity.class);
        startActivity(intent);
        finish();
    }
}
