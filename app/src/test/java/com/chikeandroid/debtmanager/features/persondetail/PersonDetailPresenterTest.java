package com.chikeandroid.debtmanager.features.persondetail;

import com.google.common.collect.Lists;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.chikeandroid.debtmanager.data.Debt;
import com.chikeandroid.debtmanager.data.Person;
import com.chikeandroid.debtmanager.features.persondetail.loader.PersonDebtsLoader;
import com.chikeandroid.debtmanager.util.TestUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by Chike on 5/23/2017.
 * Unit tests for the implementation of {@link PersonDetailPresenter
 */
public class PersonDetailPresenterTest {

    private List<Debt> mDebts;

    @Mock
    private PersonDetailContract.View mPersonDetailView;

    @Captor
    private ArgumentCaptor<List> mShowDebtsArgumentCaptor;

    @Mock
    private PersonDebtsLoader mPersonDebtsLoader;

    @Mock
    private LoaderManager mLoaderManager;

    private PersonDetailPresenter mPersonDetailPresenter;

    @Before
    public void setUpPersonDetailPresenter() {
        MockitoAnnotations.initMocks(this);

        Person person = TestUtil.createAndGetPerson();
        mPersonDetailPresenter = new PersonDetailPresenter(mPersonDetailView, mLoaderManager, mPersonDebtsLoader, person);
        Debt debt1 = TestUtil.createDebt(person.getId(), 757575, Debt.DEBT_TYPE_OWED, Debt.DEBT_STATUS_ACTIVE, "note 123");
        Debt debt2 = TestUtil.createDebt(person.getId(), 40000, Debt.DEBT_TYPE_IOWE, Debt.DEBT_STATUS_ACTIVE, "note 456");
        Debt debt3 = TestUtil.createDebt(person.getId(), 5999, Debt.DEBT_TYPE_IOWE, Debt.DEBT_STATUS_ACTIVE, "note 54444");
        Debt debt4 = TestUtil.createDebt(person.getId(), 39000, Debt.DEBT_TYPE_OWED, Debt.DEBT_STATUS_ACTIVE, "note 666");
        mDebts = Lists.newArrayList(debt1, debt2, debt3, debt4);
    }

    @Test
    public void shouldBeAbleToLoadPersonDebtsFromRepositoryAndLoadIntoView() {

        mPersonDetailPresenter.onLoadFinished(mock(Loader.class), mDebts);

        verify(mPersonDetailView).showPersonDebts(mShowDebtsArgumentCaptor.capture());
        assertThat(mShowDebtsArgumentCaptor.getValue().size(), is(4));
    }

    @Test
    public void shouldBeAbleToLoadPersonDebtsFromRepositoryAndShowEmptyViewIfNotAvailable() {

        mPersonDetailPresenter.onLoadFinished(mock(Loader.class), new ArrayList<Debt>());

        verify(mPersonDetailView).showMissingPersonDebts();
    }

}