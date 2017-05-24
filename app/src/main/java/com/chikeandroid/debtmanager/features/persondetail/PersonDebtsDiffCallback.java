package com.chikeandroid.debtmanager.features.persondetail;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.chikeandroid.debtmanager.data.Debt;

import java.util.List;

/**
 * Created by Chike on 4/27/2017.
 * DiffCallback for People RecyclerView
 */
public class PersonDebtsDiffCallback extends DiffUtil.Callback {

    private final List<Debt> mOldDebtList;
    private final List<Debt> mNewDebtList;

    public PersonDebtsDiffCallback(List<Debt> oldDebtList, List<Debt> newDebtList) {
        mOldDebtList = oldDebtList;
        mNewDebtList = newDebtList;
    }

    @Override
    public int getOldListSize() {
        return mOldDebtList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewDebtList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldDebtList.get(oldItemPosition).getId().equals(
                mNewDebtList.get(newItemPosition).getId());
    }


    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final Debt debt = mOldDebtList.get(oldItemPosition);
        final Debt debt1 = mNewDebtList.get(newItemPosition);

        return debt.equals(debt1);
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
