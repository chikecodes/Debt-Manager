package com.chikeandroid.debtmanager20.data.source;

import android.content.Context;

import com.chikeandroid.debtmanager20.data.Debt;
import com.chikeandroid.debtmanager20.data.Person;
import com.chikeandroid.debtmanager20.data.PersonDebt;
import com.chikeandroid.debtmanager20.util.TestUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Chike on 4/5/2017.
 * Unit tests for the implementation of the in-memory repository with cache.
 */
public class DebtsRepositoryTest {

    private PersonDebtsRepository mDebtsRepository;

    @Mock
    private PersonDebtsDataSource mDebtsLocalDataSource;

    @Mock
    private Context mContext;

    @Before
    public void setUp() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mDebtsRepository = new PersonDebtsRepository(mDebtsLocalDataSource);
    }

    @Test
    public void shouldBeAbleToSaveDebtToLocalDataSource() {

        // save owe me debt
        Person person1 = TestUtil.createPerson("Chike Mgbemena", "07038111534");
        Debt debt1 = TestUtil.createDebt(person1.getId(), 4000, Debt.DEBT_TYPE_OWED,
                Debt.DEBT_STATUS_ACTIVE, "note 123");
        mDebtsRepository.savePersonDebt(debt1, person1);

        verify(mDebtsLocalDataSource).savePersonDebt(debt1, person1);
        assertThat(mDebtsRepository.getCachedDebts().size(), is(1));

        // save i owe debt
        Person person2 = TestUtil.createPerson("Mary Jane", "08023021782");
        Debt debt2 = TestUtil.createDebt(person2.getId(), 60000, Debt.DEBT_TYPE_IOWE,
                Debt.DEBT_STATUS_ACTIVE, "note 4345");
        mDebtsRepository.savePersonDebt(debt2, person2);

        verify(mDebtsLocalDataSource).savePersonDebt(debt2, person2);
        assertThat(mDebtsRepository.getCachedDebts().size(), is(2));
    }

    @Test
    public void shouldBeAbleToGetAllDebtsFromLocalDataSource() {

        mDebtsRepository.getAllPersonDebts();

        verify(mDebtsLocalDataSource).getAllPersonDebts();
    }

    @Test
    public void shouldBeAbleToReturnNullWhenLocalDataSourceUnavailable() {

        setOweMeDebtsNotAvailable(mDebtsLocalDataSource);

        List<PersonDebt> returnedDebts = mDebtsRepository.getAllPersonDebts();

        assertNull(returnedDebts);
    }

    private void setOweMeDebtsNotAvailable(PersonDebtsDataSource dataSource) {

        // owe me debts
        when(dataSource.getAllPersonDebts()).thenReturn(null);
    }

    @Test
    public void shouldBeAbleToDeleteAllDebts() {

        Person person1 = TestUtil.createPerson("Chike Mgbemena", "07038111534");
        Debt debt1 = TestUtil.createDebt(person1.getId(), 4000, Debt.DEBT_TYPE_OWED,
                Debt.DEBT_STATUS_ACTIVE, "note 123");
        mDebtsRepository.savePersonDebt(debt1, person1);
        verify(mDebtsLocalDataSource).savePersonDebt(debt1, person1);

        Person person2 = TestUtil.createPerson("Mary Jane", "08023021782");
        Debt debt2 = TestUtil.createDebt(person2.getId(), 60000, Debt.DEBT_TYPE_IOWE,
                Debt.DEBT_STATUS_ACTIVE, "note 4345");
        mDebtsRepository.savePersonDebt(debt2, person2);
        verify(mDebtsLocalDataSource).savePersonDebt(debt2, person2);

        mDebtsRepository.deleteAllPersonDebts();

        verify(mDebtsLocalDataSource).deleteAllPersonDebts();

        assertTrue(mDebtsRepository.mCachedDebts.size() == 0);
    }

    @Test
    public void shouldBeAbleToGetDebtFromLocalDataSource() {

        String id = "1234";
        mDebtsRepository.getPersonDebt(id);

        verify(mDebtsLocalDataSource).getPersonDebt(eq(id));
    }

    @Test
    public void shouldBeAbleToDeleteAPersonDebtFromLocalDataSource() {

        Person person1 = TestUtil.createPerson("Mary Jane", "08023021782");
        Debt debt1 = TestUtil.createDebt(person1.getId(), 60000, Debt.DEBT_TYPE_IOWE,
                Debt.DEBT_STATUS_ACTIVE, "note 4345");

        mDebtsRepository.savePersonDebt(debt1, person1);

        PersonDebt personDebt = new PersonDebt(person1, debt1);
        mDebtsRepository.deletePersonDebt(personDebt);

        verify(mDebtsLocalDataSource).deletePersonDebt(personDebt);

        assertTrue(mDebtsRepository.mCachedDebts.size() == 0);
    }

    @Test
    public void shouldBeAbleToGetAllDebtsByTypeFromLocalDataSource() {

        mDebtsRepository.getAllPersonDebtsByType(Debt.DEBT_TYPE_IOWE);

        verify(mDebtsLocalDataSource).getAllPersonDebtsByType(eq(Debt.DEBT_TYPE_IOWE));
    }

    @Test
    public void shouldBeAbleToDeleteDebtsByTypeFromLocalDataSource() {

        Person person1 = TestUtil.createPerson("Chike Mgbemena", "07038111534");
        Debt debt1 = TestUtil.createDebt(person1.getId(), 4000, Debt.DEBT_TYPE_OWED,
                Debt.DEBT_STATUS_ACTIVE, "note 123");
        mDebtsRepository.savePersonDebt(debt1, person1);

        Person person2 = TestUtil.createPerson("Mary Jane", "08023021782");
        Debt debt2 = TestUtil.createDebt(person2.getId(), 60000, Debt.DEBT_TYPE_OWED,
                Debt.DEBT_STATUS_ACTIVE, "note 4345");
        mDebtsRepository.savePersonDebt(debt2, person2);

        mDebtsRepository.deleteAllPersonDebtsByType(Debt.DEBT_TYPE_OWED);

        verify(mDebtsLocalDataSource).deleteAllPersonDebtsByType(eq(Debt.DEBT_TYPE_OWED));

        assertTrue(mDebtsRepository.getAllPersonDebtsByType(Debt.DEBT_TYPE_OWED).size() == 0);
    }

    @Test
    public void shouldBeAbleToUpdateDebtFromLocalDataSource() {

        Person person1 = TestUtil.createPerson("Chike Mgbemena", "07038111534");
        Debt debt1 = TestUtil.createDebt(person1.getId(), 4000, Debt.DEBT_TYPE_OWED,
                Debt.DEBT_STATUS_ACTIVE, "note 123");
        mDebtsRepository.savePersonDebt(debt1, person1);

        PersonDebt personDebt = mDebtsRepository.getPersonDebt(debt1.getId());

        personDebt.getPerson().setFullname("Emeka Onu");
        personDebt.getDebt().setAmount(300);

        mDebtsRepository.updatePersonDebt(personDebt);

        verify(mDebtsLocalDataSource).updatePersonDebt(eq(personDebt));

        PersonDebt personDebt1 = mDebtsRepository.getPersonDebt(debt1.getId());

        assertEquals(personDebt1, personDebt);
    }
}
