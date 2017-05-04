package com.chikeandroid.debtmanager20.data.source;

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
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
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
        Person person1 = TestUtil.getPerson();
        Debt debt1 = TestUtil.createAndGetOwedDebt(person1.getId());
        mDebtsRepository.savePersonDebt(debt1, person1);

        verify(mDebtsLocalDataSource).savePersonDebt(eq(debt1), eq(person1));
        assertThat(mDebtsRepository.getCacheOwed().size(), is(1));
    }

    @Test
    public void shouldBeAbleToSaveIOweDebtToLocalDataSource() {
        // save i owe debt
        Person person2 = TestUtil.createPerson("Nkeiru Ineh", "070414741");
        Debt debt2 = TestUtil.createDebt(person2.getId(), 60000000, Debt.DEBT_TYPE_IOWE,
                Debt.DEBT_STATUS_ACTIVE, "");
        mDebtsRepository.savePersonDebt(debt2, person2);

        verify(mDebtsLocalDataSource).savePersonDebt(eq(debt2), eq(person2));
        assertThat(mDebtsRepository.getCacheIOwe().size(), is(1));
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

        List<PersonDebt> returnedDebts = mDebtsRepository.getAllPersonDebts();

        assertNull(returnedDebts);
    }

    private void setOweMeDebtsNotAvailable(PersonDebtsDataSource dataSource) {

        // owe me debts
        when(dataSource.getAllPersonDebts()).thenReturn(null);
    }

    @Test
    public void shouldBeAbleToDeleteAllOwedDebts() {

        Person person1 = TestUtil.getPerson();
        Debt debt1 = TestUtil.createAndGetOwedDebt(person1.getId());
        mDebtsRepository.savePersonDebt(debt1, person1);
        verify(mDebtsLocalDataSource).savePersonDebt(eq(debt1), eq(person1));

        Person person2 = TestUtil.createPerson("Emeka Onu", "07045124589");
        Debt debt2 = TestUtil.createDebt(person2.getId(), 400, Debt.DEBT_TYPE_OWED,
                Debt.DEBT_STATUS_ACTIVE, "note 4345");
        mDebtsRepository.savePersonDebt(debt2, person2);
        verify(mDebtsLocalDataSource).savePersonDebt(eq(debt2), eq(person2));

        mDebtsRepository.deleteAllPersonDebtsByType(Debt.DEBT_TYPE_OWED);

        verify(mDebtsLocalDataSource).deleteAllPersonDebtsByType(eq(Debt.DEBT_TYPE_OWED));

        assertTrue(mDebtsRepository.mCacheOwed.size() == 0);
    }


    @Test
    public void shouldBeAbleToGetDebtFromLocalDataSource() {

        String id = "1234";
        mDebtsRepository.getPersonDebt(id, Debt.DEBT_TYPE_OWED);

        verify(mDebtsLocalDataSource).getPersonDebt(eq(id), eq(Debt.DEBT_TYPE_OWED));
    }

    @Test
    public void shouldBeAbleToDeleteAPersonDebtFromLocalDataSource() {

        Person person1 = TestUtil.getPerson();
        Debt debt1 = TestUtil.createAndGetOwedDebt(person1.getId());
        mDebtsRepository.savePersonDebt(debt1, person1);

        verify(mDebtsLocalDataSource).savePersonDebt(eq(debt1), eq(person1));

        PersonDebt personDebt = new PersonDebt(person1, debt1);
        mDebtsRepository.deletePersonDebt(personDebt);

        verify(mDebtsLocalDataSource).deletePersonDebt(eq(personDebt));

        assertTrue(mDebtsRepository.mCacheOwed.size() == 0);
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
        Person person1 = TestUtil.getPerson();
        Debt debt1 = TestUtil.createAndGetOwedDebt(person1.getId());
        mDebtsRepository.savePersonDebt(debt1, person1);

        Person person2 = TestUtil.createPerson("Ijeoma James", "0501245784");
        Debt debt2 = TestUtil.createDebt(person2.getId(), 600000, Debt.DEBT_TYPE_OWED,
                Debt.DEBT_STATUS_ACTIVE, "note 7774");
        mDebtsRepository.savePersonDebt(debt2, person2);

        mDebtsRepository.deleteAllPersonDebtsByType(Debt.DEBT_TYPE_OWED);

        verify(mDebtsLocalDataSource).deleteAllPersonDebtsByType(eq(Debt.DEBT_TYPE_OWED));

        assertTrue(mDebtsRepository.getAllPersonDebtsByType(Debt.DEBT_TYPE_OWED).size() == 0);
    }

    @Test
    public void shouldBeAbleToUpdateDebtFromLocalDataSource() {

        Person person1 = TestUtil.getPerson();
        Debt debt1 = TestUtil.createAndGetOwedDebt(person1.getId());
        mDebtsRepository.savePersonDebt(debt1, person1);

        PersonDebt personDebt = mDebtsRepository.getPersonDebt(debt1.getId(), Debt.DEBT_TYPE_OWED);

        personDebt.getPerson().setFullname("Emeka Onu");
        personDebt.getDebt().setAmount(300);

        mDebtsRepository.updatePersonDebt(personDebt);

        verify(mDebtsLocalDataSource).updatePersonDebt(eq(personDebt));

        PersonDebt personDebt1 = mDebtsRepository.getPersonDebt(debt1.getId(), Debt.DEBT_TYPE_OWED);

        assertEquals(personDebt1, personDebt);
    }
}
