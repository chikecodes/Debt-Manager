package com.chikeandroid.debtmanager20.data;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.chikeandroid.debtmanager20.data.source.local.PersonDebtsLocalDataSource;
import com.chikeandroid.debtmanager20.util.TestUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static com.chikeandroid.debtmanager20.util.TestUtil.AMOUNT;
import static com.chikeandroid.debtmanager20.util.TestUtil.NAME1;
import static com.chikeandroid.debtmanager20.util.TestUtil.PHONE_NUMBER1;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

/**
 * Created by Chike on 4/17/2017.
 * Integration test for {@link com.chikeandroid.debtmanager20.data.source.PersonDebtsDataSource}, which uses {@link com.chikeandroid.debtmanager20.data.source.local.DebtsDbHelper}.
 */
@RunWith(AndroidJUnit4.class)
public class DebtsLocalDataSourceTest {

    private PersonDebtsLocalDataSource mDebtsLocalDataSource;

    @Before
    public void setUp() {
        mDebtsLocalDataSource = new PersonDebtsLocalDataSource(InstrumentationRegistry.getTargetContext());
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

        // Owed Debts
        Person person = TestUtil.createAndGetPerson();
        Debt debt = TestUtil.createAndGetOwedDebt(person.getId());
        mDebtsLocalDataSource.savePersonDebt(debt, person);
        assertThat(new PersonDebt(person, debt), is(mDebtsLocalDataSource.getPersonDebt(debt.getId(), Debt.DEBT_TYPE_OWED)));

        // I Owe debts
        Person person2 = TestUtil.createPerson("Mary Jane", "07038666534");
        Debt debt2 = TestUtil.createAndGetIOweDebt(person2.getId());
        mDebtsLocalDataSource.savePersonDebt(debt2, person2);
        assertThat(new PersonDebt(person2, debt2), is(mDebtsLocalDataSource.getPersonDebt(debt2.getId(), Debt.DEBT_TYPE_IOWE)));
    }

    @Test
    public void shouldBeAbleToGetOwedPersonDebtsSaved() {

        // Owed Debts
        Person person1 = TestUtil.createAndGetPerson();
        Debt debt1 = TestUtil.createAndGetOwedDebt(person1.getId());
        PersonDebt personDebt1 = new PersonDebt(person1, debt1);
        mDebtsLocalDataSource.savePersonDebt(debt1, person1);

        Person person2 = TestUtil.createPerson("Mary Jane", "08012345447");
        Debt debt2 = TestUtil.createDebt(person2.getId(), AMOUNT, Debt.DEBT_TYPE_OWED,
                Debt.DEBT_STATUS_ACTIVE, "Shirt money");
        PersonDebt personDebt2 = new PersonDebt(person2, debt2);
        mDebtsLocalDataSource.savePersonDebt(debt2, person2);

        List<PersonDebt> personDebts = mDebtsLocalDataSource.getAllPersonDebtsByType(Debt.DEBT_TYPE_OWED);
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
    public void shouldBeAbleToGetIOwePersonDebtsSaved() {

        // I Owe Debts
        Person person1 = TestUtil.createAndGetPerson();
        Debt debt1 = TestUtil.createAndGetIOweDebt(person1.getId());
        PersonDebt personDebt1 = new PersonDebt(person1, debt1);
        mDebtsLocalDataSource.savePersonDebt(debt1, person1);

        Person person2 = TestUtil.createPerson("Mary Jane", "08012345447");
        Debt debt2 = TestUtil.createDebt(person2.getId(), AMOUNT, Debt.DEBT_TYPE_IOWE,
                Debt.DEBT_STATUS_ACTIVE, "Shirt money");
        PersonDebt personDebt2 = new PersonDebt(person2, debt2);
        mDebtsLocalDataSource.savePersonDebt(debt2, person2);

        List<PersonDebt> personDebts = mDebtsLocalDataSource.getAllPersonDebtsByType(Debt.DEBT_TYPE_IOWE);
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
    public void shouldBeAbleToDeleteAllOwedPersonDebts() {

        Person person1 = TestUtil.createAndGetPerson();
        Debt debt1 = TestUtil.createAndGetOwedDebt(person1.getId());
        mDebtsLocalDataSource.savePersonDebt(debt1, person1);

        Person person2 = TestUtil.createPerson("Buhari Mohammed", "02035647854");
        Debt debt2 = TestUtil.createDebt(person2.getId(), 80000, Debt.DEBT_TYPE_OWED,
                Debt.DEBT_STATUS_ACTIVE, "flexing money");
        mDebtsLocalDataSource.savePersonDebt(debt2, person2);

        mDebtsLocalDataSource.deleteAllPersonDebtsByType(Debt.DEBT_TYPE_OWED);

        List<PersonDebt> personDebts = mDebtsLocalDataSource.getAllPersonDebtsByType(Debt.DEBT_TYPE_OWED);
        assertEquals(personDebts.size(), 0);
    }

    @Test
    public void shouldBeAbleToDeleteAllIOwePersonDebts() {

        Person person1 = TestUtil.createAndGetPerson();
        Debt debt1 = TestUtil.createAndGetIOweDebt(person1.getId());
        mDebtsLocalDataSource.savePersonDebt(debt1, person1);

        Person person2 = TestUtil.createPerson("Buhari Mohammed", "02035647854");
        Debt debt2 = TestUtil.createDebt(person2.getId(), 80000, Debt.DEBT_TYPE_IOWE,
                Debt.DEBT_STATUS_ACTIVE, "flexing money");
        mDebtsLocalDataSource.savePersonDebt(debt2, person2);

        mDebtsLocalDataSource.deleteAllPersonDebtsByType(Debt.DEBT_TYPE_IOWE);

        List<PersonDebt> personDebts = mDebtsLocalDataSource.getAllPersonDebtsByType(Debt.DEBT_TYPE_IOWE);
        assertEquals(personDebts.size(), 0);
    }

    @Test
    public void shouldBeAbleToDeleteOwedDebtAndPersonIfPersonHasOneDebtOnly() {

        Person person1 = TestUtil.createAndGetPerson();
        Debt debt1 = TestUtil.createAndGetOwedDebt(person1.getId());
        mDebtsLocalDataSource.savePersonDebt(debt1, person1);
        PersonDebt personDebt = new PersonDebt(person1, debt1);
        mDebtsLocalDataSource.deletePersonDebt(personDebt);

        // ensure debt was deleted
        assertNull(mDebtsLocalDataSource.getPersonDebt(debt1.getId(), Debt.DEBT_TYPE_OWED));

        // ensure person was deleted
        Person person = mDebtsLocalDataSource.getPerson(person1.getId());
        assertNull(person);
    }

    @Test
    public void shouldBeAbleToDeleteIOweDebtAndPersonIfPersonHasOneDebtOnly() {

        Person person1 = TestUtil.createAndGetPerson();
        Debt debt1 = TestUtil.createAndGetIOweDebt(person1.getId());
        mDebtsLocalDataSource.savePersonDebt(debt1, person1);
        PersonDebt personDebt = new PersonDebt(person1, debt1);
        mDebtsLocalDataSource.deletePersonDebt(personDebt);

        // ensure debt was deleted
        assertNull(mDebtsLocalDataSource.getPersonDebt(debt1.getId(), Debt.DEBT_TYPE_IOWE));

        // ensure person was deleted
        Person person = mDebtsLocalDataSource.getPerson(person1.getId());
        assertNull(person);
    }

    @Test
    public void shouldBeAbleToDeleteOwedDebtButNotPersonIfPersonHasMoreThanOneDebt() {

        Person person1 = TestUtil.createAndGetPerson();
        Debt debt1 = TestUtil.createAndGetOwedDebt(person1.getId());
        mDebtsLocalDataSource.savePersonDebt(debt1, person1);
        PersonDebt personDebt1 = new PersonDebt(person1, debt1);

        // will just save debt and not user since user with phone number is already in the db
        Person person2 = TestUtil.createPerson(NAME1, PHONE_NUMBER1);
        Debt debt2 = TestUtil.createDebt(person1.getId(), 600000, Debt.DEBT_TYPE_OWED,
                Debt.DEBT_STATUS_ACTIVE, "computer money");
        mDebtsLocalDataSource.savePersonDebt(debt2, person2);

        // will delete debt and not person because person has more than one debt
        mDebtsLocalDataSource.deletePersonDebt(personDebt1);

        // assert that debt was deleted
        assertNull(mDebtsLocalDataSource.getPersonDebt(debt1.getId(), Debt.DEBT_TYPE_OWED));

        // assert that person still exist
        Person person = mDebtsLocalDataSource.getPerson(person1.getId());
        assertNotNull(person);

        assertThat(person1, is(person));
    }

    @Test
    public void shouldBeAbleToDeleteIOweDebtButNotPersonIfPersonHasMoreThanOneDebt() {

        Person person1 = TestUtil.createAndGetPerson();
        Debt debt1 = TestUtil.createAndGetIOweDebt(person1.getId());
        mDebtsLocalDataSource.savePersonDebt(debt1, person1);
        PersonDebt personDebt1 = new PersonDebt(person1, debt1);

        // will just save debt and not user since user with phone number is already in the db
        Person person2 = TestUtil.createPerson(NAME1, PHONE_NUMBER1);
        Debt debt2 = TestUtil.createDebt(person1.getId(), 600000, Debt.DEBT_TYPE_IOWE,
                Debt.DEBT_STATUS_ACTIVE, "computer money");
        mDebtsLocalDataSource.savePersonDebt(debt2, person2);

        // will delete debt and not person because person has more than one debt
        mDebtsLocalDataSource.deletePersonDebt(personDebt1);

        // assert that debt was deleted
        assertNull(mDebtsLocalDataSource.getPersonDebt(debt1.getId(), Debt.DEBT_TYPE_IOWE));

        // assert that person still exist
        Person person = mDebtsLocalDataSource.getPerson(person1.getId());
        assertNotNull(person);

        assertThat(person1, is(person));
    }

    @Test
    public void shouldBeAbleToDeleteAllOwedDebtsByType() {

        Person person1 = TestUtil.createAndGetPerson();
        Debt debt1 = TestUtil.createAndGetOwedDebt(person1.getId());
        mDebtsLocalDataSource.savePersonDebt(debt1, person1);

        Person person2 = TestUtil.createPerson("James Mark", "080254785965");
        Debt debt2 = TestUtil.createDebt(person2.getId(), 445444, Debt.DEBT_TYPE_OWED,
                Debt.DEBT_STATUS_ACTIVE, "Shoe money");
        mDebtsLocalDataSource.savePersonDebt(debt2, person2);

        mDebtsLocalDataSource.deleteAllPersonDebtsByType(Debt.DEBT_TYPE_OWED);

        List<PersonDebt> personDebts = mDebtsLocalDataSource.getAllPersonDebtsByType(Debt.DEBT_TYPE_OWED);
        assertEquals(personDebts.size(), 0);
    }

    @Test
    public void shouldBeAbleToDeleteAllIOweDebtsByType() {

        Person person1 = TestUtil.createAndGetPerson();
        Debt debt1 = TestUtil.createAndGetIOweDebt(person1.getId());
        mDebtsLocalDataSource.savePersonDebt(debt1, person1);

        Person person2 = TestUtil.createPerson("James Mark", "080254785965");
        Debt debt2 = TestUtil.createDebt(person2.getId(), 445444, Debt.DEBT_TYPE_IOWE,
                Debt.DEBT_STATUS_ACTIVE, "Shoe money");
        mDebtsLocalDataSource.savePersonDebt(debt2, person2);

        mDebtsLocalDataSource.deleteAllPersonDebtsByType(Debt.DEBT_TYPE_IOWE);

        List<PersonDebt> personDebts = mDebtsLocalDataSource.getAllPersonDebtsByType(Debt.DEBT_TYPE_IOWE);
        assertEquals(personDebts.size(), 0);
    }


    @Test
    public void shouldBeAbleToUpdatePersonDebtWithOutPhoneNumber() {

        Person person1 = TestUtil.createAndGetPerson();
        Debt debt1 = TestUtil.createAndGetOwedDebt(person1.getId());
        mDebtsLocalDataSource.savePersonDebt(debt1, person1);

        PersonDebt personDebt = mDebtsLocalDataSource.getPersonDebt(debt1.getId(), Debt.DEBT_TYPE_OWED);

        // a new phone number will create a new Person
        personDebt.getPerson().setFullname("Emeka Onu");
        personDebt.getDebt().setAmount(500);
        personDebt.getDebt().setNote("meat money");

        mDebtsLocalDataSource.updatePersonDebt(personDebt);

        PersonDebt personDebt1 = mDebtsLocalDataSource.getPersonDebt(debt1.getId(), Debt.DEBT_TYPE_OWED);

        // ensure Person did not change
        assertThat(personDebt, is(personDebt1));
    }

    @Test
    public void shouldCreateNewUserOnUpdateWithPhoneNumberIfNotAlreadyInDatabase() {

        Person person1 = TestUtil.createAndGetPerson();
        Debt debt1 = TestUtil.createAndGetOwedDebt(person1.getId());
        mDebtsLocalDataSource.savePersonDebt(debt1, person1);

        PersonDebt personDebt = mDebtsLocalDataSource.getPersonDebt(debt1.getId(), Debt.DEBT_TYPE_OWED);

        personDebt.getPerson().setFullname("Emeka Onu");
        // changed phone number, so new Person is created
        personDebt.getPerson().setPhoneNumber("4190");

        mDebtsLocalDataSource.updatePersonDebt(personDebt);

        // it should create a new Person
        PersonDebt personDebt1 = mDebtsLocalDataSource.getPersonDebt(debt1.getId(), Debt.DEBT_TYPE_OWED);

        Person newPerson = mDebtsLocalDataSource.getPerson(personDebt1.getPerson().getId());

        assertNotNull(newPerson);

        // means that a new person was created
        assertThat(personDebt.getPerson(), is(not(newPerson)));
    }

    @Test
    public void shouldBeAbleToGetAllPersonWithDebts() {

        // Owed Debts
        Person person1 = TestUtil.createAndGetPerson();
        Debt debt1 = TestUtil.createAndGetOwedDebt(person1.getId());
        mDebtsLocalDataSource.savePersonDebt(debt1, person1);

        Person person2 = TestUtil.createPerson("Mary Jane", "08012345447");
        Debt debt2 = TestUtil.createDebt(person2.getId(), AMOUNT, Debt.DEBT_TYPE_OWED,
                Debt.DEBT_STATUS_ACTIVE, "Shirt money");
        mDebtsLocalDataSource.savePersonDebt(debt2, person2);

        List<Person> persons = mDebtsLocalDataSource.getAllPersonWithDebts();
        assertNotNull(persons);
        assertTrue(persons.size() >= 2);
        assertTrue(persons.get(0).getDebts().size() == 1);
        assertTrue(persons.get(1).getDebts().size() == 1);

        boolean person1Found = false;
        boolean person2Found = false;
        for (Person person : persons) {
            if (person.getId().equals(person1.getId())) {
                person1Found = true;
            }
            if (person.getId().equals(person2.getId())) {
                person2Found = true;
            }
        }
        assertTrue(person1Found);
        assertTrue(person2Found);

    }
}
