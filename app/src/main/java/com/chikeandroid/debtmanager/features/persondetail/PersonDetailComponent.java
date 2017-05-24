package com.chikeandroid.debtmanager.features.persondetail;

import com.chikeandroid.debtmanager.ApplicationComponent;
import com.chikeandroid.debtmanager.util.FragmentScoped;

import dagger.Component;

/**
 * Created by Chike on 4/20/2017.
 * This is a Dagger component. Refer to {@link com.chikeandroid.debtmanager.DebtManagerApplication} for the list of Dagger components
 * used in this application.
 * <P>
 * Because this component depends on the {@link ApplicationComponent}, which is a singleton, a
 * scope must be specified. All fragment components use a custom scope for this purpose.
 */
@FragmentScoped
@Component(dependencies = ApplicationComponent.class, modules = PersonDetailPresenterModule.class)
public interface PersonDetailComponent {

    void inject(PersonDetailFragment personDetailFragment);
}

