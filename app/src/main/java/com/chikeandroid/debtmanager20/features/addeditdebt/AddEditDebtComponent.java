package com.chikeandroid.debtmanager20.features.addeditdebt;

import com.chikeandroid.debtmanager20.ApplicationComponent;
import com.chikeandroid.debtmanager20.util.FragmentScoped;

import dagger.Component;

/**
 * Created by Chike on 3/28/2017.
 * This is a Dagger component. Refer to {@link com.chikeandroid.debtmanager20.DebtManagerApplication} for the list of Dagger components
 * used in this application.
 * <P>
 * Because this component depends on the {@link com.chikeandroid.debtmanager20.ApplicationComponent}, which is a singleton, a
 * scope must be specified. All fragment components use a custom scope for this purpose.
 */
@FragmentScoped
@Component(dependencies = ApplicationComponent.class, modules = AddEditDebtPresenterModule.class)
public interface AddEditDebtComponent {

    void inject(AddEditDebtActivity addEditDebtActivity);
}
