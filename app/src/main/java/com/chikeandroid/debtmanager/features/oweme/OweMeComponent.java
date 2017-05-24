package com.chikeandroid.debtmanager.features.oweme;

import com.chikeandroid.debtmanager.ApplicationComponent;
import com.chikeandroid.debtmanager.util.FragmentScoped;

import dagger.Component;

/**
 * Created by Chike on 4/14/2017.
 * This is a Dagger component.
 * Because this component depends on the {@link ApplicationComponent}, which is a singleton, a
 * scope must be specified. All fragment components use a custom scope for this purpose.
 */
@FragmentScoped
@Component(dependencies = ApplicationComponent.class, modules = OweMePresenterModule.class)
public interface OweMeComponent {

    void inject(OweMeFragment fragment);
}
