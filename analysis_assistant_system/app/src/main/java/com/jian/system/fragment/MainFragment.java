
package com.jian.system.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.jian.system.R;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.QMUITabSegment;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainFragment extends QMUIFragment {

    @BindView(R.id.pager)
    ViewPager mViewPager;
    @BindView(R.id.tabs)
    QMUITabSegment mTabSegment;

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
    protected View onCreateView() {
        FrameLayout layout = (FrameLayout) LayoutInflater.from(getActivity()).inflate(R.layout.fragment_main, null);
        ButterKnife.bind(this, layout);
        initTabs();
        initPagers();
        return layout;
    }

    private void initTabs() {
        int normalColor = QMUIResHelper.getAttrColor(getActivity(), R.attr.qmui_config_color_gray_6);
        int selectColor = QMUIResHelper.getAttrColor(getActivity(), R.attr.qmui_config_color_blue);
        mTabSegment.setDefaultNormalColor(normalColor);
        mTabSegment.setDefaultSelectedColor(selectColor);

        QMUITabSegment.Tab home = new QMUITabSegment.Tab(
                ContextCompat.getDrawable(getContext(), R.mipmap.icon_tabbar_component),
                ContextCompat.getDrawable(getContext(), R.mipmap.icon_tabbar_component_selected),
                "首页", false
        );

        QMUITabSegment.Tab map = new QMUITabSegment.Tab(
                ContextCompat.getDrawable(getContext(), R.mipmap.icon_tabbar_component),
                ContextCompat.getDrawable(getContext(), R.mipmap.icon_tabbar_component_selected),
                "地图", false
        );
        QMUITabSegment.Tab scan = new QMUITabSegment.Tab(
                ContextCompat.getDrawable(getContext(), R.mipmap.icon_tabbar_component),
                ContextCompat.getDrawable(getContext(), R.mipmap.icon_tabbar_component_selected),
                "扫一扫", false
        );
        QMUITabSegment.Tab msg = new QMUITabSegment.Tab(
                ContextCompat.getDrawable(getContext(), R.mipmap.icon_tabbar_component),
                ContextCompat.getDrawable(getContext(), R.mipmap.icon_tabbar_component_selected),
                "消息", false
        );
        QMUITabSegment.Tab center = new QMUITabSegment.Tab(
                ContextCompat.getDrawable(getContext(), R.mipmap.icon_tabbar_component),
                ContextCompat.getDrawable(getContext(), R.mipmap.icon_tabbar_component_selected),
                "个人中心", false
        );
        mTabSegment.addTab(home)
                .addTab(map)
                .addTab(scan)
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
        };

        HomeView homeView = new HomeView(getActivity());
        homeView.setMainListener(listener);
        mPages.put(Pager.Home, homeView);

        MapView mapView = new MapView(getActivity());
        mapView.setMainListener(listener);
        mPages.put(Pager.Map, mapView);

        ScanView scanView = new ScanView(getActivity());
        scanView.setMainListener(listener);
        mPages.put(Pager.Scan, scanView);

        MsgView msgView = new MsgView(getActivity());
        msgView.setMainListener(listener);
        mPages.put(Pager.Msg, msgView);

        CenterView centerView = new CenterView(getActivity());
        centerView.setMainListener(listener);
        mPages.put(Pager.Center, centerView);

        mViewPager.setAdapter(mPagerAdapter);
        mTabSegment.setupWithViewPager(mViewPager, false);
    }

    enum Pager {
        Home, Map, Scan, Msg, Center;

        public static Pager getPagerFromPositon(int position) {
            switch (position) {
                case 0:
                    return Home;
                case 1:
                    return Map;
                case 2:
                    return Scan;
                case 3:
                    return Msg;
                case 4:
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

}