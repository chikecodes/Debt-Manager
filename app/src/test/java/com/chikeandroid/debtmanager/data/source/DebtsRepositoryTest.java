package com.chikeandroid.debtmanager.data.source;

import com.chikeandroid.debtmanager.data.Debt;
import com.chikeandroid.debtmanager.data.Payment;
import com.chikeandroid.debtmanager.data.Person;
import com.chikeandroid.debtmanager.data.PersonDebt;
import com.chikeandroid.debtmanager.util.TestUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by Chike on 4/5/2017.
 * Unit tests for the implementation of the in-memory repository with cache.
 */
public class DebtsRepositoryTest {

    private PersonDebtsRepository mDebtsRepository;

    @Mock
    private PersonDebtsDataSource mDebtsLocalDataSource;

    @Before
    public void setUp() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mDebtsRepository = new PersonDebtsRepository(mDebtsLocalDataSource);
    }

    @Test
    public void shouldBeAbleToSaveOwedDebtToLocalDataSource() {

        // save debt owed
        Person person1 = TestUtil.createAndGetPerson();
        Debt debt1 = TestUtil.createAndGetOwedDebt(person1.getPhoneNumber());
        mDebtsRepository.savePersonDebt(debt1, person1);

        verify(mDebtsLocalDataSource).savePersonDebt(eq(debt1), eq(person1));
        assertThat(mDebtsRepository.getCacheOweMePersonDebts().size(), is(1));
    }

    @Test
    public void shouldBeAbleToSaveIOweDebtToLocalDataSource() {
        // save i owe debt
        Person person2 = TestUtil.createPerson("Nkeiru Ineh", "070414741");
        Debt debt2 = TestUtil.createDebt(person2.getPhoneNumber(), 60000000, Debt.DEBT_TYPE_IOWE,
                Debt.DEBT_STATUS_ACTIVE, "");
        mDebtsRepository.savePersonDebt(debt2, person2);

        verify(mDebtsLocalDataSource).savePersonDebt(eq(debt2), eq(person2));
        assertThat(mDebtsRepository.getCacheIOwePersonDebts().size(), is(1));
    }

    @Test
    public void shouldBeAbleGetAllDebsFromLocalDatabaseByType() {

        mDebtsRepository.getAllPersonDebtsByType(Debt.DEBT_TYPE_IOWE);
        verify(mDebtsLocalDataSource).getAllPersonDebtsByType(eq(Debt.DEBT_TYPE_IOWE));

        mDebtsRepository.getAllPersonDebtsByType(Debt.DEBT_TYPE_OWED);
        verify(mDebtsLocalDataSource).getAllPersonDebtsByType(eq(Debt.DEBT_TYPE_OWED));
    }

    @Test
    public void shouldBeAbleToReturnNullWhenLocalDataSourceUnavailable() {

        setOweMeDebtsNotAvailable(mDebtsLocalDataSource);

        List<PersonDebt> returnedDebts = mDebtsRepository.getAllPersonDebtsByType(Debt.DEBT_TYPE_OWED);

        assertNull(returnedDebts);
    }

    private void setOweMeDebtsNotAvailable(PersonDebtsDataSource dataSource) {

        // owe me debts
        when(dataSource.getAllPersonDebtsByType(Debt.DEBT_TYPE_OWED)).thenReturn(null);
    }

    @Test
    public void shouldBeAbleToDeleteAllOwedDebts() {

        Person person1 = TestUtil.createAndGetPerson();
        Debt debt1 = TestUtil.createAndGetOwedDebt(person1.getPhoneNumber());
        mDebtsRepository.savePersonDebt(debt1, person1);
        verify(mDebtsLocalDataSource).savePersonDebt(eq(debt1), eq(person1));

        Person person2 = TestUtil.createPerson("Emeka Onu", "07045124589");
        Debt debt2 = TestUtil.createDebt(person2.getPhoneNumber(), 400, Debt.DEBT_TYPE_OWED,
                Debt.DEBT_STATUS_ACTIVE, "note 4345");
        mDebtsRepository.savePersonDebt(debt2, person2);
        verify(mDebtsLocalDataSource).savePersonDebt(eq(debt2), eq(person2));

        mDebtsRepository.deleteAllPersonDebtsByType(Debt.DEBT_TYPE_OWED);

        verify(mDebtsLocalDataSource).deleteAllPersonDebtsByType(eq(Debt.DEBT_TYPE_OWED));

        assertTrue(mDebtsRepository.mCacheOweMePersonDebts.size() == 0);
    }

    @Test
    public void shouldBeAbleToGetDebtFromLocalDataSource() {

        String id = "1234";
        mDebtsRepository.getPersonDebt(id, Debt.DEBT_TYPE_OWED);

        verify(mDebtsLocalDataSource).getPersonDebt(eq(id), eq(Debt.DEBT_TYPE_OWED));
    }

    @Test
    public void shouldBeAbleToDeleteAPersonDebtFromLocalDataSource() {

        Person person1 = TestUtil.createAndGetPerson();
        Debt debt1 = TestUtil.createAndGetOwedDebt(person1.getPhoneNumber());
        mDebtsRepository.savePersonDebt(debt1, person1);

        verify(mDebtsLocalDataSource).savePersonDebt(eq(debt1), eq(person1));

        PersonDebt personDebt = new PersonDebt(person1, debt1);
        mDebtsRepository.deletePersonDebt(personDebt);

        verify(mDebtsLocalDataSource).deletePersonDebt(eq(personDebt));

        assertTrue(mDebtsRepository.mCacheOweMePersonDebts.size() == 0);
    }

    @Test
    public void shouldBeAbleToDeleteAPersonAlsoWhenDeletingPersonDebtIfPersonHasDebtsSizeOfOne() {

        Person person1 = TestUtil.createAndGetPerson();
        Debt debt1 = TestUtil.createAndGetOwedDebt(person1.getPhoneNumber());
        mDebtsRepository.savePersonDebt(debt1, person1);

        Debt debt2 = TestUtil.createDebt(person1.getPhoneNumber(), 600000, Debt.DEBT_TYPE_OWED,
                Debt.DEBT_STATUS_ACTIVE, "note 7774");
        mDebtsRepository.savePersonDebt(debt2, person1);

        Debt debt3 = TestUtil.createDebt(person1.getPhoneNumber(), 5455555, Debt.DEBT_TYPE_OWED,
                Debt.DEBT_STATUS_ACTIVE, "note 2343");
        mDebtsRepository.savePersonDebt(debt3, person1);

        PersonDebt personDebt = new PersonDebt(person1, debt1);
        mDebtsRepository.deletePersonDebt(personDebt);

        PersonDebt personDebt2 = new PersonDebt(person1, debt2);
        mDebtsRepository.deletePersonDebt(personDebt2);

        PersonDebt personDebt3 = new PersonDebt(person1, debt3);
        mDebtsRepository.deletePersonDebt(personDebt3);

        assertTrue(mDebtsRepository.mCacheOweMePersonDebts.size() == 0);
        assertTrue(mDebtsRepository.mCachePersons.size() == 0);
    }

    @Test
    public void shouldNotDeleteAPersonWhenDeletingPersonDebtIfPersonHasDebtsSizeOfMoreThanOne() {

        Person person1 = TestUtil.createAndGetPerson();
        Debt debt1 = TestUtil.createAndGetOwedDebt(person1.getPhoneNumber());
        mDebtsRepository.savePersonDebt(debt1, person1);

        Debt debt2 = TestUtil.createDebt(person1.getPhoneNumber(), 4000, Debt.DEBT_TYPE_OWED,
                Debt.DEBT_STATUS_ACTIVE, "note 7774");
        mDebtsRepository.savePersonDebt(debt2, person1);

        PersonDebt personDebt = new PersonDebt(person1, debt1);
        mDebtsRepository.deletePersonDebt(personDebt);

        verify(mDebtsLocalDataSource).deletePersonDebt(eq(personDebt));

        Person person = mDebtsRepository.getPerson(person1.getPhoneNumber());
        assertNotNull(person);
    }

    @Test
    public void shouldBeAbleToGetAllDebtsByTypeFromLocalDataSource() {

        mDebtsRepository.getAllPersonDebtsByType(Debt.DEBT_TYPE_IOWE);

        verify(mDebtsLocalDataSource).getAllPersonDebtsByType(eq(Debt.DEBT_TYPE_IOWE));

        mDebtsRepository.getAllPersonDebtsByType(Debt.DEBT_TYPE_OWED);

        verify(mDebtsLocalDataSource).getAllPersonDebtsByType(eq(Debt.DEBT_TYPE_OWED));
    }

    @Test
    public void shouldBeAbleToDeleteDebtsByTypeFromLocalDataSource() {

        // Owed debt
        Person person1 = TestUtil.createAndGetPerson();
        Debt debt1 = TestUtil.createAndGetOwedDebt(person1.getPhoneNumber());
        mDebtsRepository.savePersonDebt(debt1, person1);

        Person person2 = TestUtil.createPerson("Ijeoma James", "0501245784");
        Debt debt2 = TestUtil.createDebt(person2.getPhoneNumber(), 600000, Debt.DEBT_TYPE_OWED,
                Debt.DEBT_STATUS_ACTIVE, "note 7774");
        mDebtsRepository.savePersonDebt(debt2, person2);

        mDebtsRepository.deleteAllPersonDebtsByType(Debt.DEBT_TYPE_OWED);

        verify(mDebtsLocalDataSource).deleteAllPersonDebtsByType(eq(Debt.DEBT_TYPE_OWED));

        assertTrue(mDebtsRepository.getAllPersonDebtsByType(Debt.DEBT_TYPE_OWED).size() == 0);
    }

    @Test
    public void shouldBeAbleToUpdateDebtFromLocalDataSource() {

        Person person1 = TestUtil.createAndGetPerson();
        Debt debt1 = TestUtil.createAndGetOwedDebt(person1.getPhoneNumber());
        mDebtsRepository.savePersonDebt(debt1, person1);

        PersonDebt personDebt = mDebtsRepository.getPersonDebt(debt1.getId(), Debt.DEBT_TYPE_OWED);

        personDebt.getPerson().setFullname("Emeka Onu");
        personDebt.getDebt().setAmount(300);

        mDebtsRepository.updatePersonDebt(personDebt);

        verify(mDebtsLocalDataSource).updatePersonDebt(eq(personDebt));

        PersonDebt personDebt1 = mDebtsRepository.getPersonDebt(debt1.getId(), Debt.DEBT_TYPE_OWED);

        assertEquals(personDebt1, personDebt);
    }

    @Test
    public void shouldBeAbleToBatchDeleteFromLocalDataSource() {

        Person person1 = TestUtil.createAndGetPerson();
        Debt debt1 = TestUtil.createAndGetOwedDebt(person1.getPhoneNumber());
        mDebtsRepository.savePersonDebt(debt1, person1);
        PersonDebt personDebt1 = new PersonDebt(person1, debt1);

        Person person2 = TestUtil.createPerson("Emeka Onu", "07045124589");
        Debt debt2 = TestUtil.createDebt(person2.getPhoneNumber(), 400, Debt.DEBT_TYPE_OWED,
                Debt.DEBT_STATUS_ACTIVE, "note 4345");
        mDebtsRepository.savePersonDebt(debt2, person2);
        PersonDebt personDebt2 = new PersonDebt(person2, debt2);

        List<PersonDebt> personDebts = new ArrayList<>();
        personDebts.add(personDebt1);
        personDebts.add(personDebt2);

        mDebtsRepository.batchDelete(personDebts, Debt.DEBT_TYPE_OWED);

        verify(mDebtsLocalDataSource).batchDelete(eq(personDebts), eq(Debt.DEBT_TYPE_OWED));
        assertThat(mDebtsRepository.mCacheOweMePersonDebts.size(), is(0));
    }

    @Test
    public void shouldBeAbleToGetAllPersonsFromLocalDataSource() {

        mDebtsRepository.getAllPersonWithDebts();

        verify(mDebtsLocalDataSource).getAllPersonWithDebts();
    }

    @Test
    public void shouldBeAbleToGetPersonDebtsFromLocalDatSource() {

        Person person = TestUtil.createAndGetPerson();
        Debt debt1 = TestUtil.createAndGetIOweDebt(person.getPhoneNumber());
        mDebtsRepository.savePersonDebt(debt1, person);
        Debt debt2 = TestUtil.createAndGetOwedDebt(person.getPhoneNumber());
        mDebtsRepository.savePersonDebt(debt2, person);

        List<Debt> debts = mDebtsRepository.getPersonDebts(person.getPhoneNumber());
        assertTrue(debts.size() == 2);

        boolean debt1found = false;
        boolean debt2found = false;

        for (Debt debt : debts) {
            if (debt.equals(debt1)) {
                debt1found = true;
            }
            if (debt.equals(debt2)) {
                debt2found = true;
            }
        }
        assertTrue(debt1found);
        assertTrue(debt2found);
    }

    @Test
    public void shouldBeAbleToSaveDebtPaymentAndIncreaseDebtAmountToLocalDataSource() {

        Person person = TestUtil.createAndGetPerson();
        Debt debt = TestUtil.createAndGetOwedDebt(person.getPhoneNumber());
        mDebtsRepository.savePersonDebt(debt, person);

        Payment payment = new Payment.Builder()
                .note("payment note 101")
                .debtId(debt.getId())
                .id("666666")
                .amount(1000)
                .dateEntered(System.currentTimeMillis())
                .action(Payment.PAYMENT_ACTION_DEBT_INCREASE)
                .personPhoneNumber("0703811153464")
                .build();

        mDebtsRepository.savePayment(payment);
        verify(mDebtsLocalDataSource).savePayment(payment);

        assertTrue(mDebtsRepository.mCacheOweMePersonDebts.get(debt.getId()).getPayments().size() == 1);

        // verify if debt amount increased
        Debt updatedDebt = mDebtsRepository.getDebt(debt.getId());
        double newDebtAmount = updatedDebt.getAmount();
        assertTrue(Math.abs(newDebtAmount - (debt.getAmount() + payment.getAmount())) < .0000001);
    }

    @Test
    public void shouldBeAbleToSaveDebtPaymentAndDecreaseDebtAmountToLocalDataSource() {

        Person person = TestUtil.createAndGetPerson();
        Debt debt = TestUtil.createAndGetOwedDebt(person.getPhoneNumber());
        mDebtsRepository.savePersonDebt(debt, person);

        Payment payment = new Payment.Builder()
                .note("payment note 666")
                .debtId(debt.getId())
                .id("666666")
                .amount(1000)
                .dateEntered(System.currentTimeMillis())
                .action(Payment.PAYMENT_ACTION_DEBT_DECREASE)
                .personPhoneNumber("170381115344")
                .build();

        mDebtsRepository.savePayment(payment);
        verify(mDebtsLocalDataSource).savePayment(payment);

        assertTrue(mDebtsRepository.mCacheOweMePersonDebts.get(debt.getId()).getPayments().size() == 1);

        // verify if debt amount decreased
        Debt updatedDebt = mDebtsRepository.getDebt(debt.getId());
        double newDebtAmount = updatedDebt.getAmount();
        assertTrue(Math.abs(newDebtAmount - (debt.getAmount() - payment.getAmount())) < .0000001);
    }

    @Test
    public void shouldBeAbleToSaveDebtPaymentAndDoNothingToDebtAmountToLocalDataSource() {

        Person person = TestUtil.createAndGetPerson();
        Debt debt = TestUtil.createAndGetOwedDebt(person.getPhoneNumber());
        mDebtsRepository.savePersonDebt(debt, person);

        Payment payment = new Payment.Builder()
                .note("payment note 419")
                .debtId(debt.getId())
                .id("666666")
                .amount(1000)
                .dateEntered(System.currentTimeMillis())
                .action(Payment.PAYMENT_ACTION_DEBT_DONT_CHANGE)
                .personPhoneNumber("070381115244")
                .build();

        mDebtsRepository.savePayment(payment);
        verify(mDebtsLocalDataSource).savePayment(payment);

        assertTrue(mDebtsRepository.mCacheOweMePersonDebts.get(debt.getId()).getPayments().size() == 1);

        // verify if debt payment decreased
        Debt debt1 = mDebtsRepository.getDebt(debt.getId());
        double newDebtAmount = debt1.getAmount();
        assertTrue(newDebtAmount == debt.getAmount());
    }

    @Test
    public void shouldBeAbleToGetDebtPaymentsFromLocalDataSource() {
        mDebtsRepository.getDebtPayments(TestUtil.DEBT_ID);
        verify(mDebtsLocalDataSource).getDebtPayments(eq(TestUtil.DEBT_ID));
    }

    @Test
    public void shouldBeAbleToGetDebtPaymentFromLocalDataSource() {

        Person person = TestUtil.createAndGetPerson();
        Debt debt = TestUtil.createAndGetOwedDebt(person.getPhoneNumber());
        mDebtsRepository.savePersonDebt(debt, person);
        String paymentId = "123";
        mDebtsRepository.getPayment(paymentId, debt);
        verify(mDebtsLocalDataSource).getPayment(eq(paymentId), eq(debt));
    }

    @Test
    public void shouldBeAbleToGetDebtPaymentFromCacheLocalDataSource() {

        Person person = TestUtil.createAndGetPerson();
        Debt debt = TestUtil.createAndGetOwedDebt(person.getPhoneNumber());
        mDebtsRepository.savePersonDebt(debt, person);

        Payment payment = TestUtil.createAndGetADebtPayment1(debt.getId());
        mDebtsRepository.savePayment(payment);

        Payment payment1 = mDebtsRepository.getPayment(payment.getId(), debt);
        assertThat(payment, is(payment1));

        verify(mDebtsLocalDataSource, never()).getPayment(eq(payment.getId()), eq(debt));
    }

    @Test
    public void shouldBeAbleToEditDebtPaymentFromLocalDataSource() {

        Person person = TestUtil.createAndGetPerson();
        Debt debt = TestUtil.createAndGetOwedDebt(person.getPhoneNumber());
        Payment payment = TestUtil.createAndGetADebtPayment1(debt.getId());

        mDebtsRepository.savePersonDebt(debt, person);

        mDebtsRepository.savePayment(payment);

        Payment updatedPayment = new Payment.Builder()
                .id(payment.getId())
                .action(Payment.PAYMENT_ACTION_DEBT_DECREASE)
                .amount(89999)
                .dateEntered(System.currentTimeMillis())
                .note("updated note")
                .debtId(debt.getId())
                .personPhoneNumber("070381515344")
                .build();

        mDebtsRepository.editPayment(updatedPayment, debt);

        Payment retrievedUpdatedPayment = mDebtsRepository.getPayment(updatedPayment.getId(), debt);

        assertThat(retrievedUpdatedPayment, is(updatedPayment));

        verify(mDebtsLocalDataSource).editPayment(eq(updatedPayment), eq(debt));
    }

    @Test
    public void shouldBeAbleToEditPayment() {

        Person person = TestUtil.createAndGetPerson();
        Debt debt = TestUtil.createAndGetOwedDebt(person.getPhoneNumber());
        mDebtsRepository.savePersonDebt(debt, person);

        Payment payment = TestUtil.createAndGetADebtPayment1(debt.getId());
        mDebtsRepository.savePayment(payment);

        Payment updatedPayment = new Payment.Builder()
                .id(payment.getId())
                .action(Payment.PAYMENT_ACTION_DEBT_INCREASE)
                .amount(400)
                .dateEntered(System.currentTimeMillis())
                .debtId(debt.getId())
                .note("updated note")
                .personPhoneNumber("0703911153445")
                .build();

        mDebtsRepository.editPayment(updatedPayment, debt);

        verify(mDebtsLocalDataSource).editPayment(eq(updatedPayment), eq(debt));

        Payment retrievedEditedPayment = mDebtsRepository.getPayment(updatedPayment.getId(), debt);

        assertTrue(payment.getId().equals(retrievedEditedPayment.getId()));
        assertNotSame(payment, retrievedEditedPayment);

        // verify if debt amount increased
        Debt newDebt = mDebtsRepository.getDebt(debt.getId());
        double newDebtAmount = newDebt.getAmount();
        assertTrue(Math.abs(newDebtAmount - (debt.getAmount() + updatedPayment.getAmount())) < .0000001);
    }

    @Test
    public void shouldBeAbleToDeletePayment() {

        Person person = TestUtil.createAndGetPerson();
        Debt debt = TestUtil.createAndGetOwedDebt(person.getPhoneNumber());
        mDebtsRepository.savePersonDebt(debt, person);

        Payment payment = new Payment.Builder()
                .note("payment note 232")
                .debtId(debt.getId())
                .id("666")
                .amount(1000)
                .dateEntered(System.currentTimeMillis())
                .action(Payment.PAYMENT_ACTION_DEBT_INCREASE)
                .personPhoneNumber("0703811153440")
                .build();

        mDebtsRepository.savePayment(payment);

        mDebtsRepository.deletePayment(payment);

        verify(mDebtsLocalDataSource).deletePayment(eq(payment));

        Payment retrievedPayment = mDebtsRepository.getPayment(payment.getId(), debt);
        assertNull(retrievedPayment);
        // revert debt amount back
        double updatedDebtAmount = mDebtsRepository.getDebt(debt.getId()).getAmount();
        assertTrue(updatedDebtAmount == debt.getAmount());
    }

    @Test
    public void shouldBeAbleToDeletePersonDebtAndAlsoPaymentsIfAnyExist() {

        Person person = TestUtil.createAndGetPerson();
        Debt debt = TestUtil.createAndGetOwedDebt(person.getPhoneNumber());
        PersonDebt personDebt = new PersonDebt(person, debt);
        mDebtsRepository.savePersonDebt(debt, person);

        Payment payment1 = TestUtil.createAndGetADebtPayment1(debt.getId());
        Payment payment2 = TestUtil.createAndGetADebtPayment2(debt.getId());

        mDebtsRepository.savePayment(payment1);
        mDebtsRepository.savePayment(payment2);

        mDebtsRepository.deletePersonDebt(personDebt);

        // check if payments were deleted
        assertNull(mDebtsRepository.getPayment(payment1.getId(), debt));
        assertNull(mDebtsRepository.getPayment(payment2.getId(), debt));
    }
}
