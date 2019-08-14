package com.jian.system.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.jian.system.R;
import com.jian.system.entity.Dict;
import com.jian.system.entity.Equip;
import com.jian.system.utils.FormatUtils;

import java.util.ArrayList;
import java.util.List;

public class EquipAdapter extends BaseRecyclerAdapter<Equip> {

    List<Dict> equipTypeData = new ArrayList<>();
    List<Dict> equipStatusData = new ArrayList<>();

    public EquipAdapter(Context ctx, List<Equip> data, List<Dict> equipTypeData, List<Dict> equipStatusData) {
        super(ctx, data);
        this.equipTypeData = equipTypeData;
        this.equipStatusData = equipStatusData;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.fragment_equip_list_item;
    }

    @Override
    public int getFootLayoutId(int viewType) {
        return R.layout.fragment_list_foot;
    }

    @Override
    public void bindData(BaseRecyclerViewHolder holder, int position, Equip item) {
        holder.getTextView(R.id.item_title).setText(item.getsEquip_Name());
        holder.getTextView(R.id.item_no).setText(item.getsEquip_NO());

        String statusName = FormatUtils.formatDict(item.getsEquip_Status(), equipStatusData);
        holder.getTextView(R.id.item_state).setText(statusName);

        String color = FormatUtils.formatDictDesc(item.getsEquip_Status(), equipStatusData);
		color = Utils.isNullOrEmpty(color) ? "#3B7FD4" : color;
        holder.getTextView(R.id.item_state).setBackgroundColor(Color.parseColor(color));

        String typeName = FormatUtils.formatDict(item.getsEquip_Type(), equipTypeData);
        holder.getTextView(R.id.item_detail).setText(typeName);
    }
}
