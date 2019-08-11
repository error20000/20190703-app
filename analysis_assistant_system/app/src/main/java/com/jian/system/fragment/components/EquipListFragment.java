
package com.jian.system.fragment.components;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

    private QMUITipDialog tipDialog;

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.floating_search_view)
    FloatingSearchView mSearchView;

    @Override
    protected View onCreateView() {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_equip_list, null);
        ButterKnife.bind(this, rootView);

        initTopBar();
        initSearchBar();
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
                } else {
                    mSearchView.showProgress();
                }
                Log.d(TAG, "onSearchTextChanged()");
            }
        });

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {

                Log.d(TAG, "onSuggestionClicked()");

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
                Toast.makeText(getContext().getApplicationContext(), item.getTitle(),
                        Toast.LENGTH_SHORT).show();

            }
        });

        mSearchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
            @Override
            public void onBindSuggestion(View suggestionView, ImageView leftIcon,
                                         TextView textView, SearchSuggestion item, int itemPosition) {

            }

        });
    }

    private void initEquipList() {
        mItemAdapter = new EquipAdapter(getActivity(), data);
        mItemAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
                Log.d(TAG, "onItemClick: " + data.get(pos).getsEquip_ID());
                EquipDetailFragment fragment = new EquipDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putString("id", data.get(pos).getsEquip_ID());
                fragment.setArguments(bundle);
                startFragment(fragment);
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
                //refreshData();
                // 模拟获取网络数据，延时1s
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        refreshData();
                    }
                }, 1000);
            }
        });

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
                default:
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


}
