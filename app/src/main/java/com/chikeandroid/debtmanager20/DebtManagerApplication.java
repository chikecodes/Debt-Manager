package com.chikeandroid.debtmanager20;

import android.app.Application;

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
    }

    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }
}
