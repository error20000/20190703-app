package com.jian.system.fragment.components;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONObject;
import com.jian.system.MainActivity;
import com.jian.system.R;
import com.jian.system.adapter.DialogSearchItemAdapter;
import com.jian.system.adapter.StopwatchItemAdapter;
import com.jian.system.model.StopwatchItem;
import com.jian.system.utils.FormatUtils;
import com.jian.system.utils.Utils;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StopwatchFragment extends QMUIFragment {

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;
    @BindView(R.id.textView1)
    TextView mTextView1;
    @BindView(R.id.textView2)
    TextView mTextView2;
    @BindView(R.id.textView3)
    TextView mTextView3;
    @BindView(R.id.textView4)
    TextView mTextView4;
    @BindView(R.id.buttonStart)
    QMUIRoundButton mButtonStart;
    @BindView(R.id.buttonClear)
    QMUIRoundButton mButtonClear;
    @BindView(R.id.listView)
    ListView mListView;

    String title = "秒表";
    boolean isStart = false;
    boolean canClear = false;
    Date startDate = null;
    long time = 0;
    long lastTime = 0;
    int index = 0;
    Thread mThread;
    List<StopwatchItem> mData = new ArrayList<>();
    StopwatchItemAdapter adapter;

    @Override
    protected View onCreateView() {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_stopwatch, null);
        ButterKnife.bind(this, rootView);

        initTopBar();
        init();

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

    private void init(){
        //计时器线程
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(!Thread.interrupted()){

                        Thread.sleep(100);

                        time += 100;

                        Message message = mHandler.obtainMessage();
                        mHandler.sendMessage(message);

                    }
                } catch (InterruptedException e) {

                }

            }
        });
        //按钮事件
        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isStart){
                    mButtonStart.setText("开始");
                    stop();
                }else{
                    mButtonStart.setText("停止");
                    start();
                }
            }
        });

        mButtonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isStart){
                    count();
                }else{
                    clear();
                }
            }
        });
        //绑定list
        adapter = new StopwatchItemAdapter(getContext(), mData, R.layout.fragment_stopwatch_list_item);
        mListView.setAdapter(adapter);
    }

    private void start(){
        if(isStart){
            return;
        }
        startDate = new Date();
        mThread.start();
        isStart = true;
        canClear = true;
        mButtonClear.setText("计次");
        index = 0;
        //list
        String dateStr = FormatUtils.formatDate("yyyy-MM-dd HH:mm:ss.S", startDate);
        mData.add(0, new StopwatchItem("开始时间", dateStr, ""));
        updateList();
    }

    private void stop(){
        if(!isStart){
            return;
        }
        mThread.interrupt();
        isStart = false;
        mButtonClear.setText("重置");
        //list
        long add = time - lastTime;
        startDate = new Date(startDate.getTime() + add);
        String dateStr = FormatUtils.formatDate("yyyy-MM-dd HH:mm:ss.S", startDate);
        mData.add(0, new StopwatchItem("结束时间", dateStr, "+" + format(add)));
        lastTime = time;
        updateList();
    }

    private void clear(){
        if(!canClear){
            return;
        }
        time = 0;
        lastTime = 0;
        index = 0;
        startDate = null;
        updateTime();
        //list
        mData.clear();
        updateList();
    }

    private void count(){
        index++;
        //list
        long add = time - lastTime;
        startDate = new Date(startDate.getTime() + add);
        String dateStr = FormatUtils.formatDate("yyyy-MM-dd HH:mm:ss.S", startDate);
        mData.add(0, new StopwatchItem("计次"+index, dateStr, "+" + format(add)));
        lastTime = time;
        updateList();
    }

    private void updateTime(){
        int hour=(int) (time / (3600 * 1000)) % 60;
        int minute = (int) (time / (60 * 1000)) % 60;
        int second = (int) (time / 1000) % 1000;
        int msecond = (int) (time % 1000) / 100;
        if (hour < 10)
            mTextView1.setText("0" + hour+":");
        else
            mTextView1.setText("" + hour+":");
        if (minute < 10)
            mTextView2.setText("0" + minute+":");
        else
            mTextView2.setText("" + minute+":");
        if (second < 10)
            mTextView3.setText("0" + second);
        else
            mTextView3.setText("" + second);
        mTextView4.setText("." + msecond);
    }

    private void updateList(){
        adapter.notifyDataSetChanged();
    }

    private String format(long time){
        int hour=(int) (time / (3600 * 1000)) % 60;
        int minute = (int) (time / (60 * 1000)) % 60;
        int second = (int) (time / 1000) % 1000;
        int msecond = (int) (time % 1000) / 100;
        String str = "";
        if (hour < 10)
            str += "0" + hour+":";
        else
            str += "" + hour+":";
        if (minute < 10)
            str += "0" + minute+":";
        else
            str += "" + minute+":";
        if (second < 10)
            str += "0" + second;
        else
            str += "" + second;
        str += "." + msecond;
        return str;
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            updateTime();
        }
    };
}
