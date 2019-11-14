
package com.jian.system.fragment.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baoyachi.stepview.VerticalStepView;
import com.jian.system.R;
import com.jian.system.config.Constant;
import com.jian.system.config.UrlConfig;
import com.jian.system.entity.Dict;
import com.jian.system.entity.Equip;
import com.jian.system.entity.EquipLog;
import com.jian.system.entity.Nfc;
import com.jian.system.entity.Note;
import com.jian.system.entity.Store;
import com.jian.system.entity.StoreType;
import com.jian.system.utils.DataUtils;
import com.jian.system.utils.FormatUtils;
import com.jian.system.utils.HttpUtils;
import com.jian.system.utils.ThreadUtils;
import com.jian.system.utils.Utils;
import com.jian.system.view.SearchDialogBuilder;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.util.QMUIViewHelper;
import com.qmuiteam.qmui.widget.QMUIEmptyView;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListSectionHeaderFooterView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import chihane.jdaddressselector.BottomDialog;
import chihane.jdaddressselector.DataProvider;
import chihane.jdaddressselector.ISelectAble;
import chihane.jdaddressselector.SelectedListener;
import chihane.jdaddressselector.Selector;

public class NoteDetailFragment extends QMUIFragment {

    private final static String TAG = NoteDetailFragment.class.getSimpleName();
    private String title = "查看";
    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;
    private QMUITipDialog tipDialog;

    private Note note;

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;
    @BindView(R.id.text_content)
    TextView mTextView1;
    @BindView(R.id.text_date)
    TextView mTextView2;

    @Override
    protected View onCreateView() {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_note_view, null);
        ButterKnife.bind(this, rootView);

        Bundle bundle = this.getArguments();
        String obj =  bundle.getString("obj");
        if(!Utils.isNullOrEmpty(obj)){
            note = JSONObject.parseObject(obj, Note.class);
        }
        initTopBar();
        //initData();

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
    


}
