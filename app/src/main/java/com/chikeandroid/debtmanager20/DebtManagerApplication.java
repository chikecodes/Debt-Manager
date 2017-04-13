package com.chikeandroid.debtmanager20;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by Chike on 3/24/2017.
 */

public class DebtManagerApplication extends Application{

    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(getApplicationContext()))
                .build();

        if(BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }
    }

    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }
}
