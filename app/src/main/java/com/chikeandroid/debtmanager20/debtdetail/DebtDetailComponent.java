package com.chikeandroid.debtmanager20.debtdetail;

import com.chikeandroid.debtmanager20.ApplicationComponent;
import com.chikeandroid.debtmanager20.DebtManagerApplication;
import com.chikeandroid.debtmanager20.util.FragmentScoped;

import dagger.Component;

/**
 * Created by Chike on 4/20/2017.
 * This is a Dagger component. Refer to {@link DebtManagerApplication} for the list of Dagger components
 * used in this application.
 * <P>
 * Because this component depends on the {@link ApplicationComponent}, which is a singleton, a
 * scope must be specified. All fragment components use a custom scope for this purpose.
 */
@FragmentScoped
@Component(dependencies = ApplicationComponent.class, modules = DebtDetailPresenterModule.class)
public interface DebtDetailComponent {

    void inject(DebtDetailFragment debtDetailFragment);
}

