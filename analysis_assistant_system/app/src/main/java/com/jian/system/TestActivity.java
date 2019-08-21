
package com.jian.system;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.jian.system.fragment.TestFragment;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TestActivity extends AppCompatActivity {


    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;
    @BindView(R.id.button)
    Button mButton;

    private final static String TAG = NfcActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View rootView = LayoutInflater.from(this).inflate(R.layout.test_main, null);
        ButterKnife.bind(this, rootView);
        rootView.setId(getContentId());

        initTopBar();
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TestFragment fragment = new TestFragment();

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(getContentId(), fragment, "testmain")
                        .addToBackStack("testmain")
                        .commit();
            }
        });

        setContentView(rootView);
    }

    private int getContentId(){
        return R.id.main;
    }

    private void initTopBar() {

        mTopBar.setTitle("test电子地图");
    }


}
