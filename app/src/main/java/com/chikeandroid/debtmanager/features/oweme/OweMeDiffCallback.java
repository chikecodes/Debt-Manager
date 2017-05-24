package com.chikeandroid.debtmanager.features.oweme;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.chikeandroid.debtmanager.data.PersonDebt;

import java.util.List;

/**
 * Created by Chike on 4/27/2017.
 * DiifCallback for OweMe RecyclerView
 */

public class OweMeDiffCallback extends DiffUtil.Callback {

    private final List<PersonDebt> mOldOweMeList;
    private final List<PersonDebt> mNewOweMeList;

    public OweMeDiffCallback(List<PersonDebt> oldOweMeList, List<PersonDebt> newOweMeList) {
        mOldOweMeList = oldOweMeList;
        mNewOweMeList = newOweMeList;
    }

    @Override
    public int getOldListSize() {
        return mOldOweMeList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewOweMeList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldOweMeList.get(oldItemPosition).getDebt().getId().equals(
                mNewOweMeList.get(newItemPosition).getDebt().getId());
    }


    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final PersonDebt oldPersonDebt = mOldOweMeList.get(oldItemPosition);
        final PersonDebt newPersonDebt = mNewOweMeList.get(newItemPosition);

        return oldPersonDebt.equals(newPersonDebt);
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
