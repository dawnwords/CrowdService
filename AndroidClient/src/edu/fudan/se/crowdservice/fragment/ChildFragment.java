package edu.fudan.se.crowdservice.fragment;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import edu.fudan.se.crowdservice.R;

/**
 * Created by Dawnwords on 2015/3/3.
 */
public abstract class ChildFragment<T> extends BaseFragment<T> {
    private String parentTag;

    public ChildFragment(int emptyStringResId, int itemLayoutId, String parentTag) {
        super(emptyStringResId, itemLayoutId);
        this.parentTag = parentTag;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (isVisible()) {
            inflater.inflate(R.menu.consumer_session, menu);
            ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_back) {
            ((NavigationFragment.NavigationDrawerCallbacks) getActivity()).onNavigationDrawerItemSelected(parentTag);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
