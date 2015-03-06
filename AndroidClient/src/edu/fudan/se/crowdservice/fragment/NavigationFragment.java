package edu.fudan.se.crowdservice.fragment;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.*;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;
import edu.fudan.se.crowdservice.R;
import edu.fudan.se.crowdservice.core.Template;
import edu.fudan.se.crowdservice.core.TemplateFactory;
import edu.fudan.se.crowdservice.felix.TemplateManager;
import edu.fudan.se.crowdservice.view.ConsumerDrawerView;
import org.osgi.service.obr.Requirement;

/**
 * Created by Dawnwords on 2015/2/23.
 */
public class NavigationFragment extends Fragment {
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private View mFragmentContainerView;
    private NavigationDrawerCallbacks mCallbacks;
    private View workerView;
    private ConsumerDrawerView consumerView;
    private DrawerAdapter adapter = new DrawerAdapter();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        consumerView = new ConsumerDrawerView(inflater);
        workerView = inflater.inflate(R.layout.list_item_navi_worker, null);

        mDrawerListView = (ListView) inflater.inflate(R.layout.list_navigation, container, false);
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
        mDrawerListView.setAdapter(adapter);
        return mDrawerListView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCallbacks.onNavigationDrawerItemSelected("Consumer");
        mCallbacks.onNavigationDrawerItemSelected("ConsumerSession");
        mCallbacks.onNavigationDrawerItemSelected("TaskSubmit");
        selectItem(0);
    }

    private void toast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        initActionBar();
        initDrawerToggle(drawerLayout);
        initDrawerLayout(drawerLayout);
    }

    private void initDrawerLayout(DrawerLayout drawerLayout) {
        mDrawerLayout = drawerLayout;
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void initDrawerToggle(final DrawerLayout drawerLayout) {
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, R.drawable.ic_drawer,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionMenu();
            }

            private void invalidateOptionMenu() {
                if (isAdded()) {
                    getActivity().invalidateOptionsMenu();
                }
            }
        };
    }

    private void initActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
    }

    private void selectItem(int position) {
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }

        String navigationName = adapter.getItem(position);
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(navigationName);
        }
        setActionBarTitle(navigationName);
    }

    private void setActionBarTitle(String navigationName) {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(navigationName);
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    public void setTemplateSelectCallBack(final TemplateSelectCallbacks callBack) {
        consumerView.setTemplateResolveListener(new TemplateManager.OnTemplateResolvedListener() {
            @Override
            public void onTemplateResolved(TemplateFactory templateFactory) {
                toast(templateFactory.getClass().getName());
                callBack.onTemplateSelected(templateFactory);
                selectItem(1); // Set Consumer Tag As Checked
            }

            @Override
            public void onFailure(Requirement[] unsatisfiedRequirements) {
                String output = "";
                for (Requirement requirement : unsatisfiedRequirements) {
                    output += String.format("%s:%s\n", requirement.getName(), requirement.getComment());
                }
                toast(output);
            }
        });
    }

    public void setTemplateManager(TemplateManager tm) {
        consumerView.setTemplateManager(tm);
    }

    public static interface NavigationDrawerCallbacks {
        void onNavigationDrawerItemSelected(String title);
    }

    public static interface TemplateSelectCallbacks {
        void onTemplateSelected(TemplateFactory template);
    }

    private class DrawerAdapter extends BaseAdapter {
        final String[] data = {"Worker", "Consumer"};

        @Override
        public int getCount() {
            return data.length;
        }

        @Override
        public String getItem(int position) {
            return data[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return position == 0 ? workerView : consumerView;
        }
    }
}