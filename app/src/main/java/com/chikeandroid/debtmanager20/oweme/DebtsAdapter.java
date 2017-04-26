package com.chikeandroid.debtmanager20.oweme;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chikeandroid.debtmanager20.data.PersonDebt;
import com.chikeandroid.debtmanager20.databinding.ListItemDebtBinding;
import com.chikeandroid.debtmanager20.debtdetail.DebtDetailActivity;
import com.chikeandroid.debtmanager20.debtdetail.DebtDetailFragment;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Chike on 4/25/2017.
 */

public class DebtsAdapter extends RecyclerView.Adapter<DebtsAdapter.ViewHolder> {

    private List<PersonDebt> mPersonDebts;
    private Context mContext;
    private final LayoutInflater mLayoutInflater;

    public DebtsAdapter(Context context, List<PersonDebt> personDebts) {
        mPersonDebts = personDebts;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final ListItemDebtBinding binding = ListItemDebtBinding.inflate(mLayoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PersonDebt personDebt = mPersonDebts.get(position);
        if(position == mPersonDebts.size() / 2) {
            holder.setIsInTheMiddle(true);
        }else {
            holder.setIsInTheMiddle(false);
        }
        holder.bind(personDebt);
    }

    public void replaceData(List<PersonDebt> personDebts) {
        setList(personDebts);
        notifyDataSetChanged();
    }

    private void setList(List<PersonDebt> personDebts) {
        mPersonDebts = checkNotNull(personDebts);
    }

    @Override
    public int getItemCount() {
        return mPersonDebts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ListItemDebtBinding mListItemDebtBinding;
        String mDebtId;

        private boolean mIsInTheMiddle = false;

        boolean getIsInTheMiddle() {
            return mIsInTheMiddle;
        }

        void setIsInTheMiddle(boolean isInTheMiddle) {
            mIsInTheMiddle = isInTheMiddle;
        }

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
