
package com.jian.system.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jian.system.R;
import com.jian.system.adapter.BaseRecyclerAdapter;
import com.jian.system.adapter.HomeMsgAdapter;
import com.jian.system.adapter.HomeRvItemAdapter;
import com.jian.system.config.Constant;
import com.jian.system.config.UrlConfig;
import com.jian.system.decorator.DividerItemDecoration;
import com.jian.system.decorator.GridDividerItemDecoration;
import com.jian.system.entity.Dict;
import com.jian.system.entity.Messages;
import com.jian.system.fragment.components.AidListFragment;
import com.jian.system.fragment.components.EquipListFragment;
import com.jian.system.fragment.components.MapFragment;
import com.jian.system.fragment.components.MsgDetailFragment;
import com.jian.system.model.HomeRvItem;
import com.jian.system.utils.DataUtils;
import com.jian.system.utils.HttpUtils;
import com.jian.system.utils.ThreadUtils;
import com.jian.system.utils.Utils;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.QMUIWindowInsetLayout;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeLayout extends QMUIWindowInsetLayout{

    private final static String TAG = HomeLayout.class.getSimpleName();

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.msg_title)
    QMUICommonListItemView mMsgTitle;
    @BindView(R.id.recyclerViewMsg)
    RecyclerView mMsgRecyclerView;

    private ViewPagerListener mListener;
    private Context context;
    private FragmentManager manager;
    private HomeRvItemAdapter mItemAdapter;
    private QMUITipDialog tipDialog;
    private List<Dict> msgTypeData = new ArrayList<>();
    private List<Messages> msgData = new ArrayList<>();

    private final int MsgType_Msg = 1;

    public HomeLayout(Context context, FragmentManager manager) {
        super(context);
        this.context = context;
        this.manager = manager;

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

    public void setViewPagerListener(ViewPagerListener listener) {
        mListener = listener;
    }


    private void initTopBar() {
        mTopBar.setTitle("首页");
    }

    private void initRecyclerView() {

        List<HomeRvItem> data = new ArrayList<>();
        data.add(new HomeRvItem(AidListFragment.class, "航标列表", R.drawable.ic_home_aid));
        data.add(new HomeRvItem(EquipListFragment.class, "器材管理", R.drawable.ic_home_equip));
        data.add(new HomeRvItem(EquipListFragment.class, "器材扫码", R.mipmap.icon_tabbar_component));
        data.add(new HomeRvItem(EquipListFragment.class, "器材NFC", R.mipmap.icon_tabbar_component));
        data.add(new HomeRvItem(MapFragment.class, "电子地图", R.drawable.ic_map_foreground));
        data.add(new HomeRvItem(EquipListFragment.class, "消息中心", R.mipmap.icon_tabbar_component));

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

        //查询消息种类
        DataUtils.getDictData(Constant.msgTypeDict, msgTypeData);

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

                String res = HttpUtils.getInstance().sendPost(UrlConfig.msgQueryPageUrl, params);

                Message msg = mHandler.obtainMessage();
                msg.what = MsgType_Msg;
                msg.obj = res;
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
        if(msgData == null){
            msgData = new ArrayList<>();
        }
        JSONArray resData = resObj.getJSONArray("data");
        for (int i = 0; i < resData.size(); i++) {
            msgData.add(resData.getObject(i, Messages.class));
        }
        //初始化消息列表
        initMsgRecyclerView();
    }

    private void initMsgRecyclerView() {

        HomeMsgAdapter itemAdapter = new HomeMsgAdapter(context, msgData, msgTypeData);
        itemAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
                Messages item = itemAdapter.getItem(pos);
                //进入详情页面
                Bundle bundle = new Bundle();
                bundle.putString("sMsg_ID", item.getsMsg_ID());
                bundle.putString("sMsg_AidID", item.getsMsg_AidID());
                bundle.putString("sMsg_EquipID", item.getsMsg_EquipID());
                MsgDetailFragment fragment = new MsgDetailFragment();
                fragment.setArguments(bundle);
                startFragment(fragment);
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
