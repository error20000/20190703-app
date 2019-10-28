
package com.jian.system.fragment.components;

import android.Manifest;
import android.content.Intent;
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
import com.jian.system.adapter.AidAdapter;
import com.jian.system.adapter.BaseRecyclerAdapter;
import com.jian.system.adapter.BaseRecyclerOnScrollListener;
import com.jian.system.adapter.EquipAdapter;
import com.jian.system.config.Constant;
import com.jian.system.config.UrlConfig;
import com.jian.system.decorator.DividerItemDecoration;
import com.jian.system.entity.Aid;
import com.jian.system.entity.Dict;
import com.jian.system.entity.Equip;
import com.jian.system.utils.DataUtils;
import com.jian.system.utils.HttpUtils;
import com.jian.system.utils.ThreadUtils;
import com.jian.system.utils.Utils;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class AidListFragment extends QMUIFragment {

    private final static String TAG = AidListFragment.class.getSimpleName();
    private String title = "航标列表";

    private List<Aid> data = new ArrayList<>();
    private List<Dict> aidTypeData = new ArrayList<>();
    private List<Dict> aidStationData = new ArrayList<>();
    private List<Dict> aidIconData = new ArrayList<>();
    private List<Dict> aidLightingData = new ArrayList<>();
    private List<Dict> aidMarkData = new ArrayList<>();
    private int total = 0;
    private int page = 1;
    private int rows = 10;
    private AidAdapter mItemAdapter;
    private final int MsgType_Init = 0;
    private final int MsgType_LoadMore = 1;
    private final int MsgType_Refresh = 2;
    private final int MsgType_Search = 3;

    //search
    List<Aid> sdata = new ArrayList<>();
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
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_aid_list, null);
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
                intoDetail(sdata.get(index).getsAid_ID());
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
                clearData();
                //请求数据
                Map<String, Object> params = new HashMap<>();
                params.put("page", page);
                params.put("rows", rows);
                queryData(params, MsgType_Refresh);
            }
        });
    }

    private void initAidList() {
        mItemAdapter = new AidAdapter(getActivity(), data, aidTypeData);
        mItemAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
                Log.d(TAG, "onItemClick: " + data.get(pos).getsAid_Name());
                //进入详情页面
                intoDetail(data.get(pos).getsAid_ID());
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
                loadMoreData();
            }
        });

    }

    private void intoDetail(String sAid_ID){
        Bundle bundle = new Bundle();
        bundle.putString("sAid_ID", sAid_ID);
        AidDetailFragment fragment = new AidDetailFragment();
        fragment.setArguments(bundle);
        startFragment(fragment);
    }

    private void clearData(){
        data = new ArrayList<>();
        total = 0;
        page = 1;
        rows = 10;
    }

    private void initData(){
        //清空数据
        clearData();
        //查询数据 -- 判断网络
        tipDialog = new QMUITipDialog.Builder(getContext())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();
        tipDialog.show();
        //查询航标种类
        DataUtils.getDictData(Constant.aidTypeDict, aidTypeData);
        //查询航标列表
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        params.put("rows", rows);
        queryData(params, MsgType_Init);
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
                case MsgType_Init:
                    handleAidListInit(resData);
                    break;
                case MsgType_LoadMore:
                    handleAidLoadMore(resData);
                    break;
                case MsgType_Refresh:
                    handleAidRefresh(resData);
                    break;
                case MsgType_Search:
                    handleSearch(resData);
                    break;
                default:
                    break;
            }

        }
    };

    //TODO --------------------------------------------------------------------------- handle

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

    private void queryData(Map<String, Object> params, int init){
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                String res = HttpUtils.getInstance().sendPost(UrlConfig.aidQueryPageUrl, params);
                Message msg = mHandler.obtainMessage();
                msg.what = init; //init
                msg.obj = res;
                mHandler.sendMessage(msg);
            }
        });
    }

    private void handleAidListInit(JSONObject resObj){
        if (handleErrorCode(resObj)) {
            Log.d(TAG, resObj.toJSONString());
            return;
        }
        //分页
        total = resObj.getInteger("total");
        JSONArray resData = resObj.getJSONArray("data");
        if(data == null){
            data = new ArrayList<>();
        }
        for (int i = 0; i < resData.size(); i++) {
            data.add(resData.getObject(i, Aid.class));
        }
        //展示数据
        initAidList();
    }

    private void loadMoreData(){
        if( data.size() >= total ){
            return;
        }
        page = page + 1;
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        params.put("rows", rows);
        queryData(params, MsgType_LoadMore);
    }

    private void handleAidLoadMore(JSONObject resObj){
        if (handleErrorCode(resObj)) {
            Log.d(TAG, resObj.toJSONString());
            return;
        }
        //分页
        total = resObj.getInteger("total");
        JSONArray resData = resObj.getJSONArray("data");
        if(data == null){
            data = new ArrayList<>();
        }
        for (int i = 0; i < resData.size(); i++) {
            data.add(resData.getObject(i, Aid.class));
        }
        //刷新数据
        mItemAdapter.setLoadState(mItemAdapter.LOADING_COMPLETE);
    }

    private void handleAidRefresh(JSONObject resObj){
        if (handleErrorCode(resObj)) {
            Log.d(TAG, resObj.toJSONString());
            return;
        }
        //分页
        total = resObj.getInteger("total");
        JSONArray resData = resObj.getJSONArray("data");
        if(data == null){
            data = new ArrayList<>();
        }
        for (int i = 0; i < resData.size(); i++) {
            data.add(resData.getObject(i, Aid.class));
        }
        //刷新数据
        showToast("刷新成功");
        mItemAdapter.setData(data);
        mPullRefreshLayout.finishRefresh();
    }

    private void searchData(String keywords){
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<>();
                params.put("keywords", keywords);
                String res = HttpUtils.getInstance().sendPost(UrlConfig.aidSearchUrl, params);
                Message msg = mHandler.obtainMessage();
                msg.what = MsgType_Search;
                msg.obj = res;
                mHandler.sendMessage(msg);
            }
        });
    }

    private void handleSearch(JSONObject resObj){
        if (handleErrorCode(resObj)) {
            Log.d(TAG, resObj.toJSONString());
            return;
        }
        JSONArray resData = resObj.getJSONArray("data");

        mSearchView.hideProgress();
        sdata = new ArrayList<>();
        slist = new ArrayList<>();

        //更新搜索建议
        SearchSuggestion node = null;
        for (int i = 0; i < resData.size(); i++) {
            Aid temp = resData.getObject(i, Aid.class);
            sdata.add(temp);
            int index = i;
            node = new SearchSuggestion() {
                @Override
                public String getBody() {
                    return temp.getsAid_NO();
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
