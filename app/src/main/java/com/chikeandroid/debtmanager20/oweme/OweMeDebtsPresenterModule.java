package com.chikeandroid.debtmanager20.oweme;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;

import com.chikeandroid.debtmanager20.data.source.PersonDebtsRepository;
import com.chikeandroid.debtmanager20.data.loaders.DebtsLoader;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Chike on 4/14/2017.
 * This is a Dagger module. We use this to pass in the View dependency to the
 * {@link OweMeDebtsPresenter}.
 */
@Module
public class OweMeDebtsPresenterModule {

    private final OweMeDebtsContract.View mView;
    private final Fragment mContext;

    public OweMeDebtsPresenterModule(OweMeDebtsContract.View view) {
        mView = view;
        mContext = (Fragment) view;
    }

    @Provides
    OweMeDebtsContract.View provideOweMeDebtsContractView() {
        return mView;
    }

    @Provides
    LoaderManager providesLoaderManager() {
        return mContext.getLoaderManager();
    }

    @Provides
    DebtsLoader providesOweMeDebtsLoader(Context context, PersonDebtsRepository repository) {
        return new DebtsLoader(context, repository);
    }
}
