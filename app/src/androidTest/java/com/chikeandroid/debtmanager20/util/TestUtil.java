package com.chikeandroid.debtmanager20.util;

import com.chikeandroid.debtmanager20.data.Debt;
import com.chikeandroid.debtmanager20.data.Person;

import java.util.UUID;

/**
 * Created by Chike on 4/25/2017.
 */

public class TestUtil {

    public static Debt createDebt(String personId, double amount, int debtType, int debtStatus, String note) {

        Debt debt = new Debt.Builder(UUID.randomUUID().toString(), personId, amount,
                System.currentTimeMillis(), debtType,
                debtStatus)
                .dueDate(System.currentTimeMillis())
                .note(note)
                .build();

        return debt;
    }

    public static Person createPerson(String name, String phoneNumber) {

        return new Person(UUID.randomUUID().toString(), name, phoneNumber);
    }

}
