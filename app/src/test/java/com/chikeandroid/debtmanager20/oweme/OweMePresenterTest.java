package com.chikeandroid.debtmanager20.oweme;

import com.google.common.collect.Lists;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.chikeandroid.debtmanager20.data.Debt;
import com.chikeandroid.debtmanager20.data.Person;
import com.chikeandroid.debtmanager20.data.PersonDebt;
import com.chikeandroid.debtmanager20.data.loaders.DebtsLoader;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by Chike on 4/16/2017.
 * Unit tests for the implementation of {@link OweMeDebtsPresenter
 */

public class OweMePresenterTest {

    private static List<PersonDebt> mPersonDebts;

    @Mock
    private OweMeDebtsContract.View mOweMeDebtsView;

    @Captor
    private ArgumentCaptor<List> mShowDebtsArgumentCaptor;

    @Mock
    private DebtsLoader mDebtsLoader;

    @Mock
    private LoaderManager mLoaderManager;

    private OweMeDebtsPresenter mOweMeDebtsPresenter;

    @Before
    public void setUpOweMeDebtsPresenter() {
        MockitoAnnotations.initMocks(this);

        mOweMeDebtsPresenter = new OweMeDebtsPresenter(mOweMeDebtsView,
                mLoaderManager, mDebtsLoader);

        Person person1 = new Person(UUID.randomUUID().toString(), "Chike Mgbemena", "07038111534");

        String personId = person1.getId();

        Debt debt1 = new Debt.Builder(UUID.randomUUID().toString(), personId, 5000.87, System.currentTimeMillis(), Debt.DEBT_TYPE_OWED,
                Debt.DEBT_STATUS_PARTIAL)
                .dueDate(System.currentTimeMillis())
                .note("school fees")
                .build();
        PersonDebt personDebt1 = new PersonDebt(person1, debt1);

        Person person2 = new Person(UUID.randomUUID().toString(), "Chinedu Mandu", "08047541254");

        String personId2 = person2.getId();

        Debt debt2 = new Debt.Builder(UUID.randomUUID().toString(), personId2, 87000.00, System.currentTimeMillis(), Debt.DEBT_TYPE_OWED,
                Debt.DEBT_STATUS_PARTIAL)
                .dueDate(System.currentTimeMillis())
                .note("note 2")
                .build();
        PersonDebt personDebt2 = new PersonDebt(person2, debt2);

        Person person3 = new Person(UUID.randomUUID().toString(), "Mary Jane", "040125789653");

        String personId3 = person3.getId();

        Debt debt3 = new Debt.Builder(UUID.randomUUID().toString(), personId3, 443420044.23, System.currentTimeMillis(), Debt.DEBT_TYPE_OWED,
                Debt.DEBT_STATUS_PARTIAL)
                .dueDate(System.currentTimeMillis())
                .note("note 3")
                .build();
        PersonDebt personDebt3 = new PersonDebt(person3, debt3);
        mPersonDebts = Lists.newArrayList(personDebt1, personDebt2, personDebt3);
    }

    @Test
    public void shouldBeAbleToLoadAllDebtsFromRepositoryAndLoadIntoView() {

        mOweMeDebtsPresenter.onLoadFinished(mock(Loader.class), mPersonDebts);

        verify(mOweMeDebtsView).showDebts(mShowDebtsArgumentCaptor.capture());
        assertThat(mShowDebtsArgumentCaptor.getValue().size(), is(3));
    }

    @Test
    public void shouldBeAbleToShowErrorWhenDebtsIsUnavailable() {

        mOweMeDebtsPresenter.onLoadFinished(mock(Loader.class), null);

        verify(mOweMeDebtsView).showLoadingDebtsError();
    }

}
