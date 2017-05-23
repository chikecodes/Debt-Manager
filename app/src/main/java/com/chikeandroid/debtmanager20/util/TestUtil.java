package com.chikeandroid.debtmanager20.util;

import com.chikeandroid.debtmanager20.data.Debt;
import com.chikeandroid.debtmanager20.data.Person;

import java.util.UUID;

/**
 * Created by Chike on 4/28/2017.
 */

public final class TestUtil {

    public static final String NAME1 = "chike mgbemena";
    public static final String NAME2 = "Mary Jane";
    public static final String NAME3 = "Chuka Smith";
    public static final String PHONE_NUMBER1 = "070381115344";
    public static final String PHONE_NUMBER2 = "07038666534";
    public static final String PHONE_NUMBER3 = "10245784";
    public static final String NOTE1 = "computer money";
    public static final String NOTE2 = "note 2";
    public static final String NOTE3 = "comment 4543";
    public static final double AMOUNT1 = 6000.545;
    public static final double AMOUNT2 = 70000;
    public static final double AMOUNT3 = 9000;

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

        return new Person(UUID.randomUUID().toString(), name, phoneNumber, "");
    }

    public static Person createAndGetPerson2() {
        return createPerson(NAME2, PHONE_NUMBER2);
    }

    public static Person createAndGetPerson() {
        return createPerson(NAME1, PHONE_NUMBER1);
    }

    public static Debt createAndGetOwedDebt(String personId) {
        return createDebt(personId, AMOUNT1, Debt.DEBT_TYPE_OWED, Debt.DEBT_STATUS_ACTIVE, NOTE1);
    }

    public static Debt createAndGetIOweDebt(String personId) {
        return createDebt(personId, AMOUNT1, Debt.DEBT_TYPE_IOWE, Debt.DEBT_STATUS_ACTIVE, NOTE1);
    }

}
