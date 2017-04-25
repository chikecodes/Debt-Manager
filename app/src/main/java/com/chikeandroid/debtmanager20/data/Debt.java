package com.chikeandroid.debtmanager20.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Chike on 3/14/2017.
 * Immutable model class for a Debt
 */
public final class Debt implements Parcelable {

    public static final int DEBT_TYPE_i_OWE = 100;
    public static final int DEBT_TYPE_OWED = 200;
    public static final int DEBT_STATUS_PARTIAL = 101;
    public static final int DEBT_STATUS_ACTIVE = 102;

    @NonNull
    private final String mId;

    @Nullable
    private double mAmount;

    @Nullable
    private String mNote;

    @NonNull
    private final String mPersonId;

    @NonNull
    private long mCreatedDate;

    @Nullable
    private long mDueDate;

    @NonNull
    private int mDebtType;

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

    public void setAmount(@Nullable double amount) {
        mAmount = amount;
    }

    public void setNote(@Nullable String note) {
        mNote = note;
    }

    public void setCreatedDate(@NonNull long createdDate) {
        mCreatedDate = createdDate;
    }

    public void setDueDate(@Nullable long dueDate) {
        mDueDate = dueDate;
    }

    public void setDebtType(@NonNull int debtType) {
        mDebtType = debtType;
    }

    public static class Builder {

        // Required parameters
        private final String mId;
        private final String mPersonId;
        private final long mCreatedDate;
        private final int mDebtType;
        private final int mStatus;
        private double mAmount;

        // Optional parameters
        private String mNote = "";
        private long mDueDate = 0;

        public Builder(String id, String personId, Double amount, long createdDate,int debtType, int status) {
            mId = id;
            mAmount = amount;
            mCreatedDate = createdDate;
            mPersonId = personId;
            mDebtType = debtType;
            mStatus = status;
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

    public boolean isEmpty() {
        return mAmount == 0;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mId);
        dest.writeDouble(this.mAmount);
        dest.writeString(this.mNote);
        dest.writeString(this.mPersonId);
        dest.writeLong(this.mCreatedDate);
        dest.writeLong(this.mDueDate);
        dest.writeInt(this.mDebtType);
        dest.writeInt(this.mStatus);
    }

    protected Debt(Parcel in) {
        this.mId = in.readString();
        this.mAmount = in.readDouble();
        this.mNote = in.readString();
        this.mPersonId = in.readString();
        this.mCreatedDate = in.readLong();
        this.mDueDate = in.readLong();
        this.mDebtType = in.readInt();
        this.mStatus = in.readInt();
    }

    public static final Parcelable.Creator<Debt> CREATOR = new Parcelable.Creator<Debt>() {
        @Override
        public Debt createFromParcel(Parcel source) {
            return new Debt(source);
        }

        @Override
        public Debt[] newArray(int size) {
            return new Debt[size];
        }
    };
}
