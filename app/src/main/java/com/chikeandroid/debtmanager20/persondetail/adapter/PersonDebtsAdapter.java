package com.chikeandroid.debtmanager20.persondetail.adapter;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chikeandroid.debtmanager20.data.Debt;
import com.chikeandroid.debtmanager20.databinding.ListItemPersonDebtBinding;
import com.chikeandroid.debtmanager20.persondetail.PersonDebtsDiffCallback;

import java.util.List;

/**
 * Created by Chike on 5/20/2017.
 * Person debts recyclerView adapter
 */

public class PersonDebtsAdapter extends RecyclerView.Adapter<PersonDebtsAdapter.ViewHolder> {

    private final List<Debt> mDebts;
    private final LayoutInflater mLayoutInflater;
    private OnItemLongClickListener mOnItemLongClickListener;
    private OnItemClickListener mOnItemClickListener;

    public PersonDebtsAdapter(Context context, List<Debt> debts) {
        mDebts = debts;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, Debt debt, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemLongClickListener {
        void onItemClick(View view, Debt debt, int position);
    }

    public void setOnItemLongClickListener(final OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final ListItemPersonDebtBinding binding = ListItemPersonDebtBinding.inflate(mLayoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Debt debt = mDebts.get(position);
        holder.bind(debt);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mOnItemLongClickListener != null) {
                    mOnItemLongClickListener.onItemClick(view, debt, holder.getAdapterPosition());
                }
                return true;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, debt, holder.getAdapterPosition());
                }
            }
        });
    }

    public void updatePersonDebtListItems(List<Debt> debts) {
        final PersonDebtsDiffCallback diffCallback = new PersonDebtsDiffCallback(this.mDebts, debts);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.mDebts.clear();
        this.mDebts.addAll(debts);
        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public int getItemCount() {
        return mDebts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final ListItemPersonDebtBinding mListItemPersonDebtBinding;

        public ViewHolder(ListItemPersonDebtBinding binding) {
            super(binding.getRoot());
            mListItemPersonDebtBinding = binding;
        }

        public void bind(Debt debt) {
            mListItemPersonDebtBinding.setDebt(debt);
        }
    }
}
