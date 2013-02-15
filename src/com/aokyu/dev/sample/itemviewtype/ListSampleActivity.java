/*
 * Copyright 2013 Yu AOKI
 */

package com.aokyu.dev.sample.itemviewtype;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;

public class ListSampleActivity extends Activity {

    private boolean mTransactionAllowed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_screen);

        mTransactionAllowed = true;
        showListFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTransactionAllowed = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mTransactionAllowed = false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mTransactionAllowed = false;
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_sample, menu);
        return true;
    }

    private void showListFragment() {
        FragmentManager manager = getFragmentManager();

        SampleListFragment fragment =
                (SampleListFragment) manager.findFragmentByTag(SampleListFragment.TAG);
        if (fragment == null) {
            fragment = SampleListFragment.newInstance();
        }

        showFragment(fragment);
    }

    private void showFragment(Fragment fragment) {
        if (!isFragmentTransactionAllowed()) {
            return;
        }

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container_view, fragment);
        transaction.commit();
    }

    public boolean isFragmentTransactionAllowed() {
        return mTransactionAllowed;
    }
}
