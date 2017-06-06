package com.chikeandroid.debtmanager.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chike on 3/14/2017.
 * Model class for a Debt
 */
public class Debt implements Parcelable {

    public static final int DEBT_TYPE_IOWE = 100;
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
    private final String mPersonPhoneNumber;

    @NonNull
    private long mCreatedDate;

    @Nullable
    private long mDueDate;

    @NonNull
    private int mDebtType;

    @NonNull
    private final int mStatus;

    @Nullable
    private List<Payment> mPayments;

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
    public String getPersonPhoneNumber() {
        return mPersonPhoneNumber;
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

    @Nullable
    public List<Payment> getPayments() {
        return mPayments;
    }

    public void setPayments(@Nullable List<Payment> payments) {
        mPayments = payments;
    }

    public void addPayment(Payment payment) {
        mPayments.add(payment);
    }

    public static class Builder {

        // Required parameters
        private final String mId;
        private final String mPersonPhoneNumber;
        private final long mCreatedDate;
        private final int mDebtType;
        private final int mStatus;
        private final double mAmount;

        // Optional parameters
        private String mNote = "";
        private long mDueDate = 0;
        private List<Payment> mPayments = new ArrayList<>();

        public Builder(String id, String personPhoneNumber, Double amount, long createdDate, int debtType, int status) {
            mId = id;
            mAmount = amount;
            mCreatedDate = createdDate;
            mPersonPhoneNumber = personPhoneNumber;
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

        public Builder addPayment(Payment payment) {
            mPayments.add(payment);
            return this;
        }

        public Builder payments(List<Payment> payments) {
            mPayments = payments;
            return this;
        }

        public Debt build() {
            return new Debt(this);
        }

    }

    public Debt(Builder builder) {

        mId = builder.mId;
        mPersonPhoneNumber = builder.mPersonPhoneNumber;
        mCreatedDate = builder.mCreatedDate;
        mDebtType = builder.mDebtType;
        mStatus = builder.mStatus;
        mAmount = builder.mAmount;
        mNote = builder.mNote;
        mDueDate = builder.mDueDate;
        mPayments = builder.mPayments;
    }

    public Debt(Debt debt) {

        mId = debt.getId();
        mPersonPhoneNumber = debt.getPersonPhoneNumber();
        mCreatedDate = debt.getCreatedDate();
        mDebtType = debt.getDebtType();
        mStatus = debt.getStatus();
        mAmount = debt.getAmount();
        mNote = debt.getNote();
        mDueDate = debt.getDueDate();
        mPayments = debt.getPayments();
    }

    public boolean isEmpty() {
        return mAmount == 0;
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
        dest.writeString(this.mPersonPhoneNumber);
        dest.writeLong(this.mCreatedDate);
        dest.writeLong(this.mDueDate);
        dest.writeInt(this.mDebtType);
        dest.writeInt(this.mStatus);
        dest.writeTypedList(this.mPayments);
    }

    protected Debt(Parcel in) {
        this.mId = in.readString();
        this.mAmount = in.readDouble();
        this.mNote = in.readString();
        this.mPersonPhoneNumber = in.readString();
        this.mCreatedDate = in.readLong();
        this.mDueDate = in.readLong();
        this.mDebtType = in.readInt();
        this.mStatus = in.readInt();
        this.mPayments = in.createTypedArrayList(Payment.CREATOR);
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
        if (!mPersonPhoneNumber.equals(debt.mPersonPhoneNumber)) return false;
        return mPayments != null ? mPayments.equals(debt.mPayments) : debt.mPayments == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = mId.hashCode();
        temp = Double.doubleToLongBits(mAmount);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (mNote != null ? mNote.hashCode() : 0);
        result = 31 * result + mPersonPhoneNumber.hashCode();
        result = 31 * result + (int) (mCreatedDate ^ (mCreatedDate >>> 32));
        result = 31 * result + (int) (mDueDate ^ (mDueDate >>> 32));
        result = 31 * result + mDebtType;
        result = 31 * result + mStatus;
        result = 31 * result + (mPayments != null ? mPayments.hashCode() : 0);
        return result;
    }
}
