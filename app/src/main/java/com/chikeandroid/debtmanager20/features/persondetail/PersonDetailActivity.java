package com.chikeandroid.debtmanager20.features.persondetail;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.chikeandroid.debtmanager20.base.SingleFragmentActivity;
import com.chikeandroid.debtmanager20.data.Person;

/**
 * Created by Chike on 4/20/2017.
 * Displays the Person Detail screen
 */
public class PersonDetailActivity extends SingleFragmentActivity {

    public static void start(Context context, Person person) {
        Intent intent = new Intent(context, PersonDetailActivity.class);
        intent.putExtra(PersonDetailFragment.EXTRA_PERSON, person);
        context.startActivity(intent);
    }

    @Override
    protected Fragment createFragment() {

        Person person = getIntent().getParcelableExtra(PersonDetailFragment.EXTRA_PERSON);

        return PersonDetailFragment.newInstance(person);
    }
}
