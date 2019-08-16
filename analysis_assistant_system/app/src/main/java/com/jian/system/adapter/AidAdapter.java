package com.jian.system.adapter;

import android.content.Context;
import android.graphics.Color;

import com.jian.system.R;
import com.jian.system.entity.Aid;
import com.jian.system.entity.Dict;
import com.jian.system.entity.Equip;
import com.jian.system.utils.FormatUtils;
import com.jian.system.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class AidAdapter extends BaseRecyclerAdapter<Aid> {


    List<Dict> aidTypeData = new ArrayList<>();

    public AidAdapter(Context ctx, List<Aid> data, List<Dict> aidTypeData) {
        super(ctx, data);
        this.aidTypeData = aidTypeData;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.fragment_aid_list_item;
    }

    @Override
    public int getFootLayoutId(int viewType) {
        return R.layout.fragment_list_foot;
    }

    @Override
    public void bindData(BaseRecyclerViewHolder holder, int position, Aid item) {
        holder.getTextView(R.id.item_title).setText(item.getsAid_Name());
        holder.getTextView(R.id.item_no).setText(item.getsAid_NO());

        String typeName = FormatUtils.formatDict(item.getsAid_Type(), aidTypeData);
        holder.getTextView(R.id.item_detail).setText(typeName);
    }
}
