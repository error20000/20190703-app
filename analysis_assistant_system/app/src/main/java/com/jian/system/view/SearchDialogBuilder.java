package com.jian.system.view;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.jian.system.R;
import com.jian.system.adapter.DialogSearchItemAdapter;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogBuilder;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogMenuItemView;

import java.util.ArrayList;
import java.util.List;

public class SearchDialogBuilder extends QMUIDialogBuilder<SearchDialogBuilder> {

    TextView titleView;
    ImageButton searchBtn;
    View searchView;
    EditText editText;
    ImageButton clearBtn;
    ListView listView;

    String mtitle;
    List<String> mData = new ArrayList<>();
    AdapterView.OnItemClickListener listener;

    public SearchDialogBuilder(Context context) {
        super(context);
    }

    @Override
    public SearchDialogBuilder setTitle(String title){
        this.mtitle = title;
        return this;
    }
    /**
     * 添加菜单项
     *
     */
    public SearchDialogBuilder addItems(CharSequence[] items, AdapterView.OnItemClickListener listener) {
        for (final CharSequence item : items) {
            mData.add(item.toString());
        }
        this.listener = listener;
        return this;
    }
    @Override
    protected void onCreateContent(QMUIDialog dialog, ViewGroup parent, Context context) {
        View mRootView = LayoutInflater.from(context).inflate(R.layout.dialog_select_search, parent, false);
        titleView = mRootView.findViewById(R.id.dialog_title);
        searchBtn = mRootView.findViewById(R.id.dialog_title_search);
        searchView = mRootView.findViewById(R.id.search_view);
        editText = mRootView.findViewById(R.id.search_edit_text);
        searchView = mRootView.findViewById(R.id.search_view);
        clearBtn = mRootView.findViewById(R.id.search_clear);
        listView = mRootView.findViewById(R.id.list_view);

        DialogSearchItemAdapter adapter = new DialogSearchItemAdapter(context, mData, R.layout.dialog_select_item);
        listView.setAdapter(adapter);
        if(listener != null){
            listView.setOnItemClickListener(listener);
        }

        parent.addView(mRootView);
    }
}
