package com.chikeandroid.debtmanager20;

import android.content.Context;

import com.chikeandroid.debtmanager20.data.source.PersonDebtsRepository;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

/**
 * Created by Chike on 3/24/2017.
 */

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    // https://google.github.io/dagger/users-guide.html#binding-instances
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder context(Context context);
        ApplicationComponent build();
    }

    PersonDebtsRepository getDebtsRepository();

    Context getContext();

}
