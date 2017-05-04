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
    Map<String, PersonDebt> mCacheOwed;
    Map<String, PersonDebt> mCacheIOwe;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    boolean mCacheOwedIsDirty;
    boolean mCacheIOweIsDirty;

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

    private void notifyContentObserver(int debtType) {
        for(DebtsRepositoryObserver observer : mObservers) {
            observer.onDebtsChanged(debtType);
        }
    }

    public boolean cachedOwedDebtsAvailable() {
        return mCacheOwed != null && !mCacheOwedIsDirty;
    }

    public boolean cachedIOweDebtsAvailable() {
        return mCacheIOwe != null && !mCacheIOweIsDirty;
    }

    public List<PersonDebt> getCacheOwed() {
        return mCacheOwed == null ? null : new ArrayList<>(mCacheOwed.values());
    }

    public List<PersonDebt> getCacheIOwe() {
        return mCacheIOwe == null ? null : new ArrayList<>(mCacheIOwe.values());
    }

    public PersonDebt getCachedOwedDebt(String debtId) {
        return mCacheOwed.get(debtId);
    }

    public PersonDebt getCachedIOweDebt(String debtId) {
        return mCacheIOwe.get(debtId);
    }

    @Override
    public PersonDebt getPersonDebt(@NonNull String debtId, @NonNull int debtType) {
        checkNotNull(debtId);
        checkNotNull(debtType);

        PersonDebt cachedPersonDebt = null;
        if(debtType == Debt.DEBT_TYPE_OWED) {

            cachedPersonDebt = getDebtOwedById(debtId);
        }else if(debtType == Debt.DEBT_TYPE_IOWE) {
            cachedPersonDebt = getDebtIOweById(debtId);
        }

        // Respond immediately with cache if we have one
        if(cachedPersonDebt != null) {
            return cachedPersonDebt;
        }

        // Is the task in the local data source? If not, query the network.
        /*if (task == null) {
            task = mTasksRemoteDataSource.getTask(taskId);
        }*/

        return mDebtsLocalDataSource.getPersonDebt(debtId, debtType);
    }

    @Nullable
    private PersonDebt getDebtOwedById(@NonNull String debtId) {
        checkNotNull(debtId);
        if(mCacheOwed == null || mCacheOwed.isEmpty()) {
            return null;
        }else {
            return mCacheOwed.get(debtId);
        }
    }

    @Nullable
    private PersonDebt getDebtIOweById(@NonNull String debtId) {
        checkNotNull(debtId);
        if(mCacheIOwe == null || mCacheIOwe.isEmpty()) {
            return null;
        }else {
            return mCacheIOwe.get(debtId);
        }
    }

    @Override
    public List<PersonDebt> getAllPersonDebts() {

        /*List<PersonDebt> personDebts = null;

        if(!mCacheOwedIsDirty) {
            // Respond immediately with cache if available and not dirty
            if(mCacheOwed == null) {
                // Query the local Storage if available
                personDebts = mDebtsLocalDataSource.getAllPersonDebts();
            } else {
                personDebts = getCacheOwed();
                return personDebts;
            }
        }

        processLoadedDebts(personDebts);

        return getCacheOwed();*/
        return null;
    }

    @Override
    public List<PersonDebt> getAllPersonDebtsByType(@NonNull int debtType) {
        checkNotNull(debtType);

        List<PersonDebt> personDebts = null;
        if(debtType == Debt.DEBT_TYPE_OWED) {
            if (!mCacheOwedIsDirty) {
                if (mCacheOwed == null) {
                    personDebts = mDebtsLocalDataSource.getAllPersonDebtsByType(Debt.DEBT_TYPE_OWED);
                } else {
                    personDebts = new ArrayList<>();
                    for (PersonDebt personDebt : getCacheOwed()) {
                        personDebts.add(personDebt);
                    }
                    return personDebts;
                }
            }
        }else if(debtType == Debt.DEBT_TYPE_IOWE) {
            if (!mCacheIOweIsDirty) {
                if (mCacheIOwe == null) {
                    personDebts = mDebtsLocalDataSource.getAllPersonDebtsByType(Debt.DEBT_TYPE_IOWE);
                } else {
                    personDebts = new ArrayList<>();
                    for (PersonDebt personDebt : getCacheIOwe()) {
                          personDebts.add(personDebt);
                    }
                    return personDebts;
                }
            }
        }

        processLoadedDebts(personDebts, debtType);

        return personDebts;
    }

    private void processLoadedDebts(List<PersonDebt> personDebts, @NonNull int debtType) {

        if(personDebts == null) {
            mCacheOwed = null;
            mCacheOwedIsDirty = false;
            return;
        }

        if(debtType == Debt.DEBT_TYPE_OWED) {
            if (mCacheOwed == null) {
                mCacheOwed = new LinkedHashMap<>();
            }
            mCacheOwed.clear();

            for(PersonDebt personDebt : personDebts) {
                mCacheOwed.put(personDebt.getDebt().getId(), personDebt);
            }
            mCacheOwedIsDirty = false;

        }else if(debtType == Debt.DEBT_TYPE_IOWE) {
            if (mCacheIOwe == null) {
                mCacheIOwe = new LinkedHashMap<>();
            }
            mCacheIOwe.clear();

            for(PersonDebt personDebt : personDebts) {
                mCacheIOwe.put(personDebt.getDebt().getId(), personDebt);
            }
            mCacheIOweIsDirty = false;
        }
    }

    @Override
    public void savePersonDebt(@NonNull Debt debt, @NonNull Person person) {
        checkNotNull(debt);
        checkNotNull(person);

        mDebtsLocalDataSource.savePersonDebt(debt, person);

        // Do in memory cache update to keep the app UI up to date
        if(debt.getDebtType() == Debt.DEBT_TYPE_OWED) {
            if (mCacheOwed == null) {
                mCacheOwed = new LinkedHashMap<>();
            }
            PersonDebt personDebt = new PersonDebt(person, debt);
            mCacheOwed.put(debt.getId(), personDebt);
        }else if(debt.getDebtType() == Debt.DEBT_TYPE_IOWE) {
            if (mCacheIOwe == null) {
                mCacheIOwe = new LinkedHashMap<>();
            }
            PersonDebt personDebt = new PersonDebt(person, debt);
            mCacheIOwe.put(debt.getId(), personDebt);
        }

        //update the UI
       notifyContentObserver(debt.getDebtType());
    }

    @Override
    public void refreshDebts() {
        mCacheOwedIsDirty = true;
        // notifyContentObserver();
    }

    @Override
    public void deleteAllPersonDebts() {

        mDebtsLocalDataSource.deleteAllPersonDebts();

        if(mCacheOwed == null) {
            mCacheOwed = new LinkedHashMap<>();
        }
        mCacheOwed.clear();

        if(mCacheIOwe == null) {
            mCacheIOwe = new LinkedHashMap<>();
        }
        mCacheIOwe.clear();

        // update the UI
        // notifyContentObserver();
    }

    @Override
    public void deletePersonDebt(@NonNull PersonDebt personDebt) {

        checkNotNull(personDebt);
        mDebtsLocalDataSource.deletePersonDebt(personDebt);

        if(personDebt.getDebt().getDebtType() == Debt.DEBT_TYPE_OWED) {
            mCacheOwed.remove(personDebt.getDebt().getId());
        }else if(personDebt.getDebt().getDebtType() == Debt.DEBT_TYPE_IOWE) {
            mCacheIOwe.remove(personDebt.getDebt().getId());
        }

        // Update the UI
        notifyContentObserver(personDebt.getDebt().getDebtType());
    }

    @Override
    public void deleteAllPersonDebtsByType(@NonNull int debtType) {

        checkNotNull(debtType);
        mDebtsLocalDataSource.deleteAllPersonDebtsByType(debtType);

        if(debtType == Debt.DEBT_TYPE_OWED) {
            if (mCacheOwed != null) {
                mCacheOwed.clear();
            }
        }else if(debtType == Debt.DEBT_TYPE_IOWE) {
            if (mCacheIOwe != null) {
                mCacheIOwe.clear();
            }
        }
    }

    @Override
    public void updatePersonDebt(@NonNull PersonDebt personDebt) {

        checkNotNull(personDebt);
        mDebtsLocalDataSource.updatePersonDebt(personDebt);

        Debt debt = personDebt.getDebt();
        if(debt.getDebtType() == Debt.DEBT_TYPE_OWED) {
            mCacheOwed.remove(debt.getId());
            mCacheOwed.put(debt.getId(), personDebt);
        }else if(debt.getDebtType() == Debt.DEBT_TYPE_IOWE) {
            mCacheIOwe.remove(debt.getId());
            mCacheIOwe.put(debt.getId(), personDebt);
        }

        //update the UI
        notifyContentObserver(personDebt.getDebt().getDebtType());
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

    public interface DebtsRepositoryObserver {
        void onDebtsChanged(int debtType);
    }
}
