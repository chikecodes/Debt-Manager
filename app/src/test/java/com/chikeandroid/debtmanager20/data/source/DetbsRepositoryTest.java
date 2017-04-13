package com.chikeandroid.debtmanager20.data.source;

import android.content.Context;

import com.chikeandroid.debtmanager20.data.Debt;
import com.chikeandroid.debtmanager20.data.Person;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

/**
 * Created by Chike on 4/5/2017.
 */

public class DetbsRepositoryTest {

    private DebtsRepository mDebtsRepository;

    @Mock
    private DebtsDataSource mDebtsLocalDataSource;

    @Mock
    private Context mContext;

    @Mock
    private DebtsDataSource.GetDebtCallback mGetDebtCallback;

    @Mock
    private DebtsDataSource.LoadDebtsCallback mLoadDebtsCallback;

    /**
     * {@link ArgumentCaptor} is a powerful Mockito API to capture argument values and use them to
     * perform further actions or assertions on them.
     */
    @Captor
    private ArgumentCaptor<DebtsDataSource.LoadDebtsCallback> mDebtsCallbackCaptor;

    @Before
    public void setUp() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mDebtsRepository = new DebtsRepository(mDebtsLocalDataSource);
    }

    @Test
    public void shouldBeAbleToSaveDebtToDb() {

        Person newPerson = new Person("Chike Mgbemena", "07038111534");

        String personId = newPerson.getId();

        Debt newDebt = new Debt.Builder(personId, 5000.87, System.currentTimeMillis(), Debt.DEBT_TYPE_OWED,
                Debt.DEBT_STATUS_PARTIAL)
                .dueDate(System.currentTimeMillis())
                .note("school fees")
                .build();

        mDebtsRepository.saveDebt(newDebt, newPerson);

        verify(mDebtsLocalDataSource).saveDebt(newDebt, newPerson);
        assertThat(mDebtsRepository.mCachedTasks.size(), is(1));
    }
}
