package com.chikeandroid.debtmanager.features.oweme;

import com.google.common.collect.Lists;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.chikeandroid.debtmanager.data.Debt;
import com.chikeandroid.debtmanager.data.Person;
import com.chikeandroid.debtmanager.data.PersonDebt;
import com.chikeandroid.debtmanager.data.source.PersonDebtsRepository;
import com.chikeandroid.debtmanager.features.oweme.loader.OweMeLoader;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by Chike on 4/16/2017.
 * Unit tests for the implementation of {@link OweMePresenter
 */
public class OweMePresenterTest {

    private List<PersonDebt> mPersonDebts;

    @Mock
    private OweMeContract.View mOweMeDebtsView;

    @Captor
    private ArgumentCaptor<List> mShowDebtsArgumentCaptor;

    @Mock
    private OweMeLoader mOweMeLoader;

    @Mock
    private LoaderManager mLoaderManager;

    @Mock
    private PersonDebtsRepository mRepository;

    private OweMePresenter mOweMePresenter;

    @Before
    public void setUpOweMeDebtsPresenter() {
        MockitoAnnotations.initMocks(this);

        mOweMePresenter = new OweMePresenter(mOweMeDebtsView, mRepository, mLoaderManager, mOweMeLoader);

        Person person1 = new Person(UUID.randomUUID().toString(), "Chike Mgbemena", "07038111534",
                "image_uri");
        Debt debt1 = new Debt.Builder(UUID.randomUUID().toString(), person1.getId(), 5000.87,
                System.currentTimeMillis(), Debt.DEBT_TYPE_OWED, Debt.DEBT_STATUS_PARTIAL)
                .dueDate(System.currentTimeMillis())
                .note("school fees")
                .build();
        PersonDebt personDebt1 = new PersonDebt(person1, debt1);

        Person person2 = new Person(UUID.randomUUID().toString(), "Chinedu Mandu", "08047541254",
                "image_uri");
        Debt debt2 = new Debt.Builder(UUID.randomUUID().toString(), person2.getId(), 87000.00,
                System.currentTimeMillis(), Debt.DEBT_TYPE_OWED, Debt.DEBT_STATUS_PARTIAL)
                .dueDate(System.currentTimeMillis())
                .note("note 2")
                .build();
        PersonDebt personDebt2 = new PersonDebt(person2, debt2);

        Person person3 = new Person(UUID.randomUUID().toString(), "Mary Jane", "040125789653", "image_uri");
        Debt debt3 = new Debt.Builder(UUID.randomUUID().toString(), person3.getId(), 443420044.23,
                System.currentTimeMillis(), Debt.DEBT_TYPE_OWED, Debt.DEBT_STATUS_PARTIAL)
                .dueDate(System.currentTimeMillis())
                .note("note 3")
                .build();
        PersonDebt personDebt3 = new PersonDebt(person3, debt3);
        mPersonDebts = Lists.newArrayList(personDebt1, personDebt2, personDebt3);
    }

    @Test
    public void shouldBeAbleToLoadAllOweMeDebtsFromRepositoryAndLoadIntoView() {

        mOweMePresenter.onLoadFinished(mock(Loader.class), mPersonDebts);

        verify(mOweMeDebtsView).showDebts(mShowDebtsArgumentCaptor.capture());
        assertThat(mShowDebtsArgumentCaptor.getValue().size(), is(3));
    }

    @Test
    public void shouldBeAbleToLoadAllOweMeDebtsFromRepositoryAndShowEmptyViewIfNotAvailable() {

        mOweMePresenter.onLoadFinished(mock(Loader.class), new ArrayList<PersonDebt>());

        verify(mOweMeDebtsView).showEmptyView();
    }

    @Test
    public void shouldBeAbleToShowErrorWhenOweMeDebtsIsUnavailable() {

        mOweMePresenter.onLoadFinished(mock(Loader.class), null);

        verify(mOweMeDebtsView).showLoadingDebtsError();
    }

    @Test
    public void shouldBeAbleToDeleteOweMeDebt() {

        mOweMePresenter.batchDeletePersonDebts(mPersonDebts, Debt.DEBT_TYPE_OWED);

        verify(mRepository).batchDelete(eq(mPersonDebts), eq(Debt.DEBT_TYPE_OWED));
    }

}
