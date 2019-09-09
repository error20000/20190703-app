
package com.jian.system.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.jian.system.R;
import com.jian.system.adapter.AidAdapter;
import com.jian.system.adapter.BaseRecyclerAdapter;
import com.jian.system.adapter.BaseRecyclerOnScrollListener;
import com.jian.system.adapter.MessagesAdapter;
import com.jian.system.config.Constant;
import com.jian.system.config.UrlConfig;
import com.jian.system.decorator.DividerItemDecoration;
import com.jian.system.entity.Aid;
import com.jian.system.entity.Dict;
import com.jian.system.entity.Messages;
import com.jian.system.fragment.components.AidDetailFragment;
import com.jian.system.fragment.components.AidListFragment;
import com.jian.system.fragment.components.MapFragment;
import com.jian.system.fragment.components.MsgAddFragment;
import com.jian.system.fragment.components.MsgDetailFragment;
import com.jian.system.fragment.components.NfcFragment;
import com.jian.system.utils.DataUtils;
import com.jian.system.utils.FormatUtils;
import com.jian.system.utils.HttpUtils;
import com.jian.system.utils.ThreadUtils;
import com.jian.system.utils.Utils;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.util.QMUIViewHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.QMUIWindowInsetLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.popup.QMUIListPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.pullRefreshLayout.QMUICenterGravityRefreshOffsetCalculator;
import com.qmuiteam.qmui.widget.pullRefreshLayout.QMUIPullRefreshLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MsgLayout extends QMUIWindowInsetLayout {

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.floating_search_view)
    FloatingSearchView mSearchView;
    @BindView(R.id.pull_to_refresh)
    QMUIPullRefreshLayout mPullRefreshLayout;


    private final static String TAG = AidListFragment.class.getSimpleName();
    private String title = "消息中心";
    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;

    private List<Messages> data = new ArrayList<>();
    private List<Dict> msgTypeData = new ArrayList<>();
    private List<Dict> msgStatusData = new ArrayList<>();
    private List<Dict> msgLabelData = new ArrayList<>();

    private int total = 0;
    private int page = 1;
    private int rows = 10;
    private MessagesAdapter mItemAdapter;
    private final int MsgType_Init = 0;
    private final int MsgType_LoadMore = 1;
    private final int MsgType_Refresh = 2;
    private final int MsgType_Search = 3;

    //search
    List<Messages> sdata = new ArrayList<>();
    List<SearchSuggestion> slist = new ArrayList<>();

    private QMUITipDialog tipDialog;
    private QMUIListPopup mListPopup;
    private int typeFilterCheckIndex = -1;
    private int labelFilterCheckIndex = -1;
    private Date timeFilterStartDate;
    private Date timeFilterEndDate;

    private ViewPagerListener mListener;
    Context context;
    FragmentManager manager;

    public MsgLayout(Context context, FragmentManager manager) {
        super(context);
        this.context = context;
        this.manager = manager;

        LayoutInflater.from(context).inflate(R.layout.layout_msg, this);
        ButterKnife.bind(this);

        initTopBar();
        initSearchBar();
        initPullRefresh();
        initData();
    }


    private void initTopBar() {

        mTopBar.addRightImageButton(R.drawable.ic_xinzeng, R.id.topbar_right_change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MsgAddFragment fragment = new MsgAddFragment();
                startFragment(fragment);
            }
        });
        mTopBar.addRightImageButton(R.drawable.ic_filter2, R.id.topbar_right_about_button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showPopupList(view);
            }
        });

        mTopBar.setTitle(title);
    }

    private void showPopupList(View view) {
        initListPopupIfNeed();
        mListPopup.setAnimStyle(QMUIPopup.ANIM_GROW_FROM_RIGHT);
        mListPopup.setPreferredDirection(QMUIPopup.DIRECTION_BOTTOM);
        mListPopup.show(view);
    }
    private void initListPopupIfNeed() {
        if (mListPopup == null) {

            String[] listItems = new String[]{
                    "按日期",
                    "按类型",
                    "按标签",
            };
            List<String> data = new ArrayList<>();

            Collections.addAll(data, listItems);

            ArrayAdapter adapter = new ArrayAdapter<>(context, R.layout.simple_list_item, data);

            mListPopup = new QMUIListPopup(getContext(), QMUIPopup.DIRECTION_NONE, adapter);
            mListPopup.create(QMUIDisplayHelper.dp2px(getContext(), 150), QMUIDisplayHelper.dp2px(getContext(), 180), new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    mListPopup.dismiss();
                    switch (i){
                        case 0:
                            showTimeFilter();
                            break;
                        case 1:
                            showTypeFilter();
                            break;
                        case 2:
                            showLabelFilter();
                            break;
                    }
                }
            });
            mListPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                }
            });
        }
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
                Bundle bundle = new Bundle();
                bundle.putString("sMsg_ID", sdata.get(index).getsMsg_ID());
                bundle.putString("sMsg_AidID", sdata.get(index).getsMsg_AidID());
                bundle.putString("sMsg_EquipID", sdata.get(index).getsMsg_EquipID());
                intoDetail(bundle);
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
                data = new ArrayList<>();
                total = 0;
                page = 1;
                rows = 10;
                //请求数据
                Map<String, Object> params = new HashMap<>();
                params.put("page", page);
                params.put("rows", rows);
                queryData(params, MsgType_Refresh);
            }
        });
    }

    private void initList() {
        mItemAdapter = new MessagesAdapter(context, data, msgTypeData, msgStatusData, msgLabelData);
        mItemAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
                Log.d(TAG, "onItemClick: " + data.get(pos).getsMsg_Title());
                //进入详情页面
                Bundle bundle = new Bundle();
                bundle.putString("sMsg_ID", data.get(pos).getsMsg_ID());
                bundle.putString("sMsg_AidID", data.get(pos).getsMsg_AidID());
                bundle.putString("sMsg_EquipID", data.get(pos).getsMsg_EquipID());
                intoDetail(bundle);
            }
        });
        mRecyclerView.setAdapter(mItemAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
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

    private void intoDetail( Bundle bundle){
        MsgDetailFragment fragment = new MsgDetailFragment();
        fragment.setArguments(bundle);
        startFragment(fragment);
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
        //查询消息状态
        DataUtils.getDictData(Constant.msgStatusDict, msgStatusData);
        //查询消息自定义标签
        DataUtils.getDictData(Constant.msgLabelDict, msgLabelData);
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
                    handleListInit(resData);
                    break;
                case MsgType_LoadMore:
                    handleLoadMore(resData);
                    break;
                case MsgType_Refresh:
                    handleRefresh(resData);
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

    private void queryData(Map<String, Object> params, int init){
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                String res = HttpUtils.getInstance().sendPost(UrlConfig.msgQueryPageUrl, params);
                Message msg = mHandler.obtainMessage();
                msg.what = init; //init
                msg.obj = res;
                mHandler.sendMessage(msg);
            }
        });
    }

    private void handleListInit(JSONObject resObj){
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
            data.add(resData.getObject(i, Messages.class));
        }
        //展示数据
        initList();
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

    private void handleLoadMore(JSONObject resObj){
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
            data.add(resData.getObject(i, Messages.class));
        }
        //刷新数据
        mItemAdapter.setLoadState(mItemAdapter.LOADING_COMPLETE);
    }

    private void handleRefresh(JSONObject resObj){
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
            data.add(resData.getObject(i, Messages.class));
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
                String res = HttpUtils.getInstance().sendPost(UrlConfig.msgSearchUrl, params);
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
            Messages temp = resData.getObject(i, Messages.class);
            sdata.add(temp);
            int index = i;
            node = new SearchSuggestion() {
                @Override
                public String getBody() {
                    return temp.getsMsg_Title();
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

    private void filterData(Map<String, Object> params){
        //清空数据
        data = new ArrayList<>();
        total = 0;
        page = 1;
        rows = 10;
        //请求数据
        if(params == null){
            params = new HashMap<>();
        }
        params.put("page", page);
        params.put("rows", rows);
        queryData(params, MsgType_Refresh);
    }

    private void showTimeFilter(){
        TimeFilterDialogBuilder builder = new TimeFilterDialogBuilder(context);
        builder.setTitle("请选择日期");
        builder.addAction("取消", new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                dialog.dismiss();
            }
        });
        builder.addAction("确定", new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                dialog.dismiss();
                if(timeFilterStartDate == null){
                    timeFilterStartDate = new Date();
                }
                if(timeFilterEndDate == null){
                    timeFilterEndDate = new Date();
                }
                Log.d(TAG, "timeFilterStartDate: " + FormatUtils.formatDate("yyyy-MM-dd HH:mm:ss", timeFilterStartDate));
                Log.d(TAG, "timeFilterEndDate: " + FormatUtils.formatDate("yyyy-MM-dd HH:mm:ss", timeFilterEndDate));
                //筛选数据
                Map<String, Object> params = new HashMap<>();
                params.put("startDate", timeFilterStartDate.getTime());
                params.put("endDate", timeFilterEndDate.getTime());
                filterData(params);
            }
        });
        builder.create(mCurrentDialogStyle).show();
    }

    class TimeFilterDialogBuilder extends QMUIDialog.AutoResizeDialogBuilder {
        private Context mContext;
        private TextView mTextView1;
        private TextView mTextView2;

        public TimeFilterDialogBuilder(Context context) {
            super(context);
            mContext = context;
        }

        public TextView getTextView1() {
            return mTextView1;
        }

        public TextView getTextView2() {
            return mTextView2;
        }

        @Override
        public View onBuildContent(QMUIDialog dialog, ScrollView parent) {
            LinearLayout layout = new LinearLayout(mContext);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setLayoutParams(new ScrollView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            int padding = QMUIDisplayHelper.dp2px(mContext, 20);
            layout.setPadding(padding, padding, padding, padding);
            //开始时间
            mTextView1 = new AppCompatTextView(mContext);
            QMUIViewHelper.setBackgroundKeepingPadding(mTextView1, QMUIResHelper.getAttrDrawable(mContext, R.attr.qmui_list_item_bg_with_border_bottom));
            if(timeFilterStartDate == null){
                mTextView1.setHint("请选择开始日期");
            }else{
                mTextView1.setText(FormatUtils.formatDate("yyyy-MM-dd", timeFilterStartDate));
            }
            layout.addView(mTextView1);
            mTextView1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "请选择开始日期");
                    TimePickerDialog mDialogTime = new TimePickerDialog.Builder()
                            .setCallBack(new OnDateSetListener() {
                                @Override
                                public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                                    timeFilterStartDate = new Date(millseconds);
                                    String str = FormatUtils.formatDate("yyyy-MM-dd", timeFilterStartDate);
                                    timeFilterStartDate = FormatUtils.formatDate("yyyy-MM-dd HH:mm:ss", str+" 00:00:00");
                                    mTextView1.setText(str);
                                }
                            })
                            .setCancelStringId("取消")
                            .setSureStringId("确定")
                            .setTitleStringId("选择日期")
                            .setYearText("年")
                            .setMonthText("月")
                            .setDayText("日")
                            .setHourText("时")
                            .setMinuteText("分")
                            .setCyclic(false)
                            //.setMinMillseconds(System.currentTimeMillis())
                            //.setMaxMillseconds(System.currentTimeMillis() + tenYears)
                            .setCurrentMillseconds(timeFilterStartDate == null ? System.currentTimeMillis() : timeFilterStartDate.getTime())
                            .setThemeColor(getResources().getColor(R.color.app_color_blue))
                            .setType(Type.YEAR_MONTH_DAY)
                            .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                            .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
                            .setWheelItemTextSize(12)
                            .build();
                    mDialogTime.show(manager, "startDate");
                }
            });
            //分隔符
            TextView mTextViewLine = new AppCompatTextView(mContext);
            mTextViewLine.setText("~");
            LinearLayout.LayoutParams lineLP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lineLP.leftMargin = QMUIDisplayHelper.dp2px(getContext(), 10);
            lineLP.rightMargin = QMUIDisplayHelper.dp2px(getContext(), 10);
            mTextViewLine.setLayoutParams(lineLP);
            layout.addView(mTextViewLine);
            //结束时间
            mTextView2 = new AppCompatTextView(mContext);
            QMUIViewHelper.setBackgroundKeepingPadding(mTextView2, QMUIResHelper.getAttrDrawable(mContext, R.attr.qmui_list_item_bg_with_border_bottom));
            if(timeFilterEndDate == null){
                mTextView2.setHint("请选择结束日期");
            }else{
                mTextView2.setText(FormatUtils.formatDate("yyyy-MM-dd", timeFilterEndDate));
            }

            layout.addView(mTextView2);
            mTextView2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "请选择结束日期");
                    TimePickerDialog mDialogTime = new TimePickerDialog.Builder()
                            .setCallBack(new OnDateSetListener() {
                                @Override
                                public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                                    timeFilterEndDate = new Date(millseconds);
                                    String str = FormatUtils.formatDate("yyyy-MM-dd", timeFilterEndDate);
                                    timeFilterEndDate = FormatUtils.formatDate("yyyy-MM-dd HH:mm:ss", str+" 23:59:59");
                                    mTextView2.setText(str);
                                }
                            })
                            .setCancelStringId("取消")
                            .setSureStringId("确定")
                            .setTitleStringId("选择日期")
                            .setYearText("年")
                            .setMonthText("月")
                            .setDayText("日")
                            .setHourText("时")
                            .setMinuteText("分")
                            .setCyclic(false)
                            //.setMinMillseconds(System.currentTimeMillis())
                            //.setMaxMillseconds(System.currentTimeMillis() + tenYears)
                            .setCurrentMillseconds(timeFilterEndDate == null ? System.currentTimeMillis() : timeFilterEndDate.getTime())
                            .setThemeColor(getResources().getColor(R.color.app_color_blue))
                            .setType(Type.YEAR_MONTH_DAY)
                            .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                            .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
                            .setWheelItemTextSize(12)
                            .build();
                    mDialogTime.show(manager, "startDate");
                }
            });

            /*LinearLayout.LayoutParams editTextLP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, QMUIDisplayHelper.dpToPx(50));
            editTextLP.bottomMargin = QMUIDisplayHelper.dp2px(getContext(), 15);
            mEditText.setLayoutParams(editTextLP);
            layout.addView(mEditText);
            TextView textView = new TextView(mContext);
            textView.setLineSpacing(QMUIDisplayHelper.dp2px(getContext(), 4), 1.0f);
            textView.setText("观察聚焦输入框后，键盘升起降下时 dialog 的高度自适应变化。\n\n" +
                    "QMUI Android 的设计目的是用于辅助快速搭建一个具备基本设计还原效果的 Android 项目，" +
                    "同时利用自身提供的丰富控件及兼容处理，让开发者能专注于业务需求而无需耗费精力在基础代码的设计上。" +
                    "不管是新项目的创建，或是已有项目的维护，均可使开发效率和项目质量得到大幅度提升。");
            textView.setTextColor(ContextCompat.getColor(getContext(), R.color.app_color_description));
            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layout.addView(textView);*/
            return layout;
        }
    }

    private void showTypeFilter(){

        String[] items = new String[msgTypeData.size()];
        for (int i = 0; i < msgTypeData.size(); i++) {
            items[i] = msgTypeData.get(i).getsDict_Name();
        }
        new QMUIDialog.CheckableDialogBuilder(context)
                .setTitle("请选择")
                .setCheckedIndex(typeFilterCheckIndex)
                .addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //记录本次选择
                        typeFilterCheckIndex = which;
                        //筛选数据
                        Map<String, Object> params = new HashMap<>();
                        params.put("sMsg_Type", msgTypeData.get(which).getsDict_NO());
                        filterData(params);
                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    private void showLabelFilter(){
        String[] items = new String[msgLabelData.size()];
        for (int i = 0; i < msgLabelData.size(); i++) {
            items[i] = msgLabelData.get(i).getsDict_Name();
        }
        new QMUIDialog.CheckableDialogBuilder(context)
                .setTitle("请选择")
                .setCheckedIndex(labelFilterCheckIndex)
                .addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //记录本次选择
                        labelFilterCheckIndex = which;
                        //筛选数据
                        Map<String, Object> params = new HashMap<>();
                        params.put("sMsg_Label", msgLabelData.get(which).getsDict_NO());
                        filterData(params);
                    }
                })
                .create(mCurrentDialogStyle).show();
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
