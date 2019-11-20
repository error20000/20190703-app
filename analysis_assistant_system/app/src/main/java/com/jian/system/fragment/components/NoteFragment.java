
package com.jian.system.fragment.components;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jian.system.R;
import com.jian.system.entity.Note;
import com.jian.system.utils.FormatUtils;
import com.jian.system.utils.ThreadUtils;
import com.jian.system.utils.Utils;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoteFragment extends QMUIFragment {

    private final static String TAG = EquipListFragment.class.getSimpleName();
    private String title = "记事本";
    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;
    private QMUITipDialog tipDialog;
    private final int MsgType_List = 1;
    private final int MsgType_Delete = 2;
    private final int MsgType_Add = 3;

    //search
    List<Note> data = new ArrayList<>();

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;
    @BindView(R.id.groupListView)
    QMUIGroupListView mGroupListView;

    @Override
    protected View onCreateView() {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_note_list, null);
        ButterKnife.bind(this, rootView);

        initTopBar();
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
        mTopBar.addRightImageButton(R.drawable.ic_xinzeng, R.id.topbar_right_about_button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                NoteAddFragment fragment = new NoteAddFragment();
                startFragment(fragment);
            }
        });

        mTopBar.setTitle(title);
    }

    private void  clearData(){
        //清空数据
        data = new ArrayList<>();
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
        //查询列表
        Map<String, Object> params = new HashMap<>();
        queryData(params);

    }

    private void queryData(Map<String, Object> params){
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                //String res = HttpUtils.getInstance().sendPost(UrlConfig.equipQueryDetailUrl, params);
                Message msg = mHandler.obtainMessage();
                msg.what = MsgType_List;
                //msg.obj = res;
                msg.obj = "{code:1,data:[]}";
                mHandler.sendMessage(msg);
            }
        });
    }

    private void initNode() {

        if(data.size() == 0){
            return;
        }

        //按更新日期排序
        Collections.sort(data, new Comparator<Note>() {
            @Override
            public int compare(Note o1, Note o2) {
                return o1.getdNote_UpdateDate().getTime() > o2.getdNote_UpdateDate().getTime() ? -1 : 1;
            }
        });

        QMUIGroupListView.Section section = QMUIGroupListView.newSection(getContext()).setTitle("");
        for (int i = 0; i < data.size(); i++){
            Note note = data.get(i);
            QMUICommonListItemView node = mGroupListView.createItemView(note.getsNote_Content());
            node.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
            node.getTextView().setMaxWidth(QMUIDisplayHelper.dp2px(getContext(), 200));
            node.getTextView().setSingleLine(true);
            node.getTextView().setEllipsize(TextUtils.TruncateAt.END);
            node.setDetailText(FormatUtils.formatDate("yyyy-MM-dd", note.getdNote_UpdateDate()));
            node.setTag(JSONObject.toJSONString(note)); //传参
            section.addItemView(node, mOnClickListenerGroup, mOnLongClickListenerGroup);
        }
        section.addTo(mGroupListView);

    }

    private View.OnClickListener mOnClickListenerGroup;
    private View.OnLongClickListener mOnLongClickListenerGroup;
    {
        mOnClickListenerGroup = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String objStr = (String) v.getTag();
                //进入详情
                goDetail(objStr);
            }
        };

        mOnLongClickListenerGroup = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String objStr = (String) v.getTag();
                String[] items = {"删除"};
                new QMUIDialog.MenuDialogBuilder(getActivity())
                        .setTitle("请选择")
                        .addItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                //二次确认
                                new QMUIDialog.MessageDialogBuilder(getContext())
                                        .setTitle("")
                                        .setMessage("确定要删除该记录吗？")
                                        .addAction("取消", new QMUIDialogAction.ActionListener() {
                                            @Override
                                            public void onClick(QMUIDialog dialog, int index) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .addAction(0, "确定", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                                            @Override
                                            public void onClick(QMUIDialog dialog, int index) {
                                                dialog.dismiss();
                                                //删除
                                                deleteNote(objStr);
                                            }
                                        })
                                        .create(mCurrentDialogStyle).show();
                            }
                        })
                        .create(mCurrentDialogStyle).show();

                return true;
            }
        };
    }

    private void deleteNote(String objStr){
        Log.e("deleteNote", objStr);
    }

    private void goDetail(String objStr){
        Log.e("goDetail", objStr);
        Bundle bundle = new Bundle();
        bundle.putString("obj", objStr);
        NoteDetailFragment fragment = new NoteDetailFragment();
        fragment.setArguments(bundle);
        startFragment(fragment);
    }


    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            hideTips();
            String str =  (String) msg.obj;
            //异常处理
            if(Utils.isNullOrEmpty(str)){
                showToast("网络异常，请检查网络。");
                return;
            }
            JSONObject resData = JSONObject.parseObject(str);
            if (handleErrorCode(resData)) {
                Log.d(TAG, resData.toJSONString());
                return;
            }
            //处理数据
            switch (msg.what) {
                case MsgType_List: //入库
                    handleData(resData);
                    break;
                default:
                    break;
            }
        }
    };
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


    //TODO --------------------------------------------------------------------------- handle

    private void handleData(JSONObject resObj) {

        JSONArray resData = resObj.getJSONArray("data");
        if(data == null){
            data = new ArrayList<>();
        }
        for (int i = 0; i < resData.size(); i++) {
            data.add(resData.getObject(i, Note.class));
        }

        //模拟数据
        for (int i = 0; i < 10; i++) {
            Note node = new Note();
            node.setdNote_UpdateDate(new Date(new Date().getTime() + i * 24*3600*1000));
            node.setsNote_Content(i+"成都测试测试测试村\n上春树菜市场上传市场上\n菜市场生产上厕所");
            data.add(node);
        }
        //初始化列表
        initNode();
    }
}
