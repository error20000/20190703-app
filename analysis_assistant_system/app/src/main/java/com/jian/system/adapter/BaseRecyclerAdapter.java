
package com.jian.system.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jian.system.R;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<T> mData;
    private final Context mContext;
    private LayoutInflater mInflater;
    private OnItemClickListener mClickListener;
    private OnItemLongClickListener mLongClickListener;
    private final int TYPE_ITEM = 1; // 普通布局
    private final int TYPE_FOOTER = 2;// 脚布局
    private int loadState = 2;// 当前加载状态，默认为加载完成
    public final int LOADING = 1; // 正在加载
    public final int LOADING_COMPLETE = 2;// 加载完成
    public final int LOADING_END = 3;// 加载到底

    public BaseRecyclerAdapter(Context ctx, List<T> list) {
        mData = (list != null) ? list : new ArrayList<T>();
        mContext = ctx;
        mInflater = LayoutInflater.from(ctx);
    }

    public void setData(List<T> list) {
        mData = (list != null) ? list : new ArrayList<T>();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType  == TYPE_FOOTER){
            BaseFootViewHolder holder = new BaseFootViewHolder(
                    mInflater.inflate(getFootLayoutId(viewType), parent, false));
            return holder;
        }
        BaseRecyclerViewHolder holder = new BaseRecyclerViewHolder(mContext,
                mInflater.inflate(getItemLayoutId(viewType), parent, false));
        if (mClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(holder.itemView, holder.getLayoutPosition());
                }
            });
        }
        if (mLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mLongClickListener.onItemLongClick(holder.itemView, holder.getLayoutPosition());
                    return true;
                }
            });
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BaseRecyclerViewHolder) {
            bindData((BaseRecyclerViewHolder)holder, position, mData.get(position));
        } else if (holder instanceof BaseFootViewHolder) {
            BaseFootViewHolder footHolder = (BaseFootViewHolder) holder;
            switch (loadState) {
                case LOADING: // 正在加载
                    footHolder.pbLoading.setVisibility(View.VISIBLE);
                    footHolder.tvLoading.setVisibility(View.VISIBLE);
                    footHolder.llEnd.setVisibility(View.GONE);
                    break;

                case LOADING_COMPLETE: // 加载完成
                    footHolder.pbLoading.setVisibility(View.INVISIBLE);
                    footHolder.tvLoading.setVisibility(View.INVISIBLE);
                    footHolder.llEnd.setVisibility(View.GONE);
                    break;

                case LOADING_END: // 加载到底
                    footHolder.pbLoading.setVisibility(View.GONE);
                    footHolder.tvLoading.setVisibility(View.GONE);
                    footHolder.llEnd.setVisibility(View.VISIBLE);
                    break;

                default:
                    break;
            }
        }
    }

    public T getItem(int pos){
        return mData.get(pos);
    }

    @Override
    public int getItemCount() {
        return mData.size()+1;
    }

    public void add(int pos, T item) {
        mData.add(pos, item);
        notifyItemInserted(pos);
    }

    public void delete(int pos) {
        mData.remove(pos);
        notifyItemRemoved(pos);
    }

    public void setLoadState(int loadState) {
        this.loadState = loadState;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mLongClickListener = listener;
    }

    @SuppressWarnings("SameReturnValue")
    abstract public int getItemLayoutId(int viewType);
    abstract public int getFootLayoutId(int viewType);

    abstract public void bindData(BaseRecyclerViewHolder holder, int position, T item);

    public interface OnItemClickListener {
        void onItemClick(View itemView, int pos);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View itemView, int pos);
    }
}
