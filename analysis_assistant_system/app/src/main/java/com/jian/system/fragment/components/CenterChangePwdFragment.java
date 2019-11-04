
package com.jian.system.fragment.components;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONObject;
import com.jian.system.LoginActivity;
import com.jian.system.R;
import com.jian.system.config.Constant;
import com.jian.system.config.UrlConfig;
import com.jian.system.gesture.util.GestureUtils;
import com.jian.system.utils.HttpUtils;
import com.jian.system.utils.ThreadUtils;
import com.jian.system.utils.Utils;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CenterChangePwdFragment extends QMUIFragment {

    private final static String TAG = CenterChangePwdFragment.class.getSimpleName();
    private String title = "修改密码";
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
        //新密码规则提示
        mEditText1.setHint("新"+ Constant.pwdRegTip);

        return rootView;
    }



    private void initTopBar() {
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        mTopBar.addRightTextButton("提交", R.id.topbar_right_about_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean flag = checkEditText();
                if(!flag){
                    return;
                }

                new QMUIDialog.MessageDialogBuilder(getActivity())
                        .setTitle("")
                        .setMessage("确定要提交吗？")
                        .addAction(0,"取消", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        })
                        .addAction("确定", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                                sendChangePwd();
                            }
                        })
                        .create(mCurrentDialogStyle).show();
            }
        });
        mTopBar.setTitle(title);
    }

    private boolean checkEditText(){
        String text = mEditText.getText().toString();
        String text1 = mEditText1.getText().toString();
        String text2 = mEditText2.getText().toString();
        //效验原密码
        if(Utils.isNullOrEmpty(text)){
            showTips("原密码不能为空", QMUITipDialog.Builder.ICON_TYPE_FAIL, 2);
            return false;
        }
        String password = GestureUtils.get(getContext(), GestureUtils.USER_PASSWORD);
        if(!Utils.md5(text).equals(password)){
            showTips("原密码不正确", QMUITipDialog.Builder.ICON_TYPE_FAIL, 2);
            return false;
        }
        //效验新密码
        if(Utils.isNullOrEmpty(text1)){
            showTips("新密码不能为空", QMUITipDialog.Builder.ICON_TYPE_FAIL, 2);
            return false;
        }
        if(!text1.matches(Constant.pwdReg)){
            showTips(Constant.pwdRegTip, QMUITipDialog.Builder.ICON_TYPE_FAIL, 3);
            return false;
        }
        if(!text1.equals(text2)){
            showTips("两次密码不一致", QMUITipDialog.Builder.ICON_TYPE_FAIL, 2);
            return false;
        }
        return true;
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            //处理结果
            hideTips();
            String str =  (String) msg.obj;
            if(Utils.isNullOrEmpty(str)){
                showToast("网络异常，请检查网络。");
                return;
            }
            JSONObject resData = JSONObject.parseObject(str);
            //处理数据
            switch (msg.what){
                case MsgType_ChangePwd:
                    handleChangePwd(resData);
                    break;
            }
        }
    };

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

    private void hideTips(){
        tipDialog.dismiss();
    }
    private void showToast(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    private void sendChangePwd(){
        showTips("提交中");
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<>();
                params.put("oldPwd", mEditText.getText().toString());
                params.put("newPwd", mEditText1.getText().toString());
                String res = HttpUtils.getInstance().sendPost(UrlConfig.userChangePwdUrl, params);
                Message message = mHandler.obtainMessage(MsgType_ChangePwd);
                message.obj = res;
                mHandler.sendMessage(message);
            }
        });
    }

    private void handleChangePwd(JSONObject resObj){
        if(resObj.getInteger("code") <= 0){
            showToast(resObj.getString("msg"));
            return;
        }
        showToast("保存成功");
        logout();
    }

    private void logout(){
        //清空本地密码
        GestureUtils.remove(getContext(), GestureUtils.USER_USERNAME);
        GestureUtils.remove(getContext(), GestureUtils.USER_PASSWORD);
        LoginActivity.isShowGestureLogin = false;
        //跳转登录
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
    }
}
