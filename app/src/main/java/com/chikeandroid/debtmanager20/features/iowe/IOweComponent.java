package com.chikeandroid.debtmanager20.features.iowe;

import com.chikeandroid.debtmanager20.ApplicationComponent;
import com.chikeandroid.debtmanager20.util.FragmentScoped;

import dagger.Component;

/**
 * Created by Chike on 5/1/2017.
 * This is a Dagger component.
 * Because this component depends on the {@link ApplicationComponent}, which is a singleton, a
 * scope must be specified. All fragment components use a custom scope for this purpose.
 */
@FragmentScoped
@Component(dependencies = ApplicationComponent.class, modules = IOwePresenterModule.class)
public interface IOweComponent {

    void inject(IOweFragment fragment);
}
