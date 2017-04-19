package com.chikeandroid.debtmanager20.data;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.chikeandroid.debtmanager20.data.source.DebtsDataSource;
import com.chikeandroid.debtmanager20.data.source.local.DebtsDbHelper;
import com.chikeandroid.debtmanager20.data.source.local.DebtsLocalDataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.UUID;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by Chike on 4/17/2017.
 * Integration test for the {@link DebtsDataSource}, which uses the {@link DebtsDbHelper}.
 */
@RunWith(AndroidJUnit4.class)
public class DebtsLocalDataSourceTest {

    private DebtsLocalDataSource mDebtsLocalDataSource;

    @Before
    public void setUp() {
        mDebtsLocalDataSource = new DebtsLocalDataSource(InstrumentationRegistry.getTargetContext());
    }

    @After
    public void cleanUp() {
        mDebtsLocalDataSource.deleteAllDebts();
    }

    @Test
    public void testPreConditions() {
        assertNotNull(mDebtsLocalDataSource);
    }

    @Test
    public void shouldBeAbleToSaveDebtAndThenRetrieveIt() {

        Person person = new Person(UUID.randomUUID().toString(), "Chike Mgbemena", "07038111534");
        Debt debt = new Debt.Builder(UUID.randomUUID().toString(), person.getId(), 5000.87,
                System.currentTimeMillis(), Debt.DEBT_TYPE_OWED,
                Debt.DEBT_STATUS_PARTIAL)
                .dueDate(System.currentTimeMillis())
                .note("school fees")
                .build();
        mDebtsLocalDataSource.saveDebt(debt, person);
        PersonDebt personDebt1 = new PersonDebt(person, debt);

        PersonDebt personDebt2 = mDebtsLocalDataSource.getDebt(debt.getId());

        assertThat(personDebt1, is(personDebt2));
    }

    @Test
    public void shouldBeAbleToGetAllDebtsSaved() {

        Person person1 = new Person(UUID.randomUUID().toString(), "Chike Mgbemena", "07038111534");
        Debt debt1 = new Debt.Builder(UUID.randomUUID().toString(), person1.getId(), 5000.87,
                System.currentTimeMillis(), Debt.DEBT_TYPE_OWED,
                Debt.DEBT_STATUS_PARTIAL)
                .dueDate(System.currentTimeMillis())
                .note("school fees")
                .build();
        PersonDebt personDebt1 = new PersonDebt(person1, debt1);
        mDebtsLocalDataSource.saveDebt(debt1, person1);

        Person person2 = new Person(UUID.randomUUID().toString(), "Mary Jane", "080145236987");
        Debt debt2 = new Debt.Builder(UUID.randomUUID().toString(), person2.getId(), 474514.154,
                System.currentTimeMillis(), Debt.DEBT_TYPE_i_OWE,
                Debt.DEBT_STATUS_PARTIAL)
                .dueDate(System.currentTimeMillis())
                .note("food money")
                .build();

        PersonDebt personDebt2 = new PersonDebt(person2, debt2);
        mDebtsLocalDataSource.saveDebt(debt2, person2);

        List<PersonDebt> personDebts = mDebtsLocalDataSource.getAllDebts();
        assertNotNull(personDebts);
        assertTrue(personDebts.size() >= 2);

        boolean personDebt1Found = false;
        boolean personDebt2Found = false;
        for (PersonDebt personDebt : personDebts) {
            if (personDebt.getDebt().getId().equals(personDebt1.getDebt().getId())) {
                personDebt1Found = true;
            }
            if (personDebt.getDebt().getId().equals(personDebt2.getDebt().getId())) {

                personDebt2Found = true;
            }
        }
        assertTrue(personDebt1Found);
        assertTrue(personDebt2Found);
    }

    @Test
    public void shouldBeAbleToDeleteAllDebts() {

        Person person1 = new Person(UUID.randomUUID().toString(), "Chike Mgbemena", "07038111534");
        Debt debt1 = new Debt.Builder(UUID.randomUUID().toString(), person1.getId(), 5000.87,
                System.currentTimeMillis(), Debt.DEBT_TYPE_OWED,
                Debt.DEBT_STATUS_PARTIAL)
                .dueDate(System.currentTimeMillis())
                .note("school fees")
                .build();
        mDebtsLocalDataSource.saveDebt(debt1, person1);

        mDebtsLocalDataSource.deleteAllDebts();

        List<PersonDebt> personDebts = mDebtsLocalDataSource.getAllDebts();
        assertEquals(personDebts.size(), 0);
    }

    @Test
    public void shouldBeAbleToDeleteDebt() {

        Person person1 = new Person(UUID.randomUUID().toString(), "Chike Mgbemena", "07038111534");
        Debt debt1 = new Debt.Builder(UUID.randomUUID().toString(), person1.getId(), 5000.87,
                System.currentTimeMillis(), Debt.DEBT_TYPE_OWED,
                Debt.DEBT_STATUS_PARTIAL)
                .dueDate(System.currentTimeMillis())
                .note("school fees")
                .build();
        mDebtsLocalDataSource.saveDebt(debt1, person1);

        mDebtsLocalDataSource.deleteDebt(debt1.getId());

        assertNull(mDebtsLocalDataSource.getDebt(debt1.getId()));
    }

    @Test
    public void shouldBeAbleToGetAllDebtsByType() {

        Person person1 = new Person(UUID.randomUUID().toString(), "Chike Mgbemena", "07038111534");
        Debt debt1 = new Debt.Builder(UUID.randomUUID().toString(), person1.getId(), 5000.87,
                System.currentTimeMillis(), Debt.DEBT_TYPE_OWED,
                Debt.DEBT_STATUS_PARTIAL)
                .dueDate(System.currentTimeMillis())
                .note("school fees")
                .build();

        PersonDebt personDebt1 = new PersonDebt(person1, debt1);
        mDebtsLocalDataSource.saveDebt(debt1, person1);

        Person person2 = new Person(UUID.randomUUID().toString(), "Mary Jane", "05014578496");
        Debt debt2 = new Debt.Builder(UUID.randomUUID().toString(), person2.getId(), 784574.5698,
                System.currentTimeMillis(), Debt.DEBT_TYPE_OWED,
                Debt.DEBT_STATUS_PARTIAL)
                .dueDate(System.currentTimeMillis())
                .note("hair salon")
                .build();

        PersonDebt personDebt2 = new PersonDebt(person2, debt2);
        mDebtsLocalDataSource.saveDebt(debt2, person2);

        List<PersonDebt> personDebts = mDebtsLocalDataSource.getAllDebtsByType(Debt.DEBT_TYPE_OWED);
        assertNotNull(personDebts);
        assertTrue(personDebts.size() > 0);

        boolean personDebt1Found = false;
        boolean personDebt2Found = false;
        for (PersonDebt personDebt : personDebts) {
            if (personDebt.getDebt().getId().equals(personDebt1.getDebt().getId())) {
                personDebt1Found = true;
            }
            if (personDebt.getDebt().getId().equals(personDebt2.getDebt().getId())) {
                personDebt2Found = true;
            }
        }
        assertTrue(personDebt1Found);
        assertTrue(personDebt2Found);
    }

    @Test
    public void shouldBeAbleToDeleteAllDebtsByType() {

        Person person1 = new Person(UUID.randomUUID().toString(), "Chike Mgbemena", "07038111534");
        Debt debt1 = new Debt.Builder(UUID.randomUUID().toString(), person1.getId(), 5000.87,
                System.currentTimeMillis(), Debt.DEBT_TYPE_i_OWE,
                Debt.DEBT_STATUS_PARTIAL)
                .dueDate(System.currentTimeMillis())
                .note("school fees")
                .build();
        mDebtsLocalDataSource.saveDebt(debt1, person1);

        Person person2 = new Person(UUID.randomUUID().toString(), "Mary Jane", "05014578496");
        Debt debt2 = new Debt.Builder(UUID.randomUUID().toString(), person2.getId(), 784574.5698,
                System.currentTimeMillis(), Debt.DEBT_TYPE_i_OWE,
                Debt.DEBT_STATUS_PARTIAL)
                .dueDate(System.currentTimeMillis())
                .note("hair salon")
                .build();
        mDebtsLocalDataSource.saveDebt(debt2, person2);

        mDebtsLocalDataSource.deleteAllDebtsByType(Debt.DEBT_TYPE_i_OWE);

        List<PersonDebt> personDebts = mDebtsLocalDataSource.getAllDebtsByType(Debt.DEBT_TYPE_i_OWE);
        assertEquals(personDebts.size(), 0);
    }
}
