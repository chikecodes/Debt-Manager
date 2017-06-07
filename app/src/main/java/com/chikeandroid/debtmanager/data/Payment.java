package com.chikeandroid.debtmanager.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by Chike on 5/24/2017.
 * Model class for a Payment
 */
public class Payment implements Parcelable {

    public static final int PAYMENT_ACTION_DEBT_INCREASE = 100;
    public static final int PAYMENT_ACTION_DEBT_DECREASE = 200;
    public static final int PAYMENT_ACTION_DEBT_DONT_CHANGE = 300;

    @NonNull
    private String mId;

    @NonNull
    private double mAmount;

    @NonNull
    private String mDebtId;

    @NonNull
    private long mDateEntered;

    @NonNull
    private String mNote;

    @NonNull
    private int mAction;

    @NonNull
    private String mPersonPhoneNo;

    @NonNull
    public String getId() {
        return mId;
    }

    public void setId(@NonNull String id) {
        mId = id;
    }

    @NonNull
    public String getDebtId() {
        return mDebtId;
    }

    public void setDebtId(@NonNull String debtId) {
        mDebtId = debtId;
    }

    @NonNull
    public long getDateEntered() {
        return mDateEntered;
    }

    public void setDateEntered(@NonNull long dateEntered) {
        mDateEntered = dateEntered;
    }

    @NonNull
    public String getNote() {
        return mNote;
    }

    public void setNote(@NonNull String note) {
        mNote = note;
    }

    @NonNull
    public int getAction() {
        return mAction;
    }

    public void setAction(@NonNull int action) {
        mAction = action;
    }

    @NonNull
    public double getAmount() {
        return mAmount;
    }

    public void setAmount(@NonNull double amount) {
        mAmount = amount;
    }

    @NonNull
    public String getPersonPhoneNo() {
        return mPersonPhoneNo;
    }

    public void setPersonPhoneNo(@NonNull String personPhoneNo) {
        mPersonPhoneNo = personPhoneNo;
    }

    public static class Builder {

        // required parameters
        private String mId;
        private double mAmount;
        private String mDebtId;
        private long mDateEntered;
        private String mNote;
        private int mAction;
        private String mPersonPhoneNumber;

        public Builder id(String id) {
            mId = id;
            return this;
        }

        public Builder amount(double amount) {
            mAmount = amount;
            return this;
        }

        public Builder debtId(String debtId) {
            mDebtId = debtId;
            return this;
        }

        public Builder dateEntered(long dateEntered) {
            mDateEntered = dateEntered;
            return this;
        }

        public Builder note(String note) {
            mNote = note;
            return this;
        }

        public Builder action(int action) {
            mAction = action;
            return this;
        }

        public Builder personPhoneNumber(String personPhoneNumber) {
            mPersonPhoneNumber = personPhoneNumber;
            return this;
        }

        public Payment build() {
            return new Payment(this);
        }
    }

    public Payment(Builder builder) {

        mId = builder.mId;
        mAmount = builder.mAmount;
        mAction = builder.mAction;
        mNote = builder.mNote;
        mDateEntered = builder.mDateEntered;
        mDebtId = builder.mDebtId;
        mPersonPhoneNo = builder.mPersonPhoneNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mId);
        dest.writeString(this.mDebtId);
        dest.writeLong(this.mDateEntered);
        dest.writeString(this.mNote);
        dest.writeInt(this.mAction);
        dest.writeDouble(this.mAmount);
        dest.writeString(this.mPersonPhoneNo);
    }

    protected Payment(Parcel in) {
        this.mId = in.readString();
        this.mDebtId = in.readString();
        this.mDateEntered = in.readLong();
        this.mNote = in.readString();
        this.mAction = in.readInt();
        this.mAmount = in.readDouble();
        this.mPersonPhoneNo = in.readString();
    }

    public static final Creator<Payment> CREATOR = new Creator<Payment>() {
        @Override
        public Payment createFromParcel(Parcel source) {
            return new Payment(source);
        }

        @Override
        public Payment[] newArray(int size) {
            return new Payment[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Payment payment = (Payment) o;

        if (Double.compare(payment.mAmount, mAmount) != 0) {
            return false;
        }
        if (mDateEntered != payment.mDateEntered) {
            return false;
        }
        if (mAction != payment.mAction) {
            return false;
        }
        if (!mId.equals(payment.mId)) {
            return false;
        }
        if (!mDebtId.equals(payment.mDebtId)) {
            return false;
        }
        if (!mNote.equals(payment.mNote)) {
            return false;
        }
        return mPersonPhoneNo.equals(payment.mPersonPhoneNo);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = mId.hashCode();
        temp = Double.doubleToLongBits(mAmount);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + mDebtId.hashCode();
        result = 31 * result + (int) (mDateEntered ^ (mDateEntered >>> 32));
        result = 31 * result + mNote.hashCode();
        result = 31 * result + mAction;
        result = 31 * result + mPersonPhoneNo.hashCode();
        return result;
    }
}
