package com.chikeandroid.debtmanager20.data.source.local;

import android.provider.BaseColumns;

/**
 * Created by Chike on 3/22/2017.
 * The contract used for the db to save the debts locally
 */
public class DebtsPersistenceContract {

    // prevent instantiation
    private DebtsPersistenceContract() {}

    public static abstract class DebtsEntry implements BaseColumns {

        public static final String TABLE_NAME = "debts";

        public static final String ALIAS_DEBT_ID = "debt_id";
        public static final String COLUMN_ENTRY_ID = "entry_id";
        public static final String COLUMN_PERSON_ID = "person_id";
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
                    COLUMN_PERSON_ID,
                    COLUMN_STATUS,
                    COLUMN_AMOUNT,
                    COLUMN_DATE_DUE,
                    COLUMN_DATE_ENTERED,
                    COLUMN_NOTE,
                    COLUMN_TYPE
            };
        }
    }

    public static abstract class PersonsEntry implements BaseColumns {

        public static final String TABLE_NAME = "persons";

        public static final String ALIAS_PERSON_ID = "person_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_ENTRY_ID = "entry_id";
        public static final String COLUMN_PHONE_NO = "phone_no";

        // returns all columns as array
        public static String[] getAllColumns() {
            String[] columns = {
                    COLUMN_NAME,
                    COLUMN_ENTRY_ID,
                    COLUMN_PHONE_NO
            };

            return columns;
        }
    }


}
