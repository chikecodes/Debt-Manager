package com.chikeandroid.debtmanager20.data.source;

import android.support.annotation.NonNull;

import com.chikeandroid.debtmanager20.data.Debt;
import com.chikeandroid.debtmanager20.data.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Chike on 3/24/2017.
 */

public class DebtsRepository implements DebtsDataSource {

    private static DebtsRepository INSTANCE = null;

    private final DebtsDataSource mDebtsLocalDataSource;

    private final DebtsDataSource mDebtsRemoteDataSource;

    private List<DebtsRepositoryObserver> mObservers = new ArrayList<>();

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    Map<String, Debt> mCachedTasks;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    boolean mCacheIsDirty;

    public static DebtsRepository getInstance(DebtsDataSource debtsRemoteDataSource, DebtsDataSource debtsLocalDataSource) {

        if(INSTANCE == null) {
            INSTANCE = new DebtsRepository(debtsRemoteDataSource, debtsLocalDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    // prevent direct instantiation
    private DebtsRepository(@NonNull DebtsDataSource debtsRemoteDataSource, @NonNull DebtsDataSource debtsLocalDataSource) {

        mDebtsRemoteDataSource = debtsRemoteDataSource;
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

    @Override
    public void getDebts(@NonNull LoadDebtsCallback callback) {

    }

    public boolean cachedDebtsAvailable() {
        return mCachedTasks != null && !mCacheIsDirty;
    }

    public List<Debt> getCachedDebts() {
        return mCachedTasks == null ? null : new ArrayList<>(mCachedTasks.values());
    }

    private void saveDebtInLocalDataSource(List<Debt> debts) {

    }

    @Override
    public void getDebt(@NonNull String debtId, @NonNull GetDebtCallback callback) {

    }

    @Override
    public void getIOwedDebts(@NonNull LoadDebtsCallback callback) {

    }

    @Override
    public void getOwedDebts(@NonNull LoadDebtsCallback callback) {

    }

    @Override
    public void saveDebt(@NonNull Debt debt, @NonNull Person person) {

        checkNotNull(debt);
        checkNotNull(person);
        mDebtsLocalDataSource.saveDebt(debt, person);

//        // Do in memory cache update to keep the app UI up to date
//        if(mCachedTasks == null) {
//            mCachedTasks = new LinkedHashMap<>();
//        }
//        mCachedTasks.put()

        // update the UI
       // notifyContentObserver();
    }

    @Override
    public void refreshDebts() {
        mCacheIsDirty = true;
        notifyContentObserver();
    }

    @Override
    public void deleteAllDebts() {

    }

    @Override
    public void deleteDebt(@NonNull String debtId) {

    }

    public interface DebtsRepositoryObserver {
        void onDebtsChanged();
    }
}
