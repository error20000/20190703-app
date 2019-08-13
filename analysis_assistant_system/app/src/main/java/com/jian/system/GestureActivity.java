
package com.jian.system;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jian.system.gesture.GesturePwdCheckActivity;
import com.jian.system.gesture.GesturePwdResetActivity;
import com.jian.system.gesture.GesturePwdSettingActivity;


public class GestureActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);

        findViewById(R.id.tv_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GestureActivity.this, GesturePwdSettingActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.tv_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GestureActivity.this, GesturePwdCheckActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.tv_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GestureActivity.this, GesturePwdResetActivity.class);
                startActivity(intent);
            }
        });
    }
}
