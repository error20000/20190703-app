package com.jian.system.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class CommonAdapter<T> extends BaseAdapter {

    private Context context;
    private List<T> data;
    private int layoutId;

    public CommonAdapter(Context context, List<T> data, int layoutId){
        this.context = context;
        this.data = data;
        this.layoutId = layoutId;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public T getItem(int position) {
        return data == null ? null :data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommonViewHolder viewHolder = null;
        if(convertView == null) {
            convertView =  LayoutInflater.from(context).inflate(layoutId , null);
            viewHolder = new CommonViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CommonViewHolder) convertView.getTag();
        }
        T item = getItem(position);
        handleItem(convertView, position, item, viewHolder);
        return convertView;
    }

    public abstract void handleItem(View convertView , int position , T item , CommonViewHolder holder );
}
