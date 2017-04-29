package com.chikeandroid.debtmanager20.util;

import android.support.test.espresso.IdlingResource;

/**
 * Created by Chike on 4/29/2017.
 */

public final class EspressoIdlingResource {

    private static final String RESOURCE = "GLOBAL";

    private static SimpleCountingIdlingResource sSimpleCountingIdlingResource =
            new SimpleCountingIdlingResource(RESOURCE);

    private EspressoIdlingResource() {}

    public static void increment() {
        sSimpleCountingIdlingResource.increment();
    }

    public static void decrement() {
        sSimpleCountingIdlingResource.decrement();
    }

    public static IdlingResource getIdlingResource() {
        return sSimpleCountingIdlingResource;
    }

}
