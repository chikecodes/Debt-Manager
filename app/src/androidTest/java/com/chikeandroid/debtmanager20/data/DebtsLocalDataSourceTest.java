package com.chikeandroid.debtmanager20.data;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.chikeandroid.debtmanager20.data.source.DebtsDataSource;
import com.chikeandroid.debtmanager20.data.source.local.DebtsDbHelper;
import com.chikeandroid.debtmanager20.data.source.local.DebtsLocalDataSource;
import com.chikeandroid.debtmanager20.util.TestUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
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
        mDebtsLocalDataSource.deleteAllPersonDebts();
    }

    @Test
    public void testPreConditions() {
        assertNotNull(mDebtsLocalDataSource);
    }

    @Test
    public void shouldBeAbleToSaveDebtAndThenRetrieveIt() {

        Person person = TestUtil.createPerson("Chike Mgbemena", "07038111534");
        Debt debt = TestUtil.createDebt(person.getId(), 5000.34, Debt.DEBT_TYPE_OWED,
                Debt.DEBT_STATUS_ACTIVE, "food money");

        mDebtsLocalDataSource.savePersonDebt(debt, person);
        PersonDebt personDebt1 = new PersonDebt(person, debt);

        PersonDebt personDebt2 = mDebtsLocalDataSource.getPersonDebt(debt.getId());
        assertThat(personDebt1, is(personDebt2));
    }

    @Test
    public void shouldBeAbleToGetAllPersonDebtsSaved() {

        Person person1 = TestUtil.createPerson("Chike Mgbemena", "07038111534");
        Debt debt1 = TestUtil.createDebt(person1.getId(), 564744, Debt.DEBT_TYPE_OWED,
                Debt.DEBT_STATUS_ACTIVE, "textbook money");
        PersonDebt personDebt1 = new PersonDebt(person1, debt1);
        mDebtsLocalDataSource.savePersonDebt(debt1, person1);

        Person person2 = TestUtil.createPerson("Mary Jane", "080145236987");
        Debt debt2 = TestUtil.createDebt(person2.getId(), 445444, Debt.DEBT_TYPE_i_OWE,
                Debt.DEBT_STATUS_ACTIVE, "Hair money");
        PersonDebt personDebt2 = new PersonDebt(person2, debt2);
        mDebtsLocalDataSource.savePersonDebt(debt2, person2);

        List<PersonDebt> personDebts = mDebtsLocalDataSource.getAllPersonDebts();
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
    public void shouldBeAbleToDeleteAllPersonDebts() {

        Person person1 = TestUtil.createPerson("Chike Mgbemena", "07038111534");
        Debt debt1 = TestUtil.createDebt(person1.getId(), 564744, Debt.DEBT_TYPE_OWED,
                Debt.DEBT_STATUS_ACTIVE, "textbook money");
        mDebtsLocalDataSource.savePersonDebt(debt1, person1);

        Person person2 = TestUtil.createPerson("Mary Jane", "080145236987");
        Debt debt2 = TestUtil.createDebt(person2.getId(), 445444, Debt.DEBT_TYPE_i_OWE,
                Debt.DEBT_STATUS_ACTIVE, "Hair money");
        mDebtsLocalDataSource.savePersonDebt(debt2, person2);

        mDebtsLocalDataSource.deleteAllPersonDebts();

        List<PersonDebt> personDebts = mDebtsLocalDataSource.getAllPersonDebts();
        assertEquals(personDebts.size(), 0);
    }

    @Test
    public void shouldBeAbleToDeleteDebtAndPersonIfPersonHasOneDebtOnly() {

        Person person1 = TestUtil.createPerson("Chike Mgbemena", "07038111534");
        Debt debt1 = TestUtil.createDebt(person1.getId(), 564744, Debt.DEBT_TYPE_OWED,
                Debt.DEBT_STATUS_ACTIVE, "textbook money");
        mDebtsLocalDataSource.savePersonDebt(debt1, person1);
        PersonDebt personDebt = new PersonDebt(person1, debt1);
        mDebtsLocalDataSource.deletePersonDebt(personDebt);

        // ensure debt was deleted
        assertNull(mDebtsLocalDataSource.getPersonDebt(debt1.getId()));

        // ensure person was deleted
        Person person = mDebtsLocalDataSource.getPerson(person1.getId());
        assertNull(person);
    }

    @Test
    public void shouldBeAbleToDeleteDebtButNotPersonIfPersonHasMoreThanOneDebt() {

        Person person1 = TestUtil.createPerson("Chike Mgbemena", "07038111534");
        Debt debt1 = TestUtil.createDebt(person1.getId(), 564744, Debt.DEBT_TYPE_OWED,
                Debt.DEBT_STATUS_ACTIVE, "textbook money");
        mDebtsLocalDataSource.savePersonDebt(debt1, person1);
        PersonDebt personDebt1 = new PersonDebt(person1, debt1);

        // will just save debt and not user since user with phone number is already in the db
        Person person2 = TestUtil.createPerson("Chike Mgbemena", "07038111534");
        Debt debt2 = TestUtil.createDebt(person1.getId(), 600000, Debt.DEBT_TYPE_OWED,
                Debt.DEBT_STATUS_ACTIVE, "computer money");
        mDebtsLocalDataSource.savePersonDebt(debt2, person2);

        // will delete debt and not person because person has more than one debt
        mDebtsLocalDataSource.deletePersonDebt(personDebt1);

        // assert that debt was deleted
        assertNull(mDebtsLocalDataSource.getPersonDebt(debt1.getId()));

        // assert that person still exist
        Person person = mDebtsLocalDataSource.getPerson(person1.getId());
        assertNotNull(person);

        assertThat(person1, is(person));
    }

    @Test
    public void shouldBeAbleToGetAllDebtsByType() {

        Person person1 = TestUtil.createPerson("Chike Mgbemena", "07038111534");
        Debt debt1 = TestUtil.createDebt(person1.getId(), 564744, Debt.DEBT_TYPE_OWED,
                Debt.DEBT_STATUS_ACTIVE, "textbook money");
        PersonDebt personDebt1 = new PersonDebt(person1, debt1);
        mDebtsLocalDataSource.savePersonDebt(debt1, person1);

        Person person2 = TestUtil.createPerson("Mary Jane", "080145236987");
        Debt debt2 = TestUtil.createDebt(person2.getId(), 445444, Debt.DEBT_TYPE_OWED,
                Debt.DEBT_STATUS_ACTIVE, "Hair money");
        PersonDebt personDebt2 = new PersonDebt(person2, debt2);
        mDebtsLocalDataSource.savePersonDebt(debt2, person2);

        List<PersonDebt> personDebts = mDebtsLocalDataSource.getAllPersonDebtsByType(Debt.DEBT_TYPE_OWED);
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

        Person person1 = TestUtil.createPerson("Chike Mgbemena", "07038111534");
        Debt debt1 = TestUtil.createDebt(person1.getId(), 564744, Debt.DEBT_TYPE_i_OWE,
                Debt.DEBT_STATUS_ACTIVE, "textbook money");
        mDebtsLocalDataSource.savePersonDebt(debt1, person1);

        Person person2 = TestUtil.createPerson("Mary Jane", "080145236987");
        Debt debt2 = TestUtil.createDebt(person2.getId(), 445444, Debt.DEBT_TYPE_i_OWE,
                Debt.DEBT_STATUS_ACTIVE, "Hair money");
        mDebtsLocalDataSource.savePersonDebt(debt2, person2);

        mDebtsLocalDataSource.deleteAllPersonDebtsByType(Debt.DEBT_TYPE_i_OWE);

        List<PersonDebt> personDebts = mDebtsLocalDataSource.getAllPersonDebtsByType(Debt.DEBT_TYPE_i_OWE);
        assertEquals(personDebts.size(), 0);
    }

    @Test
    public void shouldBeAbleToUpdateDebtWithOutPhoneNumber() {

        Person person1 = TestUtil.createPerson("Chike Mgbemena", "07038111534");
        Debt debt1 = TestUtil.createDebt(person1.getId(), 564744, Debt.DEBT_TYPE_i_OWE,
                Debt.DEBT_STATUS_ACTIVE, "textbook money");
        mDebtsLocalDataSource.savePersonDebt(debt1, person1);

        PersonDebt personDebt = mDebtsLocalDataSource.getPersonDebt(debt1.getId());

        // a new phone number will create a new Person
        personDebt.getPerson().setFullname("Emeka Onu");
        personDebt.getDebt().setAmount(500);
        personDebt.getDebt().setNote("meat money");

        mDebtsLocalDataSource.updatePersonDebt(personDebt);

        PersonDebt personDebt1 = mDebtsLocalDataSource.getPersonDebt(debt1.getId());

        // ensure Person did not change
        assertThat(personDebt, is(personDebt1));
    }

    @Test
    public void shouldCreateNewUserOnUpdateWithPhoneNumberIfNotAlreadyInDatabase() {

        Person person1 = TestUtil.createPerson("Chike Mgbemena", "07038111534");
        Debt debt1 = TestUtil.createDebt(person1.getId(), 564744, Debt.DEBT_TYPE_i_OWE,
                Debt.DEBT_STATUS_ACTIVE, "textbook money");
        mDebtsLocalDataSource.savePersonDebt(debt1, person1);

        PersonDebt personDebt = mDebtsLocalDataSource.getPersonDebt(debt1.getId());

        personDebt.getPerson().setFullname("Emeka Onu");
        // changed phone number, so new Person is created
        personDebt.getPerson().setPhoneNumber("4190");

        mDebtsLocalDataSource.updatePersonDebt(personDebt);

        // it should create a new Person
        PersonDebt personDebt1 = mDebtsLocalDataSource.getPersonDebt(debt1.getId());

        Person newPerson = mDebtsLocalDataSource.getPerson(personDebt1.getPerson().getId());

        assertNotNull(newPerson);

        // means that a new person was created
        assertThat(personDebt.getPerson(), is(not(newPerson)));
    }
}
