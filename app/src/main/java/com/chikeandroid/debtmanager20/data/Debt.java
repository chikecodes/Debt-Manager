package com.chikeandroid.debtmanager20.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.UUID;

/**
 * Created by Chike on 3/14/2017.
 * Immutable model class for a Debt
 */
public final class Debt {

    @NonNull
    private final String mId;

    @Nullable
    private final double mAmount;

    @Nullable
    private final String mItemName;

    @Nullable
    private final String mNote;

    @NonNull
    private final String mName;

    @NonNull
    private final long mCreatedDate;

    @Nullable
    private final long mDueDate;

    @NonNull
    private final int mDebtType;

    @Nullable
    private final String mPhoneNumber;

    @Nullable
    private final String mEmail;

    @NonNull
    private final int mStatus;

    public static class Builder {

        // Required parameters
        private final String mId;
        private final String mName;
        private final long mCreatedDate;
        private final int mDebtType;
        private final int mStatus;

        // Optional parameters
        private double mAmount = 0;
        private String mItemName = "";
        private String mNote = "";
        private long mDueDate = 0;
        private String mPhoneNumber = "";
        private String mEmail = "";

        public Builder(String name, long createdDate,int debtType, int status) {
            this.mId = UUID.randomUUID().toString();
            this.mCreatedDate = createdDate;
            this.mName = name;
            this.mDebtType = debtType;
            this.mStatus = status;
        }

        public Builder amount(double amount) {
            mAmount = amount;
            return this;
        }

        public Builder itemName(String name) {
            mItemName = name;
            return this;
        }

        public Builder note(String note) {
            mNote = note;
            return this;
        }

        public Builder dueDate(long dueDate) {
            mDueDate = dueDate;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            mPhoneNumber = phoneNumber;
            return this;
        }

        public Builder email(String email) {
            mEmail = email;
            return this;
        }

        public Debt build() {
            return new Debt(this);
        }

    }

    private Debt(Builder builder) {

        mId = builder.mId;
        mName = builder.mName;
        mCreatedDate = builder.mCreatedDate;
        mDebtType = builder.mDebtType;
        mStatus = builder.mStatus;
        mAmount = builder.mAmount;
        mItemName = builder.mItemName;
        mNote = builder.mNote;
        mDueDate = builder.mDueDate;
        mPhoneNumber = builder.mPhoneNumber;
        mEmail = builder.mEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Debt debt = (Debt) o;

        if (Double.compare(debt.mAmount, mAmount) != 0) return false;
        if (mCreatedDate != debt.mCreatedDate) return false;
        if (mDueDate != debt.mDueDate) return false;
        if (mDebtType != debt.mDebtType) return false;
        if (mStatus != debt.mStatus) return false;
        if (!mId.equals(debt.mId)) return false;
        if (!mItemName.equals(debt.mItemName)) return false;
        if (!mNote.equals(debt.mNote)) return false;
        if (!mName.equals(debt.mName)) return false;
        if (!mPhoneNumber.equals(debt.mPhoneNumber)) return false;
        return mEmail.equals(debt.mEmail);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = mId.hashCode();
        temp = Double.doubleToLongBits(mAmount);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + mItemName.hashCode();
        result = 31 * result + mNote.hashCode();
        result = 31 * result + mName.hashCode();
        result = 31 * result + (int) (mCreatedDate ^ (mCreatedDate >>> 32));
        result = 31 * result + (int) (mDueDate ^ (mDueDate >>> 32));
        result = 31 * result + mDebtType;
        result = 31 * result + mPhoneNumber.hashCode();
        result = 31 * result + mEmail.hashCode();
        result = 31 * result + mStatus;
        return result;
    }
}
