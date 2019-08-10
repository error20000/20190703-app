package com.jian.system.adapter;

import android.content.Context;

import com.jian.system.R;
import com.jian.system.entity.Equip;

import java.util.List;

public class EquipAdapter extends BaseRecyclerAdapter<Equip> {


    public EquipAdapter(Context ctx, List<Equip> data) {
        super(ctx, data);
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
        holder.getTextView(R.id.item_state).setText(item.getsEquip_Status());
        holder.getTextView(R.id.item_detail).setText(item.getsEquip_Type());
    }
}
