package com.chikeandroid.debtmanager20.oweme.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chikeandroid.debtmanager20.data.PersonDebt;
import com.chikeandroid.debtmanager20.databinding.ListItemDebtBinding;
import com.chikeandroid.debtmanager20.oweme.OweMeDiffCallback;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Chike on 4/25/2017.
 */

public class OweMeAdapter extends RecyclerView.Adapter<OweMeAdapter.ViewHolder> {

    private SparseBooleanArray mSelectedItems;
    private HashMap<Integer, Object> mSelectedPersonDebts;
    private final List<PersonDebt> mPersonDebts;
    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private Fragment mFragment;

    RecyclerView mRecyclerView;

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        mRecyclerView = recyclerView;
    }

    public OweMeAdapter(Context context, List<PersonDebt> personDebts, Fragment fragment) {
        mPersonDebts = personDebts;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mFragment = fragment;
        mSelectedItems = new SparseBooleanArray();
        mSelectedPersonDebts = new HashMap<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final ListItemDebtBinding binding = ListItemDebtBinding.inflate(mLayoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final PersonDebt personDebt = mPersonDebts.get(position);
        holder.bind(personDebt);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(mOnItemLongClickListener != null) {
                    mOnItemLongClickListener.onItemClick(view, personDebt, position);
                }
                return true;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, personDebt, position);
                }
            }
        });

        holder.itemView.setActivated(mSelectedItems.get(position, false));
    }

    public void updatePersonDebtListItems(List<PersonDebt> personDebts) {
        final OweMeDiffCallback diffCallback = new OweMeDiffCallback(this.mPersonDebts, personDebts);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.mPersonDebts.clear();
        this.mPersonDebts.addAll(personDebts);
        diffResult.dispatchUpdatesTo(this);
    }

    // for item click listener
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, PersonDebt personDebt, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    // for item long click listener
    private OnItemLongClickListener mOnItemLongClickListener;

    public interface OnItemLongClickListener {
        void onItemClick(View view, PersonDebt personDebt, int position);
    }

    public void setOnItemLongClickListener(final OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    @Override
    public int getItemCount() {
        return mPersonDebts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final ListItemDebtBinding mListItemDebtBinding;
        String mDebtId;

        public ViewHolder(ListItemDebtBinding binding) {
            super(binding.getRoot());
            mListItemDebtBinding = binding;
        }

        public void bind(PersonDebt personDebt) {
            mDebtId = personDebt.getDebt().getId();
            mListItemDebtBinding.setPersonDebt(personDebt);
        }
    }

    /**
     * For multiple selection
     */
    public void toggleSelection(int position, View view) {
        if(mSelectedItems.get(position, false)) {
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

        for(int i = 0; i < getSelectedItemCount(); i++) {
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
