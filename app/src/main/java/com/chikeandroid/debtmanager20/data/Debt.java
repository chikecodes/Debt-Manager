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
    private final String mNote;

    @NonNull
    private final String mPersonId;

    @NonNull
    private final long mCreatedDate;

    @Nullable
    private final long mDueDate;

    @NonNull
    private final int mDebtType;

    @NonNull
    private final int mStatus;

    @NonNull
    public String getId() {
        return mId;
    }

    @Nullable
    public double getAmount() {
        return mAmount;
    }

    @Nullable
    public String getNote() {
        return mNote;
    }

    @NonNull
    public String getPersonId() {
        return mPersonId;
    }

    @NonNull
    public long getCreatedDate() {
        return mCreatedDate;
    }

    @Nullable
    public long getDueDate() {
        return mDueDate;
    }

    @NonNull
    public int getDebtType() {
        return mDebtType;
    }

    @NonNull
    public int getStatus() {
        return mStatus;
    }

    public static class Builder {

        // Required parameters
        private final String mId;
        private final String mPersonId;
        private final long mCreatedDate;
        private final int mDebtType;
        private final int mStatus;

        // Optional parameters
        private double mAmount = 0;
        private String mNote = "";
        private long mDueDate = 0;

        public Builder(String personId, long createdDate,int debtType, int status) {
            this.mId = UUID.randomUUID().toString();
            this.mCreatedDate = createdDate;
            this.mPersonId = personId;
            this.mDebtType = debtType;
            this.mStatus = status;
        }

        public Builder amount(double amount) {
            mAmount = amount;
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

        public Debt build() {
            return new Debt(this);
        }

    }

    private Debt(Builder builder) {

        mId = builder.mId;
        mPersonId = builder.mPersonId;
        mCreatedDate = builder.mCreatedDate;
        mDebtType = builder.mDebtType;
        mStatus = builder.mStatus;
        mAmount = builder.mAmount;
        mNote = builder.mNote;
        mDueDate = builder.mDueDate;
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
        if (mNote != null ? !mNote.equals(debt.mNote) : debt.mNote != null) return false;
        return mPersonId.equals(debt.mPersonId);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = mId.hashCode();
        temp = Double.doubleToLongBits(mAmount);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (mNote != null ? mNote.hashCode() : 0);
        result = 31 * result + mPersonId.hashCode();
        result = 31 * result + (int) (mCreatedDate ^ (mCreatedDate >>> 32));
        result = 31 * result + (int) (mDueDate ^ (mDueDate >>> 32));
        result = 31 * result + mDebtType;
        result = 31 * result + mStatus;
        return result;
    }


}
