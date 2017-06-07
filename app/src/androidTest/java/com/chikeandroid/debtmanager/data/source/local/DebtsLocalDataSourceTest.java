package com.chikeandroid.debtmanager.data.source.local;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.chikeandroid.debtmanager.data.Debt;
import com.chikeandroid.debtmanager.data.Payment;
import com.chikeandroid.debtmanager.data.Person;
import com.chikeandroid.debtmanager.data.PersonDebt;
import com.chikeandroid.debtmanager.util.TestUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static com.chikeandroid.debtmanager.util.TestUtil.AMOUNT1;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

/**
 * Created by Chike on 4/17/2017.
 * Integration test for {@link com.chikeandroid.debtmanager.data.source.PersonDebtsDataSource}, which uses {@link com.chikeandroid.debtmanager.data.source.local.DebtsDbHelper}.
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
        mDebtsLocalDataSource.deleteAllDebtPayments(TestUtil.DEBT_ID);
        mDebtsLocalDataSource.deleteAllPayments();
    }

    @Test
    public void testPreConditions() {
        assertNotNull(mDebtsLocalDataSource);
    }

    @Test
    public void shouldBeAbleToSavePersonDebtAndThenRetrieveIt() {

        // Owed Debts
        Person person = TestUtil.createAndGetPerson();
        Debt debt = TestUtil.createAndGetOwedDebt(person.getPhoneNumber());
        mDebtsLocalDataSource.savePersonDebt(debt, person);

        PersonDebt personDebt1 = new PersonDebt(person, debt);
        PersonDebt personDebt2 = mDebtsLocalDataSource.getPersonDebt(debt.getId(), Debt.DEBT_TYPE_OWED);

        assertThat(personDebt1, is(personDebt2));

        // I Owe debts
        Person person2 = TestUtil.createAndGetPerson2();
        Debt debt2 = TestUtil.createAndGetIOweDebt(person2.getPhoneNumber());
        mDebtsLocalDataSource.savePersonDebt(debt2, person2);
        assertThat(new PersonDebt(person2, debt2), is(mDebtsLocalDataSource.getPersonDebt(debt2.getId(), Debt.DEBT_TYPE_IOWE)));
    }

    @Test
    public void shouldBeAbleToGetOwedPersonDebtsSaved() {

        // Owed Debts
        Person person1 = TestUtil.createAndGetPerson();
        Debt debt1 = TestUtil.createAndGetOwedDebt(person1.getPhoneNumber());
        PersonDebt personDebt1 = new PersonDebt(person1, debt1);
        mDebtsLocalDataSource.savePersonDebt(debt1, person1);

        Person person2 = TestUtil.createAndGetPerson2();
        Debt debt2 = TestUtil.createDebt(person2.getPhoneNumber(), AMOUNT1, Debt.DEBT_TYPE_OWED,
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
        Debt debt1 = TestUtil.createAndGetIOweDebt(person1.getPhoneNumber());
        PersonDebt personDebt1 = new PersonDebt(person1, debt1);
        mDebtsLocalDataSource.savePersonDebt(debt1, person1);

        Person person2 = TestUtil.createAndGetPerson2();
        Debt debt2 = TestUtil.createDebt(person2.getPhoneNumber(), AMOUNT1, Debt.DEBT_TYPE_IOWE,
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
        Debt debt1 = TestUtil.createAndGetOwedDebt(person1.getPhoneNumber());
        mDebtsLocalDataSource.savePersonDebt(debt1, person1);

        Person person2 = TestUtil.createAndGetPerson2();
        Debt debt2 = TestUtil.createDebt(person2.getPhoneNumber(), 80000, Debt.DEBT_TYPE_OWED,
                Debt.DEBT_STATUS_ACTIVE, "flexing money");
        mDebtsLocalDataSource.savePersonDebt(debt2, person2);

        mDebtsLocalDataSource.deleteAllPersonDebtsByType(Debt.DEBT_TYPE_OWED);

        List<PersonDebt> personDebts = mDebtsLocalDataSource.getAllPersonDebtsByType(Debt.DEBT_TYPE_OWED);
        assertEquals(personDebts.size(), 0);
    }

    @Test
    public void shouldBeAbleToDeleteAllIOwePersonDebts() {

        Person person1 = TestUtil.createAndGetPerson();
        Debt debt1 = TestUtil.createAndGetIOweDebt(person1.getPhoneNumber());
        mDebtsLocalDataSource.savePersonDebt(debt1, person1);

        Person person2 = TestUtil.createAndGetPerson2();
        Debt debt2 = TestUtil.createDebt(person2.getPhoneNumber(), 80000, Debt.DEBT_TYPE_IOWE,
                Debt.DEBT_STATUS_ACTIVE, "flexing money");
        mDebtsLocalDataSource.savePersonDebt(debt2, person2);

        mDebtsLocalDataSource.deleteAllPersonDebtsByType(Debt.DEBT_TYPE_IOWE);

        List<PersonDebt> personDebts = mDebtsLocalDataSource.getAllPersonDebtsByType(Debt.DEBT_TYPE_IOWE);
        assertEquals(personDebts.size(), 0);
    }

    @Test
    public void shouldBeAbleToDeleteOwedDebtAndPersonIfPersonHasOneDebtOnly() {

        Person person1 = TestUtil.createAndGetPerson();
        Debt debt1 = TestUtil.createAndGetOwedDebt(person1.getPhoneNumber());
        mDebtsLocalDataSource.savePersonDebt(debt1, person1);
        PersonDebt personDebt = new PersonDebt(person1, debt1);
        mDebtsLocalDataSource.deletePersonDebt(personDebt);

        // ensure debt was deleted
        assertNull(mDebtsLocalDataSource.getPersonDebt(debt1.getId(), Debt.DEBT_TYPE_OWED));

        // ensure person was deleted
        Person person = mDebtsLocalDataSource.getPerson(person1.getPhoneNumber());
        assertNull(person);
    }

    @Test
    public void shouldBeAbleToDeleteIOweDebtAndPersonIfPersonHasOneDebtOnly() {

        Person person1 = TestUtil.createAndGetPerson();
        Debt debt1 = TestUtil.createAndGetIOweDebt(person1.getPhoneNumber());
        mDebtsLocalDataSource.savePersonDebt(debt1, person1);
        PersonDebt personDebt = new PersonDebt(person1, debt1);
        mDebtsLocalDataSource.deletePersonDebt(personDebt);

        // ensure debt was deleted
        assertNull(mDebtsLocalDataSource.getPersonDebt(debt1.getId(), Debt.DEBT_TYPE_IOWE));

        // ensure person was deleted
        Person person = mDebtsLocalDataSource.getPerson(person1.getPhoneNumber());
        assertNull(person);
    }

    @Test
    public void shouldBeAbleToDeleteOwedDebtButNotPersonIfPersonHasMoreThanOneDebt() {

        Person person1 = TestUtil.createAndGetPerson();
        Debt debt1 = TestUtil.createAndGetOwedDebt(person1.getPhoneNumber());
        mDebtsLocalDataSource.savePersonDebt(debt1, person1);
        PersonDebt personDebt1 = new PersonDebt(person1, debt1);

        // will just save debt and not user since user with phone number is already in the db
        Person person2 = TestUtil.createPerson(person1.getFullname(), person1.getPhoneNumber());
        Debt debt2 = TestUtil.createDebt(person1.getPhoneNumber(), 600000, Debt.DEBT_TYPE_OWED,
                Debt.DEBT_STATUS_ACTIVE, "computer money");
        mDebtsLocalDataSource.savePersonDebt(debt2, person2);

        // will delete debt and not person because person has more than one debt
        mDebtsLocalDataSource.deletePersonDebt(personDebt1);

        // assert that debt was deleted
        assertNull(mDebtsLocalDataSource.getPersonDebt(debt1.getId(), Debt.DEBT_TYPE_OWED));

        // assert that person still exist
        Person person = mDebtsLocalDataSource.getPerson(person1.getPhoneNumber());
        assertNotNull(person);

        assertThat(person1, is(person));
    }

    @Test
    public void shouldBeAbleToDeleteIOweDebtButNotPersonIfPersonHasMoreThanOneDebt() {

        Person person1 = TestUtil.createAndGetPerson();
        Debt debt1 = TestUtil.createAndGetIOweDebt(person1.getPhoneNumber());
        mDebtsLocalDataSource.savePersonDebt(debt1, person1);
        PersonDebt personDebt1 = new PersonDebt(person1, debt1);

        // will just save debt and not user since user with phone number is already in the db
        Person person2 = TestUtil.createPerson(person1.getFullname(), person1.getPhoneNumber());
        Debt debt2 = TestUtil.createDebt(person1.getPhoneNumber(), 600000, Debt.DEBT_TYPE_IOWE,
                Debt.DEBT_STATUS_ACTIVE, "computer money");
        mDebtsLocalDataSource.savePersonDebt(debt2, person2);

        // will delete debt and not person because person has more than one debt
        mDebtsLocalDataSource.deletePersonDebt(personDebt1);

        // assert that debt was deleted
        assertNull(mDebtsLocalDataSource.getPersonDebt(debt1.getId(), Debt.DEBT_TYPE_IOWE));

        // assert that person still exist
        Person person = mDebtsLocalDataSource.getPerson(person1.getPhoneNumber());
        assertNotNull(person);

        assertThat(person1, is(person));
    }

    @Test
    public void shouldBeAbleToDeleteAllOwedDebtsByType() {

        Person person1 = TestUtil.createAndGetPerson();
        Debt debt1 = TestUtil.createAndGetOwedDebt(person1.getPhoneNumber());
        mDebtsLocalDataSource.savePersonDebt(debt1, person1);

        Person person2 = TestUtil.createAndGetPerson2();
        Debt debt2 = TestUtil.createDebt(person2.getPhoneNumber(), 445444, Debt.DEBT_TYPE_OWED,
                Debt.DEBT_STATUS_ACTIVE, "Shoe money");
        mDebtsLocalDataSource.savePersonDebt(debt2, person2);

        mDebtsLocalDataSource.deleteAllPersonDebtsByType(Debt.DEBT_TYPE_OWED);

        List<PersonDebt> personDebts = mDebtsLocalDataSource.getAllPersonDebtsByType(Debt.DEBT_TYPE_OWED);
        assertEquals(personDebts.size(), 0);
    }

    @Test
    public void shouldBeAbleToDeleteAllIOweDebtsByType() {

        Person person1 = TestUtil.createAndGetPerson();
        Debt debt1 = TestUtil.createAndGetIOweDebt(person1.getPhoneNumber());
        mDebtsLocalDataSource.savePersonDebt(debt1, person1);

        Person person2 = TestUtil.createPerson("James Mark", "080254785965");
        Debt debt2 = TestUtil.createDebt(person2.getPhoneNumber(), 445444, Debt.DEBT_TYPE_IOWE,
                Debt.DEBT_STATUS_ACTIVE, "Shoe money");
        mDebtsLocalDataSource.savePersonDebt(debt2, person2);

        mDebtsLocalDataSource.deleteAllPersonDebtsByType(Debt.DEBT_TYPE_IOWE);

        List<PersonDebt> personDebts = mDebtsLocalDataSource.getAllPersonDebtsByType(Debt.DEBT_TYPE_IOWE);
        assertEquals(personDebts.size(), 0);
    }

    @Test
    public void shouldBeAbleToUpdatePersonDebtWithOutPhoneNumber() {

        Person person1 = TestUtil.createAndGetPerson();
        Debt debt1 = TestUtil.createAndGetOwedDebt(person1.getPhoneNumber());
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
        Debt debt1 = TestUtil.createAndGetOwedDebt(person1.getPhoneNumber());
        mDebtsLocalDataSource.savePersonDebt(debt1, person1);

        PersonDebt personDebt = mDebtsLocalDataSource.getPersonDebt(debt1.getId(), Debt.DEBT_TYPE_OWED);

        PersonDebt updatedPersonDebt = new PersonDebt(person1, debt1);
        updatedPersonDebt.getPerson().setFullname("Emeka Onu");
        // changed phone number, so new Person is created
        updatedPersonDebt.getPerson().setPhoneNumber("4190");

        mDebtsLocalDataSource.updatePersonDebt(personDebt);

        // it should return a new Person
        PersonDebt personDebt1 = mDebtsLocalDataSource.getPersonDebt(debt1.getId(), Debt.DEBT_TYPE_OWED);

        Person newPerson = mDebtsLocalDataSource.getPerson(personDebt1.getPerson().getPhoneNumber());

        assertNotNull(newPerson);

        // means that a new person was created
        assertThat(updatedPersonDebt.getPerson(), is(not(newPerson)));
    }

    @Test
    public void shouldBeAbleToGetAllPersonWithDebts() {

        // Owed Debts
        Person person1 = TestUtil.createAndGetPerson();
        Debt debt1 = TestUtil.createAndGetOwedDebt(person1.getPhoneNumber());
        mDebtsLocalDataSource.savePersonDebt(debt1, person1);

        Person person2 = TestUtil.createAndGetPerson2();
        Debt debt2 = TestUtil.createDebt(person2.getPhoneNumber(), AMOUNT1, Debt.DEBT_TYPE_OWED,
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
            if (person.getPhoneNumber().equals(person1.getPhoneNumber())) {
                person1Found = true;
            }
            if (person.getPhoneNumber().equals(person2.getPhoneNumber())) {
                person2Found = true;
            }
        }
        assertTrue(person1Found);
        assertTrue(person2Found);
    }

    @Test
    public void shouldBeAbleToGetAllDebtsFromPerson() {

        Person person1 = TestUtil.createAndGetPerson();
        Debt debt1 = TestUtil.createAndGetOwedDebt(person1.getPhoneNumber());
        mDebtsLocalDataSource.savePersonDebt(debt1, person1);

        Debt debt2 = TestUtil.createDebt(person1.getPhoneNumber(), 50000, Debt.DEBT_TYPE_OWED,
                Debt.DEBT_STATUS_ACTIVE, "Trouser money");
        mDebtsLocalDataSource.savePersonDebt(debt2, person1);

        Debt debt3 = TestUtil.createDebt(person1.getPhoneNumber(), 60000, Debt.DEBT_TYPE_OWED,
                Debt.DEBT_STATUS_ACTIVE, "Food money");
        mDebtsLocalDataSource.savePersonDebt(debt3, person1);

        Debt debt4 = TestUtil.createDebt(person1.getPhoneNumber(), 30000, Debt.DEBT_TYPE_OWED,
                Debt.DEBT_STATUS_ACTIVE, "Flexing money");
        mDebtsLocalDataSource.savePersonDebt(debt4, person1);

        List<Debt> debts = mDebtsLocalDataSource.getPersonDebts(person1.getPhoneNumber());
        assertTrue(debts.size() == 4);

        boolean debt1found = false;
        boolean debt2found = false;
        boolean debt3found = false;
        boolean debt4found = false;

        for (Debt debt : debts) {
            if (debt.getId().equals(debt1.getId())) {
                debt1found = true;
            }
            if (debt.getId().equals(debt2.getId())) {
                debt2found = true;
            }
            if (debt.getId().equals(debt3.getId())) {
                debt3found = true;
            }
            if (debt.getId().equals(debt4.getId())) {
                debt4found = true;
            }
        }
        assertTrue(debt1found);
        assertTrue(debt2found);
        assertTrue(debt3found);
        assertTrue(debt4found);
    }

    @Test
    public void shouldBeAbleToSaveDebtPaymentsAndThenRetrievePayments() {

        Person person = TestUtil.createAndGetPerson();
        Debt debt = TestUtil.createAndGetOwedDebt(person.getPhoneNumber());
        mDebtsLocalDataSource.savePersonDebt(debt, person);

        Payment payment1 = TestUtil.createAndGetADebtPayment1(debt.getId());
        mDebtsLocalDataSource.savePayment(payment1);

        Payment payment2 = new Payment.Builder()
                .note("payment note 2")
                .debtId(debt.getId())
                .id("666666")
                .amount(1000)
                .dateEntered(System.currentTimeMillis())
                .action(Payment.PAYMENT_ACTION_DEBT_INCREASE)
                .personPhoneNumber("070381415344")
                .build();
        mDebtsLocalDataSource.savePayment(payment2);

        List<Payment> payments = mDebtsLocalDataSource.getDebtPayments(debt.getId());
        assertTrue(payments.size() == 2);

        boolean payment1found = false;
        boolean payment2found = false;

        for (Payment payment : payments) {
            if (payment.equals(payment1)) {
                payment1found = true;
            }
            if (payment.equals(payment2)) {
                payment2found = true;
            }
        }
        assertTrue(payment1found);
        assertTrue(payment2found);
    }

    @Test
    public void shouldBeAbleToSaveDebtPayment() {

        Person person = TestUtil.createAndGetPerson();
        Debt debt = TestUtil.createAndGetOwedDebt(person.getPhoneNumber());
        mDebtsLocalDataSource.savePersonDebt(debt, person);

        Payment payment = new Payment.Builder()
                .note("payment note")
                .debtId(debt.getId())
                .id("666666")
                .amount(5656565)
                .dateEntered(System.currentTimeMillis())
                .action(Payment.PAYMENT_ACTION_DEBT_INCREASE)
                .personPhoneNumber("072381115344")
                .build();
        mDebtsLocalDataSource.savePayment(payment);

        Payment retrievedPayment = mDebtsLocalDataSource.getPayment(payment.getId(), debt);
        assertThat(payment, is(retrievedPayment));

        // verify if debt amount increased
        Debt newDebt = mDebtsLocalDataSource.getDebt(debt.getId());
        double newDebtAmount = newDebt.getAmount();
        assertTrue(Math.abs(newDebtAmount - (debt.getAmount() + payment.getAmount())) < .0000001);
    }

    @Test
    public void shouldBeAbleToRetrievePayment() {

        Person person = TestUtil.createAndGetPerson();
        Debt debt = TestUtil.createAndGetOwedDebt(person.getPhoneNumber());
        mDebtsLocalDataSource.savePersonDebt(debt, person);

        Payment payment = TestUtil.createAndGetADebtPayment1(debt.getId());
        mDebtsLocalDataSource.savePayment(payment);

        Payment retrievedPayment = mDebtsLocalDataSource.getPayment(payment.getId(), debt);
        assertThat(payment, is(retrievedPayment));
    }

    @Test
    public void shouldBeAbleToEditPayment() {

        Person person = TestUtil.createAndGetPerson();
        Debt debt = TestUtil.createAndGetOwedDebt(person.getPhoneNumber());
        mDebtsLocalDataSource.savePersonDebt(debt, person);

        Payment payment = TestUtil.createAndGetADebtPayment1(debt.getId());
        mDebtsLocalDataSource.savePayment(payment);

        Payment updatedPayment = new Payment.Builder()
                .id(payment.getId())
                .action(Payment.PAYMENT_ACTION_DEBT_INCREASE)
                .amount(400)
                .dateEntered(System.currentTimeMillis())
                .debtId(debt.getId())
                .note("updated note")
                .personPhoneNumber("070681115344")
                .build();

        mDebtsLocalDataSource.editPayment(updatedPayment, debt);

        Payment retrievedEditedPayment = mDebtsLocalDataSource.getPayment(updatedPayment.getId(), debt);

        assertTrue(payment.getId().equals(retrievedEditedPayment.getId()));
        assertNotSame(payment, retrievedEditedPayment);

        // verify if debt amount increased
        Debt newDebt = mDebtsLocalDataSource.getDebt(debt.getId());
        double newDebtAmount = newDebt.getAmount();
        assertTrue(Math.abs(newDebtAmount - (debt.getAmount() + updatedPayment.getAmount())) < .0000001);
    }

    @Test
    public void shouldBeAbleToDeletePayment() {

        Person person = TestUtil.createAndGetPerson();
        Debt debt = TestUtil.createAndGetOwedDebt(person.getPhoneNumber());
        mDebtsLocalDataSource.savePersonDebt(debt, person);

        Payment payment = new Payment.Builder()
                .note("payment note")
                .debtId(debt.getId())
                .id("666")
                .amount(1000)
                .dateEntered(System.currentTimeMillis())
                .action(Payment.PAYMENT_ACTION_DEBT_INCREASE)
                .personPhoneNumber("070381215344")
                .build();
        mDebtsLocalDataSource.savePayment(payment);

        mDebtsLocalDataSource.deletePayment(payment);

        Payment retrievedPayment = mDebtsLocalDataSource.getPayment(payment.getId(), debt);
        assertNull(retrievedPayment);
        // revert debt amount back
        double updatedDebtAmount = mDebtsLocalDataSource.getDebt(debt.getId()).getAmount();
        assertTrue(updatedDebtAmount == debt.getAmount());
    }

    @Test
    public void shouldBeAbleToDeletePersonDebtAndAlsoPaymentsIfAnyExist() {

        Person person = TestUtil.createAndGetPerson();
        Debt debt = TestUtil.createAndGetOwedDebt(person.getPhoneNumber());
        PersonDebt personDebt = new PersonDebt(person, debt);
        mDebtsLocalDataSource.savePersonDebt(debt, person);
        Payment payment1 = TestUtil.createAndGetADebtPayment1(debt.getId());
        Payment payment2 = TestUtil.createAndGetADebtPayment2(debt.getId());

        mDebtsLocalDataSource.savePayment(payment1);
        mDebtsLocalDataSource.savePayment(payment2);

        mDebtsLocalDataSource.deletePersonDebt(personDebt);

        // check if payments were deleted
        assertNull(mDebtsLocalDataSource.getPayment(payment1.getId(), debt));
        assertNull(mDebtsLocalDataSource.getPayment(payment2.getId(), debt));
    }
}
