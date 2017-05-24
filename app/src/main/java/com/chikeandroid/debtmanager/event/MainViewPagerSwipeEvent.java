package com.chikeandroid.debtmanager.event;


public class MainViewPagerSwipeEvent {

    public String mMessage;

    public MainViewPagerSwipeEvent(String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }
}
