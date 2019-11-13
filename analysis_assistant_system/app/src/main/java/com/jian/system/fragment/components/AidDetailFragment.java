
package com.jian.system.fragment.components;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jian.system.R;
import com.jian.system.config.UrlConfig;
import com.jian.system.entity.Aid;
import com.jian.system.entity.AidEquip;
import com.jian.system.entity.Dict;
import com.jian.system.entity.Nfc;
import com.jian.system.fragment.MainFragment;
import com.jian.system.fragment.ViewPagerListener;
import com.jian.system.utils.DataUtils;
import com.jian.system.utils.HttpUtils;
import com.jian.system.utils.ThreadUtils;
import com.jian.system.utils.Utils;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUITabSegment;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import chihane.jdaddressselector.BottomDialog;

public class AidDetailFragment extends QMUIFragment {

    private final static String TAG = AidDetailFragment.class.getSimpleName();
    private String title = "航标详情";
    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;
    private QMUITipDialog tipDialog;
    private final int MsgType_Unusual = 1;
    private final int MsgType_Normal = 2;

    private String sAid_ID;
    private String from;
    private String remarks = "";
    private int contentId = R.id.map_container;

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;
    @BindView(R.id.tabSegment)
    QMUITabSegment mTabSegment;
    @BindView(R.id.contentViewPager)
    ViewPager mContentViewPager;

    @Override
    protected View onCreateView() {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_aid_detail, null);
        ButterKnife.bind(this, rootView);

        Bundle bundle = this.getArguments();
        sAid_ID = bundle.getString("sAid_ID");
        from =  bundle.getString("from");

        initTopBar();
        initTabAndPager();

        return rootView;
    }

    private void initTopBar() {
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("map".equals(from)){
                    getFragmentManager().popBackStack();
                }else{
                    popBackStack();
                }
            }
        });
        // 切换其他情况的按钮
        mTopBar.addRightImageButton(R.mipmap.icon_topbar_overflow, R.id.topbar_right_change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetList();
            }
        });
        mTopBar.setTitle(title);
    }

    private void showBottomSheetList() {
        new QMUIBottomSheet.BottomListSheetBuilder(getActivity())
                .addItem("标记为异常", "unusual")
                .addItem("恢复正常", "normal")
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                        dialog.dismiss();
                        showEditTextDialog(tag);
                    }
                })
                .build()
                .show();
    }

    private void showEditTextDialog(String tag) {
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(getActivity());
        builder.setTitle("备注")
                .setPlaceholder("请填写备注，非必填！")
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .addAction(0, "取消", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener(){
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        //二次确认
                        new QMUIDialog.MessageDialogBuilder(getActivity())
                                .setTitle("")
                                .setMessage("确定要取消本次操作吗？")
                                .addAction("取消", new QMUIDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(QMUIDialog dialog2, int index) {
                                        dialog2.dismiss();
                                    }
                                })
                                .addAction(0,"确定", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(QMUIDialog dialog2, int index) {
                                        dialog2.dismiss();
                                        dialog.dismiss();
                                    }
                                })
                                .create(mCurrentDialogStyle).show();
                    }
                })
                .addAction("提交", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        remarks = builder.getEditText().getText().toString();
                        dialog.dismiss();
                        switch (tag) {
                            case "unusual":
                                itemUnusual();
                                break;
                            case "normal":
                                itemNormal();
                                break;
                            default:
                                break;
                        }
                    }
                })
                .create(mCurrentDialogStyle).show();
    }
    //TODO --------------------------------------------------------------------------- handle

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
            switch (msg.what) {
                case MsgType_Unusual:
                    handleUnusual(resData);
                    break;
                case MsgType_Normal:
                    handleNormal(resData);
                    break;
                default:
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

    private void hideTips(){
        tipDialog.dismiss();
    }
    private void showToast(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
    private boolean handleErrorCode(JSONObject resObj){
        if (resObj.getInteger("code") <= 0) {
            showToast(resObj.getString("msg"));
            return true;
        }
        return false;
    }

    private void itemUnusual(){
        showTips("保存中");
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<>();
                params.put("sAid_ID", sAid_ID);
                params.put("remarks", remarks);
                String res = HttpUtils.getInstance().sendPost(UrlConfig.aidUnusualUrl, params);
                Message message = mHandler.obtainMessage(MsgType_Unusual);
                message.obj = res;
                mHandler.sendMessage(message);
            }
        });
    }

    private void handleUnusual(JSONObject resObj) {
        if (handleErrorCode(resObj)) {
            Log.d(TAG, resObj.toJSONString());
            return;
        }
        showToast("保存成功");
    }

    private void itemNormal(){
        showTips("保存中");
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<>();
                params.put("sAid_ID", sAid_ID);
                params.put("remarks", remarks);
                String res = HttpUtils.getInstance().sendPost(UrlConfig.aidNormalUrl, params);
                Message message = mHandler.obtainMessage(MsgType_Normal);
                message.obj = res;
                mHandler.sendMessage(message);
            }
        });
    }

    private void handleNormal(JSONObject resObj) {
        if (handleErrorCode(resObj)) {
            Log.d(TAG, resObj.toJSONString());
            return;
        }
        showToast("保存成功");
    }
    //TODO -------------------------------------------------------------------------------------viewpager

    private Map<Pager, View> mPages = new HashMap<>();

    private PagerAdapter mPagerAdapter = new PagerAdapter() {

        private int mChildCount = 0;

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getCount() {
            return mPages.size();
        }

        @Override
        public Object instantiateItem(final ViewGroup container, int position) {
            View view = mPages.get(Pager.getPagerFromPositon(position));
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            container.addView(view, params);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            if (mChildCount == 0) {
                return POSITION_NONE;
            }
            return super.getItemPosition(object);
        }

        @Override
        public void notifyDataSetChanged() {
            mChildCount = getCount();
            super.notifyDataSetChanged();
        }
    };


    private void initTabAndPager() {

        ViewPagerListener listener = new ViewPagerListener(){
            @Override
            public void startFragment(QMUIFragment fragment) {
                AidDetailFragment.this.startFragment(fragment);
            }
            @Override
            public void startFragmentForResult(QMUIFragment fragment, int requestCode) {
                AidDetailFragment.this.startFragmentForResult(fragment, requestCode);
            }
            @Override
            public void startActivity(Intent intent) {
                AidDetailFragment.this.startActivity(intent);
            }
        };

        AidDetailView detailView = new AidDetailView(getActivity(), sAid_ID);
        detailView.setViewPagerListener(listener);
        mPages.put(Pager.Detail, detailView);

        AidEquipView equipView = new AidEquipView(getActivity(), sAid_ID, from, getFragmentManager(), contentId);
        equipView.setViewPagerListener(listener);
        mPages.put(Pager.Equip, equipView);

        mContentViewPager.setAdapter(mPagerAdapter);

        mTabSegment.addTab(new QMUITabSegment.Tab("基础信息"));
        mTabSegment.addTab(new QMUITabSegment.Tab("航标器材"));

        mTabSegment.setupWithViewPager(mContentViewPager, false);
        mTabSegment.setMode(QMUITabSegment.MODE_FIXED);
        mTabSegment.addOnTabSelectedListener(new QMUITabSegment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {
                mTabSegment.hideSignCountView(index);
            }

            @Override
            public void onTabUnselected(int index) {

            }

            @Override
            public void onTabReselected(int index) {
                mTabSegment.hideSignCountView(index);
            }

            @Override
            public void onDoubleTap(int index) {

            }
        });
    }

    enum Pager {
        Detail(0), Equip(1);

        private int index;

        private Pager(int index){
            this.index = index;
        }

        public static Pager getPagerFromPositon(int position) {
            switch (position) {
                case 0:
                    return Detail;
                case 1:
                    return Equip;
                default:
                    return Detail;
            }
        }
    }
}
