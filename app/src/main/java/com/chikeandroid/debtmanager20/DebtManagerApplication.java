package com.chikeandroid.debtmanager20;

import android.app.Application;


/**
 * Created by Chike on 3/24/2017.
 * Application class
 */

public class DebtManagerApplication extends Application{

    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        // no need to include the applicationModule since it requires no constructor argument
        mApplicationComponent = DaggerApplicationComponent.builder().context(getApplicationContext()).build();

       /* if(BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }*/
    }

    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }
}
