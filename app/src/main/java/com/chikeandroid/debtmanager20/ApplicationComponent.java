package com.chikeandroid.debtmanager20;

import com.chikeandroid.debtmanager20.data.source.DebtsRepository;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Chike on 3/24/2017.
 */

@Singleton
@Component(modules = { ApplicationModule.class} )
public interface ApplicationComponent {

    DebtsRepository getDebtsRepository();
}
