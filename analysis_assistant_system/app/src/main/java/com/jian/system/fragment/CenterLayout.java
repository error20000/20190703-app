
package com.jian.system.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.jian.system.LoginActivity;
import com.jian.system.MainActivity;
import com.jian.system.R;
import com.jian.system.entity.User;
import com.jian.system.gesture.GesturePwdCheckActivity;
import com.jian.system.gesture.GesturePwdResetActivity;
import com.jian.system.gesture.util.GestureUtils;
import com.jian.system.utils.Utils;
import com.qmuiteam.qmui.arch.QMUIFragment;
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
        changePwd.setTag("changePwd");

        QMUICommonListItemView gesturePwd =  mGroupListView.createItemView("手势密码");
        gesturePwd.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        gesturePwd.setTag("gesturePwd");

        QMUICommonListItemView lockScreen =  mGroupListView.createItemView("锁屏时间");
        lockScreen.setDetailText("10秒");
        lockScreen.setTag("lockScreen");

        QMUICommonListItemView logout =  mGroupListView.createItemView("退出登录");
        logout.setTag("logout");

        QMUIGroupListView.newSection(getContext())
                .setTitle("")
                .addItemView(gesturePwd, mOnClickListenerGroup)
                .addItemView(changePwd, mOnClickListenerGroup)
                .addItemView(lockScreen, mOnClickListenerGroup)
                .addItemView(logout, mOnClickListenerGroup)
                .addTo(mGroupListView);
    }

    private View.OnClickListener mOnClickListenerGroup;

    {
        mOnClickListenerGroup = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QMUICommonListItemView viewList = (QMUICommonListItemView) view;
                switch ((String) viewList.getTag()) {
                    case "changePwd":
                        break;
                    case "gesturePwd":
                        String[] items = {"修改手势", "重置手势"};
                        new QMUIDialog.CheckableDialogBuilder(getContext())
                                .setTitle("请选择")
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
                                        //清空本地密码
                                        GestureUtils.remove(getContext(), GestureUtils.USER_USERNAME);
                                        GestureUtils.remove(getContext(), GestureUtils.USER_PASSWORD);
                                        //跳转登录
                                        Intent intent = new Intent(context, LoginActivity.class);
                                        context.startActivity(intent);

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
        Intent intent = new Intent(getContext(), GesturePwdCheckActivity.class);
        intent.putExtra(GestureUtils.Gesture_Model_Type, GestureUtils.Gesture_Model_Type_Change_Pwd);
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
