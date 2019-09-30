package com.chivesoft.ewavi;


import android.Manifest;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends Fragment {

    private static final int REQUEST_LOCATION = 1;
    MyGeocoder myGeocoder;
//    static Spinner spinnerProvince;
    Spinner spinnerProvince;

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    //    private ListView mDrawerListView;
    private ExpandableListView mDrawerListView;
    private View mFragmentContainerView;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;
    private int imgSize;

    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imgSize = getResources().getDimensionPixelSize(R.dimen.expandablelist_item_image_size);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        // Select either the default item (0) or the last selected item.
        selectItem(mCurrentSelectedPosition);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mDrawerListView = (ExpandableListView) inflater.inflate(R.layout.drawer_main, container, false);
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });

        populateDrawerList();

        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
        return mDrawerListView;
    }

    private void populateDrawerList() {
        Bitmap icon = BitmapFactory.decodeResource(getActionBar().getThemedContext().getResources(), R.mipmap.ewavi8_1024x500);

        RelativeLayout relativeLayout = new RelativeLayout(getActionBar().getThemedContext());
        ImageView imageView = new ImageView(getActionBar().getThemedContext());

        imageView.setImageBitmap(icon);
        imageView.setAdjustViewBounds(true);
        relativeLayout.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
        relativeLayout.addView(imageView);
        mDrawerListView.addHeaderView(relativeLayout);

        HashMap<DrawerListModel, List<View>> listChildData = new HashMap<>();

        String[] stores = getResources().getStringArray(R.array.store_items);
        List<View> viewList1 = new ArrayList<>(stores.length);

        for (int i = 0; i < stores.length; i++) {
            final CheckBox checkBox = new CheckBox(getActionBar().getThemedContext());
            checkBox.setText(stores[i]);
            checkBox.setTextColor(getResources().getColor(android.R.color.black));
            checkBox.setId(i);
            checkBox.setChecked(DataGetter.isStoreEnabled[i]);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    DataGetter.isStoreEnabled[checkBox.getId()] = isChecked;
                }
            });
            viewList1.add(checkBox);
        }

        String[] searchOptionOrderChoice = getResources().getStringArray(R.array.searchOptionOrderChoice);
        List<View> viewList2 = new ArrayList<>(searchOptionOrderChoice.length);
        RadioGroup radioGroupSearchOrder = new RadioGroup(getActionBar().getThemedContext());
        for (int i = 0; i < searchOptionOrderChoice.length; i++) {
            RadioButton radioButton = new RadioButton(getActionBar().getThemedContext());
            radioButton.setText(searchOptionOrderChoice[i]);
            radioButton.setTextColor(getResources().getColor(android.R.color.black));
            radioButton.setId(i);
            radioGroupSearchOrder.addView(radioButton);
        }
        radioGroupSearchOrder.check(DataGetter.orderSelected);
        radioGroupSearchOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                DataGetter.orderSelected = checkedId;
            }
        });
        final CheckBox checkBoxSearchOptionLocationOnly = new CheckBox(getActionBar().getThemedContext());
        checkBoxSearchOptionLocationOnly.setText(R.string.searchOptionLocationOnly);
        checkBoxSearchOptionLocationOnly.setChecked(DataGetter.checkSearchOptionLocationOnly);
        checkBoxSearchOptionLocationOnly.setEnabled(DataGetter.checkSearchOptionLocation);
        checkBoxSearchOptionLocationOnly.setTextColor(getResources().getColor(android.R.color.black));
        checkBoxSearchOptionLocationOnly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataGetter.checkSearchOptionLocationOnly = isChecked;
                SharedPreferences.Editor editor = Main2Activity.prefs.edit();
                editor.putBoolean(Main2Activity.StringLocationOnly, isChecked);
                editor.apply();
            }
        });
        spinnerProvince = new Spinner(getActionBar().getThemedContext(), Spinner.MODE_DIALOG);
//        spinnerProvince = new Spinner(getActionBar().getThemedContext());
        ArrayAdapter<CharSequence> adapterProvinces = ArrayAdapter.createFromResource(getActionBar().getThemedContext(), R.array.provinces_show, R.layout.layout_spinner_provinces);
        adapterProvinces.setDropDownViewResource(R.layout.layout_spinner_dropdown);
//        adapterProvinces.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        spinnerProvince.setAdapter(adapterProvinces);
        if (DataGetter.locationCityNumber == -1) DataGetter.locationCityNumber = 30; //default Madrid
        spinnerProvince.setSelection(DataGetter.locationCityNumber);
        spinnerProvince.setEnabled(DataGetter.checkSearchOptionLocation);
        spinnerProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DataGetter.locationCityNumber = position;
                double[] coordinates = myGeocoder.getCoordinates(position);
                if (coordinates.length == 2){
                    DataGetter.locationLat = coordinates[0];
                    DataGetter.locationLong = coordinates[1];
                }
                DataGetter.locationManuallyChanged = true;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        CheckBox checkBoxSearchOptionLocation = new CheckBox(getActionBar().getThemedContext());
        checkBoxSearchOptionLocation.setText(R.string.searchOptionLocation);
        checkBoxSearchOptionLocation.setChecked(DataGetter.checkSearchOptionLocation);
        checkBoxSearchOptionLocation.setTextColor(getResources().getColor(android.R.color.black));
        checkBoxSearchOptionLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = Main2Activity.prefs.edit();
                editor.putBoolean(Main2Activity.StringSearchByLocation, isChecked);
                editor.apply();
                DataGetter.checkSearchOptionLocation = isChecked;
                DataGetter.checkSearchOptionLocationOnly = isChecked;
                checkBoxSearchOptionLocationOnly.setEnabled(isChecked);
                spinnerProvince.setEnabled(isChecked);
            }
        });
        viewList2.add(checkBoxSearchOptionLocation);

        viewList2.add(checkBoxSearchOptionLocationOnly);
        viewList2.add(spinnerProvince);
        viewList2.add(radioGroupSearchOrder);
        List<View> viewList3 = new ArrayList<>();

        CheckBox checkBoxListEffects = new CheckBox(getActionBar().getThemedContext());
        checkBoxListEffects.setChecked(DataGetter.checkListEffects);
        checkBoxListEffects.setText(R.string.list_effects);
        checkBoxListEffects.setTextColor(getResources().getColor(android.R.color.black));
        checkBoxListEffects.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataGetter.checkListEffects = buttonView.isChecked();
                SharedPreferences.Editor editor = Main2Activity.prefs.edit();
                editor.putBoolean(Main2Activity.StringListEffect, DataGetter.checkListEffects);
                editor.apply();
            }
        });

        LinearLayout layoutOptions = (LinearLayout) LayoutInflater.from(getActionBar().getThemedContext()).inflate(R.layout.options_general, null);

        final TextView textViewMaxSizeList = layoutOptions.findViewById(R.id.textViewMaxSizeList);
        textViewMaxSizeList.setTextColor(getResources().getColor(android.R.color.black));
//        textViewMaxSizeList.setTypeface(Main2Activity.typeface);
        SeekBar seekBar = layoutOptions.findViewById(R.id.sliderSizeList);
        seekBar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        seekBar.setMax(9);
        seekBar.setProgress(6);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = (progress + 1) * 10;
                textViewMaxSizeList.setText(String.format(getString(R.string.max_size_list) + ": %d", progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                DataGetter.maxSizeList = (seekBar.getProgress() + 1) * 10;
                SharedPreferences.Editor editor = Main2Activity.prefs.edit();
                editor.putInt(Main2Activity.StringSizeList, DataGetter.maxSizeList);
                editor.apply();
            }
        });
        seekBar.setProgress((DataGetter.maxSizeList / 10) - 1);
        textViewMaxSizeList.setText(String.format(getString(R.string.max_size_list) + ": %d", DataGetter.maxSizeList));
/*
        Spinner spinnerOrderList = new Spinner(getActionBar().getThemedContext());
        spinnerOrderList.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        spinnerOrderList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DataGetter.orderListSelected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerOrderList.setAdapter();
*/
        String[] orderListChoice = getResources().getStringArray(R.array.orderListChoice);
        RadioGroup radioGroupListOrder = new RadioGroup(getActionBar().getThemedContext());
        for (int i = 0; i < orderListChoice.length; i++) {
            RadioButton radioButton = new RadioButton(getActionBar().getThemedContext());
            radioButton.setText(orderListChoice[i]);
            radioButton.setTextColor(getResources().getColor(android.R.color.black));
            radioButton.setId(i);
            radioGroupListOrder.addView(radioButton);
        }
        radioGroupListOrder.check(DataGetter.orderListSelected);
        radioGroupListOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                DataGetter.orderListSelected = checkedId;
            }
        });
        viewList3.add(radioGroupListOrder);
        viewList3.add(layoutOptions);
        viewList3.add(checkBoxListEffects);

        ArrayList<DrawerListModel> drawerListModels = new ArrayList<>(3);
        icon = BitmapFactory.decodeResource(getActionBar().getThemedContext().getResources(), R.drawable.ic_drawer);
        icon = getResizedBitmap(icon, imgSize, imgSize);
        drawerListModels.add(new DrawerListModel(icon, getResources().getString(R.string.optionSelectStore)));
        icon = BitmapFactory.decodeResource(getActionBar().getThemedContext().getResources(), android.R.drawable.ic_search_category_default);
        icon = getResizedBitmap(icon, imgSize, imgSize);
        drawerListModels.add(new DrawerListModel(icon, getResources().getString(R.string.optionSearch)));
        icon = BitmapFactory.decodeResource(getActionBar().getThemedContext().getResources(), R.mipmap.icon_settings_gray);
        icon = getResizedBitmap(icon, imgSize, imgSize);
        drawerListModels.add(new DrawerListModel(icon, getResources().getString(R.string.optionList)));


        listChildData.put(drawerListModels.get(0), viewList1);
        listChildData.put(drawerListModels.get(1), viewList2);
        listChildData.put(drawerListModels.get(2), viewList3);


        ExpandableListAdapter expandableListAdapter = new ExpandableListAdapter(getActionBar().getThemedContext(), drawerListModels, listChildData);

        mDrawerListView.setAdapter(expandableListAdapter);
        mDrawerListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousItem = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousItem)
                    mDrawerListView.collapseGroup(previousItem);
                previousItem = groupPosition;
            }
        });
    }

    private boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
//    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        myGeocoder = new MyGeocoder(getActivity());
        getAddress();
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                //R.drawable.ic_drawer,             /* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }
                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
//                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                inputMethodManager.hideSoftInputFromWindow(mFragmentContainerView.getWindowToken(), 0);
                if (!isAdded()) {
                    return;
                }

                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }

                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (mDrawerLayout != null && isDrawerOpen()) {
//            inflater.inflate(R.menu.global, menu);

            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        /*if (item.getItemId() == R.id.action_example) {
            Toast.makeText(getActivity(), "Example action.", Toast.LENGTH_SHORT).show();
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(R.string.app_name);
    }

    private ActionBar getActionBar() {
        return getActivity().getActionBar();
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
    }

    private Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
//        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
//        bm.recycle();
//        return resizedBitmap;
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
    }
    private void getAddress() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            return;
        }
        final LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                DataGetter.locationLat = location.getLatitude();
                DataGetter.locationLong = location.getLongitude();
//                if (DataGetter.locationCityNumber == -1){
                if (!DataGetter.locationManuallyChanged){
                    DataGetter.locationCityNumber = myGeocoder.getCity(DataGetter.locationLat, DataGetter.locationLong);
                    if (spinnerProvince != null) {
                        spinnerProvince.setSelection(DataGetter.locationCityNumber);
                    }
                }
                locationManager.removeUpdates(this);
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) { }
            @Override
            public void onProviderEnabled(String provider) { }
            @Override
            public void onProviderDisabled(String provider) { }
        };
        if (locationManager.getAllProviders().contains("passive")) locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 1000, 1, locationListener);
        if (locationManager.getAllProviders().contains("network")) locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, locationListener);
        else if (locationManager.getAllProviders().contains("gps")) locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);

    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getAddress();
            } else {
                // Permission was denied or request was cancelled
                Log.e("locationCity","Location permission not accepted");
            }
        }
    }
}
