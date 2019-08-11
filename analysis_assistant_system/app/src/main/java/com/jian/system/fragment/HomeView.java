
package com.jian.system.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.arlib.floatingsearchview.util.Util;
import com.jian.system.R;
import com.jian.system.fragment.components.EquipAddFragment;
import com.jian.system.fragment.components.EquipDetailFragment;
import com.jian.system.fragment.components.EquipListFragment;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.QMUILoadingView;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.QMUIWindowInsetLayout;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeView extends QMUIWindowInsetLayout{

    private final static String TAG = HomeView.class.getSimpleName();

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;
    @BindView(R.id.groupListView)
    QMUIGroupListView mGroupListView;
    @BindView(R.id.floating_search_view)
    FloatingSearchView mSearchView;

    private MainListener mListener;
    private Context context;

    public HomeView(Context context) {
        super(context);
        this.context = context;

        LayoutInflater.from(context).inflate(R.layout.fragment_home, this);
        ButterKnife.bind(this);

        initTopBar();
        initGroupListView();
        initSearchBar();
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

    private void initGroupListView() {
        QMUICommonListItemView normalItem = mGroupListView.createItemView(
                null,
                "Item 1",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE);
        normalItem.setOrientation(QMUICommonListItemView.VERTICAL);

        QMUICommonListItemView itemWithDetail = mGroupListView.createItemView(
                null,
                "Item 2",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE);
        itemWithDetail.setDetailText("在右方的详细信息");

        QMUICommonListItemView itemWithDetailBelow = mGroupListView.createItemView("Item 3");
        itemWithDetailBelow.setOrientation(QMUICommonListItemView.VERTICAL);
        itemWithDetailBelow.setDetailText("在标题下方的详细信息");

        QMUICommonListItemView itemWithChevron = mGroupListView.createItemView("Item 4");
        itemWithChevron.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUICommonListItemView itemWithSwitch = mGroupListView.createItemView("Item 5");
        itemWithSwitch.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_SWITCH);
        itemWithSwitch.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(getContext(), "checked = " + isChecked, Toast.LENGTH_SHORT).show();
            }
        });

        QMUICommonListItemView itemWithCustom = mGroupListView.createItemView("Item 6");
        itemWithCustom.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        //QMUILoadingView loadingView = new QMUILoadingView(getContext());
        //itemWithCustom.addAccessoryCustomView(loadingView);
        EditText editText = new EditText(getContext());
        editText.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
        editText.setMaxWidth(QMUIDisplayHelper.dp2px(getContext(), 250));
        editText.setSingleLine();
        editText.setHint("test");
        editText.setBackgroundDrawable(null);
        editText.setTextColor(QMUIResHelper.getAttrColor(getContext(), R.attr.qmui_config_color_gray_5));
        editText.setTextSize(QMUIDisplayHelper.px2sp(getContext(), QMUIResHelper.getAttrDimen(getContext(), R.attr.qmui_common_list_item_detail_h_text_size) ));

        itemWithCustom.addAccessoryCustomView(editText);

        QMUICommonListItemView test = mGroupListView.createItemView("Item 7");
        test.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        View testView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_list_item_edit, null);
        test.addAccessoryCustomView(testView);

        QMUICommonListItemView test8 = mGroupListView.createItemView("Item 8");
        test8.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        View testView8 = LayoutInflater.from(getContext()).inflate(R.layout.fragment_list_item_edit, null);
        test8.addAccessoryCustomView(testView8);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof QMUICommonListItemView) {
                    CharSequence text = ((QMUICommonListItemView) v).getText();
                    Toast.makeText(getContext(), text + " is Clicked", Toast.LENGTH_SHORT).show();
                }
            }
        };

        int size = QMUIDisplayHelper.dp2px(getContext(), 20);
        QMUIGroupListView.newSection(getContext())
                .setTitle("Section 1: 默认提供的样式")
                .setDescription("Section 1 的描述")
                .setLeftIconSize(size, ViewGroup.LayoutParams.WRAP_CONTENT)
                .addItemView(normalItem, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EquipDetailFragment fragment = new EquipDetailFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("data","606098577566990336");
                        fragment.setArguments(bundle);
                        startFragment(fragment);
                    }
                })
                .addItemView(itemWithDetail, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EquipAddFragment fragment = new EquipAddFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("data","传递到的数据");
                        fragment.setArguments(bundle);
                        startFragment(fragment);
                    }
                })
                .addItemView(itemWithDetailBelow, onClickListener)
                .addItemView(itemWithChevron, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EquipListFragment fragment = new EquipListFragment();
                        startFragment(fragment);
                    }
                })
                .addItemView(itemWithSwitch, onClickListener)
                .addTo(mGroupListView);

        QMUIGroupListView.newSection(getContext())
                .setTitle("Section 2: 自定义右侧 View")
                .addItemView(itemWithCustom, onClickListener)
                .addItemView(test, onClickListener)
                .addItemView(test8, onClickListener)
                .addTo(mGroupListView);
        QMUIGroupListView.newSection(getContext())
                .setTitle("Section 3: 自定义右侧 View")
                .addTo(mGroupListView);
        QMUIGroupListView.newSection(getContext())
                .setTitle("Section 4: 自定义右侧 View")
                .addTo(mGroupListView);
        QMUIGroupListView.newSection(getContext())
                .setTitle("Section 4: 自定义右侧 View")
                .addTo(mGroupListView);
        QMUIGroupListView.newSection(getContext())
                .setTitle("Section 4: 自定义右侧 View")
                .addTo(mGroupListView);
        QMUIGroupListView.newSection(getContext())
                .setTitle("Section 4: 自定义右侧 View")
                .addTo(mGroupListView);
        QMUIGroupListView.newSection(getContext())
                .setTitle("Section 4: 自定义右侧 View")
                .addTo(mGroupListView);
        QMUIGroupListView.newSection(getContext())
                .setTitle("Section 4: 自定义右侧 View")
                .addTo(mGroupListView);
    }

    private void initSearchBar() {

        mSearchView.setShowMoveUpSuggestion(false);

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
}
