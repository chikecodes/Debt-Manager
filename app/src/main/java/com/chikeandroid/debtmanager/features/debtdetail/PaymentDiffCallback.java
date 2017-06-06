package com.chikeandroid.debtmanager.features.debtdetail;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.chikeandroid.debtmanager.data.Payment;

import java.util.List;

/**
 * Created by Chike on 5/25/2017.
 */

public class PaymentDiffCallback extends DiffUtil.Callback {

    private final List<Payment> mOldDebtList;
    private final List<Payment> mNewDebtList;

    public PaymentDiffCallback(List<Payment> oldPaymentList, List<Payment> newPaymentList) {
        mOldDebtList = oldPaymentList;
        mNewDebtList = newPaymentList;
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
        final Payment payment = mOldDebtList.get(oldItemPosition);
        final Payment payment1 = mNewDebtList.get(newItemPosition);

        return payment.equals(payment1);
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
