package com.chikeandroid.debtmanager20.features.people;

import com.google.common.collect.Lists;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.chikeandroid.debtmanager20.data.Person;
import com.chikeandroid.debtmanager20.data.source.PersonDebtsRepository;
import com.chikeandroid.debtmanager20.features.people.loader.PeopleLoader;

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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by Chike on 5/10/2017.
 * Unit tests for the implementation of {@link PeoplePresenter
 */
public class PeoplePresenterTest {

    private List<Person> mPersons;

    @Mock
    private PeopleContract.View mPeopleView;

    @Captor
    private ArgumentCaptor<List> mShowPeopleArgumentCaptor;

    @Mock
    private PeopleLoader mPeopleLoader;

    @Mock
    private LoaderManager mLoaderManager;

    @Mock
    private PersonDebtsRepository mRepository;

    private PeoplePresenter mPeoplePresenter;

    @Before
    public void setUpPeoplePresenter() {
        MockitoAnnotations.initMocks(this);

        mPeoplePresenter = new PeoplePresenter(mPeopleView, mLoaderManager, mPeopleLoader);
        Person person1 = new Person(UUID.randomUUID().toString(), "Chike Mgbemena", "07038111534", "image_uri");
        Person person2 = new Person(UUID.randomUUID().toString(), "Chinedu Mandu", "08047541254", "image_uri");
        Person person3 = new Person(UUID.randomUUID().toString(), "Mary Jane", "040125789653", "image_uri");
        mPersons = Lists.newArrayList(person1, person2, person3);
    }

    @Test
    public void shouldBeAbleToLoadPeopleFromRepositoryAndLoadIntoView() {

        mPeoplePresenter.onLoadFinished(mock(Loader.class), mPersons);

        verify(mPeopleView).showPeople(mShowPeopleArgumentCaptor.capture());
        assertThat(mShowPeopleArgumentCaptor.getValue().size(), is(3));
    }

    @Test
    public void shouldBeAbleToLoadPeopleFromRepositoryAndShowEmptyViewIfNotAvailable() {

        mPeoplePresenter.onLoadFinished(mock(Loader.class), new ArrayList<Person>());

        verify(mPeopleView).showEmptyView();
    }

    @Test
    public void shouldBeAbleToShowErrorWhenPeopleIsUnavailable() {

        mPeoplePresenter.onLoadFinished(mock(Loader.class), null);

        verify(mPeopleView).showLoadingPeopleError();
    }
}
