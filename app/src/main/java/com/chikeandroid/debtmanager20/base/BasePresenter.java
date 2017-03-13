package com.chikeandroid.debtmanager20.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by Kornet-Mac-4 on 19/10/2016.
 */

public class BasePresenter<T extends MvpView> implements Presenter<T>  {

    private T mMvpView;

    @Override
    public void attachView(T mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void detachView() {
        mMvpView = null;
    }

    public boolean isViewAttached() {
        return mMvpView != null;
    }

    public T getMvpView() {
        return mMvpView;
    }

    public void checkViewAttached() {
        if (!isViewAttached()) throw new MvpViewNotAttachedException();
    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Please call Presenter.attachView(MvpView) before" +
                    " requesting data to the Presenter");
        }
    }

    @Override
    public void start(@Nullable Bundle args) {

    }

    @Override
    public void stop() {

    }
}
