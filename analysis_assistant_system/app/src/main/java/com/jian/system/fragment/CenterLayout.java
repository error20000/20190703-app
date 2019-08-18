
package com.jian.system.fragment;

import android.content.Context;
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
import com.jian.system.gesture.util.GestureUtils;
import com.jian.system.utils.Utils;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.QMUIWindowInsetLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListSectionHeaderFooterView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CenterLayout extends QMUIWindowInsetLayout {

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;
    @BindView(R.id.item_nick)
    TextView mNick;
    @BindView(R.id.item_id)
    TextView mId;
    @BindView(R.id.item_logout)
    QMUICommonListItemView mCommonListItemView;
    @BindView(R.id.item_split_1)
    QMUIGroupListSectionHeaderFooterView mSplit1;
    @BindView(R.id.item_split_2)
    QMUIGroupListSectionHeaderFooterView mSplit2;


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
        if(!Utils.isNullOrEmpty(userInfo)){
            User user = JSONObject.parseObject(userInfo, User.class);
            String nickName = Utils.isNullOrEmpty(user.getsUser_Nick()) ? user.getsUser_UserName() : user.getsUser_Nick();
            mNick.setText(nickName);
            mId.setText(user.getsUser_ID());
        }

        mSplit1.setText("");
        mSplit2.setText("");

        mCommonListItemView.setOrientation(QMUICommonListItemView.VERTICAL);
        mCommonListItemView.setDetailText("退出登录");
        mCommonListItemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
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
                                GestureUtils.clear(context);
                                //
                                Intent intent = new Intent(context, LoginActivity.class);
                                context.startActivity(intent);

                            }
                        })
                        .create(mCurrentDialogStyle).show();
            }
        });
    }


    protected void startFragment(QMUIFragment fragment) {
        if (mListener != null) {
            mListener.startFragment(fragment);
        }
    }

    public void setViewPagerListener(ViewPagerListener listener) {
        mListener = listener;
    }

}
