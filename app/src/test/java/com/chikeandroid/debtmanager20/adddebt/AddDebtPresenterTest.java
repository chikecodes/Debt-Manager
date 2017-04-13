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
 */

public class AddDebtPresenterTest {

    @Mock
    private DebtsRepository mDebtsRepository;

    @Mock
    private AddDebtContract.View mAddDebtView;

    private AddDebtPresenter mAddDebtPresenter;

    @Before
    public void setUpMocksAndView() {

        MockitoAnnotations.initMocks(this);

        // The presenter wont't update the view unless it's active.
        when(mAddDebtView.isActive()).thenReturn(true);
    }

    @Test
    public void shouldSaveDebtToRepository() {

        mAddDebtPresenter = new AddDebtPresenter(mDebtsRepository, mAddDebtView);

        mAddDebtPresenter.saveDebt("Chike Mgbemena", "07038111534", 5045.34, "my note",
                System.currentTimeMillis(), System.currentTimeMillis(),
                Debt.DEBT_TYPE_OWED, Debt.DEBT_STATUS_ACTIVE);

        verify(mDebtsRepository).saveDebt(any(Debt.class), any(Person.class));
    }
}
