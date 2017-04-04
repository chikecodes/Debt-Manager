package com.chikeandroid.debtmanager20.adddebt;

import com.chikeandroid.debtmanager20.ApplicationComponent;
import com.chikeandroid.debtmanager20.util.FragmentScoped;

import dagger.Component;

/**
 * Created by Chike on 3/28/2017.
 */
@FragmentScoped
@Component(dependencies = ApplicationComponent.class, modules = AddDebtPresenterModule.class)
public interface AddDebtComponent {

    void inject(AddDebtActivity addDebtActivity);
}
