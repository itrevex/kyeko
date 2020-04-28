package com.trevexs.kyeko.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.trevexs.kyeko.BR;


/**
 * This Class was Created by Ssekamatte Isaac on 12/08/2019.
 */
public abstract class RecyclerViewBaseAdapter extends
        RecyclerView.Adapter<RecyclerViewBaseAdapter.MaaloViewHolder> {
    @NonNull
    @Override
    public MaaloViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding =
                DataBindingUtil.inflate(inflater, viewType, parent, false);
        return new MaaloViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MaaloViewHolder holder, int position) {
        final Object obj = getItemForPosition(position);
        holder.bind(obj);
    }

    abstract Object getItemForPosition(int position);

    abstract int getLayoutIdForPosition(int position);

    @Override
    public int getItemViewType(int position) {
        return getLayoutIdForPosition(position);
    }

    static class MaaloViewHolder extends RecyclerView.ViewHolder {
        private final ViewDataBinding binding;

        MaaloViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Object obj) {
            binding.setVariable(BR.obj, obj);
            binding.executePendingBindings();
        }

    }
}
