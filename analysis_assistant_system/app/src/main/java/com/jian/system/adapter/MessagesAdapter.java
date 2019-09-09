package com.jian.system.adapter;

import android.content.Context;
import android.graphics.Color;

import com.jian.system.R;
import com.jian.system.entity.Aid;
import com.jian.system.entity.Dict;
import com.jian.system.entity.Messages;
import com.jian.system.utils.FormatUtils;
import com.jian.system.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class MessagesAdapter extends BaseRecyclerAdapter<Messages> {


    List<Dict> msgTypeData = new ArrayList<>();
    List<Dict> msgStatusData = new ArrayList<>();
    List<Dict> msgLabelData = new ArrayList<>();

    public MessagesAdapter(Context ctx, List<Messages> data,
                           List<Dict> msgTypeData, List<Dict> msgStatusData, List<Dict> msgLabelData) {
        super(ctx, data);
        this.msgTypeData = msgTypeData;
        this.msgStatusData = msgStatusData;
        this.msgLabelData = msgLabelData;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.layout_msg_list_item;
    }

    @Override
    public int getFootLayoutId(int viewType) {
        return R.layout.fragment_list_foot;
    }

    @Override
    public void bindData(BaseRecyclerViewHolder holder, int position, Messages item) {
        holder.getTextView(R.id.item_title).setText(item.getsMsg_Title());

        String typeName = FormatUtils.formatDict(item.getsMsg_Type(), msgTypeData);
        holder.getTextView(R.id.item_type).setText(typeName);

        String statusName = FormatUtils.formatDict(item.getsMsg_Status(), msgStatusData);
        holder.getTextView(R.id.item_status).setText(statusName);
        String color = FormatUtils.formatDictCustom(item.getsMsg_Status(), msgStatusData, "Color");
        color = Utils.isNullOrEmpty(color) ? "#3B7FD4" : color;
        holder.getTextView(R.id.item_status).setBackgroundColor(Color.parseColor(color));

        holder.getTextView(R.id.item_detail).setText(item.getsMsg_Describe());
        holder.getTextView(R.id.item_date).setText(FormatUtils.formatDate("yyyy-MM-dd HH:mm:ss", item.getdMsg_CreateDate()));
    }
}
