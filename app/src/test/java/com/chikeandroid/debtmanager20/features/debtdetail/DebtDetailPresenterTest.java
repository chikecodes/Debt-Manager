package com.chikeandroid.debtmanager20.features.debtdetail;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.chikeandroid.debtmanager20.data.Debt;
import com.chikeandroid.debtmanager20.data.Person;
import com.chikeandroid.debtmanager20.data.PersonDebt;
import com.chikeandroid.debtmanager20.data.loaders.DebtLoader;
import com.chikeandroid.debtmanager20.data.source.PersonDebtsRepository;
import com.chikeandroid.debtmanager20.util.TestUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by Chike on 5/24/2017.
 */

public class DebtDetailPresenterTest {

    @Mock
    private DebtDetailContract.View mView;

    @Captor
    private ArgumentCaptor<PersonDebt> mShowPersonDebtArgumentCaptor;

    @Mock
    private DebtLoader mDebtLoader;

    @Mock
    private LoaderManager mLoaderManager;

    @Mock
    private PersonDebtsRepository mRepository;

    private DebtDetailPresenter mDebtDetailPresenter;

    private PersonDebt mPersonDebt;

    @Before
    public void setUpDebtDetailPresenter() {
        MockitoAnnotations.initMocks(this);

        mDebtDetailPresenter = new DebtDetailPresenter("434", mRepository, mView, mLoaderManager, mDebtLoader);

        Person person = TestUtil.createAndGetPerson();
        Debt debt = TestUtil.createAndGetIOweDebt(person.getId());
        mPersonDebt = new PersonDebt(person, debt);
    }

    @Test
    public void shouldBeAbleToLoadPersonDebtFromRepositoryAndLoadIntoView() {

        mDebtDetailPresenter.onLoadFinished(mock(Loader.class), mPersonDebt);

        verify(mView).showPersonDebt(mShowPersonDebtArgumentCaptor.capture());
        assertThat(mShowPersonDebtArgumentCaptor.getValue(), is(mPersonDebt));
    }

    @Test
    public void shouldBeAbleToDeletePersonDebt() {

        mDebtDetailPresenter.deletePersonDebt(mPersonDebt);
        verify(mRepository).deletePersonDebt(eq(mPersonDebt));
        verify(mView).showPersonDebtDeleted();
    }

    @Test
    public void shouldBeAbleToShowMissingDebtMessageWhenPersonDebtIsNull() {

        mDebtDetailPresenter.onLoadFinished(mock(Loader.class), null);

        verify(mView).showMissingDebt();
    }
}
