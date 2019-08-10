package com.jian.system.adapter;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jian.system.R;

public class BaseFootViewHolder extends  RecyclerView.ViewHolder {

    ProgressBar pbLoading;
    TextView tvLoading;
    LinearLayout llEnd;

    BaseFootViewHolder(View itemView) {
        super(itemView);
        pbLoading = (ProgressBar) itemView.findViewById(R.id.pb_loading);
        tvLoading = (TextView) itemView.findViewById(R.id.tv_loading);
        llEnd = (LinearLayout) itemView.findViewById(R.id.ll_end);
    }
}
