
package com.jian.system.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSONObject;
import com.jian.system.ChangeActivity;
import com.jian.system.LoginActivity;
import com.jian.system.MainActivity;
import com.jian.system.R;
import com.jian.system.config.UrlConfig;
import com.jian.system.entity.User;
import com.jian.system.fragment.components.CenterChangePwdFragment;
import com.jian.system.fragment.components.CenterResetGestureFragment;
import com.jian.system.gesture.GesturePwdCheckActivity;
import com.jian.system.gesture.GesturePwdResetActivity;
import com.jian.system.gesture.util.GestureUtils;
import com.jian.system.utils.DataCacheUtils;
import com.jian.system.utils.Utils;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.QMUIWindowInsetLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListSectionHeaderFooterView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CenterLayout extends QMUIWindowInsetLayout {

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;
    @BindView(R.id.item_nick)
    TextView mNick;
    @BindView(R.id.item_id)
    TextView mId;
    @BindView(R.id.item_split_1)
    QMUIGroupListSectionHeaderFooterView mSplit1;
    @BindView(R.id.item_split_2)
    QMUIGroupListSectionHeaderFooterView mSplit2;
    @BindView(R.id.groupListView)
    QMUIGroupListView mGroupListView;


    private final static String TAG = CenterLayout.class.getSimpleName();
    private ViewPagerListener mListener;
    String  title = "个人中心";
    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;

    String[] times = {"15秒", "30秒", "1分钟", "2分钟", "5分钟", "10分钟", "30分钟", "从不"};
    String[] values = {"15", "30", "60", "120", "300", "600", "1800", "never"};

    Context context;

    public CenterLayout(Context context) {
        super(context);
        this.context = context;

        LayoutInflater.from(context).inflate(R.layout.layout_center, this);
        ButterKnife.bind(this);

        initTopBar();
        initData();
    }

    private void initTopBar() {
        mTopBar.setTitle(title);
    }

    private void initData(){
        String userInfo = GestureUtils.get(context, GestureUtils.USER_INFO);
        Log.d(TAG, "userInfo ==>" + userInfo);
        if(!Utils.isNullOrEmpty(userInfo)){
            User user = JSONObject.parseObject(userInfo, User.class);
            String nickName = Utils.isNullOrEmpty(user.getsUser_Nick()) ? user.getsUser_UserName() : user.getsUser_Nick();
            mNick.setText(nickName);
            mId.setText(user.getsUser_ID());
        }

        mSplit1.setText("");
        mSplit2.setText("");

        QMUICommonListItemView changePwd =  mGroupListView.createItemView("修改密码");
        changePwd.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        changePwd.setTag("changePwd");

        QMUICommonListItemView gesturePwd =  mGroupListView.createItemView("手势设置");
        gesturePwd.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        gesturePwd.setTag("gesturePwd");

        QMUICommonListItemView lockScreen =  mGroupListView.createItemView("锁屏时间");
        String timeStr = "";
        String time = GestureUtils.get(getContext(), GestureUtils.USER_LOCK_SCREEN_TIME);
        if(Utils.isNullOrEmpty(time)){
            time = GestureUtils.USER_LOCK_SCREEN_TIME_DEF;
        }
        for(int i = 0; i < values.length; i++){
            if(time.equals(values[i])){
                timeStr = times[i];
                break;
            }
        }
        lockScreen.setDetailText(timeStr);
        lockScreen.setTag("lockScreen");

        QMUICommonListItemView serverChange =  mGroupListView.createItemView("切换服务器");
        serverChange.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        serverChange.setTag("serverChange");

        QMUICommonListItemView logout =  mGroupListView.createItemView("退出登录");
        logout.getTextView().setTextColor(QMUIResHelper.getAttrColor(getContext(), R.attr.qmui_config_color_red));
        logout.setTag("logout");

        QMUIGroupListView.Section section = QMUIGroupListView.newSection(getContext())
                .setTitle("");
        section.addItemView(gesturePwd, mOnClickListenerGroup);
        section.addItemView(changePwd, mOnClickListenerGroup);
        section.addItemView(lockScreen, mOnClickListenerGroup);
        if(UrlConfig.debug){
            section.addItemView(serverChange, mOnClickListenerGroup);
        }
        section.addItemView(logout, mOnClickListenerGroup);
        section.addTo(mGroupListView);
    }

    private View.OnClickListener mOnClickListenerGroup;

    {
        mOnClickListenerGroup = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QMUICommonListItemView viewList = (QMUICommonListItemView) view;
                switch ((String) viewList.getTag()) {
                    case "changePwd":
                        CenterChangePwdFragment fragment = new CenterChangePwdFragment();
                        startFragment(fragment);
                        break;
                    case "gesturePwd":
                        String[] items = {"修改手势密码", "重置手势密码"};
                        new QMUIDialog.CheckableDialogBuilder(getContext())
                                .setTitle("")
                                .addItems(items, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        switch (which){
                                            case 0:
                                                showChangeGesture();
                                                break;
                                            case 1:
                                                showResetGesture();
                                                break;
                                        }
                                    }
                                })
                                .create(mCurrentDialogStyle).show();
                        break;
                    case "lockScreen":
                        //查询锁屏时间
                        String time = GestureUtils.get(getContext(), GestureUtils.USER_LOCK_SCREEN_TIME);
                        if(Utils.isNullOrEmpty(time)){
                            time = GestureUtils.USER_LOCK_SCREEN_TIME_DEF;
                        }
                        int checkedIndex = 0;
                        for(int i = 0; i < values.length; i++){
                            if(time.equals(values[i])){
                                checkedIndex = i;
                                break;
                            }
                        }
                        new QMUIDialog.CheckableDialogBuilder(getContext())
                                .setTitle("请选择")
                                .setCheckedIndex(checkedIndex)
                                .addItems(times, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        //设置锁屏时间
                                        viewList.setDetailText(times[which]);
                                        GestureUtils.set(getContext(), GestureUtils.USER_LOCK_SCREEN_TIME, values[which]);
                                    }
                                })
                                .create(mCurrentDialogStyle).show();
                        break;
                    case "serverChange":
                        Intent intent = new Intent(getContext(), ChangeActivity.class);
                        intent.putExtra(ChangeActivity.isFromSet, true);
                        startActivity(intent);
                        break;
                    case "logout":
                        new QMUIDialog.MessageDialogBuilder(context)
                                .setTitle("")
                                .setMessage("确定要退出登录吗？")
                                .addAction("取消", new QMUIDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(QMUIDialog dialog, int index) {
                                        dialog.dismiss();
                                    }
                                })
                                .addAction(0, "确定", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(QMUIDialog dialog, int index) {
                                        dialog.dismiss();
                                        logout();
                                    }
                                })
                                .create(mCurrentDialogStyle).show();
                        break;
                }
            }
        };
    }

    private  void showChangeGesture(){
        Intent intent = new Intent(getContext(), GesturePwdResetActivity.class);
        intent.putExtra(GestureUtils.Gesture_Model_Type, GestureUtils.Gesture_Model_Type_Change_Pwd);
        startActivity(intent);
    }

    private  void showResetGesture(){
        CenterResetGestureFragment fragment = new CenterResetGestureFragment();
        startFragment(fragment);
    }

    private void logout(){
        //清空本地密码
        GestureUtils.remove(getContext(), GestureUtils.USER_USERNAME);
        GestureUtils.remove(getContext(), GestureUtils.USER_PASSWORD);
        LoginActivity.isShowGestureLogin = false;
        //清除数据缓存
        DataCacheUtils.clearAll();
        //跳转登录
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
    }

    protected void startFragment(QMUIFragment fragment) {
        if (mListener != null) {
            mListener.startFragment(fragment);
        }
    }

    protected void startActivity(Intent intent) {
        if (mListener != null) {
            mListener.startActivity(intent);
        }
    }

    public void setViewPagerListener(ViewPagerListener listener) {
        mListener = listener;
    }

}
