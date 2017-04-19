package com.chikeandroid.debtmanager20.data;

import android.support.annotation.NonNull;

/**
 * Created by Chike on 3/23/2017.
 */

public final class PersonDebt {

    @NonNull
    private final Person mPerson;

    @NonNull
    private final Debt mDebt;

    public PersonDebt(Person person, Debt debt) {

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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersonDebt that = (PersonDebt) o;

        if (!mPerson.equals(that.mPerson)) return false;
        return mDebt.equals(that.mDebt);

    }

    @Override
    public int hashCode() {
        int result = mPerson.hashCode();
        result = 31 * result + mDebt.hashCode();
        return result;
    }
}
