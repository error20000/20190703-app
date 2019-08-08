
package com.jian.system.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.jian.system.R;
import com.jian.system.fragment.components.EquipAddFragment;
import com.jian.system.fragment.components.EquipDetailFragment;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUILoadingView;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.QMUIWindowInsetLayout;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeView extends QMUIWindowInsetLayout{

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;
    @BindView(R.id.groupListView)
    QMUIGroupListView mGroupListView;

    private MainListener mListener;
    private Context context;

    public HomeView(Context context) {
        super(context);
        this.context = context;

        LayoutInflater.from(context).inflate(R.layout.fragment_home, this);
        ButterKnife.bind(this);

        initTopBar();
        initGroupListView();
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
        editText.setWidth(QMUIDisplayHelper.dp2px(getContext(), 300));
        editText.setHint("test");
        itemWithCustom.addAccessoryCustomView(editText);

        QMUICommonListItemView test = mGroupListView.createItemView("Item 7");
        test.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        View testView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_list_item_edit, null);
        test.addAccessoryCustomView(testView);

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
                .addItemView(itemWithChevron, onClickListener)
                .addItemView(itemWithSwitch, onClickListener)
                .addTo(mGroupListView);

        QMUIGroupListView.newSection(getContext())
                .setTitle("Section 2: 自定义右侧 View")
                .addItemView(itemWithCustom, onClickListener)
                .addItemView(test, onClickListener)
                .addTo(mGroupListView);
    }
}
