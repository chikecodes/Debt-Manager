package com.chikeandroid.debtmanager20;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.chikeandroid.debtmanager20.data.source.Local;
import com.chikeandroid.debtmanager20.data.source.PersonDebtsDataSource;
import com.chikeandroid.debtmanager20.data.source.local.PersonDebtsLocalDataSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Chike on 3/24/2017.
 */

@Module
public class ApplicationModule {

    @Provides
    @Singleton
    public Resources providesResources(Context context) {
        return context.getResources();
    }

    @Provides
    @Singleton
    public SharedPreferences providesSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Singleton
    @Provides
    @Local
    PersonDebtsDataSource providesDebtsLocalDataSource(Context context) {
        return new PersonDebtsLocalDataSource(context);
    }
}
