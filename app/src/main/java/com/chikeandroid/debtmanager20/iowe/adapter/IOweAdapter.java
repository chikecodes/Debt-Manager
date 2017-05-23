package com.chikeandroid.debtmanager20.iowe.adapter;

import android.support.v4.app.Fragment;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.chikeandroid.debtmanager20.R;
import com.chikeandroid.debtmanager20.data.PersonDebt;
import com.chikeandroid.debtmanager20.databinding.ListItemDebtBinding;
import com.chikeandroid.debtmanager20.iowe.IOweDiffCallback;

import java.util.List;

/**
 * Created by Chike on 4/25/2017.
 * IOwe RecyclerView Adapter
 */

public class IOweAdapter extends RecyclerView.Adapter<IOweAdapter.ViewHolder> {

    private final SparseBooleanArray mSelectedItems;
    private final List<PersonDebt> mPersonDebts;
    private final LayoutInflater mLayoutInflater;
    private RecyclerView mRecyclerView;
    private final Fragment mFragment;
    private OnItemLongClickListener mOnItemLongClickListener;
    private OnItemClickListener mOnItemClickListener;

    public IOweAdapter(Fragment fragment, List<PersonDebt> personDebts) {
        mPersonDebts = personDebts;
        mFragment = fragment;
        mLayoutInflater = LayoutInflater.from(fragment.getActivity());
        mSelectedItems = new SparseBooleanArray();
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final PersonDebt personDebt = mPersonDebts.get(position);
        holder.bind(personDebt);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mOnItemLongClickListener != null) {
                    mOnItemLongClickListener.onItemClick(view, personDebt, holder.getAdapterPosition());
                }
                return true;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, personDebt, holder.getAdapterPosition());
                }
            }
        });

        holder.itemView.setActivated(mSelectedItems.get(position, false));
    }

    public void updatePersonDebtListItems(List<PersonDebt> personDebts) {
        final IOweDiffCallback diffCallback = new IOweDiffCallback(this.mPersonDebts, personDebts);
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

            Glide.with(mFragment)
                    .load(personDebt.getPerson().getImageUri())
                    .placeholder(R.drawable.ic_avatar)
                    .dontAnimate()
                    .into(mListItemDebtBinding.ivUser);
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
