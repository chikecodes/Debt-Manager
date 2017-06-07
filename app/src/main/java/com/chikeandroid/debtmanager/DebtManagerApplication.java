package com.chikeandroid.debtmanager;

import android.app.Application;
import android.os.StrictMode;

import com.facebook.stetho.Stetho;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;


/**
 * Created by Chike on 3/24/2017.
 * Application class
 */

public class DebtManagerApplication extends Application {

    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        // no need to include the applicationModule since it requires no constructor argument
        mApplicationComponent = DaggerApplicationComponent.builder().context(getApplicationContext()).build();

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }

        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build());
        }
    }

    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }
}
