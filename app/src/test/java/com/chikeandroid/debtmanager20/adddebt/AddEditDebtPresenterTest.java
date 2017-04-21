package com.chikeandroid.debtmanager20.adddebt;

import com.chikeandroid.debtmanager20.data.Debt;
import com.chikeandroid.debtmanager20.data.Person;
import com.chikeandroid.debtmanager20.data.source.DebtsRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Chike on 4/12/2017.
 * Unit tests for the implementation of {@link AddEditDebtPresenter}.
 */

public class AddEditDebtPresenterTest {

    @Mock
    private DebtsRepository mDebtsRepository;

    @Mock
    private AddEditDebtContract.View mAddDebtView;

    private AddEditDebtPresenter mAddEditDebtPresenter;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        // The presenter wont't update the view unless it's active.
        when(mAddDebtView.isActive()).thenReturn(true);

        mAddEditDebtPresenter = new AddEditDebtPresenter(mDebtsRepository, mAddDebtView);
    }

    @Test
    public void shouldSaveDebtToRepository() {

        mAddEditDebtPresenter = new AddEditDebtPresenter(mDebtsRepository, mAddDebtView);

        mAddEditDebtPresenter.saveDebt("Chike Mgbemena", "07038111534", 5045.34, "my note",
                System.currentTimeMillis(), System.currentTimeMillis(),
                Debt.DEBT_TYPE_OWED, Debt.DEBT_STATUS_ACTIVE);

        verify(mDebtsRepository).saveDebt(any(Debt.class), any(Person.class));
        verify(mAddDebtView).showDebts();
    }

    @Test
    public void shouldShowEmptyDebtErrorUiWhenDebtSaved() {

        mAddEditDebtPresenter.saveDebt("", "", 0, "", 0, 0, Debt.DEBT_TYPE_OWED, 0);

        verify(mAddDebtView).showEmptyDebtError();
    }
}
