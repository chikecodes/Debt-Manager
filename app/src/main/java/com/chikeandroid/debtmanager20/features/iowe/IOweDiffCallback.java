package com.chikeandroid.debtmanager20.features.iowe;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.chikeandroid.debtmanager20.data.PersonDebt;

import java.util.List;

/**
 * Created by Chike on 5/2/2017.
 */

public class IOweDiffCallback extends DiffUtil.Callback {

    private final List<PersonDebt> mOldIOweList;
    private final List<PersonDebt> mNewIOweList;

    public IOweDiffCallback(List<PersonDebt> oldIOweList, List<PersonDebt> newIOweList) {
        mOldIOweList = oldIOweList;
        mNewIOweList = newIOweList;
    }

    @Override
    public int getOldListSize() {
        return mOldIOweList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewIOweList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldIOweList.get(oldItemPosition).getDebt().getId().equals(
                mNewIOweList.get(newItemPosition).getDebt().getId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final PersonDebt oldPersonDebt = mOldIOweList.get(oldItemPosition);
        final PersonDebt newPersonDebt = mNewIOweList.get(newItemPosition);

        return oldPersonDebt.equals(newPersonDebt);
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
