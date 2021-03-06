package com.chikeandroid.debtmanager.features.people;

import com.chikeandroid.debtmanager.ApplicationComponent;
import com.chikeandroid.debtmanager.util.FragmentScoped;

import dagger.Component;

/**
 * Created by Chike on 5/10/2017.
 * This is a Dagger component.
 * Because this component depends on the {@link ApplicationComponent}, which is a singleton, a
 * scope must be specified. All fragment components use a custom scope for this purpose.
 */
@FragmentScoped
@Component(dependencies = ApplicationComponent.class, modules = PeoplePresenterModule.class)
public interface PeopleComponent {

    void inject(PeopleFragment fragment);
}
