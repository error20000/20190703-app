package com.jian.system.adapter;

import android.content.Context;

import com.jian.system.R;
import com.jian.system.entity.Aid;
import com.jian.system.entity.AidEquip;
import com.jian.system.entity.Dict;
import com.jian.system.utils.FormatUtils;

import java.util.ArrayList;
import java.util.List;

public class AidEquipAdapter extends BaseRecyclerAdapter<AidEquip> {


    public AidEquipAdapter(Context ctx, List<AidEquip> data) {
        super(ctx, data, false);
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.fragment_aid_equip_view_item;
    }

    @Override
    public int getFootLayoutId(int viewType) {
        return R.layout.fragment_list_foot;
    }

    @Override
    public void bindData(BaseRecyclerViewHolder holder, int position, AidEquip item) {
        holder.getTextView(R.id.item_title).setText(item.getsAidEquip_EquipName());
        holder.getTextView(R.id.item_no).setText(item.getsAidEquip_EquipNO());
        holder.getTextView(R.id.item_detail).setText(item.getsAidEquip_EquipTypeName());
        holder.getTextView(R.id.item_date).setText(FormatUtils.formatDate("yyyy-MM-dd HH:mm:ss", item.getdAidEquip_CreateDate()));
    }
}
