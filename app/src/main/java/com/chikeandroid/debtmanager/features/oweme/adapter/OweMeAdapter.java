package com.chikeandroid.debtmanager.features.oweme.adapter;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.chikeandroid.debtmanager.R;
import com.chikeandroid.debtmanager.data.PersonDebt;
import com.chikeandroid.debtmanager.databinding.ListItemDebtBinding;
import com.chikeandroid.debtmanager.features.oweme.OweMeDiffCallback;

import java.util.List;


/**
 * Created by Chike on 4/25/2017.
 * OweMe RecyclerView Adapter
 */

public class OweMeAdapter extends RecyclerView.Adapter<OweMeAdapter.ViewHolder> {

    private final SparseBooleanArray mSelectedItems;
    private final List<PersonDebt> mPersonDebts;
    private final LayoutInflater mLayoutInflater;
    private RecyclerView mRecyclerView;
    private final Fragment mFragment;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public OweMeAdapter(Fragment fragment, List<PersonDebt> personDebts) {
        mPersonDebts = personDebts;
        mLayoutInflater = LayoutInflater.from(fragment.getActivity());
        mSelectedItems = new SparseBooleanArray();
        mFragment = fragment;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, PersonDebt personDebt, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemLongClickListener {
        void onItemClick(View view, PersonDebt personDebt, int position);
    }

    public void setOnItemLongClickListener(final OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final ListItemDebtBinding binding = ListItemDebtBinding.inflate(mLayoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final PersonDebt personDebt = mPersonDebts.get(position);
        holder.bind(personDebt);
        holder.itemView.setOnLongClickListener(view -> {
            if (mOnItemLongClickListener != null) {
                mOnItemLongClickListener.onItemClick(view, personDebt, holder.getAdapterPosition());
            }
            return true;
        });

        holder.itemView.setOnClickListener(view -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(view, personDebt, holder.getAdapterPosition());
            }
        });

        holder.itemView.setActivated(mSelectedItems.get(position, false));

        if (System.currentTimeMillis() > personDebt.getDebt().getDueDate()) {
            holder.mListItemDebtBinding.tvDueDate.setTextColor(Color.RED);
        }

        Glide.with(mFragment)
                .load(personDebt.getPerson().getImageUri())
                .placeholder(R.drawable.ic_avatar)
                .dontAnimate()
                .into(holder.mListItemDebtBinding.ivUser);
    }

    public void updatePersonDebtListItems(List<PersonDebt> personDebts) {
        final OweMeDiffCallback diffCallback = new OweMeDiffCallback(this.mPersonDebts, personDebts);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.mPersonDebts.clear();
        this.mPersonDebts.addAll(personDebts);
        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public int getItemCount() {
        return mPersonDebts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final ListItemDebtBinding mListItemDebtBinding;

        public ViewHolder(ListItemDebtBinding binding) {
            super(binding.getRoot());
            mListItemDebtBinding = binding;
        }

        public void bind(PersonDebt personDebt) {
            mListItemDebtBinding.setPersonDebt(personDebt);
        }
    }

    /**
     * For multiple selection
     */
    public void toggleSelection(int position, View view) {
        if (mSelectedItems.get(position, false)) {
            mSelectedItems.delete(position);
            view.setSelected(false);
        } else {
            mSelectedItems.put(position, true);
            view.setSelected(true);
        }
    }

    public int getSelectedItemCount() {
        return mSelectedItems.size();
    }

    public SparseBooleanArray getSelectedItems() {
        return mSelectedItems;
    }

    public void clearSelections() {

        for (int i = 0; i < getSelectedItemCount(); i++) {
            int position = getSelectedItems().keyAt(i);

            ViewHolder vh = (ViewHolder) mRecyclerView.findViewHolderForAdapterPosition(position);

            vh.itemView.setSelected(false);
        }

        mSelectedItems.clear();
    }

    public PersonDebt getPersonDebt(int position) {
        return mPersonDebts.get(position);
    }
}
