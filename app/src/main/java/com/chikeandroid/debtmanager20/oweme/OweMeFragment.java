package com.chikeandroid.debtmanager20.oweme;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chikeandroid.debtmanager20.DebtManagerApplication;
import com.chikeandroid.debtmanager20.R;
import com.chikeandroid.debtmanager20.data.Debt;
import com.chikeandroid.debtmanager20.data.PersonDebt;
import com.chikeandroid.debtmanager20.databinding.OweMeFragmentBinding;
import com.chikeandroid.debtmanager20.debtdetail.DebtDetailActivity;
import com.chikeandroid.debtmanager20.event.MainViewPagerSwipeEvent;
import com.chikeandroid.debtmanager20.oweme.adapter.OweMeAdapter;
import com.chikeandroid.debtmanager20.util.ViewUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Chike on 3/13/2017.
 * Display a List of {@link PersonDebt}s that people owe the user.
 */
public class OweMeFragment extends Fragment implements OweMeContract.View {

    private android.view.ActionMode mActionMode;

    private static final String TAG = "OweMeDebtsFragment";
    private OweMeAdapter mOweMeAdapter;
    private TextView mTextViewEmptyDebts;
    private ActionModeCallback mActionModeCallback = new ActionModeCallback();

    @Inject
    OweMePresenter mOweMePresenter;

    private OweMeContract.Presenter mPresenter;

    public OweMeFragment() {
        // Required empty public constructor
    }

    public static OweMeFragment newInstance() {
        return new OweMeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mOweMeAdapter = new OweMeAdapter(getActivity(), new ArrayList<PersonDebt>(0));
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
        mPresenter.start();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(MainViewPagerSwipeEvent event) {
        if(event != null && mActionMode != null) {
            mActionMode.finish();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated()");
        DaggerOweMeComponent.builder()
                .oweMePresenterModule(new OweMePresenterModule(this))
                .applicationComponent(((DebtManagerApplication) getActivity().getApplication()).getComponent())
                .build()
                .inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        OweMeFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.owe_me_fragment, container, false);
        final View view = binding.getRoot();

        RecyclerView recyclerView = binding.rvOweme;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mOweMeAdapter);
        mTextViewEmptyDebts = binding.tvNoDebts;


        mOweMeAdapter.setOnItemClickListener(new OweMeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, PersonDebt personDebt, int position) {
                if(mActionMode != null) {
                    myToggleSelection(position, view);
                    return;
                }
                DebtDetailActivity.start(getActivity(), personDebt.getDebt().getId(),
                        personDebt.getDebt().getDebtType());
            }
        });

        mOweMeAdapter.setOnItemLongClickListener(new OweMeAdapter.OnItemLongClickListener() {
            @Override
            public void onItemClick(View view, PersonDebt personDebt, int position) {
                mActionMode = getActivity().startActionMode(mActionModeCallback);
                myToggleSelection(position, view);
            }
        });

        return view;
    }

    @Override
    public void showDebts(List<PersonDebt> debts) {

        if(mTextViewEmptyDebts.getVisibility() == View.VISIBLE) {
            mTextViewEmptyDebts.setVisibility(View.GONE);
        }

        mOweMeAdapter.updatePersonDebtListItems(debts);
    }

    @Override
    public void showEmptyView() {
        mOweMeAdapter.updatePersonDebtListItems(new ArrayList<PersonDebt>());
        mTextViewEmptyDebts.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoadingDebtsError() {
        ViewUtil.showToast(getActivity(), getString(R.string.msg_loading_debts_error));
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setPresenter(OweMeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.stop();
        Log.d(TAG, "onStop: ");
    }

    private void myToggleSelection(int position, View view) {
        mOweMeAdapter.toggleSelection(position, view);
        String title = mOweMeAdapter.getSelectedItemCount() + " selected";
        Log.d(TAG, "size is : " + mOweMeAdapter.getSelectedItemCount());
        mActionMode.setTitle(title);

    }

    // Define the callback when ActionMode is activated
    private class ActionModeCallback implements ActionMode.Callback {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.setTitle("Actions");
            mode.getMenuInflater().inflate(R.menu.actions_debt, menu);
            //((AppCompatActivity) getActivity()).getSupportActionBar().hide();
            return true;
        }

        // Called each time the action mode is shown.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:

                    if(mOweMeAdapter.getSelectedItemCount() > 0) {
                        openConfirmDialog();

                    }
                   // mode.finish();
                    return false;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode.finish();
            mActionMode = null;
            mOweMeAdapter.clearSelections();
        }
    }

    private void openConfirmDialog() {
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setMessage("Are you sure you want to delete?")
                .setPositiveButton(getString(R.string.dialog_delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        batchDelete();

                    }
                })
                .setNegativeButton(getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();

        dialog.show();
    }

    public void batchDelete() {

        ProgressDialog progressDialog = ViewUtil.getProgressDialog(getActivity(), "Deleting...");
        progressDialog.show();

        int debtsDeleted = 0;

        List<PersonDebt> deletePersonDebts = new ArrayList<>();
        for(int i = 0; i < mOweMeAdapter.getSelectedItemCount(); i++) {
            int position = mOweMeAdapter.getSelectedItems().keyAt(i);
            PersonDebt personDebt = mOweMeAdapter.getPersonDebt(position);
            deletePersonDebts.add(personDebt);
            debtsDeleted++;
        }

        mPresenter.batchDeletePersonDebts(deletePersonDebts, Debt.DEBT_TYPE_OWED);

        if(debtsDeleted == mOweMeAdapter.getSelectedItemCount()) {
            ViewUtil.showToast(getActivity(), "Debts deleted successfully");
        }

        progressDialog.dismiss();
        mActionMode.finish();
    }
}
