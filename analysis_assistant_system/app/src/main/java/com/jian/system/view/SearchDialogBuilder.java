package com.jian.system.view;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jian.system.R;
import com.jian.system.adapter.DialogSearchItemAdapter;
import com.jian.system.utils.Utils;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class SearchDialogBuilder extends QMUIDialogBuilder<SearchDialogBuilder> {

    TextView titleView;
    ImageButton searchBtn;
    View searchView;
    EditText editText;
    ImageButton clearBtn;
    ListView listView;

    Context context;
    int contentId = R.layout.dialog_select_item;
    String mtitle;
    String mhint;
    List<ItemEntity> mData = new ArrayList<>();
    OnSelectedListiner listener;
    DialogSearchItemAdapter adapter;
    QMUIDialog dialog;
    View mRootView;

    public SearchDialogBuilder(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public SearchDialogBuilder setTitle(String title){
        this.mtitle = title;
        return this;
    }

    public SearchDialogBuilder setHint(String hint){
        this.mhint = hint;
        return this;
    }

    public SearchDialogBuilder addItems(CharSequence[] items, OnSelectedListiner listener) {
        for (int i = 0; i < items.length; i++) {
            mData.add(new ItemEntity(items[i].toString(), i));
        }
        this.listener = listener;
        return this;
    }

    public SearchDialogBuilder addItems(List<ItemEntity> items, OnSelectedListiner listener) {
        if(items != null){
            mData = items;
        }
        this.listener = listener;
        return this;
    }

    @Override
    protected void onCreateContent(QMUIDialog dialog, ViewGroup parent, Context context) {
        mRootView = LayoutInflater.from(context).inflate(R.layout.dialog_select_search, parent, false);
        titleView = mRootView.findViewById(R.id.dialog_title);
        searchBtn = mRootView.findViewById(R.id.dialog_title_search);
        searchView = mRootView.findViewById(R.id.search_view);
        editText = mRootView.findViewById(R.id.search_edit_text);
        clearBtn = mRootView.findViewById(R.id.search_clear);
        listView = mRootView.findViewById(R.id.list_view);
        this.dialog = dialog;

        if(!Utils.isNullOrEmpty(mtitle)){
            titleView.setText(mtitle);
        }

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(searchView.getVisibility() == View.GONE){
                    searchView.setVisibility(View.VISIBLE);
                }else{
                    searchView.setVisibility(View.GONE);
                }
            }
        });

        if(!Utils.isNullOrEmpty(mhint)){
            editText.setHint(mhint);
        }
        editText.addTextChangedListener(new EditChangedListener());

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("");
                updateList("");
                clearBtn.setVisibility(View.GONE);
            }
        });

        updateList("");

        parent.addView(mRootView);
    }

    @Override
    protected void onAfter(QMUIDialog dialog, LinearLayout parent, Context context) {
        super.onAfter(dialog, parent, context);

        /*mRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                View mDecor = mDialog.getWindow().getDecorView();
                Rect r = new Rect();
                mDecor.getWindowVisibleDisplayFrame(r);

                int mScreenHeight = QMUIDisplayHelper.getScreenHeight(context);
                LinearLayout.LayoutParams slp = (LinearLayout.LayoutParams) mRootView.getLayoutParams();
                Toast.makeText(context,mScreenHeight+"===="+r.bottom+"---->"+slp.height,Toast.LENGTH_SHORT).show();
                slp.height = Math.min(mScreenHeight, slp.height);
                mRootView.setLayoutParams(slp);
                mRootView.requestLayout();
            }
        });*/

    }

    private void updateList(String stext){
        List<ItemEntity> data = searchItem(stext);
        adapter = new DialogSearchItemAdapter(context, data, contentId);
        listView.setAdapter(adapter);
        if(listener != null){
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    listener.onSelected(dialog, adapter.getItem(i));
                }
            });
        }
    }

    public List<ItemEntity> searchItem(String name) {
        List<ItemEntity> mSearchList = new ArrayList<>();
        for (int i = 0; i < mData.size(); i++) {
            int index = mData.get(i).getName().indexOf(name);
            // 存在匹配的数据
            if (index != -1) {
                mSearchList.add(new ItemEntity(mData.get(i).getName(), i, mData.get(i).getDetail()));
            }
        }
        return mSearchList;
    }

    private class EditChangedListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (!"".equals(charSequence.toString())) {
                clearBtn.setVisibility(View.VISIBLE);
            } else {
                clearBtn.setVisibility(View.GONE);
            }
            //更新数据
            updateList(charSequence + "");
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    public static abstract class OnSelectedListiner{
        public abstract void onSelected(DialogInterface dialog, ItemEntity item);
    }

    public static class ItemEntity{
        private String name;
        private String detail;
        private int index;

        public ItemEntity() {

        }

        public ItemEntity(String name, int index) {
            this.name = name;
            this.index = index;
        }
        public ItemEntity(String name, int index, String detail) {
            this.name = name;
            this.index = index;
            this.detail = detail;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }
    }
}
