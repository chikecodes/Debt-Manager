package com.chikeandroid.debtmanager.data.source.local;


import android.provider.BaseColumns;

/**
 * Created by Chike on 3/22/2017.
 * The contract used for the db to save the debts locally
 */
public class DebtsPersistenceContract {

    // prevent instantiation
    private DebtsPersistenceContract() {}

    // Debts Table
    public static abstract class DebtsEntry implements BaseColumns {

        public static final String TABLE_NAME = "debts";

        public static final String ALIAS_DEBT_ID = "debt_id";
        public static final String ALIAS_DATE_ENTERED = "debt_date_entered";
        public static final String ALIAS_AMOUNT = "debt_amount";
        public static final String ALIAS_NOTE = "debt_note";
        public static final String ALIAS_PERSON_PHONE_NUMBER = "debt_person_phone_number";
        public static final String COLUMN_ENTRY_ID = "entry_id";
        public static final String COLUMN_PERSON_PHONE_NUMBER = "person_phone_number";
        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_AMOUNT = "amount";
        public static final String COLUMN_DATE_DUE = "date_due";
        public static final String COLUMN_DATE_ENTERED = "date_entered";
        public static final String COLUMN_NOTE = "note";
        public static final String COLUMN_TYPE = "debt_type";

        // returns all columns as array
        public static String[] getAllColumns() {

            return new String[]{
                    COLUMN_ENTRY_ID,
                    COLUMN_PERSON_PHONE_NUMBER,
                    COLUMN_STATUS,
                    COLUMN_AMOUNT,
                    COLUMN_DATE_DUE,
                    COLUMN_DATE_ENTERED,
                    COLUMN_NOTE,
                    COLUMN_TYPE
            };
        }
    }

    // Persons Table
    public static abstract class PersonsEntry implements BaseColumns {

        public static final String TABLE_NAME = "persons";

        // public static final String ALIAS_PERSON_ID = "person_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PHONE_NO = "phone_no";
        public static final String COLUMN_IMAGE_URI = "image_uri";

        // returns all columns as array
        public static String[] getAllColumns() {

            return new String[]{
                    COLUMN_NAME,
                    COLUMN_PHONE_NO,
                    COLUMN_IMAGE_URI
            };
        }
    }

    // Payments Table
    public static abstract class PaymentsEntry implements BaseColumns {

        public static final String TABLE_NAME = "payments";
        public static final String COLUMN_AMOUNT = "amount";
        public static final String ALIAS_AMOUNT = "payment_amount";
        public static final String ALIAS_DATE_ENTERED = "payment_date_entered";
        public static final String ALIAS_PERSON_PHONE_NUMBER = "payment_person_number";
        public static final String ALIAS_NOTE = "payment_note";
        public static final String COLUMN_ENTRY_ID = "entry_id";
        public static final String COLUMN_DEBT_ID = "debt_id";
        public static final String COLUMN_DATE_ENTERED = "date_entered";
        public static final String COLUMN_NOTE = "note";
        public static final String COLUMN_ACTION = "action";
        public static final String COLUMN_PERSON_PHONE_NUMBER = "person_phone_number";

        public static String[] getAllColumns() {
            return new String[] {
                    COLUMN_AMOUNT,
                    COLUMN_ACTION,
                    COLUMN_DEBT_ID,
                    COLUMN_DATE_ENTERED,
                    COLUMN_ENTRY_ID,
                    COLUMN_NOTE,
                    COLUMN_PERSON_PHONE_NUMBER
            };
        }
    }


}
