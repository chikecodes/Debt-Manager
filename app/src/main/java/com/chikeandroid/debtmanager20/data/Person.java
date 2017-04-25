package com.chikeandroid.debtmanager20.data;

import com.google.common.base.Strings;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;

/**
 * Created by Chike on 3/14/2017.
 */

public final class Person implements Parcelable {

    @NonNull
    private final String mId;

    @NonNull
    private String mFullname;

    @Nullable
    private String mPhoneNumber;

    private ArrayList<Debt> mDebts;

    public Person(@NonNull String id, @NonNull String fullname, @Nullable String phoneNumber) {
        mId = id;
        mFullname = fullname;
        mPhoneNumber = phoneNumber;
    }

    public boolean isEmpty() {
        return Strings.isNullOrEmpty(mPhoneNumber)
                && Strings.isNullOrEmpty(String.valueOf(mFullname));
    }

    @NonNull
    public String getFullname() {
        return mFullname;
    }

    @Nullable
    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public ArrayList<Debt> getDebts() {
        return mDebts;
    }

    public void setDebts(ArrayList<Debt> debts) {
        mDebts = debts;
    }

    public void setFullname(@NonNull String fullname) {
        mFullname = fullname;
    }

    public void setPhoneNumber(@Nullable String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    @NonNull
    public String getId() {
        return mId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (!mId.equals(person.mId)) return false;
        if (!mFullname.equals(person.mFullname)) return false;
        if (mPhoneNumber != null ? !mPhoneNumber.equals(person.mPhoneNumber) : person.mPhoneNumber != null)
            return false;
        return mDebts != null ? mDebts.equals(person.mDebts) : person.mDebts == null;

    }

    @Override
    public int hashCode() {
        int result = mId.hashCode();
        result = 31 * result + mFullname.hashCode();
        result = 31 * result + (mPhoneNumber != null ? mPhoneNumber.hashCode() : 0);
        result = 31 * result + (mDebts != null ? mDebts.hashCode() : 0);
        return result;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mId);
        dest.writeString(this.mFullname);
        dest.writeString(this.mPhoneNumber);
        dest.writeList(this.mDebts);
    }

    protected Person(Parcel in) {
        this.mId = in.readString();
        this.mFullname = in.readString();
        this.mPhoneNumber = in.readString();
        this.mDebts = new ArrayList<Debt>();
        in.readList(this.mDebts, Debt.class.getClassLoader());
    }

    public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel source) {
            return new Person(source);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };
}