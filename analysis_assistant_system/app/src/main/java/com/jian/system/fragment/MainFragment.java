
package com.jian.system.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSONObject;
import com.jian.system.R;
import com.jian.system.config.UrlConfig;
import com.jian.system.entity.Equip;
import com.jian.system.entity.Messages;
import com.jian.system.fragment.components.EquipDetailFragment;
import com.jian.system.fragment.components.MsgAddFragment;
import com.jian.system.fragment.components.MsgDetailFragment;
import com.jian.system.utils.HttpUtils;
import com.jian.system.utils.ThreadUtils;
import com.jian.system.utils.Utils;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.QMUITabSegment;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainFragment extends QMUIFragment {

    @BindView(R.id.pager)
    ViewPager mViewPager;
    @BindView(R.id.tabs)
    QMUITabSegment mTabSegment;

    public static final int Fragment_Msg_Result_Detail = 1;
    public static final int Fragment_Msg_Result_Add = 2;
    private final int MsgResult_Detail = 1;
    private final int MsgResult_Add = 2;
    private final int MsgResult_Num = 3;

    private HashMap<Pager, View> mPages;

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
            container.addView(view);
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

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Intent data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case Fragment_Msg_Result_Detail:
                    String sMsg_ID = data.getStringExtra(MsgDetailFragment.Fragment_Result);
                    if(Utils.isNullOrEmpty(sMsg_ID)){
                        return;
                    }
                    //查询消息
                    Map<String, Object> params = new HashMap<>();
                    params.put("sMsg_ID", sMsg_ID);
                    queryMsgDetail(params);
                    //查询未读数量
                    queryMsgUnReadNum();
                    break;
                case Fragment_Msg_Result_Add:
                    String add = data.getStringExtra(MsgAddFragment.Fragment_Result);
                    if(Utils.isNullOrEmpty(add)){
                        return;
                    }
                    //刷新消息
                    queryMsgRefresh();
                    break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected View onCreateView() {
        FrameLayout layout = (FrameLayout) LayoutInflater.from(getActivity()).inflate(R.layout.fragment_main, null);
        ButterKnife.bind(this, layout);
        initTabs();
        initPagers();
        queryMsgUnReadNum();
        return layout;
    }

    private void initTabs() {
        int normalColor = QMUIResHelper.getAttrColor(getActivity(), R.attr.qmui_config_color_gray_6);
        int selectColor = QMUIResHelper.getAttrColor(getActivity(), R.attr.qmui_config_color_blue);
        mTabSegment.setDefaultNormalColor(normalColor);
        mTabSegment.setDefaultSelectedColor(selectColor);

        QMUITabSegment.Tab home = new QMUITabSegment.Tab(
                ContextCompat.getDrawable(getContext(), R.drawable.ic_home_background),
                ContextCompat.getDrawable(getContext(), R.drawable.ic_home_foreground),
                "首页", false
        );

        QMUITabSegment.Tab map = new QMUITabSegment.Tab(
                ContextCompat.getDrawable(getContext(), R.drawable.ic_map_background),
                ContextCompat.getDrawable(getContext(), R.drawable.ic_map_foreground),
                "地图", false
        );
        QMUITabSegment.Tab msg = new QMUITabSegment.Tab(
                ContextCompat.getDrawable(getContext(), R.drawable.ic_msg_background),
                ContextCompat.getDrawable(getContext(), R.drawable.ic_msg_foreground),
                "消息", false
        );
        QMUITabSegment.Tab center = new QMUITabSegment.Tab(
                ContextCompat.getDrawable(getContext(), R.drawable.ic_center_background),
                ContextCompat.getDrawable(getContext(), R.drawable.ic_center_foreground),
                "个人中心", false
        );
        mTabSegment.addTab(home)
                //.addTab(map)
                .addTab(msg)
                .addTab(center);
    }

    private void initPagers() {
        mPages = new HashMap<>();

        ViewPagerListener listener = new ViewPagerListener(){

            @Override
            public void startFragment(QMUIFragment fragment) {
                MainFragment.this.startFragment(fragment);
            }

            @Override
            public void startFragmentForResult(QMUIFragment fragment, int requestCode) {
                MainFragment.this.startFragmentForResult(fragment, requestCode);
            }

            @Override
            public void startActivity(Intent intent) {
                MainFragment.this.startActivity(intent);
            }
        };

        HomeLayout homeLayout = new HomeLayout(getActivity(), getActivity().getSupportFragmentManager());
        homeLayout.setViewPagerListener(listener);
        mPages.put(Pager.Home, homeLayout);

        /*MapLayout mapLayout = new MapLayout(getActivity());
        mapLayout.setViewPagerListener(listener);
        mPages.put(Pager.Map, mapLayout);*/

        MsgLayout msgLayout = new MsgLayout(getActivity(), getActivity().getSupportFragmentManager());
        msgLayout.setViewPagerListener(listener);
        mPages.put(Pager.Msg, msgLayout);

        CenterLayout centerLayout = new CenterLayout(getActivity());
        centerLayout.setViewPagerListener(listener);
        mPages.put(Pager.Center, centerLayout);

        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(5);
        mTabSegment.setupWithViewPager(mViewPager, false);
    }

    enum Pager {
        Home, /*Map,*/ Msg, Center;

        public static Pager getPagerFromPositon(int position) {
            switch (position) {
                case 0:
                    return Home;
                /*case 1:
                    return Map;*/
                case 1:
                    return Msg;
                case 2:
                    return Center;
                default:
                    return Home;
            }
        }
    }

    @Override
    protected boolean canDragBack() {
        return false;
    }


    private void queryMsgDetail(Map<String, Object> params){
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                String res = HttpUtils.getInstance().sendPost(UrlConfig.msgQueryDetailUrl, params);
                Message msg = mHandler.obtainMessage();
                msg.what = MsgResult_Detail;
                msg.obj = res;
                mHandler.sendMessage(msg);
            }
        });
    }

    private void handleMsgDetail(JSONObject resObj){
        JSONObject resData = resObj.getJSONObject("data");
        if(resData == null){
            return;
        }
        Messages msg = resData.toJavaObject(Messages.class);
        if(msg == null){
            return;
        }
        //更新列表
        MsgLayout msgLayout = (MsgLayout) mPages.get(Pager.Msg);
        List<Messages> data = msgLayout.getData();
        for (int i = 0; i < data.size(); i++) {
            if( data.get(i).getsMsg_ID().equals(msg.getsMsg_ID()) ){
                data.set(i, msg);
                break;
            }
        }
        msgLayout.setData(data);
    };

    private void queryMsgRefresh(){
        MsgLayout msgLayout = (MsgLayout) mPages.get(Pager.Msg);
        msgLayout.refreshData();
    }

    private void queryMsgUnReadNum() {
        //查询未读消息数量
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                String res = HttpUtils.getInstance().sendPost(UrlConfig.msgUnReadNumUrl, new HashMap<>());
                Message msg = mHandler.obtainMessage();
                msg.what = MsgResult_Num;
                msg.obj = res;
                mHandler.sendMessage(msg);
            }
        });
    }

    private void handleMsgUnReadNum(JSONObject resObj){
        int count = resObj.getIntValue("data");
        if(count != 0){
            mTabSegment.showSignCountView(getContext(), 1, count);
        }else{
            mTabSegment.hideSignCountView(1);
        }
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            //处理结果
            String str =  (String) msg.obj;
            if(Utils.isNullOrEmpty(str)){
                showToast("网络异常，请检查网络。");
                return;
            }
            JSONObject resData = JSONObject.parseObject(str);
            //处理数据
            switch (msg.what) {
                case MsgResult_Detail:
                    handleMsgDetail(resData);
                    break;
                case MsgResult_Num:
                    handleMsgUnReadNum(resData);
                    break;
                default:
                    break;
            }
        }
    };
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
}