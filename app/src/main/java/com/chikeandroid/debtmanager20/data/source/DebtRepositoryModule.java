package com.chikeandroid.debtmanager20.data.source;

import android.content.Context;

import com.chikeandroid.debtmanager20.data.source.local.DebtsLocalDataSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Chike on 3/27/2017.
 */
@Module
public class DebtRepositoryModule {

    @Singleton
    @Provides
    @Local
    DebtsDataSource providesDebtsLocalDataSource(Context context) {
        return new DebtsLocalDataSource(context);
    }
}
