package com.chikeandroid.debtmanager20.oweme;

import android.content.Context;
import android.content.Intent;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chikeandroid.debtmanager20.data.PersonDebt;
import com.chikeandroid.debtmanager20.databinding.ListItemDebtBinding;
import com.chikeandroid.debtmanager20.debtdetail.DebtDetailActivity;
import com.chikeandroid.debtmanager20.debtdetail.DebtDetailFragment;

import java.util.List;

/**
 * Created by Chike on 4/25/2017.
 */

public class DebtsAdapter extends RecyclerView.Adapter<DebtsAdapter.ViewHolder> {

    private final List<PersonDebt> mPersonDebts;
    private final Context mContext;
    private final LayoutInflater mLayoutInflater;

    public DebtsAdapter(Context context, List<PersonDebt> personDebts) {
        mPersonDebts = personDebts;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final ListItemDebtBinding binding = ListItemDebtBinding.inflate(mLayoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PersonDebt personDebt = mPersonDebts.get(position);
        holder.bind(personDebt);
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ListItemDebtBinding mListItemDebtBinding;
        String mDebtId;

        public ViewHolder(ListItemDebtBinding binding) {
            super(binding.getRoot());
            mListItemDebtBinding = binding;
            itemView.setOnClickListener(this);
        }

        public void bind(PersonDebt personDebt) {
            mDebtId = personDebt.getDebt().getId();
            mListItemDebtBinding.setPersonDebt(personDebt);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, DebtDetailActivity.class);
            intent.putExtra(DebtDetailFragment.EXTRA_DEBT_ID, mDebtId);
            mContext.startActivity(intent);
        }
    }
}
