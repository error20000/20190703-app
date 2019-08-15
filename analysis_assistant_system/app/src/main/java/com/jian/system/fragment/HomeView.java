
package com.jian.system.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.jian.system.R;
import com.jian.system.adapter.BaseRecyclerAdapter;
import com.jian.system.adapter.HomeRvItemAdapter;
import com.jian.system.config.UrlConfig;
import com.jian.system.decorator.DividerItemDecoration;
import com.jian.system.decorator.GridDividerItemDecoration;
import com.jian.system.fragment.components.EquipAddFragment;
import com.jian.system.fragment.components.EquipDetailFragment;
import com.jian.system.fragment.components.EquipListFragment;
import com.jian.system.model.HomeRvItem;
import com.jian.system.utils.DataUtils;
import com.jian.system.utils.HttpUtils;
import com.jian.system.utils.ThreadUtils;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.QMUIWindowInsetLayout;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeView extends QMUIWindowInsetLayout{

    private final static String TAG = HomeView.class.getSimpleName();

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.msg_title)
    QMUICommonListItemView mMsgTitle;
    @BindView(R.id.recyclerViewMsg)
    RecyclerView mMsgRecyclerView;

    private MainListener mListener;
    private Context context;
    private HomeRvItemAdapter mItemAdapter;
    private QMUITipDialog tipDialog;

    private final int MsgType_Msg = 1;

    public HomeView(Context context) {
        super(context);
        this.context = context;

        LayoutInflater.from(context).inflate(R.layout.layout_home, this);
        ButterKnife.bind(this);

        initTopBar();
        initRecyclerView();

        mMsgTitle.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        mMsgTitle.setOrientation(QMUICommonListItemView.VERTICAL);
        mMsgTitle.setText("消息中心");

        initData();
    }

    protected void startFragment(QMUIFragment fragment) {
        if (mListener != null) {
            mListener.startFragment(fragment);
        }
    }

    public void setMainListener(MainListener listener) {
        mListener = listener;
    }


    private void initTopBar() {
        mTopBar.setTitle("首页");
    }

    private void initRecyclerView() {

        List<HomeRvItem> data = new ArrayList<>();
        data.add(new HomeRvItem(EquipListFragment.class, "器材列表", R.mipmap.icon_tabbar_component));
        data.add(new HomeRvItem(EquipListFragment.class, "器材列表", R.mipmap.icon_tabbar_component));
        data.add(new HomeRvItem(EquipListFragment.class, "器材列表", R.mipmap.icon_tabbar_component));
        data.add(new HomeRvItem(EquipListFragment.class, "器材列表", R.mipmap.icon_tabbar_component));

        mItemAdapter = new HomeRvItemAdapter(context, data);
        mItemAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
                HomeRvItem item = mItemAdapter.getItem(pos);
                try {
                    QMUIFragment fragment = item.getFragment().newInstance();
                    startFragment(fragment);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mRecyclerView.setAdapter(mItemAdapter);
        int spanCount = 3;
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
        mRecyclerView.addItemDecoration(new GridDividerItemDecoration(getContext(), spanCount));
    }

    private void initData(){
        //查询数据 -- 判断网络
        tipDialog = new QMUITipDialog.Builder(getContext())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();
        tipDialog.show();
        //查询消息
        queryMsgData();
    }
    private void queryMsgData(){
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<>();
                params.put("page", 1);
                params.put("rows", 5);

                String res = HttpUtils.getInstance().sendPost(UrlConfig.equipQueryPageUrl, params);
                if(res == null || "".equals(res)){
                    Log.d(TAG, UrlConfig.equipQueryPageUrl + " return  is null ");
                    return;
                }

                JSONObject resObj = JSONObject.parseObject(res);
                Message msg = mHandler.obtainMessage();
                msg.what = MsgType_Msg;
                msg.obj = resObj;
                mHandler.sendMessage(msg);
            }
        });
    }

    //TODO --------------------------------------------------------------------------------- Handle

    private void showTips(String msg){
        tipDialog = new QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(msg)
                .create();
        tipDialog.show();
    }

    private void hideTips(){
        tipDialog.dismiss();
    }

    private void showToast(String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    private boolean handleErrorCode(JSONObject resObj){
        if (resObj.getInteger("code") <= 0) {
            showToast(resObj.getString("msg"));
            return true;
        }
        return false;
    }


    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            hideTips();
            JSONObject resData = (JSONObject) msg.obj;
            //处理数据
            switch (msg.what){
                case MsgType_Msg:
                    handleMsg(resData);
                    break;
            }
        }
    };

    private void handleMsg(JSONObject resObj) {
        if (handleErrorCode(resObj)) {
            Log.d(TAG, resObj.toJSONString());
            return;
        }
        //初始化消息列表
        initMsgRecyclerView();
    }

    private void initMsgRecyclerView() {

        List<HomeRvItem> data = new ArrayList<>();
        data.add(new HomeRvItem(EquipListFragment.class, "器材列表", R.mipmap.icon_tabbar_component));

        data.add(new HomeRvItem(EquipListFragment.class, "器材列表", R.mipmap.icon_tabbar_component));

        data.add(new HomeRvItem(EquipListFragment.class, "器材列表", R.mipmap.icon_tabbar_component));

        data.add(new HomeRvItem(EquipListFragment.class, "器材列表", R.mipmap.icon_tabbar_component));

        data.add(new HomeRvItem(EquipListFragment.class, "器材列表", R.mipmap.icon_tabbar_component));

        HomeRvItemAdapter itemAdapter = new HomeRvItemAdapter(context, data);
        itemAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
                HomeRvItem item = mItemAdapter.getItem(pos);
                showToast(item.getName());
            }
        });
        //禁用RecyclerView滚动，或者重写 LinearLayoutManager  canScrollVertically
        mMsgRecyclerView.setHasFixedSize(true);
        mMsgRecyclerView.setNestedScrollingEnabled(false);

        mMsgRecyclerView.setAdapter(itemAdapter);
        mMsgRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mMsgRecyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
    }

}
