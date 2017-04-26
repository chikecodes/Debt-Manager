package com.chikeandroid.debtmanager20.oweme;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.chikeandroid.debtmanager20.data.PersonDebt;

import java.util.List;

/**
 * Created by Chike on 4/27/2017.
 */

public class OweMeDiffCallback extends DiffUtil.Callback {

    private final List<PersonDebt> mOldOweMeDebtList;
    private final List<PersonDebt> mNewOweMeDebtList;

    public OweMeDiffCallback(List<PersonDebt> oldOweMeDebtList, List<PersonDebt> newOweMeDebtList) {
        mOldOweMeDebtList = oldOweMeDebtList;
        mNewOweMeDebtList = newOweMeDebtList;
    }

    @Override
    public int getOldListSize() {
        return mOldOweMeDebtList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewOweMeDebtList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldOweMeDebtList.get(oldItemPosition).getDebt().getId().equals(
                mNewOweMeDebtList.get(newItemPosition).getDebt().getId());
    }


    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final PersonDebt oldPersonDebt = mOldOweMeDebtList.get(oldItemPosition);
        final PersonDebt newPersonDebt = mNewOweMeDebtList.get(newItemPosition);

        return oldPersonDebt.getDebt().getAmount() == newPersonDebt.getDebt().getAmount();
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
