package com.chikeandroid.debtmanager.data;

import com.google.common.base.Strings;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chike on 3/14/2017.
 * Model class for a Person.
 */
public class Person implements Parcelable {

    @NonNull
    private String mFullname;

    @NonNull
    private String mPhoneNumber;

    @Nullable
    private String mImageUri;

    private List<Debt> mDebts;

    public Person(@NonNull String fullname, @NonNull String phoneNumber,
                  @Nullable String imageUri) {
        mFullname = fullname;
        mPhoneNumber = phoneNumber;
        mImageUri = imageUri;
        mDebts = new ArrayList<>();
    }

    public Person(Person person) {

        mDebts = person.getDebts();
        mFullname = person.getFullname();
        mPhoneNumber = person.getPhoneNumber();
        mImageUri = person.getImageUri();
    }

    public boolean isEmpty() {
        return Strings.isNullOrEmpty(mPhoneNumber)
                && Strings.isNullOrEmpty(String.valueOf(mFullname));
    }

    @NonNull
    public String getFullname() {
        return mFullname;
    }

    @NonNull
    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public List<Debt> getDebts() {
        return mDebts;
    }

    public void setDebts(List<Debt> debts) {
        mDebts = debts;
    }

    public void addDebt(@NonNull Debt debt) {
        mDebts.add(debt);
    }
    public void setFullname(@NonNull String fullname) {
        mFullname = fullname;
    }

    public void setPhoneNumber(@NonNull String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    @Nullable
    public String getImageUri() {
        return mImageUri;
    }

    public void setImageUri(@Nullable String imageUri) {
        mImageUri = imageUri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mFullname);
        dest.writeString(this.mPhoneNumber);
        dest.writeString(this.mImageUri);
        dest.writeTypedList(this.mDebts);
    }

    protected Person(Parcel in) {
        this.mFullname = in.readString();
        this.mPhoneNumber = in.readString();
        this.mImageUri = in.readString();
        this.mDebts = in.createTypedArrayList(Debt.CREATOR);
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel source) {
            return new Person(source);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
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

        Person person = (Person) o;

        if (!mFullname.equals(person.mFullname)) {
            return false;
        }
        if (!mPhoneNumber.equals(person.mPhoneNumber)) {
            return false;
        }
        if (mImageUri != null ? !mImageUri.equals(person.mImageUri) : person.mImageUri != null) {
            return false;
        }
        return mDebts != null ? mDebts.equals(person.mDebts) : person.mDebts == null;

    }

    @Override
    public int hashCode() {
        int result = mFullname.hashCode();
        result = 31 * result + mPhoneNumber.hashCode();
        result = 31 * result + (mImageUri != null ? mImageUri.hashCode() : 0);
        result = 31 * result + (mDebts != null ? mDebts.hashCode() : 0);
        return result;
    }
}
