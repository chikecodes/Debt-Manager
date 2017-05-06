package com.chikeandroid.debtmanager20.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Chike on 3/23/2017.
 * Immutable model class for a PersonDebt.
 */

public final class PersonDebt implements Parcelable {

    @NonNull
    private final Person mPerson;

    @NonNull
    private final Debt mDebt;

    public PersonDebt(Person person, Debt debt) {
        checkNotNull(person);
        checkNotNull(debt);

        mPerson = person;
        mDebt = debt;
    }

    @NonNull
    public Person getPerson() {
        return mPerson;
    }

    @NonNull
    public Debt getDebt() {
        return mDebt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PersonDebt that = (PersonDebt) o;

        if (!mPerson.equals(that.mPerson)) {
            return false;
        }
        return mDebt.equals(that.mDebt);

    }

    @Override
    public int hashCode() {
        int result = mPerson.hashCode();
        result = 31 * result + mDebt.hashCode();
        return result;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mPerson, flags);
        dest.writeParcelable(this.mDebt, flags);
    }

    protected PersonDebt(Parcel in) {
        this.mPerson = in.readParcelable(Person.class.getClassLoader());
        this.mDebt = in.readParcelable(Debt.class.getClassLoader());
    }

    public static final Parcelable.Creator<PersonDebt> CREATOR = new Parcelable.Creator<PersonDebt>() {
        @Override
        public PersonDebt createFromParcel(Parcel source) {
            return new PersonDebt(source);
        }

        @Override
        public PersonDebt[] newArray(int size) {
            return new PersonDebt[size];
        }
    };
}
