
package com.jian.system.fragment.components;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.jian.system.fragment.ViewPagerListener;
import com.jian.system.utils.DataUtils;
import com.jian.system.utils.HttpUtils;
import com.jian.system.utils.ThreadUtils;
import com.jian.system.utils.Utils;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUITabSegment;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
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

    private String sAid_ID;

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

        initTopBar();
        initTabAndPager();

        return rootView;
    }

    private void initTopBar() {
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        mTopBar.setTitle(title);
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
        };

        AidDetailView detailView = new AidDetailView(getActivity(), sAid_ID);
        detailView.setViewPagerListener(listener);
        mPages.put(Pager.Detail, detailView);

        AidEquipView equipView = new AidEquipView(getActivity(), sAid_ID);
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
