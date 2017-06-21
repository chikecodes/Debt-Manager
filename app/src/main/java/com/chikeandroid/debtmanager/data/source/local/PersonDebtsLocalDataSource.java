package com.chikeandroid.debtmanager.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chikeandroid.debtmanager.data.Debt;
import com.chikeandroid.debtmanager.data.Payment;
import com.chikeandroid.debtmanager.data.Person;
import com.chikeandroid.debtmanager.data.PersonDebt;
import com.chikeandroid.debtmanager.data.source.PersonDebtsDataSource;
import com.chikeandroid.debtmanager.data.source.local.DebtsPersistenceContract.DebtsEntry;
import com.chikeandroid.debtmanager.data.source.local.DebtsPersistenceContract.PaymentsEntry;
import com.chikeandroid.debtmanager.data.source.local.DebtsPersistenceContract.PersonsEntry;
import com.chikeandroid.debtmanager.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Chike on 3/22/2017.
 * Concrete implementation of a data source in a SQLite database.
 */
@Singleton
public class PersonDebtsLocalDataSource implements PersonDebtsDataSource {

    private final DebtsDbHelper mDebtsDbHelper;

    public PersonDebtsLocalDataSource(@NonNull Context context) {
        checkNotNull(context);
        mDebtsDbHelper = new DebtsDbHelper(context);
    }

    /**
     * Get all PersonDebts by debt type
     * @param debtType is the type of debt
     * @return a List<PersonDebt>
     */
    @Override
    public List<PersonDebt> getAllPersonDebtsByType(@NonNull int debtType) {
        checkNotNull(debtType);

        List<PersonDebt> personDebts = new ArrayList<>();
        SQLiteDatabase db = mDebtsDbHelper.getReadableDatabase();

        String where = DebtsEntry.TABLE_NAME + "." + DebtsEntry.COLUMN_TYPE + DebtsDbHelper.WHERE_EQUAL_TO;
        String sql = buildJoinsQueryFromDebtsPersonsTable(where);

        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(debtType)});

        if (cursor != null && cursor.getCount() > 0) {

            PersonDebt personDebt;

            while (cursor.moveToNext()) {

                long dateDue = cursor.getLong(cursor.getColumnIndexOrThrow(DebtsEntry.COLUMN_DATE_DUE));
                String personPhoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(DebtsEntry.COLUMN_PERSON_PHONE_NUMBER));
                int status = cursor.getInt(cursor.getColumnIndexOrThrow(DebtsEntry.COLUMN_STATUS));
                int debtType1 = cursor.getInt(cursor.getColumnIndexOrThrow(DebtsEntry.COLUMN_TYPE));
                String note = cursor.getString(cursor.getColumnIndexOrThrow(DebtsEntry.COLUMN_NOTE));
                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(DebtsEntry.COLUMN_AMOUNT));
                String entryId = cursor.getString(cursor.getColumnIndexOrThrow(DebtsEntry.ALIAS_DEBT_ID));
                long dateEntered = cursor.getLong(cursor.getColumnIndexOrThrow(DebtsEntry.COLUMN_DATE_ENTERED));

                Debt debt = new Debt.Builder(entryId, personPhoneNumber, amount, dateEntered, debtType1, status)
                        .dueDate(dateDue)
                        .note(note)
                        .build();

                personDebt = getPersonDebt(debt.getId(), debtType);

                personDebt.getDebt().setAmount(debt.getAmount());

                personDebts.add(personDebt);
            }
        }
        if (cursor != null) {
            cursor.close();
        }

        if (personDebts.isEmpty()) {
            return new ArrayList<>();
        } else {
            return personDebts;
        }
    }

    /**
     * Creates and then returns a {@link PersonDebt} object from joining persons, debts and payments
     * table or just joins from Debts and Peerson table depending if the Debt has payments.
     * @param debtId the Debt id
     * @param debtType the Debt type
     * @return a {@link PersonDebt} object
     */
    @Override
    public PersonDebt getPersonDebt(@NonNull String debtId, @NonNull int debtType) {
        checkNotNull(debtId);
        checkNotNull(debtType);

        PersonDebt personDebt;

        if (debtHavePayments(debtId)) {
            personDebt = getPersonDebtFromPersonsDebtsPaymentsTable(debtId, debtType);
        }else {
            personDebt = getPersonDebtFromDebtsAndPersonsTable(debtId, debtType);
        }
        return personDebt;
    }

    /**
     * Creates and then returns a {@link PersonDebt} object from persons, debts and payments table
     * @param debtId the Debt id
     * @param debtType the Debt type
     * @return a {@link PersonDebt} object
     */
    private PersonDebt getPersonDebtFromPersonsDebtsPaymentsTable(@NonNull String debtId, @NonNull int debtType) {
        SQLiteDatabase db = mDebtsDbHelper.getReadableDatabase();

        String where = DebtsEntry.TABLE_NAME + "." + DebtsEntry.COLUMN_ENTRY_ID + " =? AND " +
                DebtsEntry.TABLE_NAME + "." + DebtsEntry.COLUMN_TYPE + DebtsDbHelper.WHERE_EQUAL_TO;

        String sql = buildJoinsQueryFromDebtsPersonsPaymentsTable(where);

        Cursor cursor = db.rawQuery(sql, new String[]{debtId, String.valueOf(debtType)});
        PersonDebt personDebt = null;

        if (cursor != null && cursor.getCount() > 0) {

            cursor.moveToFirst();

            String paymentId = cursor.getString(cursor.getColumnIndexOrThrow(PaymentsEntry.COLUMN_ENTRY_ID));
            String paymentDebtId = cursor.getString(cursor.getColumnIndexOrThrow(PaymentsEntry.COLUMN_DEBT_ID));
            int action = cursor.getInt(cursor.getColumnIndexOrThrow(PaymentsEntry.COLUMN_ACTION));
            String personPhoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(PaymentsEntry.ALIAS_PERSON_PHONE_NUMBER));
            double paymentAmount = cursor.getDouble(cursor.getColumnIndexOrThrow(PaymentsEntry.ALIAS_AMOUNT));
            long paymentDateEntered = cursor.getLong(cursor.getColumnIndexOrThrow(PaymentsEntry.ALIAS_DATE_ENTERED));
            String paymentNote = cursor.getString(cursor.getColumnIndexOrThrow(PaymentsEntry.ALIAS_NOTE));

            Payment payment = new Payment.Builder()
                    .id(paymentId)
                    .amount(paymentAmount)
                    .debtId(paymentDebtId)
                    .dateEntered(paymentDateEntered)
                    .note(paymentNote)
                    .personPhoneNumber(personPhoneNumber)
                    .action(action).build();

            long dateDue = cursor.getLong(cursor.getColumnIndexOrThrow(DebtsEntry.COLUMN_DATE_DUE));
            int status = cursor.getInt(cursor.getColumnIndexOrThrow(DebtsEntry.COLUMN_STATUS));
            int debtType1 = cursor.getInt(cursor.getColumnIndexOrThrow(DebtsEntry.COLUMN_TYPE));
            String debtId2 = cursor.getString(cursor.getColumnIndexOrThrow(DebtsEntry.ALIAS_DEBT_ID));
            long dateEntered = cursor.getLong(cursor.getColumnIndexOrThrow(DebtsEntry.ALIAS_DATE_ENTERED));
            String debtNote = cursor.getString(cursor.getColumnIndexOrThrow(DebtsEntry.ALIAS_NOTE));
            double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(DebtsEntry.ALIAS_AMOUNT));

            Debt debt = new Debt.Builder(debtId2, personPhoneNumber, amount, dateEntered, debtType1, status)
                    .dueDate(dateDue)
                    .note(debtNote)
                    .build();

            debt.addPayment(payment);

            DatabaseUtils.dumpCursor(cursor);

            Person person = getPersonFromCursor(cursor);

            while (cursor.moveToNext()) {

                String paymentId2 = cursor.getString(cursor.getColumnIndexOrThrow(PaymentsEntry.COLUMN_ENTRY_ID));
                String paymentDebtId2 = cursor.getString(cursor.getColumnIndexOrThrow(PaymentsEntry.COLUMN_DEBT_ID));
                int action2 = cursor.getInt(cursor.getColumnIndexOrThrow(PaymentsEntry.COLUMN_ACTION));
                String personPhoneNumber2 = cursor.getString(cursor.getColumnIndexOrThrow(PaymentsEntry.ALIAS_PERSON_PHONE_NUMBER));
                double paymentAmount2 = cursor.getDouble(cursor.getColumnIndexOrThrow(PaymentsEntry.ALIAS_AMOUNT));
                long paymentDateEntered2 = cursor.getLong(cursor.getColumnIndexOrThrow(PaymentsEntry.ALIAS_DATE_ENTERED));
                String paymentNote2 = cursor.getString(cursor.getColumnIndexOrThrow(PaymentsEntry.ALIAS_NOTE));

                Payment payment2 = new Payment.Builder()
                        .id(paymentId2)
                        .amount(paymentAmount2)
                        .debtId(paymentDebtId2)
                        .dateEntered(paymentDateEntered2)
                        .note(paymentNote2)
                        .personPhoneNumber(personPhoneNumber2)
                        .action(action2).build();

                debt.addPayment(payment2);
            }

            personDebt = new PersonDebt(person, debt);
        }

        if (cursor != null) {
            cursor.close();
        }

        return personDebt;
    }

    /**
     * Create a {@link Person} from the given Cursor
     * @param cursor the Android database Cursor interface
     * @return a {@link Person} object
     */
    private Person getPersonFromCursor(Cursor cursor) {
        String personName = cursor.getString(cursor.getColumnIndexOrThrow(PersonsEntry.COLUMN_NAME));
        String personPhoneNo = cursor.getString(cursor.getColumnIndexOrThrow(PersonsEntry.COLUMN_PHONE_NO));
        String personImageUri = cursor.getString(cursor.getColumnIndexOrThrow(PersonsEntry.COLUMN_IMAGE_URI));
        return new Person(personName, personPhoneNo, personImageUri);
    }

    /**
     * Creates a {@link PersonDebt} object by joining from the debts and persons table
     * @param debtId the debt id
     * @param debtType the debt type
     * @return a {@link PersonDebt} object
     */
    private PersonDebt getPersonDebtFromDebtsAndPersonsTable(@NonNull String debtId, @NonNull int debtType) {

        String where = DebtsEntry.TABLE_NAME + "." + DebtsEntry.COLUMN_ENTRY_ID + " =? AND " +
                DebtsEntry.TABLE_NAME + "." + DebtsEntry.COLUMN_TYPE + DebtsDbHelper.WHERE_EQUAL_TO;

        String sql = buildJoinsQueryFromDebtsPersonsTable(where);
        SQLiteDatabase db = mDebtsDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, new String[]{debtId, String.valueOf(debtType)});
        PersonDebt personDebt = null;

        if (cursor != null && cursor.getCount() > 0) {

            cursor.moveToFirst();

            long dateDue = cursor.getLong(cursor.getColumnIndexOrThrow(DebtsEntry.COLUMN_DATE_DUE));
            String personPhoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(DebtsEntry.COLUMN_PERSON_PHONE_NUMBER));
            int status = cursor.getInt(cursor.getColumnIndexOrThrow(DebtsEntry.COLUMN_STATUS));
            int debtType1 = cursor.getInt(cursor.getColumnIndexOrThrow(DebtsEntry.COLUMN_TYPE));
            String note = cursor.getString(cursor.getColumnIndexOrThrow(DebtsEntry.COLUMN_NOTE));
            double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(DebtsEntry.COLUMN_AMOUNT));
            String entryId = cursor.getString(cursor.getColumnIndexOrThrow(DebtsEntry.ALIAS_DEBT_ID));
            long dateEntered = cursor.getLong(cursor.getColumnIndexOrThrow(DebtsEntry.COLUMN_DATE_ENTERED));

            Debt debt = new Debt.Builder(entryId, personPhoneNumber, amount, dateEntered, debtType1, status)
                    .dueDate(dateDue)
                    .note(note)
                    .build();

            Person person = getPersonFromCursor(cursor);

            personDebt = new PersonDebt(person, debt);
            cursor.close();
        }

        if (cursor != null) {
            cursor.close();
        }

        return personDebt;
    }

    /**
     * Check if a debt has payments
     * @param debtId the debt id
     * @return either true or false
     */
    private boolean debtHavePayments(@NonNull String debtId) {
        boolean hasPayments = false;
        SQLiteDatabase db = mDebtsDbHelper.getReadableDatabase();
        Cursor cursor = db.query(PaymentsEntry.TABLE_NAME, PaymentsEntry.getAllColumns(), PaymentsEntry.COLUMN_DEBT_ID + "=?",
                new String[]{debtId}, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            hasPayments = true;
        }

        if (cursor != null) {
            cursor.close();
        }
        return hasPayments;
    }

    /**
     * Builds an sql statement that joins from the debts and persons table
     * @param whereValue the where condition value
     * @return the sql statement
     */
    private String buildJoinsQueryFromDebtsPersonsTable(String whereValue) {

        String comma = ", ";
        String alias = " AS ";
        String dot = ".";
        StringBuilder sqlStringBuilder = new StringBuilder(40);
        sqlStringBuilder.append("SELECT ").append(DebtsEntry.COLUMN_PERSON_PHONE_NUMBER).append(comma)
                .append(DebtsEntry.TABLE_NAME).append(dot).append(DebtsEntry.COLUMN_ENTRY_ID).append(alias)
                .append(DebtsEntry.ALIAS_DEBT_ID).append(comma).append(DebtsEntry.COLUMN_AMOUNT).append(comma)
                .append(DebtsEntry.TABLE_NAME).append(dot).append(DebtsEntry.COLUMN_DATE_ENTERED).append(comma)
                .append(DebtsEntry.TABLE_NAME).append(dot).append(DebtsEntry.COLUMN_DATE_DUE).append(comma)
                .append(DebtsEntry.TABLE_NAME).append(dot).append(DebtsEntry.COLUMN_NOTE).append(comma)
                .append(DebtsEntry.TABLE_NAME).append(dot).append(DebtsEntry.COLUMN_STATUS).append(comma)
                .append(DebtsEntry.TABLE_NAME).append(dot).append(DebtsEntry.COLUMN_TYPE).append(comma)
                .append(PersonsEntry.TABLE_NAME).append(dot).append(PersonsEntry.COLUMN_NAME).append(comma)
                .append(PersonsEntry.TABLE_NAME).append(dot).append(PersonsEntry.COLUMN_IMAGE_URI).append(comma)
                .append(PersonsEntry.TABLE_NAME).append(dot).append(PersonsEntry.COLUMN_PHONE_NO)
                .append(" FROM ").append(DebtsEntry.TABLE_NAME).append(" INNER JOIN ").append(PersonsEntry.TABLE_NAME)
                .append(" ON ").append(DebtsEntry.TABLE_NAME).append(dot).append(DebtsEntry.COLUMN_PERSON_PHONE_NUMBER)
                .append(" = ").append(PersonsEntry.TABLE_NAME).append(dot).append(PersonsEntry.COLUMN_PHONE_NO);

        if (!"no".equals(whereValue)) {
            sqlStringBuilder.append(DebtsDbHelper.WHERE).append(whereValue);
        }

        return sqlStringBuilder.toString();
    }

    /**
     * Builds an sql statement that joins from the debts, persons and payments table
     * @param whereValue the where condition value
     * @return the sql statement
     */
    private String buildJoinsQueryFromDebtsPersonsPaymentsTable(String whereValue) {

        String comma = ", ";
        String alias = " AS ";
        String dot = ".";
        StringBuilder sqlStringBuilder = new StringBuilder(51);
        sqlStringBuilder.append("SELECT ").append(DebtsEntry.TABLE_NAME).append(dot).append(DebtsEntry.COLUMN_PERSON_PHONE_NUMBER)
                .append(alias).append(DebtsEntry.ALIAS_PERSON_PHONE_NUMBER).append(comma)
                .append(DebtsEntry.TABLE_NAME).append(dot).append(DebtsEntry.COLUMN_ENTRY_ID).append(alias)
                .append(DebtsEntry.ALIAS_DEBT_ID).append(comma).append(DebtsEntry.TABLE_NAME).append(dot)
                .append(DebtsEntry.COLUMN_AMOUNT).append(alias).append(DebtsEntry.ALIAS_AMOUNT).append(comma)
                .append(DebtsEntry.TABLE_NAME).append(dot).append(DebtsEntry.COLUMN_DATE_ENTERED)
                .append(alias).append(DebtsEntry.ALIAS_DATE_ENTERED).append(comma).append(DebtsEntry.COLUMN_DATE_DUE).append(comma)
                .append(DebtsEntry.TABLE_NAME).append(dot).append(DebtsEntry.COLUMN_NOTE).append(alias)
                .append(DebtsEntry.ALIAS_NOTE).append(comma).append(DebtsEntry.COLUMN_STATUS)
                .append(comma).append(DebtsEntry.COLUMN_TYPE).append(comma).append(PersonsEntry.COLUMN_NAME).append(comma)
                .append(PersonsEntry.COLUMN_IMAGE_URI).append(comma)
                .append(PersonsEntry.COLUMN_PHONE_NO).append(comma)
                .append(PaymentsEntry.TABLE_NAME).append(dot).append(PaymentsEntry.COLUMN_ACTION).append(comma)
                .append(PaymentsEntry.TABLE_NAME).append(dot).append(PaymentsEntry.COLUMN_AMOUNT)
                .append(alias).append(PaymentsEntry.ALIAS_AMOUNT).append(comma)
                .append(PaymentsEntry.TABLE_NAME).append(dot).append(PaymentsEntry.COLUMN_DATE_ENTERED).append(alias)
                .append(PaymentsEntry.ALIAS_DATE_ENTERED).append(comma)
                .append(PaymentsEntry.TABLE_NAME).append(dot).append(PaymentsEntry.COLUMN_DEBT_ID).append(comma)
                .append(PaymentsEntry.TABLE_NAME).append(dot).append(PaymentsEntry.COLUMN_ENTRY_ID).append(comma)
                .append(PaymentsEntry.TABLE_NAME).append(dot).append(PaymentsEntry.COLUMN_PERSON_PHONE_NUMBER).append(alias)
                .append(PaymentsEntry.ALIAS_PERSON_PHONE_NUMBER).append(comma)
                .append(PaymentsEntry.TABLE_NAME).append(dot).append(PaymentsEntry.COLUMN_NOTE).append(alias).append(PaymentsEntry.ALIAS_NOTE)
                .append(" FROM ").append(DebtsEntry.TABLE_NAME)
                .append(" INNER JOIN ").append(PersonsEntry.TABLE_NAME)
                .append(" ON ").append(DebtsEntry.TABLE_NAME).append(dot).append(DebtsEntry.COLUMN_PERSON_PHONE_NUMBER)
                .append(" = ").append(PersonsEntry.TABLE_NAME).append(dot).append(PersonsEntry.COLUMN_PHONE_NO)
                .append(" INNER JOIN ").append(PaymentsEntry.TABLE_NAME)
                .append(" ON ").append(DebtsEntry.TABLE_NAME).append(dot).append(DebtsEntry.COLUMN_ENTRY_ID)
                .append(" = ").append(PaymentsEntry.TABLE_NAME).append(dot).append(PaymentsEntry.COLUMN_DEBT_ID);

        if (!"no".equals(whereValue)) {
            sqlStringBuilder.append(DebtsDbHelper.WHERE).append(whereValue);
        }

        return sqlStringBuilder.toString();
    }

    /**
     * Saves a {@link Person} into the persons table and also a {@link Debt} into the debts table. If the person already
     * exists with the given phone number, it would'nt save the person in the database.
     * @param debt the {@link Debt} object to saved
     * @param person the {@link Person} object to saved
     */
    @Override
    public void savePersonDebt(@NonNull Debt debt, @NonNull Person person) {
        checkNotNull(debt);
        checkNotNull(person);

        SQLiteDatabase db = mDebtsDbHelper.getWritableDatabase();

        if (!personAlreadyExist(person.getPhoneNumber())) {
            ContentValues personValues = new ContentValues();
            personValues.put(PersonsEntry.COLUMN_NAME, person.getFullname());
            personValues.put(PersonsEntry.COLUMN_PHONE_NO, person.getPhoneNumber());
            personValues.put(PersonsEntry.COLUMN_IMAGE_URI, person.getImageUri());
            db.insert(PersonsEntry.TABLE_NAME, null, personValues);
        }

        ContentValues debtValues = new ContentValues();
        debtValues.put(DebtsEntry.COLUMN_ENTRY_ID, debt.getId());
        debtValues.put(DebtsEntry.COLUMN_AMOUNT, debt.getAmount());
        debtValues.put(DebtsEntry.COLUMN_DATE_DUE, debt.getDueDate());
        debtValues.put(DebtsEntry.COLUMN_DATE_ENTERED, debt.getCreatedDate());
        debtValues.put(DebtsEntry.COLUMN_NOTE, debt.getNote());
        debtValues.put(DebtsEntry.COLUMN_PERSON_PHONE_NUMBER, person.getPhoneNumber());
        debtValues.put(DebtsEntry.COLUMN_STATUS, debt.getStatus());
        debtValues.put(DebtsEntry.COLUMN_TYPE, debt.getDebtType());

        db.insert(DebtsEntry.TABLE_NAME, null, debtValues);
    }

    /**
     * Saves a {@link Payment} into the database. This also updates the debt amount based on action
     * selected on the payment.
     * @param payment the {@link Payment} object to be saved
     */
    @Override
    public void savePayment(@NonNull Payment payment) {
        checkNotNull(payment);
        SQLiteDatabase db = mDebtsDbHelper.getWritableDatabase();
        ContentValues paymentValues = getContentValuesFromPayment(payment);
        db.insert(PaymentsEntry.TABLE_NAME, null, paymentValues);

        // perform debt amount update based on action
        if (payment.getAction() == Payment.PAYMENT_ACTION_DEBT_DECREASE) {
            double newAmount = getDebt(payment.getDebtId()).getAmount() - payment.getAmount();
            updateDebtAmount(payment.getDebtId(), newAmount);
        } else if (payment.getAction() == Payment.PAYMENT_ACTION_DEBT_INCREASE) {
            Debt debt = getDebt(payment.getDebtId());
            double newAmount = debt.getAmount() + payment.getAmount();
            updateDebtAmount(payment.getDebtId(), newAmount);
        }
    }

    /**
     * Updates a debt amount in the database
     * @param debtId the debt id to be updated
     * @param newDebtAmount the new amount for the debt
     */
    private void updateDebtAmount(@NonNull String debtId, double newDebtAmount) {
        SQLiteDatabase db = mDebtsDbHelper.getWritableDatabase();
        ContentValues debtContentValue = new ContentValues();
        debtContentValue.put(DebtsEntry.COLUMN_AMOUNT, newDebtAmount);
        db.update(DebtsEntry.TABLE_NAME, debtContentValue, DebtsEntry.COLUMN_ENTRY_ID + DebtsDbHelper.WHERE_EQUAL_TO,
                new String[]{debtId});
    }

    /**
     * Edits a Payment in the database
     * @param payment the payment object that needs to be saved in the database
     * @param debt the debt object needed to get the initial payment
     */
    @Override
    public void editPayment(@NonNull Payment payment, @NonNull Debt debt) {
        checkNotNull(payment);

        Payment initialPayment = getPayment(payment.getId(), debt);
        double initialPaymentAmount = initialPayment.getAmount();
        double initialDebtAmount = getDebt(payment.getDebtId()).getAmount();

        if (initialPayment.getAction() == Payment.PAYMENT_ACTION_DEBT_DECREASE) {
            double debtAmount = initialPaymentAmount + initialDebtAmount;
            double newDebtAmount = debtAmount - payment.getAmount();
            updateDebtAmount(payment.getDebtId(), newDebtAmount);
        }else if (initialPayment.getAction() == Payment.PAYMENT_ACTION_DEBT_INCREASE) {
            double debtAmount = initialDebtAmount - initialPaymentAmount;
            double newDebtAmount = debtAmount + payment.getAmount();
            updateDebtAmount(payment.getDebtId(), newDebtAmount);
        }

        SQLiteDatabase db = mDebtsDbHelper.getWritableDatabase();
        ContentValues paymentValues = getContentValuesFromPayment(payment);
        db.update(PaymentsEntry.TABLE_NAME, paymentValues, PaymentsEntry.COLUMN_ENTRY_ID + DebtsDbHelper.WHERE_EQUAL_TO,
                new String[]{payment.getId()});
    }

    /**
     * Returns a {@link Debt} object from the database
     * @param debtId the debt id
     * @return a {@link Debt} object
     */
    @Override
    public Debt getDebt(@NonNull String debtId) {
        checkNotNull(debtId);
        SQLiteDatabase db = mDebtsDbHelper.getReadableDatabase();
        Cursor cursor = db.query(DebtsEntry.TABLE_NAME, DebtsEntry.getAllColumns(), DebtsEntry.COLUMN_ENTRY_ID + DebtsDbHelper.WHERE_EQUAL_TO,
                new String[]{debtId}, null, null, null);
        Debt debt = null;

        if (cursor != null && cursor.getCount() > 0) {

            cursor.moveToFirst();

            long dateDue = cursor.getLong(cursor.getColumnIndexOrThrow(DebtsEntry.COLUMN_DATE_DUE));
            String personPhoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(DebtsEntry.COLUMN_PERSON_PHONE_NUMBER));
            int status = cursor.getInt(cursor.getColumnIndexOrThrow(DebtsEntry.COLUMN_STATUS));
            int debtType1 = cursor.getInt(cursor.getColumnIndexOrThrow(DebtsEntry.COLUMN_TYPE));
            String note = cursor.getString(cursor.getColumnIndexOrThrow(DebtsEntry.COLUMN_NOTE));
            double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(DebtsEntry.COLUMN_AMOUNT));
            String entryId = cursor.getString(cursor.getColumnIndexOrThrow(DebtsEntry.COLUMN_ENTRY_ID));
            long dateEntered = cursor.getLong(cursor.getColumnIndexOrThrow(DebtsEntry.COLUMN_DATE_ENTERED));

            debt = new Debt.Builder(entryId, personPhoneNumber, amount, dateEntered, debtType1, status)
                    .dueDate(dateDue)
                    .note(note)
                    .build();

            cursor.close();
        }

        return debt;
     }

    /**
     * Deletes a payment from the database. This also updates the debt amount tied to the payment
     * based on the payment action.
     * @param payment a {@link Payment} object
     */
    @Override
    public void deletePayment(@NonNull Payment payment) {
        SQLiteDatabase db = mDebtsDbHelper.getWritableDatabase();
        db.delete(PaymentsEntry.TABLE_NAME, PaymentsEntry.COLUMN_ENTRY_ID + DebtsDbHelper.WHERE_EQUAL_TO,
                new String[]{payment.getId()});

        // perform debt amount update based on action
        updateDebtAmountBasedOnPaymentAction(payment);
    }

    /**
     * Update a payment in the database based on a Payment action
     * @param payment the {@link Payment} object that is needed
     */
    private void updateDebtAmountBasedOnPaymentAction(@NonNull Payment payment) {
        if (payment.getAction() == Payment.PAYMENT_ACTION_DEBT_DECREASE) {
            double newDebtAmount = getDebt(payment.getDebtId()).getAmount() + payment.getAmount();
            updateDebtAmount(payment.getDebtId(), newDebtAmount);
        } else if (payment.getAction() == Payment.PAYMENT_ACTION_DEBT_INCREASE) {
            double newDebtAmount = getDebt(payment.getDebtId()).getAmount() -  payment.getAmount();
            updateDebtAmount(payment.getDebtId(), newDebtAmount);
        }
    }

    /**
     * Return a {@link Payment} object from the database
     * @param paymentId the payment id required
     * @param debt the debt tied to the payment
     * @return a {@link Payment} object
     */
    @Override
    public Payment getPayment(@NonNull String paymentId, @Nullable Debt debt) {
        checkNotNull(paymentId);
        SQLiteDatabase db = mDebtsDbHelper.getReadableDatabase();
        Payment payment = null;
        Cursor cursor = db.query(PaymentsEntry.TABLE_NAME, PaymentsEntry.getAllColumns(),
                PaymentsEntry.COLUMN_ENTRY_ID + DebtsDbHelper.WHERE_EQUAL_TO,
                new String[]{paymentId}, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            String paymentDebtId = cursor.getString(cursor.getColumnIndexOrThrow(PaymentsEntry.COLUMN_DEBT_ID));
            int action = cursor.getInt(cursor.getColumnIndexOrThrow(PaymentsEntry.COLUMN_ACTION));
            String personPhoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(PaymentsEntry.COLUMN_PERSON_PHONE_NUMBER));
            double paymentAmount = cursor.getDouble(cursor.getColumnIndexOrThrow(PaymentsEntry.COLUMN_AMOUNT));
            long paymentDateEntered = cursor.getLong(cursor.getColumnIndexOrThrow(PaymentsEntry.COLUMN_DATE_ENTERED));
            String paymentNote = cursor.getString(cursor.getColumnIndexOrThrow(PaymentsEntry.COLUMN_NOTE));

            payment = new Payment.Builder()
                    .id(paymentId)
                    .amount(paymentAmount)
                    .debtId(paymentDebtId)
                    .dateEntered(paymentDateEntered)
                    .note(paymentNote)
                    .personPhoneNumber(personPhoneNumber)
                    .action(action).build();
        }

        if (cursor != null) {
            cursor.close();
        }
        return payment;
    }

    /**
     * Creates and returns a ContentValues from a Payment object
     * @param payment the Payment object
     * @return a ContentValues
     */
    @NonNull
    private ContentValues getContentValuesFromPayment(@NonNull Payment payment) {
        ContentValues paymentValues = new ContentValues();
        paymentValues.put(PaymentsEntry.COLUMN_ENTRY_ID, payment.getId());
        paymentValues.put(PaymentsEntry.COLUMN_ACTION, payment.getAction());
        paymentValues.put(PaymentsEntry.COLUMN_AMOUNT, payment.getAmount());
        paymentValues.put(PaymentsEntry.COLUMN_DATE_ENTERED, payment.getDateEntered());
        paymentValues.put(PaymentsEntry.COLUMN_DEBT_ID, payment.getDebtId());
        paymentValues.put(PaymentsEntry.COLUMN_NOTE, payment.getNote());
        paymentValues.put(PaymentsEntry.COLUMN_PERSON_PHONE_NUMBER, payment.getPersonPhoneNo());
        return paymentValues;
    }

    /**
     * Returns a List of Payments made to the debt
     * @param debtId the debt id required
     * @return a List of {@link Payment}s
     */
    @Override
    public List<Payment> getDebtPayments(@NonNull String debtId) {

        List<Payment> payments = new ArrayList<>();
        SQLiteDatabase db = mDebtsDbHelper.getReadableDatabase();

        Cursor cursor = db.query(PaymentsEntry.TABLE_NAME, PaymentsEntry.getAllColumns(),
                PaymentsEntry.COLUMN_DEBT_ID + DebtsDbHelper.WHERE_EQUAL_TO, new String[]{debtId}, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                String id = cursor.getString(cursor.getColumnIndexOrThrow(PaymentsEntry.COLUMN_ENTRY_ID));
                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(PaymentsEntry.COLUMN_AMOUNT));
                int action = cursor.getInt(cursor.getColumnIndexOrThrow(PaymentsEntry.COLUMN_ACTION));
                long dateEntered = cursor.getLong(cursor.getColumnIndexOrThrow(PaymentsEntry.COLUMN_DATE_ENTERED));
                String note = cursor.getString(cursor.getColumnIndexOrThrow(PaymentsEntry.COLUMN_NOTE));
                String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(PaymentsEntry.COLUMN_PERSON_PHONE_NUMBER));

                Payment payment = new Payment.Builder()
                        .action(action)
                        .dateEntered(dateEntered)
                        .amount(amount)
                        .debtId(debtId)
                        .note(note)
                        .personPhoneNumber(phoneNumber)
                        .id(id).build();

                payments.add(payment);
            }
        }
        if (cursor != null) {
            cursor.close();
        }

        if (payments.isEmpty()) {
            // return empty list
            return new ArrayList<>();
        } else {
            return payments;
        }
    }

    /**
     * Deletes all Payments made to the debt
     * @param debtId the debt id
     */
    @Override
    public void deleteAllDebtPayments(@NonNull String debtId) {
        checkNotNull(debtId);
        SQLiteDatabase db = mDebtsDbHelper.getWritableDatabase();
        db.delete(PaymentsEntry.TABLE_NAME, PaymentsEntry.COLUMN_DEBT_ID + DebtsDbHelper.WHERE_EQUAL_TO, new String[]{debtId});
    }

    /**
     * Deletes all Payments from the database
     */
    @Override
    public void deleteAllPayments() {
        SQLiteDatabase db = mDebtsDbHelper.getWritableDatabase();
        db.delete(PaymentsEntry.TABLE_NAME, null, null);
    }

    @Override
    public void refreshDebts() {
        // refresh the debts
    }

    /**
     * Deletes all rows in the persons and debts table
     */
    @Override
    public void deleteAllPersonDebts() {

        SQLiteDatabase db = mDebtsDbHelper.getWritableDatabase();
        db.delete(DebtsEntry.TABLE_NAME, null, null);
        db.delete(PersonsEntry.TABLE_NAME, null, null);
    }

    /**
     * Deletes a debt in the debts table and also a person in the persons table if the person has only
     * a debt. Also deletes the debt payments if there is any.
     * @param personDebt the {@link PersonDebt} object
     */
    @Override
    public void deletePersonDebt(@NonNull PersonDebt personDebt) {
        checkNotNull(personDebt);
        String debtId = personDebt.getDebt().getId();

        SQLiteDatabase db = mDebtsDbHelper.getWritableDatabase();
        db.delete(DebtsEntry.TABLE_NAME, DebtsEntry.COLUMN_ENTRY_ID + DebtsDbHelper.WHERE_EQUAL_TO, new String[]{debtId});

        // delete person if he has only one debt
        String personPhoneNumber = personDebt.getPerson().getPhoneNumber();
        if (personHasNoDebts(personPhoneNumber)) {
            deletePerson(personPhoneNumber);
        }

        if (debtHasPayments(debtId)) {
            deleteAllDebtPayments(debtId);
        }
    }

    /**
     * Batch delete a list of PersonDebts from the local database
     * @param personDebts the List of {@link PersonDebt} objects
     * @param debtType the debt type
     */
    @Override
    public void batchDelete(@NonNull List<PersonDebt> personDebts, @NonNull int debtType) {
        for (PersonDebt personDebt : personDebts) {
            deletePersonDebt(personDebt);
        }
    }

    /**
     * Get {@link Person}s with it's {@link Debt}s from the local database
     * @return a List of {@link Person}
     */
    @Override
    public List<Person> getAllPersonWithDebts() {
        SQLiteDatabase db = mDebtsDbHelper.getReadableDatabase();
        List<Person> persons = new ArrayList<>();
        Cursor cursor = db.query(PersonsEntry.TABLE_NAME, PersonsEntry.getAllColumns(), null, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {

            while (cursor.moveToNext()) {
                String personName = cursor.getString(cursor.getColumnIndexOrThrow(PersonsEntry.COLUMN_NAME));
                String personPhoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(PersonsEntry.COLUMN_PHONE_NO));
                String personImageUri = cursor.getString(cursor.getColumnIndexOrThrow(PersonsEntry.COLUMN_IMAGE_URI));

                Person person = new Person(personName, personPhoneNumber, personImageUri);

                person.setDebts(getPersonDebts(personPhoneNumber));
                persons.add(person);

            }
        }

        if (cursor != null) {
            cursor.close();
        }

        if (persons.isEmpty()) {
            return new ArrayList<>();
        } else {
            return persons;
        }
    }

    /**
     * Get a Person {@link Debt}s
     * @param personPhoneNumber the person phone number
     * @return a List of {@link Debt}
     */
    @Override
    public List<Debt> getPersonDebts(@NonNull String personPhoneNumber) {
        checkNotNull(personPhoneNumber);

        SQLiteDatabase db = mDebtsDbHelper.getReadableDatabase();
        List<Debt> debts = new ArrayList<>();
        Cursor cursor = db.query(DebtsEntry.TABLE_NAME, DebtsEntry.getAllColumns(),
                DebtsEntry.COLUMN_PERSON_PHONE_NUMBER + DebtsDbHelper.WHERE_EQUAL_TO, new String[]{personPhoneNumber}, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {

            while (cursor.moveToNext()) {

                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(DebtsEntry.COLUMN_AMOUNT));
                long dateDue = cursor.getLong(cursor.getColumnIndexOrThrow(DebtsEntry.COLUMN_DATE_DUE));
                long dateEntered = cursor.getLong(cursor.getColumnIndexOrThrow(DebtsEntry.COLUMN_DATE_ENTERED));
                String note = cursor.getString(cursor.getColumnIndexOrThrow(DebtsEntry.COLUMN_NOTE));
                int status = cursor.getInt(cursor.getColumnIndexOrThrow(DebtsEntry.COLUMN_STATUS));
                int debtType1 = cursor.getInt(cursor.getColumnIndexOrThrow(DebtsEntry.COLUMN_TYPE));
                String entryId = cursor.getString(cursor.getColumnIndexOrThrow(DebtsEntry.COLUMN_ENTRY_ID));

                Debt debt = new Debt.Builder(entryId, personPhoneNumber, amount, dateEntered, debtType1, status)
                        .dueDate(dateDue)
                        .note(note)
                        .build();

                debts.add(debt);

            }
        }
        if (cursor != null) {
            cursor.close();
        }

        if (debts.isEmpty()) {
            return new ArrayList<>();
        } else {
            return debts;
        }
    }

    /**
     * Deletes a person from the persons table
     * @param personPhoneNumber the person phone number
     */
    private void deletePerson(@NonNull String personPhoneNumber) {
        checkNotNull(personPhoneNumber);
        SQLiteDatabase db = mDebtsDbHelper.getWritableDatabase();
        db.delete(PersonsEntry.TABLE_NAME, PersonsEntry.COLUMN_PHONE_NO + DebtsDbHelper.WHERE_EQUAL_TO,
                new String[]{personPhoneNumber});
    }

    /**
     * Checks if a person has no debts
     * @param personPhoneNumber the person phone number
     * @return either true or false
     */
    private boolean personHasNoDebts(String personPhoneNumber) {
        checkNotNull(personPhoneNumber);
        SQLiteDatabase db = mDebtsDbHelper.getReadableDatabase();

        Cursor cursor = db.query(DebtsEntry.TABLE_NAME, DebtsEntry.getAllColumns(),
                DebtsEntry.COLUMN_PERSON_PHONE_NUMBER + DebtsDbHelper.WHERE_EQUAL_TO, new String[]{personPhoneNumber}, null, null, null);

        cursor.moveToFirst();

        if (cursor.getCount() == 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    /**
     * Checks if debt has payments
     * @param debtId the debt id
     * @return either true or false
     */
    private boolean debtHasPayments(String debtId) {
        checkNotNull(debtId);
        SQLiteDatabase db = mDebtsDbHelper.getWritableDatabase();

        Cursor cursor = db.query(PaymentsEntry.TABLE_NAME, PaymentsEntry.getAllColumns(),
                PaymentsEntry.COLUMN_DEBT_ID + DebtsDbHelper.WHERE_EQUAL_TO, new String[]{debtId}, null, null, null);

        cursor.moveToFirst();

        if (cursor.getCount() >= 1) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    /**
     * Delete all Debts from the database by type
     * @param debtType the debt type
     */
    @Override
    public void deleteAllPersonDebtsByType(@NonNull int debtType) {
        checkNotNull(debtType);
        SQLiteDatabase db = mDebtsDbHelper.getWritableDatabase();
        db.delete(DebtsEntry.TABLE_NAME, DebtsEntry.COLUMN_TYPE + DebtsDbHelper.WHERE_EQUAL_TO, new String[]{String.valueOf(debtType)});
    }

    /**
     * Updates a Person in the persons table and also the Debt in the debts table
     * @param personDebt the {@link PersonDebt} object
     */
    @Override
    public void updatePersonDebt(@NonNull PersonDebt personDebt) {
        checkNotNull(personDebt);
        SQLiteDatabase db = mDebtsDbHelper.getWritableDatabase();

        Debt debt = personDebt.getDebt();
        Person person = personDebt.getPerson();

        if (personAlreadyExist(person.getPhoneNumber())) {
            ContentValues personContentValues = new ContentValues();
            personContentValues.put(PersonsEntry.COLUMN_PHONE_NO, person.getPhoneNumber());
            personContentValues.put(PersonsEntry.COLUMN_NAME, person.getFullname());
            db.update(PersonsEntry.TABLE_NAME, personContentValues, PersonsEntry.COLUMN_PHONE_NO +
                    DebtsDbHelper.WHERE_EQUAL_TO, new String[]{person.getPhoneNumber()});
        } else {
            saveNewPerson(person);
        }

        ContentValues debtContentValues = new ContentValues();
        debtContentValues.put(DebtsEntry.COLUMN_DATE_ENTERED, debt.getCreatedDate());
        debtContentValues.put(DebtsEntry.COLUMN_PERSON_PHONE_NUMBER, person.getPhoneNumber());
        debtContentValues.put(DebtsEntry.COLUMN_DATE_DUE, debt.getDueDate());
        debtContentValues.put(DebtsEntry.COLUMN_NOTE, debt.getNote());
        debtContentValues.put(DebtsEntry.COLUMN_AMOUNT, debt.getAmount());
        debtContentValues.put(DebtsEntry.COLUMN_TYPE, debt.getDebtType());
        debtContentValues.put(DebtsEntry.COLUMN_STATUS, debt.getStatus());

        db.update(DebtsEntry.TABLE_NAME, debtContentValues, DebtsEntry.COLUMN_ENTRY_ID +
                DebtsDbHelper.WHERE_EQUAL_TO, new String[]{debt.getId()});
    }

    /**
     * Savaes a {@link Person} object into the database
     * @param person the Person object
     */
    private void saveNewPerson(@NonNull Person person) {
        checkNotNull(person);
        SQLiteDatabase db = mDebtsDbHelper.getWritableDatabase();
        ContentValues personValues = new ContentValues();
        personValues.put(PersonsEntry.COLUMN_NAME, person.getFullname());
        personValues.put(PersonsEntry.COLUMN_PHONE_NO, person.getPhoneNumber());
        personValues.put(PersonsEntry.COLUMN_IMAGE_URI, person.getImageUri());
        db.insert(PersonsEntry.TABLE_NAME, null, personValues);
    }

    /**
     * Get a {@link Person} object from the database using the id
     * @param personPhoneNumber the person id
     * @return a {@link Person} object
     */
    @Override
    public Person getPerson(@NonNull String personPhoneNumber) {
        checkNotNull(personPhoneNumber);
        SQLiteDatabase db = mDebtsDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + PersonsEntry.TABLE_NAME + DebtsDbHelper.WHERE +
                PersonsEntry.COLUMN_PHONE_NO + DebtsDbHelper.WHERE_EQUAL_TO, new String[]{personPhoneNumber});
        Person person = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            String fullName = cursor.getString(cursor.getColumnIndexOrThrow(PersonsEntry.COLUMN_NAME));
            String phoneNo = cursor.getString(cursor.getColumnIndexOrThrow(PersonsEntry.COLUMN_PHONE_NO));
            String personImageUri = cursor.getString(cursor.getColumnIndexOrThrow(PersonsEntry.COLUMN_IMAGE_URI));
            person = new Person(fullName, phoneNo, personImageUri);
        }

        if (cursor != null) {
            cursor.close();
        }
        return person;
    }

    /**
     * Checks if a Person already exist
     * @param phoneNumber the person phone number
     * @return either true or false if a Person exists
     */
    private boolean personAlreadyExist(String phoneNumber) {

        if (!StringUtil.isEmpty(phoneNumber)) {
            SQLiteDatabase db = mDebtsDbHelper.getWritableDatabase();

            Cursor cursor = db.rawQuery("SELECT " + PersonsEntry.COLUMN_PHONE_NO + " FROM " + PersonsEntry.TABLE_NAME +
                    DebtsDbHelper.WHERE + PersonsEntry.COLUMN_PHONE_NO + DebtsDbHelper.WHERE_EQUAL_TO, new String[]{String.valueOf(phoneNumber)});
            if (cursor.moveToFirst()) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }
}
