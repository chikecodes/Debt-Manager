package com.chikeandroid.debtmanager20.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;

/**
 * Created by Chike on 3/14/2017.
 */

public final class Person {

    @NonNull
    private final String mFullname;

    @Nullable
    private final String mPhoneNumber;

    @Nullable
    private final String mEmail;

    private ArrayList<Debt> mDebts;

    public Person(@NonNull String fullname, @Nullable String phoneNumber, @Nullable String email) {
        mFullname = fullname;
        mPhoneNumber = phoneNumber;
        mEmail = email;
    }

    @NonNull
    public String getFullname() {
        return mFullname;
    }

    @Nullable
    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    @Nullable
    public String getEmail() {
        return mEmail;
    }

    public ArrayList<Debt> getDebts() {
        return mDebts;
    }

    public void setDebts(ArrayList<Debt> debts) {
        mDebts = debts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (!mFullname.equals(person.mFullname)) return false;
        if (mPhoneNumber != null ? !mPhoneNumber.equals(person.mPhoneNumber) : person.mPhoneNumber != null)
            return false;
        if (mEmail != null ? !mEmail.equals(person.mEmail) : person.mEmail != null) return false;
        return mDebts != null ? mDebts.equals(person.mDebts) : person.mDebts == null;

    }

    @Override
    public int hashCode() {
        int result = mFullname.hashCode();
        result = 31 * result + (mPhoneNumber != null ? mPhoneNumber.hashCode() : 0);
        result = 31 * result + (mEmail != null ? mEmail.hashCode() : 0);
        result = 31 * result + (mDebts != null ? mDebts.hashCode() : 0);
        return result;
    }
}
