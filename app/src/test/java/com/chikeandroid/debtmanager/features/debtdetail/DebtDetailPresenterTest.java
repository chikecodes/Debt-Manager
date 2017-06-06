package com.chikeandroid.debtmanager.features.debtdetail;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.chikeandroid.debtmanager.data.Debt;
import com.chikeandroid.debtmanager.data.Payment;
import com.chikeandroid.debtmanager.data.Person;
import com.chikeandroid.debtmanager.data.PersonDebt;
import com.chikeandroid.debtmanager.features.debtdetail.loaders.DebtLoader;
import com.chikeandroid.debtmanager.data.source.PersonDebtsRepository;
import com.chikeandroid.debtmanager.util.TestUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

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

    private Debt mDebt;

    @Before
    public void setUpDebtDetailPresenter() {
        MockitoAnnotations.initMocks(this);

        mDebtDetailPresenter = new DebtDetailPresenter("434", mRepository, mView, mLoaderManager, mDebtLoader);

        Person person = TestUtil.createAndGetPerson();
        mDebt = TestUtil.createAndGetIOweDebt(person.getPhoneNumber());
        mPersonDebt = new PersonDebt(person, mDebt);
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

    @Test
    public void shouldBeAbleToAddPartialPayment() {

        Payment payment = TestUtil.createAndGetADebtPayment1(mDebt.getId());
        when(mRepository.getPersonDebt(anyString(), anyInt())).thenReturn(mPersonDebt);
        mDebtDetailPresenter.addPartialPayment(payment);
        verify(mRepository).savePayment(eq(payment));
        verify(mView).showPersonDebt(eq(mPersonDebt));
    }

    @Test
    public void shouldBeAbleToEditPayment() {

        Payment payment = TestUtil.createAndGetADebtPayment1(mDebt.getId());
        when(mRepository.getPersonDebt(anyString(), anyInt())).thenReturn(mPersonDebt);
        mDebtDetailPresenter.editPayment(payment, mDebt);
        verify(mRepository).editPayment(eq(payment), eq(mDebt));
        verify(mView).showPersonDebt(eq(mPersonDebt));
    }

    @Test
    public void shouldBeAbleToDeletePayment() {

        Payment payment = TestUtil.createAndGetADebtPayment1(mDebt.getId());
        when(mRepository.getPersonDebt(anyString(), anyInt())).thenReturn(mPersonDebt);
        mDebtDetailPresenter.deletePayment(payment);
        verify(mRepository).deletePayment(eq(payment));
        verify(mView).showPersonDebt(eq(mPersonDebt));
    }
}