package com.jian.system.adapter;

import android.content.Context;
import android.view.View;

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
    public void bindData(RecyclerViewHolder holder, int position, Equip item) {
        holder.getTextView(R.id.item_name).setText(item.getsEquip_Name());

    }
}
