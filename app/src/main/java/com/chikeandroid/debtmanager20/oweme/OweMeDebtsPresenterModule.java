package com.chikeandroid.debtmanager20.oweme;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;

import com.chikeandroid.debtmanager20.data.DebtsLoader;
import com.chikeandroid.debtmanager20.data.source.DebtsRepository;

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
    private final FragmentActivity mContext;

    public OweMeDebtsPresenterModule(OweMeDebtsContract.View view, FragmentActivity context) {
        mView = view;
        mContext = context;
    }

    @Provides
    OweMeDebtsContract.View provideOweMeDebtsContractView() {
        return mView;
    }

    @Provides
    LoaderManager providesLoaderManager() {
        return mContext.getSupportLoaderManager();
    }

    @Provides
    DebtsLoader providesOweMeDebtsLoader(Context context, DebtsRepository repository) {
        return new DebtsLoader(context, repository);
    }
}
