package com.blogspot.androidgaidamak.tvojakrajina;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 *
 * Neds retrolambda to work. Or java 1.8 version.
 * @param <T> POJO to be binded to layout. Must be the same as in layout.
 */
public class BindingRecyclerViewAdapter<T> extends RecyclerView.Adapter<BindingRecyclerViewAdapter.BindingViewHolder<T>> {
    private final int mVariableId;
    @LayoutRes
    private final int mItemLayout;
    private final OnItemClickListener<T> mClickListener;
    private List<T> mItems;

    /**
     *
     * @param itemLayout layout id
     * @param variableId variable in layout, ie BR.pojo;
     * @param clickListener item click listener
     */
    public BindingRecyclerViewAdapter(@LayoutRes int itemLayout, int variableId, OnItemClickListener<T> clickListener) {
        mItemLayout = itemLayout;
        mVariableId = variableId;
        mClickListener = clickListener;
    }

    public void setItems(List<T> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    @Override
    public BindingViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());
        ViewDataBinding itemBinding = DataBindingUtil.inflate(layoutInflater, mItemLayout, parent, false);
        return new BindingViewHolder<T>(itemBinding, mVariableId, mClickListener);
    }

    @Override
    public void onBindViewHolder(BindingViewHolder<T> holder, int position) {
        holder.bind(getItem(position));
    }

    public T getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public int getItemCount() {
        if (mItems == null) {
            return 0;
        } else {
            return mItems.size();
        }
    }

    public static class BindingViewHolder<S> extends RecyclerView.ViewHolder {
        private final ViewDataBinding binding;
        private final int mVariableId;
        private final OnItemClickListener<S> mListener;
        private final View mView;

        BindingViewHolder(ViewDataBinding binding, int variableId, OnItemClickListener<S> listener) {
            super(binding.getRoot());
            this.binding = binding;
            mVariableId = variableId;
            mListener = listener;
            mView = binding.getRoot();
        }

        void bind(S obj) {
            binding.setVariable(mVariableId, obj);
            binding.executePendingBindings();
            if (mListener != null) {
                binding.getRoot().setOnClickListener(v -> mListener.onItemClick(obj));
            }
        }

        public View getView() {
            return mView;
        }
    }

    public interface OnItemClickListener<S> {
        void onItemClick(S item);
    }
}
