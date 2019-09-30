package com.chivesoft.ewavi;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.google.ads.consent.ConsentForm;
import com.google.ads.consent.ConsentFormListener;
import com.google.ads.consent.ConsentInfoUpdateListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.security.ProviderInstaller;

import java.net.MalformedURLException;
import java.net.URL;

public class Main2Activity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

//    private static final int REQUEST_LOCATION = 1;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;


    private static InputMethodManager inputMethodManager;
    private static ListView listView;
    private static SearchView editTextSearch;
    private static EditText editTextPriceMin;
    private static EditText editTextPriceMax;
    private static Button buttonSearch;
//    private static TextView searchText;
//    private static TextView textViewPrice;
    static ImageView largeImageView;
    private static Context mContext;
    private static DataGetter dataGetter;
    private static AlertDialog dialog;
    private static Handler handler;
    private static Runnable runnable;
//    static Typeface typeface;
//    static Typeface typefaceBold;
    static SharedPreferences prefs;
    static String StringSizeList = "size_list";
    static String StringListEffect = "list_effect";
    static String StringSearchByLocation= "search_by_location";
    static String StringLocationOnly = "location_only";
//    static String StringLocationName = "location_name";

    private boolean fragmentLoaded = false;


    private static Animator mCurrentAnimator;
    private static int mShortAnimationDuration;
    private static View mainView;
    private static Activity mActivity;

    private static ConsentForm form;

    private static int progress_layout_size;

    private static int large_img_max_size;

    static MyGeocoder myGeocoder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progress_layout_size = getResources().getDimensionPixelSize(R.dimen.progress_layout_size);

        mContext = getApplicationContext();
        prefs = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        DataGetter.checkListEffects = prefs.getBoolean(StringListEffect, true);
        DataGetter.maxSizeList = prefs.getInt(StringSizeList, 70);
        DataGetter.checkSearchOptionLocation = prefs.getBoolean(StringSearchByLocation, true);
        DataGetter.checkSearchOptionLocationOnly = prefs.getBoolean(StringLocationOnly, false);

//        typeface = ResourcesCompat.getFont(mContext, R.font.bauhausmedium);
//        typefaceBold = ResourcesCompat.getFont(mContext, R.font.bauhausdemi);

        dataGetter = new DataGetter(mContext, listView);

        setContentView(R.layout.activity_main2);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
//        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), spinnerProvince);
        myGeocoder = new MyGeocoder(mContext);
//        DPI = getResources().getDisplayMetrics().density;
        large_img_max_size = getResources().getDimensionPixelSize(R.dimen.large_img_max_size);

//        TextView title = findViewById(getResources().getIdentifier("action_bar_title", "id", "android"));
//        title.setTypeface(typeface);
        ImageView view = findViewById(android.R.id.home);
        view.setPadding(20, 0, 0, 0);

        //Log.e("version",""+Build.VERSION.SDK_INT);
//        ActionBar bar = getActionBar();
//        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#042d4d")));
//        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffE9AF10")));
        mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
//        getAddress();

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        switch (position) {
            case 0: //header
                if (!fragmentLoaded) {
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.container, PlaceholderFragment.newInstance(position + 1)).commit();
                    fragmentLoaded = true;
                }
                break;
            case 1: //select store
//                dialogSelectStores();
                break;
            case 2: //search list_options
//                dialogSearchOptions();
                break;
            case 3: //list_options
//                dialogOptions();
                break;
        }

    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1: //header
                break;
            case 2: //select store
//                dialogSelectStores();
                break;
            case 3: //search list_options
//                dialogSearchOptions();
                break;
            case 4: //list_options
//                dialogOptions();
                break;
        }
    }

//    public void restoreActionBar() {
//        ActionBar actionBar = getActionBar();
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
//        actionBar.setDisplayShowTitleEnabled(true);
//    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main2, container, false);
            mainView = rootView;
            mActivity = getActivity();
            //inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            listView = rootView.findViewById(R.id.list);

            dataGetter = new DataGetter(mContext, listView);
//            DataGetter.checkListEffects = prefs.getBoolean(StringListEffect, true);
//            DataGetter.maxSizeList = prefs.getInt(StringSizeList, 70);

            editTextSearch = rootView.findViewById(R.id.editTextSearch);
            editTextSearch.setSelected(true);
            editTextSearch.requestFocus();
            editTextSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {
                    click_search();
                    return false;
                }
            });
            editTextPriceMin = rootView.findViewById(R.id.editTextPriceMin);
            editTextPriceMax = rootView.findViewById(R.id.editTextPriceMax);
            buttonSearch = rootView.findViewById(R.id.buttonSearch);
//            textViewPrice = rootView.findViewById(R.id.textViewPriceSearch);

//            editTextPriceMin.setTypeface(typeface);
//            editTextPriceMax.setTypeface(typeface);
//            buttonSearch.setTypeface(typeface);
            buttonSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click_search();
                }
            });
//            searchText = editTextSearch.findViewById(editTextSearch.getContext().getResources().getIdentifier("search_src_text", "id", "android"));
//            searchText.setTypeface(typeface);
//            textViewPrice.setTypeface(typeface);

            largeImageView = rootView.findViewById(R.id.largeImageView);


            inputMethodManager = (InputMethodManager) mActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);

            //inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.RESULT_SHOWN);

            handler = new Handler();
            runnable = new Runnable() {
                public void run() {
                    int count = listView.getCount();
                    Log.i("results", "->" + count);
                    if (count == 0) Toast("No se han encontrado resultados");
                    stopProgressSpinner();
                }
            };
            MobileAds.initialize(mContext, "ca-app-pub-5383536429386669~5060773697");


            final SharedPreferences settings = rootView.getContext().getSharedPreferences("localPreferences", MODE_PRIVATE);
            if (settings.getBoolean("isFirstRun", true)) {
//                new AlertDialog.Builder(rootView.getContext())
//                        .setTitle("Cookies")
//                        .setMessage("Your message for visitors here")
//                        .setNeutralButton("Close message", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                settings.edit().putBoolean("isFirstRun", false).commit();
//                            }
//                        }).show();
                privacy_form(settings);
            }

            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((Main2Activity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
        }

        void Toast(String txt) {
            Toast.makeText(mContext, txt, Toast.LENGTH_LONG).show();
        }

        private void click_search() {
//            ImageView imageView = mainView.findViewById(R.id.imageView);
//            zoomImageFromThumb(imageView, R.drawable.ic_drawer);
            String txtSearch = editTextSearch.getQuery().toString().trim();
            String txtPriceMin = editTextPriceMin.getText().toString().trim();
            String txtPriceMax = editTextPriceMax.getText().toString().trim();
            if (txtSearch.length() < 2) Toast(getString(R.string.min_search_length));
            else if (txtSearch.length() > 30)
                Toast(getString(R.string.max_search_length));
            else if (txtPriceMax.length() > 8 || txtPriceMin.length() > 8)
                Toast(getString(R.string.max_search_price));
            else {
                editTextSearch.setSelected(false);
                listView.requestFocus();
                closeKeyboard();
                boolean anyStoreEnabled = false;
                for (int i = 0; i < DataGetter.isStoreEnabled.length; i++){
                    if (DataGetter.isStoreEnabled[i]) anyStoreEnabled = true;
                }
                if (!anyStoreEnabled) {
                    Toast(getString(R.string.must_activate_stores));
                    return;
                }
                startProgressSpinner();
                dataGetter.removeList();
                if (DataGetter.orderSelected == DataGetter.ORDER_BESTMATCH && txtPriceMin.length() == 0) txtPriceMin = "5";

                if (DataGetter.isStoreEnabled[DataGetter.NUM_WALLAPOP])
                    dataGetter.getWallapop(txtSearch, txtPriceMin, txtPriceMax);
                if (DataGetter.isStoreEnabled[DataGetter.NUM_VIBBO])
                    dataGetter.getVibbo(txtSearch, txtPriceMin, txtPriceMax);
                if (DataGetter.isStoreEnabled[DataGetter.NUM_EBAY])
                    dataGetter.getEbay(txtSearch, txtPriceMin, txtPriceMax);
                if (DataGetter.isStoreEnabled[DataGetter.NUM_MILANUNCIOS])
                    dataGetter.getMilanuncios(txtSearch, txtPriceMin, txtPriceMax);
                if (DataGetter.isStoreEnabled[DataGetter.NUM_CEX])
                    dataGetter.getCeX(txtSearch, txtPriceMin, txtPriceMax);
                if (DataGetter.isStoreEnabled[DataGetter.NUM_CASH_CONVERTERS])
                    dataGetter.getCashConverters(txtSearch, txtPriceMin, txtPriceMax);
                if (DataGetter.isStoreEnabled[DataGetter.NUM_NOLOTIRE])
                    dataGetter.getNolotire(txtSearch, txtPriceMin, txtPriceMax);
                if (DataGetter.isStoreEnabled[DataGetter.NUM_TODOCOLECCION])
                    dataGetter.getTodoColeccion(txtSearch, txtPriceMin, txtPriceMax);
                if (DataGetter.isStoreEnabled[DataGetter.NUM_FNAC])
                    dataGetter.getFnac(txtSearch, txtPriceMin, txtPriceMax);
            /*if (DataGetter.isStoreEnabled[DataGetter.NUM_AMAZON])
                dataGetter.getAmazon(txtSearch, txtPriceMin, txtPriceMax);*/
                //Comprobamos si han salido resultados
                handler.postDelayed(runnable, 20000);   //12 seconds

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_clear:
                clearBoxes();
                return true;
            case R.id.menu_options_item:
                //dialogSelectStores();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void clearBoxes() {
        editTextSearch.setQuery("", false);
        editTextPriceMax.setText("");
        editTextPriceMin.setText("");
        Toast.makeText(mContext, R.string.delete_search_msg, Toast.LENGTH_LONG).show();
    }

    /*private void dialogSelectStores() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.select_store);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.selection_buttons, null);
        builder.setView(dialogView);

        final boolean[] aux = DataGetter.isStoreEnabled.clone();
        builder.setMultiChoiceItems(R.array.store_items, aux, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                aux[which] = DataGetter.isStoreEnabled[which];
            }
        });
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValid = false;
                SparseBooleanArray checkedItems = dialog.getListView().getCheckedItemPositions();
                if (checkedItems != null) {
                    for (int i = 0; i < DataGetter.isStoreEnabled.length; i++) {
                        if (checkedItems.get(i)) {
                            isValid = true;
                            break;
                        }
                    }
                }
                if (!isValid) {
                    Toast("Debes elegir al menos una web de artículos");
                    return;
                }
                for (int i = 0; i < DataGetter.isStoreEnabled.length; i++) {
                    if (checkedItems.get(i) != DataGetter.isStoreEnabled[i])
                        DataGetter.isStoreEnabled[i] = checkedItems.get(i);
                    //Log.e("check","check: "+checkedItems.valueAt(i) + " enabled: "+DataGetter.isStoreEnabled[i]);
                }
                dialog.dismiss();
            }
        });
        ((TextView) dialog.getWindow().findViewById(getResources()
                .getIdentifier("alertTitle", "id", "android"))).setTypeface(typeface);
        ((TextView) dialog.getWindow().findViewById(getResources()
                .getIdentifier("button1", "id", "android"))).setTypeface(typeface);
        ((TextView) dialog.getWindow().findViewById(getResources()
                .getIdentifier("button2", "id", "android"))).setTypeface(typeface);
        ((Button) dialog.getWindow().findViewById(R.id.select_all)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListView list = dialog.getListView();
                for (int i = 0; i < list.getCount(); i++) {
                    aux[i] = true;
                    list.setItemChecked(i, true);
                }
            }
        });
        ((Button) dialog.getWindow().findViewById(R.id.select_nothing)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListView list = dialog.getListView();
                for (int i = 0; i < list.getCount(); i++) {
                    aux[i] = false;
                    list.setItemChecked(i, false);
                }
            }
        });
    }*/

    /*private void dialogOptions() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.select_store);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.list_options, null);
        builder.setView(dialogView);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        final CheckBox checkBoxListEffects = dialog.getWindow().findViewById(R.id.checkBoxListEffects);
        checkBoxListEffects.setChecked(DataGetter.checkListEffects);
        final TextView textViewMaxSizeList = dialog.getWindow().findViewById(R.id.textViewMaxSizeList);
        textViewMaxSizeList.setTypeface(typeface);
        final SeekBar seekBar = dialog.getWindow().findViewById(R.id.sliderSizeList);
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
            }
        });
        seekBar.setProgress((DataGetter.maxSizeList / 10) - 1);
        textViewMaxSizeList.setText(String.format(getString(R.string.max_size_list) + ": %d", DataGetter.maxSizeList));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataGetter.maxSizeList = (seekBar.getProgress() + 1) * 10;
                DataGetter.checkListEffects = checkBoxListEffects.isChecked();
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt(StringSizeList, DataGetter.maxSizeList);
                editor.putBoolean(StringListEffect, DataGetter.checkListEffects);
                editor.commit();
                dialog.dismiss();
            }
        });
        ((TextView) dialog.getWindow().findViewById(getResources()
                .getIdentifier("alertTitle", "id", "android"))).setTypeface(typeface);
        ((TextView) dialog.getWindow().findViewById(getResources()
                .getIdentifier("button1", "id", "android"))).setTypeface(typeface);
        ((TextView) dialog.getWindow().findViewById(getResources()
                .getIdentifier("button2", "id", "android"))).setTypeface(typeface);
    }*/

    /*private void dialogSearchOptions() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.select_store);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.list_options, null);
        builder.setView(dialogView);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        final CheckBox checkBoxListEffects = dialog.getWindow().findViewById(R.id.checkBoxListEffects);
        checkBoxListEffects.setChecked(DataGetter.checkListEffects);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataGetter.checkListEffects = checkBoxListEffects.isChecked();
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(StringListEffect, DataGetter.checkListEffects);
                editor.commit();
                dialog.dismiss();
            }
        });
        ((TextView) dialog.getWindow().findViewById(getResources()
                .getIdentifier("alertTitle", "id", "android"))).setTypeface(typeface);
        ((TextView) dialog.getWindow().findViewById(getResources()
                .getIdentifier("button1", "id", "android"))).setTypeface(typeface);
        ((TextView) dialog.getWindow().findViewById(getResources()
                .getIdentifier("button2", "id", "android"))).setTypeface(typeface);
    }*/

    static void Toast(String txt) {
        Toast.makeText(mContext, txt, Toast.LENGTH_LONG).show();
    }

    public static void stopProgressSpinner() {
        if (handler != null && runnable != null) handler.removeCallbacks(runnable);
        if (dialog != null && dialog.isShowing()) dialog.dismiss();
    }

    private static void startProgressSpinner() {
        ProgressBar progressBar1 = new ProgressBar(mainView.getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(mainView.getContext());
        builder.setView(progressBar1);
        dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
        if (dialog.getWindow() != null) dialog.getWindow().setLayout(progress_layout_size, progress_layout_size);
    }

    protected static void requestLargeImg(String imgUrl) {
        closeKeyboard();
        startProgressSpinner();
//        Log.e("imgUrl",imgUrl);
        RequestQueue queue = Volley.newRequestQueue(mContext);
        ImageRequest imageRequest = new ImageRequest(
                imgUrl, // Image URL
                new Response.Listener<Bitmap>() { // Bitmap listener
                    @Override
                    public void onResponse(Bitmap response) {
                        stopProgressSpinner();
                        zoomImageFromThumb(response);
                    }
                },
                0, // Image width
                0, // Image height
                ImageView.ScaleType.CENTER_INSIDE, // Image scale type
                Bitmap.Config.RGB_565, //Image decode configuration
                new Response.ErrorListener() { // Error listener
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast("Error");
                        stopProgressSpinner();
                    }
                }
        );
        queue.add(imageRequest);
    }

    private static void closeKeyboard() {
//        inputMethodManager.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), 0);
        inputMethodManager.hideSoftInputFromWindow(mainView.getWindowToken(), 0);
    }

    protected static void zoomImageFromThumb(Bitmap image) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ConstraintLayout expandedImageLayout = mainView.findViewById(R.id.largeImageLayout);
        final ImageView expandedImageView = mainView.findViewById(R.id.largeImageView);
        //expandedImageView.setImageResource(imageResId);
        expandedImageView.setImageBitmap(image);
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        mainView.findViewById(R.id.container);
        mainView.getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        float finalWidth = (float)finalBounds.width()*0.8f/imageWidth;
        float finalHeight = (float)finalBounds.height()*0.8f/imageHeight;
        float finalSize;
        if (finalWidth < finalHeight) finalSize = finalWidth;
        else finalSize = finalHeight;

        int padding = (int)(large_img_max_size/finalSize);
        expandedImageView.setPadding(padding,padding,padding,padding);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height() > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }


        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        expandedImageView.setVisibility(View.VISIBLE);
        expandedImageLayout.setVisibility(View.VISIBLE);


        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageLayout.setPivotX(0f);
        expandedImageLayout.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageLayout, View.X, startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageLayout, View.Y, startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScale, finalSize))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, startScale, finalSize));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageLayout, View.X, startBounds.left))
                        .with(ObjectAnimator.ofFloat(expandedImageLayout, View.Y, startBounds.top))
                        .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        expandedImageView.setVisibility(View.GONE);
                        expandedImageLayout.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        expandedImageView.setVisibility(View.GONE);
                        expandedImageLayout.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }

    @Override
    public void onBackPressed() {
        ConstraintLayout expandedImageLayout = mainView.findViewById(R.id.largeImageLayout);
        ImageView expandedImageView = mainView.findViewById(R.id.largeImageView);
        if (expandedImageView != null && expandedImageView.getVisibility() == View.VISIBLE) {
            expandedImageView.setVisibility(View.GONE);
            expandedImageLayout.setVisibility(View.GONE);
            mCurrentAnimator = null;
        } else super.onBackPressed();
    }

    private static void privacy_form(final SharedPreferences settings) {
        URL privacyUrl = null;
        try {
            privacyUrl = new URL("https://raw.githubusercontent.com/Chipipablo/eWaVi/master/privacy_policy.md");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            // Handle error.
        }
        form = new ConsentForm.Builder(mainView.getContext(), privacyUrl)
                .withListener(new ConsentFormListener() {
                    @Override
                    public void onConsentFormLoaded() {
                        // Consent form loaded successfully.
//                        Log.e("consent","loaded");
                        showform();
                    }

                    @Override
                    public void onConsentFormOpened() {
                        // Consent form was displayed.
//                        Log.e("consent","opened");
                    }

                    @Override
                    public void onConsentFormClosed(
                            ConsentStatus consentStatus, Boolean userPrefersAdFree) {
//                        Log.e("consent","closed");

                        /*Bundle extras = new Bundle();
                        extras.putString("npa", "1");

                        AdRequest request = new AdRequest.Builder()
                                .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                                .build();*/

                        settings.edit().putBoolean("isFirstRun", false).commit();
                        // Consent form was closed.
                    }

                    @Override
                    public void onConsentFormError(String errorDescription) {
                        // Consent form error.
//                        Log.e("consent","error: "+errorDescription);
                    }
                })
                .withPersonalizedAdsOption()
//                .withNonPersonalizedAdsOption()
                .build();

        form.load();
        ConsentInformation consentInformation = ConsentInformation.getInstance(mContext);
        String[] publisherIds = {"pub-5383536429386669"};
        consentInformation.requestConsentInfoUpdate(publisherIds, new ConsentInfoUpdateListener() {
            @Override
            public void onConsentInfoUpdated(ConsentStatus consentStatus) {
                // User's consent status successfully updated.
//                    List<AdProvider> adProviders = ConsentInformation.getInstance(mContext).getAdProviders();
                ConsentInformation.getInstance(mContext).setConsentStatus(consentStatus);
            }

            @Override
            public void onFailedToUpdateConsentInfo(String errorDescription) {
                // User's consent status failed to update.
            }
        });
    }

    private static void showform() {
        if (form != null) {
            Log.d("form", "Showing consent form");
            form.show();
        }
    }

    /*private void getAddress() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);
            return;
        }
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                DataGetter.locationLat = location.getLatitude();
                DataGetter.locationLong = location.getLongitude();
                Log.e("locationCity", "1");
                if (DataGetter.locationCityNumber == -1){
                    Log.e("locationCity", "2");
                    DataGetter.locationCityNumber = myGeocoder.getCity(DataGetter.locationLat, DataGetter.locationLong);
                    if (spinnerProvince != null) {
                        spinnerProvince.setSelection(DataGetter.locationCityNumber);
                        Log.e("locationCity", "3");
                    }
                }

                *//*try {
                    Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (addresses != null && addresses.size() > 0) {
//                        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//                        String[] addr = address.split(",");
//                        if (addr.length > 1) DataGetter.locationCity = addr[addr.length-2].trim().toLowerCase().replace(" ","-");
//                        DataGetter.locationCity = addresses.get(0).getSubAdminArea().trim().toLowerCase().replace(" ","-");
                        DataGetter.locationCity = addresses.get(0).getSubAdminArea();
                        String[] arrayProvinces = getResources().getStringArray(R.array.provinces_show);
                        int i;
                        for (i=0; i< arrayProvinces.length; i++){
                            if (arrayProvinces[i].equalsIgnoreCase(DataGetter.locationCity)){
                                DataGetter.locationCityNumber = i;
                                break;
                            }
                        }
                        if (arrayProvinces.length == i) DataGetter.locationCityNumber = -1;
//                        Log.e("locationCity", "city: "+DataGetter.locationCity);
//                        Log.e("locationCity", "city: "+addresses.get(0).getSubAdminArea());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }*//*
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) { }
            @Override
            public void onProviderEnabled(String provider) { }
            @Override
            public void onProviderDisabled(String provider) { }
        };
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 300000, 1000, locationListener);
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
    }*/

}
