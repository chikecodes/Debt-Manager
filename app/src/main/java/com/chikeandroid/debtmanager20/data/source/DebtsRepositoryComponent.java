package com.chikeandroid.debtmanager20.data.source;

import com.chikeandroid.debtmanager20.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Chike on 3/27/2017.
 */
@Singleton
@Component(modules = {DebtRepositoryModule.class, ApplicationModule.class})
public interface DebtsRepositoryComponent {

    DebtsRepository getDebtsRepository();
}
