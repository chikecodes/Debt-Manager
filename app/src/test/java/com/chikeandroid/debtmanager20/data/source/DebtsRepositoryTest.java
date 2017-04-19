package com.chikeandroid.debtmanager20.data.source;

import android.content.Context;

import com.chikeandroid.debtmanager20.data.Debt;
import com.chikeandroid.debtmanager20.data.Person;
import com.chikeandroid.debtmanager20.data.PersonDebt;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

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

    private DebtsRepository mDebtsRepository;

    @Mock
    private DebtsDataSource mDebtsLocalDataSource;

    @Mock
    private Context mContext;

    @Before
    public void setUp() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mDebtsRepository = new DebtsRepository(mDebtsLocalDataSource);
    }

    @Test
    public void shouldBeAbleToSaveDebtToLocalDataSource() {

        // save owe me debt
        Person person1 = new Person(UUID.randomUUID().toString(), "Chike Mgbemena", "07038111534");
        Debt debt1 = new Debt.Builder(UUID.randomUUID().toString(), person1.getId(), 5000.87,
                System.currentTimeMillis(), Debt.DEBT_TYPE_OWED, Debt.DEBT_STATUS_PARTIAL)
                .dueDate(System.currentTimeMillis())
                .note("school fees")
                .build();

        mDebtsRepository.saveDebt(debt1, person1);

        verify(mDebtsLocalDataSource).saveDebt(debt1, person1);
        assertThat(mDebtsRepository.getCachedDebts().size(), is(1));

        // save i owe debt
        /*Person person2 = new Person("Mary Jane", "080145236987");

        String personId2 = person2.getId();

        Debt debt2 = new Debt.Builder(personId2, 5000.87, System.currentTimeMillis(), Debt.DEBT_TYPE_i_OWE,
                Debt.DEBT_STATUS_PARTIAL)
                .dueDate(System.currentTimeMillis())
                .note("food money")
                .build();

        mDebtsRepository.saveDebt(debt2, person2);

        verify(mDebtsLocalDataSource).saveDebt(debt2, person2);
        assertThat(mDebtsRepository.mCachedOweMeDebts.size(), is(1));*/
    }

    @Test
    public void shouldBeAbleToGetAllDebtsFromLocalDataSource() {

        // get owe me debts
        mDebtsRepository.getAllDebts();

        verify(mDebtsLocalDataSource).getAllDebts();
    }

    @Test
    public void shouldBeAbleToReturnNullWhenLocalDataSourceUnavailable() {

        setOweMeDebtsNotAvailable(mDebtsLocalDataSource);

        List<PersonDebt> returnedDebts = mDebtsRepository.getAllDebts();

        assertNull(returnedDebts);
    }

    public void setOweMeDebtsNotAvailable(DebtsDataSource dataSource) {

        // owe me debts
        when(dataSource.getAllDebts()).thenReturn(null);
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

        mDebtsRepository.saveDebt(debt1, person1);

        verify(mDebtsLocalDataSource).saveDebt(debt1, person1);
        assertThat(mDebtsRepository.getCachedDebts().size(), is(1));

        Person person2 = new Person(UUID.randomUUID().toString(), "Mary Jane", "080145236987");
        Debt debt2 = new Debt.Builder(UUID.randomUUID().toString(), person2.getId(), 4343.223,
                System.currentTimeMillis(), Debt.DEBT_TYPE_i_OWE,
                Debt.DEBT_STATUS_PARTIAL)
                .dueDate(System.currentTimeMillis())
                .note("food money")
                .build();

        mDebtsRepository.saveDebt(debt2, person2);

        mDebtsRepository.deleteAllDebts();

        verify(mDebtsLocalDataSource).deleteAllDebts();

        assertTrue(mDebtsRepository.mCachedDebts.size() == 0);

    }

    @Test
    public void shouldBeAbleToGetDebtFromLocalDataSource() {

        String id = "1234";
        mDebtsRepository.getDebt(id);

        verify(mDebtsLocalDataSource).getDebt(eq(id));
    }

    @Test
    public void shouldBeAbleToDeleteADebtFromLocalDataSource() {

        Person person2 = new Person(UUID.randomUUID().toString(), "Mary Jane", "080145236987");
        Debt debt2 = new Debt.Builder(UUID.randomUUID().toString(), person2.getId(), 5000.87,
                System.currentTimeMillis(), Debt.DEBT_TYPE_i_OWE,
                Debt.DEBT_STATUS_PARTIAL)
                .dueDate(System.currentTimeMillis())
                .note("food money")
                .build();

        mDebtsRepository.saveDebt(debt2, person2);

        mDebtsRepository.deleteDebt(debt2.getId());

        verify(mDebtsLocalDataSource).deleteDebt(eq(debt2.getId()));

        assertTrue(mDebtsRepository.mCachedDebts.size() == 0);

    }

    @Test
    public void shouldBeAbleToGetAllDebtsByTypeFromLocalDataSource() {

        mDebtsRepository.getAllDebtsByType(Debt.DEBT_TYPE_i_OWE);

        verify(mDebtsLocalDataSource).getAllDebtsByType(eq(Debt.DEBT_TYPE_i_OWE));
    }

    @Test
    public void shouldBeAbleToDeleteDebtsByTypeFromLocalDataSource() {

        Person person1 = new Person(UUID.randomUUID().toString(), "Chike Mgbemena", "07038111534");
        Debt debt1 = new Debt.Builder(UUID.randomUUID().toString(), person1.getId(), 5000.87,
                System.currentTimeMillis(), Debt.DEBT_TYPE_OWED,
                Debt.DEBT_STATUS_PARTIAL)
                .dueDate(System.currentTimeMillis())
                .note("school fees")
                .build();

        mDebtsRepository.saveDebt(debt1, person1);

        Person person2 = new Person(UUID.randomUUID().toString(), "Mary Jane", "080145236987");
        Debt debt2 = new Debt.Builder(UUID.randomUUID().toString(), person2.getId(), 4343.223,
                System.currentTimeMillis(), Debt.DEBT_TYPE_OWED,
                Debt.DEBT_STATUS_PARTIAL)
                .dueDate(System.currentTimeMillis())
                .note("food money")
                .build();
        mDebtsRepository.saveDebt(debt2, person2);

        mDebtsRepository.deleteAllDebtsByType(Debt.DEBT_TYPE_OWED);

        verify(mDebtsLocalDataSource).deleteAllDebtsByType(eq(Debt.DEBT_TYPE_OWED));

        assertTrue(mDebtsRepository.getAllDebtsByType(Debt.DEBT_TYPE_OWED).size() == 0);
    }
}
