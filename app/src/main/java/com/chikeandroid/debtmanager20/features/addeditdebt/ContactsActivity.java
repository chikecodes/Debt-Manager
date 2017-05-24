package com.chikeandroid.debtmanager20.features.addeditdebt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;

import com.chikeandroid.debtmanager20.R;

/**
 * Created by Chike on 4/8/2017.
 */

public class ContactsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        setResult(Activity.RESULT_OK, createResultData("896-745-231"));
        finish();
    }

    @VisibleForTesting
    static Intent createResultData(String phoneNumber) {
        final Intent resultData = new Intent();
        //resultData.putExtra(KEY_PHONE_NUMBER, phoneNumber);
        return resultData;
    }
}