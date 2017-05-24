package com.chikeandroid.debtmanager.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.chikeandroid.debtmanager.data.source.local.DebtsPersistenceContract.DebtsEntry;
import com.chikeandroid.debtmanager.data.source.local.DebtsPersistenceContract.PersonsEntry;

/**
 * Created by Chike on 3/22/2017.
 */

public class DebtsDbHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "Debts.db";

    private static final String TEXT_TYPE = " TEXT";

    private static final String INTEGER_TYPE = " INTEGER";

    private static final String BOOLEAN_TYPE = " INTEGER";

    private static final String COMMA_SEP = ",";

    public static final String WHERE = " WHERE ";
    public static final String WHERE_EQUAL_TO = " = ? ";

    private static final String SQL_CREATE_DEBTS_TABLE =
            "CREATE TABLE " + DebtsEntry.TABLE_NAME + " (" +
                    DebtsEntry._ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT," +
                    DebtsEntry.COLUMN_PERSON_ID + TEXT_TYPE + COMMA_SEP +
                    DebtsEntry.COLUMN_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                    DebtsEntry.COLUMN_AMOUNT + TEXT_TYPE + COMMA_SEP +
                    DebtsEntry.COLUMN_DATE_DUE + TEXT_TYPE + COMMA_SEP +
                    DebtsEntry.COLUMN_DATE_ENTERED + TEXT_TYPE + COMMA_SEP +
                    DebtsEntry.COLUMN_STATUS + BOOLEAN_TYPE + COMMA_SEP +
                    DebtsEntry.COLUMN_NOTE + TEXT_TYPE + COMMA_SEP +
                    DebtsEntry.COLUMN_TYPE + BOOLEAN_TYPE +
                    ", FOREIGN KEY (" + DebtsEntry.COLUMN_PERSON_ID + ") REFERENCES " +
                    PersonsEntry.TABLE_NAME + " (" + PersonsEntry._ID + ")" +
                    " )";

    private static final String SQL_CREATE_PERSONS_TABLE =
            "CREATE TABLE " + PersonsEntry.TABLE_NAME + " (" +
                    PersonsEntry._ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT," +
                    PersonsEntry.COLUMN_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                    PersonsEntry.COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                    PersonsEntry.COLUMN_PHONE_NO + TEXT_TYPE + COMMA_SEP +
                    PersonsEntry.COLUMN_IMAGE_URI + TEXT_TYPE +
                    " )";

    public DebtsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_PERSONS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_DEBTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXIST " + DebtsEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXIST " + PersonsEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
