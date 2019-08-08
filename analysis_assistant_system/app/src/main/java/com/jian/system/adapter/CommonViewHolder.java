package com.jian.system.adapter;

import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class CommonViewHolder {

    private SparseArray<View> views;
    private View convertView;

    public CommonViewHolder(View view) {
        convertView = view;
        views = new SparseArray<>();
    }


    public <T extends View> T findViewById(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = convertView.findViewById(viewId);
            views.put(viewId, view);
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

    public CommonViewHolder setText(int viewId, String text) {
        TextView textView = findViewById(viewId);
        textView.setText(text);
        return this;
    }

    public CommonViewHolder setText(int viewId, int textId) {
        TextView textView = findViewById(viewId);
        textView.setText(textId);
        return this;
    }

    public CommonViewHolder setTextColor(int viewId, int colorId) {
        TextView textView = findViewById(viewId);
        textView.setTextColor(colorId);
        return this;
    }

    public CommonViewHolder setTextSize(int viewId, int size) {
        TextView textView = findViewById(viewId);
        textView.setTextSize(size);
        return this;
    }

    public CommonViewHolder setOnClickListener(int viewId, View.OnClickListener clickListener) {
        View view = findViewById(viewId);
        view.setOnClickListener(clickListener);
        return this;
    }

    public CommonViewHolder setImageResource(int viewId, int resId) {
        ImageView imageView = (ImageView) findViewById(viewId);
        imageView.setImageResource(resId);
        return this;
    }

    public CommonViewHolder setBackgroundResource(int viewId, int resId) {
        View view = findViewById(viewId);
        view.setBackgroundResource(resId);
        return this;
    }

    public CommonViewHolder setBackgroundColor(int viewId, int colorId) {
        View view = findViewById(viewId);
        view.setBackgroundColor(colorId);
        return this;
    }

    public CommonViewHolder setBackgroundDrawable(int viewId, Drawable drawable){
        View view = findViewById(viewId);
        view.setBackgroundDrawable(drawable);
        return this;
    }
}
