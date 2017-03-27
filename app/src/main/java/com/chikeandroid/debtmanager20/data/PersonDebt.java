package com.chikeandroid.debtmanager20.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Chike on 3/23/2017.
 */

public final class PersonDebt {

    @NonNull
    private final String mPersonId;

    @NonNull
    private final String mPersonName;

    @NonNull
    private final String mAmount;

    @NonNull
    private final String mDebtId;

    @Nullable
    private final String mNote;

    @NonNull
    private final String mCreatedDate;

    @Nullable
    private final String mDueDate;

    @NonNull
    private final int mDebtType;

    @NonNull
    private final int mStatus;

   public static class Builder {

       // Required parameters
       private final String mPersonId;
       private final String mPersonName;
       private final String mAmount;
       private final String mDebtId;
       private final int mStatus;
       private final String mCreatedDate;
       private final int mDebtType;

       // Optional parameters
       private String mNote = "";
       private String mDueDate = "";

       public Builder(String personId, String personName, String amount, String debtId, int status,
                      String createdDate, int debtType) {
           mPersonId = personId;
           mPersonName = personName;
           mAmount = amount;
           mDebtId = debtId;
           mStatus = status;
           mCreatedDate = createdDate;
           mDebtType = debtType;
       }

       public Builder note(String note) {
           mNote = note;
           return this;
       }

       public Builder dueDate(String dueDate) {
           mDueDate = dueDate;
           return this;
       }

       public PersonDebt build() {
           return new PersonDebt(this);
       }
   }

    private PersonDebt(Builder builder) {

        mPersonId = builder.mPersonId;
        mPersonName = builder.mPersonName;
        mAmount = builder.mAmount;
        mDebtId = builder.mDebtId;
        mCreatedDate = builder.mCreatedDate;
        mDebtType = builder.mDebtType;
        mStatus = builder.mStatus;
        mNote = builder.mNote;
        mDueDate = builder.mDueDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersonDebt that = (PersonDebt) o;

        if (mDebtType != that.mDebtType) return false;
        if (mStatus != that.mStatus) return false;
        if (!mPersonId.equals(that.mPersonId)) return false;
        if (!mPersonName.equals(that.mPersonName)) return false;
        if (!mAmount.equals(that.mAmount)) return false;
        if (!mDebtId.equals(that.mDebtId)) return false;
        if (mNote != null ? !mNote.equals(that.mNote) : that.mNote != null) return false;
        if (!mCreatedDate.equals(that.mCreatedDate)) return false;
        return mDueDate != null ? mDueDate.equals(that.mDueDate) : that.mDueDate == null;

    }

    @Override
    public int hashCode() {
        int result = mPersonId.hashCode();
        result = 31 * result + mPersonName.hashCode();
        result = 31 * result + mAmount.hashCode();
        result = 31 * result + mDebtId.hashCode();
        result = 31 * result + (mNote != null ? mNote.hashCode() : 0);
        result = 31 * result + mCreatedDate.hashCode();
        result = 31 * result + (mDueDate != null ? mDueDate.hashCode() : 0);
        result = 31 * result + mDebtType;
        result = 31 * result + mStatus;
        return result;
    }
}
