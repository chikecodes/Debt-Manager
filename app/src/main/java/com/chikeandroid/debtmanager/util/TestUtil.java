package com.chikeandroid.debtmanager.util;

import com.chikeandroid.debtmanager.data.Debt;
import com.chikeandroid.debtmanager.data.Payment;
import com.chikeandroid.debtmanager.data.Person;

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
    public static final double AMOUNT1 = 6000;
    public static final double AMOUNT2 = 70000;
    public static final double AMOUNT3 = 9000;

    public final static int CREATED_YEAR = 2017;
    public final static int CREATED_MONTH = 10;
    public final static int CREATED_DAY_OF_MONTH = 10;

    public final static int DUE_YEAR = 2017;
    public final static int DUE_MONTH = 12;
    public final static int DUE_DAY_OF_MONTH = 15;

    public final static String DEBT_ID = "12334556766";

    private TestUtil() {
    }

    public static Debt createDebt(String personPhoneNumber, double amount, int debtType, int debtStatus, String note) {

        return new Debt.Builder(UUID.randomUUID().toString(), personPhoneNumber, amount,
                System.currentTimeMillis(), debtType, debtStatus)
                .dueDate(System.currentTimeMillis())
                .note(note)
                .build();
    }

    public static Person createPerson(String name, String phoneNumber) {

        return new Person(name, phoneNumber, "");
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

    public static Payment createAndGetADebtPayment1(String debtId) {

      return new Payment.Builder()
                .note("payment note 1")
                .debtId(debtId)
                .id("666666")
                .amount(1000)
                .dateEntered(System.currentTimeMillis())
                .personPhoneNumber("070381115347")
                .action(Payment.PAYMENT_ACTION_DEBT_INCREASE)
                .build();
    }

    public static Payment createAndGetADebtPayment2(String debtId) {

      return new Payment.Builder()
                .note("payment note 2")
                .debtId(debtId)
                .id("6464647")
                .amount(90000)
                .personPhoneNumber("070381115347")
                .dateEntered(System.currentTimeMillis())
                .action(Payment.PAYMENT_ACTION_DEBT_INCREASE)
                .build();
    }

    public static Payment createAndGetADebtPayment3(String debtId) {

      return new Payment.Builder()
                .note("payment note 3")
                .debtId(debtId)
                .id("9484333")
                .amount(600040)
                .dateEntered(System.currentTimeMillis())
                .personPhoneNumber("070381115347")
                .action(Payment.PAYMENT_ACTION_DEBT_INCREASE)
                .build();
    }

}
