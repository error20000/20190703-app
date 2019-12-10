
package com.jian.system.fragment.components;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jian.system.Application;
import com.jian.system.R;
import com.jian.system.config.UrlConfig;
import com.jian.system.entity.Note;
import com.jian.system.entity.User;
import com.jian.system.service.NoteService;
import com.jian.system.utils.FormatUtils;
import com.jian.system.utils.HttpUtils;
import com.jian.system.utils.LoginUtils;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private NoteService service;
    public static String imgRegEx = "\\[img=[^\\]]+\\]";

    List<Note> data = new ArrayList<>();
    QMUIGroupListView.Section section = null;

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;
    @BindView(R.id.groupListView)
    QMUIGroupListView mGroupListView;

    @Override
    protected View onCreateView() {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_note_list, null);
        ButterKnife.bind(this, rootView);

        initTopBar();
        //initData();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
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
        //Map<String, Object> params = new HashMap<>();
        //queryData(params);

        service = new NoteService(getContext());
        queryDataLocal();
    }

    private void queryDataLocal(){

        //查询数据库
        User user = LoginUtils.getLoginUser(getContext());
        data = service.selectList(user != null ? user.getsUser_ID() : "");

        hideTips();

        //初始化列表
        initNode();
    }

    private void queryData(Map<String, Object> params){
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                String res = HttpUtils.getInstance().sendPost(UrlConfig.equipQueryDetailUrl, params);
                Message msg = mHandler.obtainMessage();
                msg.what = MsgType_List;
                msg.obj = res;
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

        if(section != null){
            section.removeFrom(mGroupListView);
        }
        section = QMUIGroupListView.newSection(getContext()).setTitle("");
        for (int i = 0; i < data.size(); i++){
            Note note = data.get(i);
            String title = note.getsNote_Content().split("\\n")[0];
            title = title.replaceAll(imgRegEx, ""); //去除图片
            QMUICommonListItemView node = mGroupListView.createItemView(title);
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
        Note note = JSONObject.parseObject(objStr, Note.class);
        if(note == null){
            showToast("删除失败：记录不存在。");
            return;
        }
        int res = service.delete(note.getsNote_ID());
        if(res > 0){
            showToast("删除成功");
            //加载数据
            initData();
        }else{
            showToast("删除失败");
        }
    }

    private void goDetail(String objStr){
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

        //初始化列表
        initNode();
    }


    public static SpannableString parseContent(Note note){
        String oragin = note.getsNote_Content();
        SpannableString spannable = new SpannableString(oragin);
        //提取图片地址
        String regEx = "\\[img=[^\\]]+\\]";
        Pattern pat = Pattern.compile(regEx);
        Matcher mat = pat.matcher(oragin);
        while (mat.find()) {
            String r = mat.group();
            String path = r.replace("[img=", "").replace("]", "");
            //读取图片
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            //解析
            int start = mat.start();
            int end = mat.end();

            Drawable drawable = new BitmapDrawable(Application.getContext().getResources(), bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),  drawable.getIntrinsicHeight());

            ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
            spannable.setSpan(imageSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        return spannable;
    }

    public static void main(String[] args){

        String test = "[img=htt://127.0.0.1/t est/test.png]测试测试[img=file://data/0/dat/com.system/test.png]ddeedd";
        String regEx = "\\[img=[^\\]]+\\]";
        Pattern pat = Pattern.compile(regEx);
        Matcher mat = pat.matcher(test);
        while (mat.find()) {
            String r = mat.group();
            System.out.println(r);
            System.out.println(mat.start()+"---"+mat.end());
        }

        System.out.println(test.replaceAll(regEx, ""));

    }
}
