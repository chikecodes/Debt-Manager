package com.chikeandroid.debtmanager20.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chikeandroid.debtmanager20.data.Debt;
import com.chikeandroid.debtmanager20.data.Person;
import com.chikeandroid.debtmanager20.data.PersonDebt;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Chike on 3/24/2017.
 * Concrete implementation to load PersonDebts from the data sources into a cache.
 */
@Singleton
public class PersonDebtsRepository implements PersonDebtsDataSource {

    private final PersonDebtsDataSource mDebtsLocalDataSource;

    // private final DebtsDataSource mDebtsRemoteDataSource;

    private final List<DebtsRepositoryObserver> mObservers = new ArrayList<>();

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    Map<String, PersonDebt> mCachedDebts;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    boolean mCacheIsDirty;

    @Inject
    public PersonDebtsRepository(@Local PersonDebtsDataSource debtsLocalDataSource) {
        mDebtsLocalDataSource = debtsLocalDataSource;
    }

    public void addContentObserver(DebtsRepositoryObserver observer) {
        if(!mObservers.contains(observer)) {
            mObservers.add(observer);
        }
    }

    public void removeContentObserver(DebtsRepositoryObserver observer) {
        if(mObservers.contains(observer)) {
            mObservers.remove(observer);
        }
    }

    private void notifyContentObserver() {
        for(DebtsRepositoryObserver observer : mObservers) {
            observer.onDebtsChanged();
        }
    }

    public boolean cachedDebtsAvailable() {
        return mCachedDebts != null && !mCacheIsDirty;
    }

    public List<PersonDebt> getCachedDebts() {
        return mCachedDebts == null ? null : new ArrayList<>(mCachedDebts.values());
    }

    public PersonDebt getCachedDebt(String debtId) {
        return mCachedDebts.get(debtId);
    }

    @Override
    public PersonDebt getPersonDebt(@NonNull String debtId) {
        checkNotNull(debtId);

        PersonDebt cachedPersonDebt = getDebtById(debtId);

        // Respond immediately with cache if we have one
        if(cachedPersonDebt != null) {
            return cachedPersonDebt;
        }

        // Is the task in the local data source? If not, query the network.
        /*if (task == null) {
            task = mTasksRemoteDataSource.getTask(taskId);
        }*/

        return mDebtsLocalDataSource.getPersonDebt(debtId);
    }

    @Nullable
    private PersonDebt getDebtById(@NonNull String debtId) {
        checkNotNull(debtId);
        if(mCachedDebts == null || mCachedDebts.isEmpty()) {
            return null;
        }else {
            return mCachedDebts.get(debtId);
        }
    }

    @Override
    public List<PersonDebt> getAllPersonDebts() {

        List<PersonDebt> personDebts = null;

        if(!mCacheIsDirty) {
            // Respond immediately with cache if available and not dirty
            if(mCachedDebts == null) {
                // Query the local Storage if available
                personDebts = mDebtsLocalDataSource.getAllPersonDebts();
            } else {
                personDebts = getCachedDebts();
                return personDebts;
            }
        }

        processLoadedDebts(personDebts);

        return getCachedDebts();
    }

    @Override
    public List<PersonDebt> getAllPersonDebtsByType(@NonNull int debtType) {
        checkNotNull(debtType);

        List<PersonDebt> personDebts = null;
        if(!mCacheIsDirty) {
            if(mCachedDebts == null) {
                personDebts = mDebtsLocalDataSource.getAllPersonDebtsByType(debtType);
            } else {
                personDebts = new ArrayList<>();
                for(PersonDebt personDebt : getCachedDebts()) {
                    if(personDebt.getDebt().getDebtType() == debtType) {
                        personDebts.add(personDebt);
                    }
                }
                return personDebts;
            }
        }
        return personDebts;
    }

    private void processLoadedDebts(List<PersonDebt> personDebts) {

        if(personDebts == null) {
            mCachedDebts = null;
            mCacheIsDirty = false;
            return;
        }

        if(mCachedDebts == null) {
            mCachedDebts = new LinkedHashMap<>();
        }
        mCachedDebts.clear();

        for(PersonDebt personDebt : personDebts) {
           mCachedDebts.put(personDebt.getDebt().getId(), personDebt);
        }
        mCacheIsDirty = false;
    }

    @Override
    public void savePersonDebt(@NonNull Debt debt, @NonNull Person person) {

        checkNotNull(debt);
        checkNotNull(person);
        mDebtsLocalDataSource.savePersonDebt(debt, person);

        // Do in memory cache update to keep the app UI up to date
        if(mCachedDebts == null) {
            mCachedDebts = new LinkedHashMap<>();
        }
        PersonDebt personDebt = new PersonDebt(person, debt);
        mCachedDebts.put(debt.getId(), personDebt);

        //update the UI
       notifyContentObserver();
    }

    @Override
    public void refreshDebts() {
        mCacheIsDirty = true;
        notifyContentObserver();
    }

    @Override
    public void deleteAllPersonDebts() {

        mDebtsLocalDataSource.deleteAllPersonDebts();

        if(mCachedDebts == null) {
            mCachedDebts = new LinkedHashMap<>();
        }
        mCachedDebts.clear();

        // update the UI
        notifyContentObserver();
    }

    @Override
    public void deletePersonDebt(@NonNull PersonDebt personDebt) {

        checkNotNull(personDebt);
        mDebtsLocalDataSource.deletePersonDebt(personDebt);

        mCachedDebts.remove(personDebt.getDebt().getId());

        // Update the UI
        notifyContentObserver();
    }

    @Override
    public void deleteAllPersonDebtsByType(@NonNull int debtType) {

        checkNotNull(debtType);
        mDebtsLocalDataSource.deleteAllPersonDebtsByType(debtType);

        removeDebtTypeFromCache(debtType);
    }

    @Override
    public void updatePersonDebt(@NonNull PersonDebt personDebt) {

        checkNotNull(personDebt);
        mDebtsLocalDataSource.updatePersonDebt(personDebt);

        Debt debt = personDebt.getDebt();
        mCachedDebts.remove(debt.getId());
        mCachedDebts.put(debt.getId(), personDebt);

        //update the UI
        notifyContentObserver();

    }

    @Override
    public String saveNewPerson(@NonNull Person person) {
        return null;
    }

    @Override
    public Person getPerson(@NonNull String personId) {
        checkNotNull(personId);

        return null;
    }

    @Override
    public void deletePerson(@NonNull String personId) {
        // call database to delete person
    }

    private void removeDebtTypeFromCache(@NonNull int debtType) {

        List<PersonDebt> personDebts = getCachedDebts();
        if(mCachedDebts != null) {
            for(PersonDebt personDebt : personDebts) {
                if(personDebt.getDebt().getDebtType() == debtType) {
                    mCachedDebts.remove(personDebt.getDebt().getId());
                }
            }
        }
    }

    public interface DebtsRepositoryObserver {
        void onDebtsChanged();
    }
}
