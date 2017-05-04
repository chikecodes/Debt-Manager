package com.chikeandroid.debtmanager20.util;

import com.chikeandroid.debtmanager20.data.Debt;
import com.chikeandroid.debtmanager20.data.Person;

import java.util.UUID;

/**
 * Created by Chike on 4/28/2017.
 */

public final class TestUtil {

    public static final String NAME1 = "chike mgbemena";
    public static final String PHONE_NUMBER1 = "070381115344";
    public static final String NOTE = "computer money";
    public static final double AMOUNT = 6000.545;

    private TestUtil() {
    }

    public static Debt createDebt(String personId, double amount, int debtType, int debtStatus, String note) {

        Debt debt = new Debt.Builder(UUID.randomUUID().toString(), personId, amount,
                System.currentTimeMillis(), debtType, debtStatus)
                .dueDate(System.currentTimeMillis())
                .note(note)
                .build();

        return debt;
    }

    public static Person createPerson(String name, String phoneNumber) {

        return new Person(UUID.randomUUID().toString(), name, phoneNumber);
    }

    public static Person createAndGetPerson() {
        return createPerson(NAME1, PHONE_NUMBER1);
    }

    public static Debt createAndGetOwedDebt(String personId) {
        return createDebt(personId, AMOUNT, Debt.DEBT_TYPE_OWED, Debt.DEBT_STATUS_ACTIVE, NOTE);
    }

    public static Debt createAndGetIOweDebt(String personId) {
        return createDebt(personId, AMOUNT, Debt.DEBT_TYPE_IOWE, Debt.DEBT_STATUS_ACTIVE, NOTE);
    }

}
