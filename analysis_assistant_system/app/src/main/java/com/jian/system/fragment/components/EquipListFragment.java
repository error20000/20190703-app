
package com.jian.system.fragment.components;

import android.Manifest;
import android.content.Intent;
import android.media.audiofx.DynamicsProcessing;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.jian.system.Application;
import com.jian.system.R;
import com.jian.system.adapter.BaseRecyclerAdapter;
import com.jian.system.adapter.BaseRecyclerOnScrollListener;
import com.jian.system.adapter.EquipAdapter;
import com.jian.system.config.UrlConfig;
import com.jian.system.decorator.DividerItemDecoration;
import com.jian.system.entity.Dict;
import com.jian.system.entity.Equip;
import com.jian.system.utils.DataUtils;
import com.jian.system.utils.HttpUtils;
import com.jian.system.utils.ThreadUtils;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.pullRefreshLayout.QMUICenterGravityRefreshOffsetCalculator;
import com.qmuiteam.qmui.widget.pullRefreshLayout.QMUIPullRefreshLayout;
import com.sonnyjack.library.qrcode.QrCodeUtils;
import com.sonnyjack.permission.IRequestPermissionCallBack;
import com.sonnyjack.permission.PermissionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EquipListFragment extends QMUIFragment {

    private final static String TAG = EquipListFragment.class.getSimpleName();
    private String title = "器材列表";
    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;
    private List<Equip> data = new ArrayList<>();
    private List<Dict> equipTypeData = new ArrayList<>();
    private List<Dict> equipStatusData = new ArrayList<>();
    private int total = 0;
    private int page = 1;
    private int rows = 10;
    private EquipAdapter mItemAdapter;
    private final int MsgType_INIT = 0;
    private final int MsgType_LOADMORE = 1;
    private final int MsgType_REFRESH = 2;
    private final int MsgType_SEARCH = 3;

    //search
    List<Equip> sdata = new ArrayList<>();
    List<SearchSuggestion> slist = new ArrayList<>();

    private QMUITipDialog tipDialog;

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.floating_search_view)
    FloatingSearchView mSearchView;
    @BindView(R.id.pull_to_refresh)
    QMUIPullRefreshLayout mPullRefreshLayout;

    @Override
    protected View onCreateView() {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_equip_list, null);
        ButterKnife.bind(this, rootView);

        initTopBar();
        initSearchBar();
        initPullRefresh();
        initData();

        return rootView;
    }

    private void initTopBar() {
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popBackStack();
            }
        });
        mTopBar.addRightTextButton("新增", R.id.topbar_right_about_button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                EquipAddFragment fragment = new EquipAddFragment();
                startFragment(fragment);
            }
        });

        mTopBar.setTitle(title);
    }

    private void initSearchBar() {

        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {

            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                if (!oldQuery.equals("") && newQuery.equals("")) {
                    mSearchView.clearSuggestions();
                    //还原列表
                    mItemAdapter.setData(data);
                } else {
                    mSearchView.showProgress();
                    //查询数据
                    searchData(newQuery);
                }
                Log.d(TAG, "onSearchTextChanged()");
            }
        });

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {
                Log.d(TAG, "onSuggestionClicked()");
                mSearchView.clearQuery();
                mSearchView.clearSearchFocus();
                //进入详情页面
                int index = searchSuggestion.describeContents();
                intoDetail(sdata.get(index).getsEquip_ID());
            }

            @Override
            public void onSearchAction(String query) {
                Log.d(TAG, "onSearchAction()");
            }
        });

        mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                Log.d(TAG, "onFocus()");
            }

            @Override
            public void onFocusCleared() {
                Log.d(TAG, "onFocusCleared()");
            }
        });

        mSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                    Log.d(TAG, "onActionMenuItemSelected()" +item.getTitle());
                    if("scan".equals(item.getTitle())){
                        scanQrCode();
                    }else if("nfc".equals(item.getTitle())){
                        scanNFC();
                    }
            }
        });

        mSearchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
            @Override
            public void onBindSuggestion(View suggestionView, ImageView leftIcon,
                                         TextView textView, SearchSuggestion item, int itemPosition) {

            }

        });
    }

    private void initPullRefresh(){
        mPullRefreshLayout.setRefreshOffsetCalculator(new QMUICenterGravityRefreshOffsetCalculator());
        mPullRefreshLayout.setOnPullListener(new QMUIPullRefreshLayout.OnPullListener() {
            @Override
            public void onMoveTarget(int offset) {

            }

            @Override
            public void onMoveRefreshView(int offset) {

            }

            @Override
            public void onRefresh() {
                //清空数据
                data = new ArrayList<>();
                total = 0;
                page = 1;
                rows = 10;
                //请求数据
                Map<String, Object> params = new HashMap<>();
                params.put("page", page);
                params.put("rows", rows);
                queryData(params, MsgType_REFRESH);
            }
        });
    }

    private void initEquipList() {
        mItemAdapter = new EquipAdapter(getActivity(), data, equipTypeData, equipStatusData);
        mItemAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
                Log.d(TAG, "onItemClick: " + data.get(pos).getsEquip_ID());
                //进入详情页面
                intoDetail(data.get(pos).getsEquip_ID());
            }
        });
        mRecyclerView.setAdapter(mItemAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        // 设置加载更多监听
        mRecyclerView.addOnScrollListener(new BaseRecyclerOnScrollListener() {

            @Override
            public void onLoadMore() {
                if( data.size() >= total ){
                    mItemAdapter.setLoadState(mItemAdapter.LOADING_END);
                    return;
                }
                mItemAdapter.setLoadState(mItemAdapter.LOADING);
                refreshData();
                // 模拟获取网络数据，延时1s
                /*new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        refreshData();
                    }
                }, 1000);*/
            }
        });

    }

    private void intoDetail(String sEquip_ID){
        Bundle bundle = new Bundle();
        bundle.putString("id", sEquip_ID);
        EquipDetailFragment fragment = new EquipDetailFragment();
        fragment.setArguments(bundle);
        startFragment(fragment);
    }

    /**
     * 扫描二维码（先请求权限，用第三方库）
     */
    private void scanQrCode() {
        ArrayList<String> permissionList = new ArrayList<>();
        permissionList.add(Manifest.permission.CAMERA);
        PermissionUtils.getInstances().requestPermission(getActivity(), permissionList, new IRequestPermissionCallBack() {
            @Override
            public void onGranted() {
                //扫描二维码/条形码
                //QrCodeUtils.startScan(getActivity(), Application.Scan_Search_Request_Code);
                Intent intent = QrCodeUtils.createScanQrCodeIntent(getActivity());
                startActivityForResult(intent, Application.Scan_Search_Request_Code);
            }

            @Override
            public void onDenied() {
                Toast.makeText(getActivity(), "请在应用管理中打开拍照权限", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case Application.Scan_Search_Request_Code:
                    String str = QrCodeUtils.getScanResult(data);
                    Log.d("onActivityResult", str);
                    //进入详情
                    intoDetail(str);
                    break;
            }
        }
    }

    /**
     * NFC扫描
     */
    private void scanNFC() {

    }


    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            JSONObject resObj = (JSONObject) msg.obj;
            if(resObj.getInteger("code") <= 0){
                QMUITipDialog tipDialog = new QMUITipDialog.Builder(getContext())
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                        .setTipWord(resObj.getString("msg"))
                        .create();
                tipDialog.show();
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tipDialog.dismiss();
                    }
                }, 1500);
                return;
            }
            total = resObj.getInteger("total");
            JSONArray resData = resObj.getJSONArray("data");
            if(data == null){
                data = new ArrayList<>();
            }
            for (int i = 0; i < resData.size(); i++) {
                data.add(resData.getObject(i, Equip.class));
            }

            //处理数据
            switch (msg.what){
                case MsgType_INIT:
                    initEquipList();
                    tipDialog.dismiss();
                    break;
                case MsgType_LOADMORE:
                    mItemAdapter.setLoadState(mItemAdapter.LOADING_COMPLETE);
                    break;
                case MsgType_REFRESH:
                    Toast.makeText(getActivity(), "刷新成功", Toast.LENGTH_SHORT).show();
                    mItemAdapter.setData(data);
                    mPullRefreshLayout.finishRefresh();
                    break;
                default:
                    break;
            }

        }
    };

    Handler mSearchHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            JSONObject resObj = (JSONObject) msg.obj;
            if(resObj.getInteger("code") <= 0){
                Toast.makeText(getActivity(), resObj.getString("msg"), Toast.LENGTH_SHORT).show();
                return;
            }
            //处理数据
            switch (msg.what){
                case MsgType_SEARCH:
                    handleSearch(resObj);
                    break;
            }
        }
    };

    private void initData(){
        //查询数据 -- 判断网络
        tipDialog = new QMUITipDialog.Builder(getContext())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();
        tipDialog.show();
        //查询器材种类
        DataUtils.getDictData("EquipType", equipTypeData);
        //查询器材状态
        DataUtils.getDictData("EquipStatus", equipStatusData);
        //查询器材列表
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        params.put("rows", rows);
        queryData(params, MsgType_INIT);

    }

    private void refreshData(){
        if( data.size() >= total ){
            return;
        }
        page = page + 1;
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        params.put("rows", rows);
        queryData(params, MsgType_LOADMORE);
    }

    private void queryData(Map<String, Object> params, int init){
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                String res = HttpUtils.getInstance().sendPost(UrlConfig.equipQueryPageUrl, params);
                if(res == null || "".equals(res)){
                    Log.d(TAG, UrlConfig.equipQueryPageUrl + " return  is null ");
                    return;
                }
                JSONObject resObj = JSONObject.parseObject(res);
                Message msg = mHandler.obtainMessage();
                msg.what = init; //init
                msg.obj = resObj;
                mHandler.sendMessage(msg);
            }
        });
    }


    private void searchData(String keywords){
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<>();
                params.put("keywords", keywords);
                String res = HttpUtils.getInstance().sendPost(UrlConfig.equipSearchUrl, params);
                if(res == null || "".equals(res)){
                    Log.d(TAG, UrlConfig.equipSearchUrl + " return  is null ");
                    return;
                }
                JSONObject resObj = JSONObject.parseObject(res);
                Message msg = mHandler.obtainMessage();
                msg.what = MsgType_SEARCH;
                msg.obj = resObj;
                mSearchHandler.sendMessage(msg);
            }
        });
    }

    private void handleSearch(JSONObject resObj){
        JSONArray resData = resObj.getJSONArray("data");
        Log.d(TAG, resObj.getString("data"));

        mSearchView.hideProgress();
        sdata = new ArrayList<>();
        slist = new ArrayList<>();

        //更新搜索建议
        SearchSuggestion node = null;
        for (int i = 0; i < resData.size(); i++) {
            Equip temp = resData.getObject(i, Equip.class);
            sdata.add(temp);
            int index = i;
            node = new SearchSuggestion() {
                @Override
                public String getBody() {
                    return temp.getsEquip_NO();
                }

                @Override
                public int describeContents() {
                    return index;
                }

                @Override
                public void writeToParcel(Parcel parcel, int i) {

                }
            };
            slist.add(node);
        }
        mSearchView.swapSuggestions(slist);

        //更新列表
        mItemAdapter.setData(sdata);
    }
}
