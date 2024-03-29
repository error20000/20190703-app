
package com.jian.system.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jian.system.R;
import com.qmuiteam.qmui.util.QMUIResHelper;

import q.rorbin.badgeview.QBadgeView;

public class BaseRecyclerViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;

    public BaseRecyclerViewHolder(Context context, View itemView) {
        super(itemView);
        mViews = new SparseArray<>();
        /*new QBadgeView(context).bindTarget(itemView.findViewById(R.id.item_badge))
                .setBadgeBackgroundColor(Color.parseColor("#ff00ff"))
                .setBadgeTextColor(QMUIResHelper.getAttrColor(context, R.attr.qmui_config_color_gray_5))
                .setBadgeGravity(Gravity.CENTER)
                .setBadgeTextSize(14, true)
                .setBadgeText("置背景色");*/
    }

    @SuppressWarnings("unchecked")
    private <T extends View> T findViewById(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getView(int viewId) {
        return findViewById(viewId);
    }

    public TextView getTextView(int viewId) {
        return (TextView) getView(viewId);
    }

    public Button getButton(int viewId) {
        return (Button) getView(viewId);
    }

    public ImageView getImageView(int viewId) {
        return (ImageView) getView(viewId);
    }

    public ImageButton getImageButton(int viewId) {
        return (ImageButton) getView(viewId);
    }

    public EditText getEditText(int viewId) {
        return (EditText) getView(viewId);
    }

    public BaseRecyclerViewHolder setText(int viewId, String value) {
        TextView view = findViewById(viewId);
        view.setText(value);
        return this;
    }

    public BaseRecyclerViewHolder setBackground(int viewId, int resId) {
        View view = findViewById(viewId);
        view.setBackgroundResource(resId);
        return this;
    }

    public BaseRecyclerViewHolder setClickListener(int viewId, View.OnClickListener listener) {
        View view = findViewById(viewId);
        view.setOnClickListener(listener);
        return this;
    }
}
