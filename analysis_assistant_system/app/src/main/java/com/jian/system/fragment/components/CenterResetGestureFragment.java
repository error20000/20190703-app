
package com.jian.system.fragment.components;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.jian.system.LoginActivity;
import com.jian.system.R;
import com.jian.system.gesture.GesturePwdSettingActivity;
import com.jian.system.gesture.util.GestureUtils;
import com.jian.system.utils.Utils;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;


import butterknife.BindView;
import butterknife.ButterKnife;


public class CenterResetGestureFragment extends QMUIFragment {

    private final static String TAG = CenterResetGestureFragment.class.getSimpleName();
    private String title = "重置手势密码";
    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;

    private QMUITipDialog tipDialog;
    private final int MsgType_ChangePwd = 1;

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;
    @BindView(R.id.old_password)
    EditText mEditText;
    @BindView(R.id.new_password)
    EditText mEditText1;
    @BindView(R.id.new_password_2)
    EditText mEditText2;

    @BindView(R.id.old_password_label)
    TextView mTextView;
    @BindView(R.id.new_password_label)
    TextView mTextView1;
    @BindView(R.id.new_password_label_2)
    TextView mTextView2;

    @Override
    protected View onCreateView() {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_change_pwd, null);
        ButterKnife.bind(this, rootView);

        initTopBar();
        //隐藏新密码
        mEditText1.setVisibility(View.GONE);
        mTextView1.setVisibility(View.GONE);
        mEditText2.setVisibility(View.GONE);
        mTextView2.setVisibility(View.GONE);
        //修改提示
        mTextView.setText("登录密码");
        mEditText.setHint("请输入登录密码");
        //获得焦点并弹出软键盘
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        mEditText.postDelayed(new Runnable() {
            @Override
            public void run() {
                mEditText.requestFocus();
                inputMethodManager.showSoftInput(mEditText, 0);
            }
        }, 300);

        return rootView;
    }

    private void initTopBar() {
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭软键盘
                InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);

                popBackStack();
            }
        });
        mTopBar.addRightTextButton("下一步", R.id.topbar_right_about_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //关闭软键盘
                InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);

                boolean flag = checkEditText();
                if(!flag){
                    return;
                }
                sendResetGesture();            }
        });
        mTopBar.setTitle(title);
    }

    private boolean checkEditText(){
        String text = mEditText.getText().toString();
        //效验原密码
        if(Utils.isNullOrEmpty(text)){
            showTips("登录密码不能为空", QMUITipDialog.Builder.ICON_TYPE_FAIL, 2);
            return false;
        }
        String password = GestureUtils.get(getContext(), GestureUtils.USER_PASSWORD);
        if(!Utils.md5(text).equals(password)){
            showTips("登录密码不正确", QMUITipDialog.Builder.ICON_TYPE_FAIL, 2);
            return false;
        }
        return true;
    }

    private void showTips(String msg){
        tipDialog = new QMUITipDialog.Builder(getActivity())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(msg)
                .create();
        tipDialog.show();
    }

    private void showTips(String msg, int iconType, int delay){
        tipDialog = new QMUITipDialog.Builder(getActivity())
                .setIconType(iconType)
                .setTipWord(msg)
                .create();
        tipDialog.show();

        if(delay > 0){
            getView().postDelayed(new Runnable() {
                @Override
                public void run() {
                    tipDialog.dismiss();
                }
            }, delay * 1000);
        }
    }

    private void sendResetGesture(){
        popBackStack();
        Intent intent = new Intent(getContext(), GesturePwdSettingActivity.class);
        intent.putExtra(GestureUtils.Gesture_Model_Type, GestureUtils.Gesture_Model_Type_Reset_Pwd);
        startActivity(intent);
    }

}
