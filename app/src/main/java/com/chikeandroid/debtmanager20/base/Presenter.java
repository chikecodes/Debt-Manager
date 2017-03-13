package com.chikeandroid.debtmanager20.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by Chike on 3/13/2017.
 * Every presenter in the app must either implement this interface or extend BasePresenter
 * indicating the MvpView type that wants to be attached with.
 */

public interface Presenter<V extends MvpView> {

    void attachView(V mvpView);

    void detachView();

    void start(@Nullable Bundle args);
    void stop();
}
