package androlite.androidfragmentsearchview;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.androidquery.AQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import androlite.androidfragmentsearchview.Adapters.CustomAdapter_Objects;
import androlite.androidfragmentsearchview.Adapters.Objects;
import androlite.androidfragmentsearchview.Menus.AboutUsScreen;
import androlite.androidfragmentsearchview.Menus.AddCompany;
import androlite.androidfragmentsearchview.Menus.SettingScreen;
import androlite.androidfragmentsearchview.Menus.WhatIsTollFree;
import androlite.androidfragmentsearchview.Menus.Wishlist;

import static android.content.Context.MODE_PRIVATE;
import static com.google.android.gms.internal.zzir.runOnUiThread;

/**
 * Created by Musabir on 4/17/2017.
 */

public class SubCompanies extends ListFragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener,NavigationView.OnNavigationItemSelectedListener {
    private Fragment fragment = null;
    private FragmentManager fragmentManager;
    private static final int DURATION = 250;
    private long activatedAt = Long.MAX_VALUE;
    List<String> mAllValues;
    List<Integer> images;
    List<String> index;
    String catName = "";
    private ArrayAdapter<String> mAdapter;
    private CustomList customList;
    private static Context mContext;
    private String TAG = MainActivity.class.getSimpleName();
    //private static String url;
    private static final int REQUEST_PHONE_CALL = 1;
    CollapsingToolbarLayout collapsingToolbarLayout;
    private ProgressDialog pDialog;
    ArrayList<String> city_id;
    ArrayList<String> city_name;
    ArrayList<String> search_city_name;
    MenuItem item;
    String selectedValue;
    public static String object_index1;
    public static String object_index;
    public static Bitmap bm;
    public static  ArrayList<Objects> allParentValues;
    public static  ArrayList<Objects> allParentValues21;
    ArrayList<Objects> filterValues;
    ArrayList<String> backgroundImg;
    ExpandableListView expListView;
    public static int parent_object_id = 0;
    boolean b = false;
    private View layout;
    FragmentManager.BackStackEntry ff;
    private String tag="";
    NavigationView navigationView;
    private Typeface tf;
    ImageView  header = null;
    AQuery aq;
    private Bitmap bitmap1;
    private BitmapDrawable ob;
    String asss = "";
    private DBHelper mydb ;
    ArrayList<Objects> allParentValues_copy1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        object_index1 = null;
        object_index = null;
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);
        aq = new AQuery(getActivity());
        mydb = new DBHelper(getActivity());

        b  =false;

    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem searchViewMenuItem = menu.findItem(R.id.action_searcha);
        SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(searchViewMenuItem);
        int searchImgId = getResources().getIdentifier("android:id/search_button", null, null);
        ImageView v = (ImageView) mSearchView.findViewById(searchImgId);
        v.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        super.onPrepareOptionsMenu(menu);
    }
    @Override
    public void onResume() {

        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        if (Company_Details.h == true) {
            activate();
            Company_Details.h = false;
        }
        fragmentManager = getActivity().getFragmentManager();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && Company_Details.vv == false) {
                    if (getFragmentManager().getBackStackEntryCount() > 0) {
                        Fragment fragment1 = null;
                        expandToolbar();

                        ff = getFragmentManager().getBackStackEntryAt(getFragmentManager().getBackStackEntryCount() - 1);
                        fragment1 = getFragmentManager().findFragmentByTag(ff.getName());

                        getFragmentManager().popBackStack();

                        // navigationView.getMenu().findItem(0).setChecked(true);
                        getActivity().setTitle(fragment1.getTag());

                    } else {
                        getActivity().finish();
                    }

                } else if (Company_Details.vv == true) Company_Details.vv = false;
                //  }
                return true;
            }
        });

    }
    public static void requestPermission() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
            }
        }
    }
    public void expandToolbar(){
        AppBarLayout appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.app_bar);
        appBarLayout.setExpanded(true);
    }
    @Override
    public void onStart() {
        new GetContacts().execute();
        super.onStart();
        backgroundImg = new ArrayList<String>();
        header = (ImageView) getActivity().findViewById(R.id.header);
        //   header.setVisibility(View.VISIBLE);
        RelativeLayout rf = (RelativeLayout) getActivity().findViewById(R.id.hs);
        rf.setVisibility(View.VISIBLE);
        Log.d("---->","Start");
        // expandToolbar();
        collapsingToolbarLayout = (CollapsingToolbarLayout) getActivity().findViewById(R.id.collapse_commonview_header);
        tf = Typeface.createFromAsset(getActivity().getAssets(), "clear.otf");
        collapsingToolbarLayout.setCollapsedTitleTypeface(tf);
        collapsingToolbarLayout.setTitleEnabled(true);


        SharedPreferences mSh = getActivity().getSharedPreferences("Value", MODE_PRIVATE);
        selectedValue = mSh.getString("val", "az");
        String cat_names = readFromFile(getActivity(), "ParentCategory.txt");
        String sub_names = readFromFile(getActivity(), "ChildCategory.txt");

    }

    @Override
    public void onPause() {

        super.onPause();
        if (pDialog != null)
            pDialog.dismiss();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings) {
            displayView(1);
        }
        if (id == R.id.about_us) {
            displayView(2);
        }
        if (id == R.id.tollfree) {
            displayView(3);
        }
        if (id == R.id.wishlist) {
            displayView(4);
        }
        if (id == R.id.add_the_company) {
            displayView(5);
        }


        //displayView(0); // call search fragment.

        DrawerLayout drawer = (DrawerLayout) layout.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }
    public void displayView(int position) {
        fragment = null;
        String fragmentTags = "";

        switch (position) {
            case 0:
                fragment = new Company_Details();
                fragmentTags = "Company_Details";
                break;
            case 1:
                fragment = new SettingScreen();
                fragmentTags = "Setting";
                break;
            case 2:
                fragment = new AboutUsScreen();
                fragmentTags = "About Us";
                break;
            case 3:
                fragment = new WhatIsTollFree();
                fragmentTags = "What Is Toll Free";
                break;
            case 4:
                fragment = new Wishlist();
                fragmentTags = "Wishlist";
                break;
            case 5:
                fragment = new AddCompany();
                fragmentTags = "Add Company";
                break;

            default:
                break;
        }

        fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(fragmentTags);
        fragmentTransaction.replace(R.id.content_frame, fragment, fragmentTags);
        fragmentTransaction.commit();

        //setTitle(fragment.getTag());

    }
    private void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    private void setListViewHeight(ExpandableListView listView,
                                   int group) {
        ExpandableListAdapter listAdapter = listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();
                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            allParentValues = new ArrayList<>();
            allParentValues21 = new ArrayList<>();
            filterValues = new ArrayList<>();
            // Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String city_names = readFromFile(getActivity(),"city.txt");

            String jsonStr = readFromFile(getActivity(),"AllData.txt");

            search_city_name = new ArrayList<>();
            city_id = new ArrayList<>();
            city_name = new ArrayList<>();

            images = new ArrayList<>();
            index = new ArrayList<>();
            mAllValues = new ArrayList<>();

            Log.e(TAG, "Response from url: " + jsonStr);
            Log.e(TAG, "Response from url: " + city_names);

            if (jsonStr != null && city_names != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject jsonCityObj = new JSONObject(city_names);
                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("server_response");
                    JSONArray cityArray = jsonCityObj.getJSONArray("server_response");
                    SharedPreferences mSh = getActivity().getSharedPreferences("Value", MODE_PRIVATE);
                    selectedValue = mSh.getString("val","az");
                    // looping through All Contacts
                    boolean tg = true;
                    for (int i = 0; i < contacts.length(); i++) {

                        JSONObject c = contacts.getJSONObject(i);
                        if(c.getString("cat_id").equals(SearchFragment.getObject_index())&&c.getString("obj_pid").equals("null")) {
                            String p_mAllValue = c.getString("name");
                            catName = c.get("name_"+selectedValue).toString();

                            String p_id = c.getString("obj_id");
                            while (tg) {
                                asss = c.getString("cat_bck");

                                tg = false;
                            }


                            String city = c.getString("address");
                            String toll_free = c.getString("tollfree");;//c.getString("toll_free");
                            Log.d("--->>>","toll_free "+toll_free);

                            if(toll_free.equals(""))
                                toll_free="null";

                            String icon_path = c.getString("logo");

                            String addr = c.getString("city_"+selectedValue);

                            Objects t2 = new Objects(p_mAllValue, icon_path, p_id, city, toll_free,addr);
                            for (int k = 0; k < contacts.length(); k++) {
                                JSONObject sub = contacts.getJSONObject(k);
                                if (sub.getString("obj_pid").equals(p_id)) {
                                    t2.addr="";
                                    t2.players.add(sub.getString("name") + "&-&-" + sub.getString("obj_id"));
                                    t2.cities.add(sub.getString("address"));
                                    String tf = sub.getString("tollfree");
                                    Log.d("--->>>","tf "+tf);
                                    if(tf.equals(""))
                                        tf = "null";
                                    t2.TollFree.add(tf);

                                    t2.add.add(sub.getString("city_"+selectedValue));

                                }
                            }

                            allParentValues.add(t2);
                        }
                    }
                    for (int i = 0; i < cityArray.length(); i++) {
                        JSONObject c = cityArray.getJSONObject(i);
                        //city_name.clear();
                      //  city_id.clear();
                        city_id.add(c.getString("id"));
                        city_name.add(c.getString(("name_") + selectedValue));
                        mydb.insertContact(c.getString(("name_") + selectedValue));

                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            aq.id(header).image(asss, true, true, 0, R.drawable.launcher_icon);
    /*
            for(int i=0;i<backgroundImageList.length;i++) {
                String url = "http://www.androidquery.com/z/images/vikispot/android-w.png";

                aq.ajax(url, Bitmap.class, new AjaxCallback<Bitmap>() {

                    @Override
                    public void callback(String url, Bitmap object, AjaxStatus status) {
                        Log.d("---->","girdi ");
                        Toast.makeText(aq.getContext(), object.toString(), Toast.LENGTH_LONG).show();
                        BitmapDrawable ob =new BitmapDrawable(context.getResources(), object);
                        bitmapArrayList.add(ob);
                    }
                });
            }
            */
            collapsingToolbarLayout.setTitle(catName);
            SubMenu subm = null;
            if(item!=null)
                subm= item.getSubMenu();

            for (int i = 0; i < city_name.size(); i++) {
                subm.add(1, Integer.parseInt(city_id.get(i)), 1, city_name.get(i));
                subm.getItem(i).setCheckable(true);
            }

            // Dismiss the progress dialog
            if (pDialog != null)
                pDialog.dismiss();
            allParentValues21 = allParentValues;
            allParentValues_copy1 = allParentValues21;
            CustomAdapter_Objects adapter = new CustomAdapter_Objects(getActivity(), allParentValues21);

            expListView.setAdapter(adapter);
            setListViewHeight(expListView);

            // customList = new CustomList(getActivity(), mAllValues, images,index);
            //  setListAdapter(customList);
        }

    }


    @Override
    public void onListItemClick(ListView listView, View v, int position, long id) {
        fragment = new Company_Details();
        fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.addToBackStack("Company_Details");
        MainActivity.tag = "Sub Companies";
        fragmentTransaction.commit();

        String item = (String) listView.getAdapter().getItem(position);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.search_fragment, container, false);
        expListView = (ExpandableListView) layout.findViewById(android.R.id.list);


        navigationView = (NavigationView) layout.findViewById(R.id.nav_view);
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                setListViewHeight(expandableListView,i);
                if(allParentValues21.get(i).players.size()==0) {
                    Log.d("---->","Musabir");
                    //  Toast.makeText(getActivity(), allParentValues21.get(i).PID, Toast.LENGTH_SHORT).show();
                    FragmentManager fragmentManager = getActivity().getFragmentManager();
                    parent_object_id=i;
                    displayView(0);
                    object_index1 = (allParentValues21.get(i).PID);
                    object_index = (allParentValues21.get(i).PID);


                }
                else{
                    expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                        @Override
                        public boolean onChildClick(ExpandableListView parent, View v, int groupPos,
                                                    int childPos, long id) {

                            parent_object_id = groupPos;
                            Log.d("---->","12344");

                            String name_decode = allParentValues21.get(groupPos).players.get(childPos).toString();
                            String[] arr = name_decode.split("\\&-&-");
                            //  Toast.makeText(getActivity(),arr[1], Toast.LENGTH_SHORT).show();
                            displayView(0);
                            object_index1 = (arr[1]);
                            object_index= (allParentValues21.get(groupPos).PID);

                            return false;
                        }
                    });
                }
                return false;
            }
        });
        return layout;



    }

    public void activate() {
        activatedAt = System.currentTimeMillis();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_searcha);
        MenuItem filter = menu.findItem(R.id.filter);
        SearchView searchView = (SearchView) searchItem.getActionView();
        SharedPreferences mSh = getActivity().getSharedPreferences("Value", MODE_PRIVATE);
        String selectedValue = mSh.getString("val","az");
        if(selectedValue.equals("az")) {
            //  searchItem.getSubMenu().getItem(0).setTitle("Axtar");
          //  filter.getSubMenu().getItem(0).setTitle("Şəhər");
          //  filter.getSubMenu().getItem(1).setTitle("Yalnız Toll Free");
        }
        else if(selectedValue.equals("en")) {
            // searchItem.getSubMenu().getItem(0).setTitle("Search");
           // filter.getSubMenu().getItem(0).setTitle("Сity");
          //  filter.getSubMenu().getItem(1).setTitle("Only Toll Free");

        }
        else if(selectedValue.equals("ru")) {
            // searchItem.getSubMenu().getItem(0).setTitle("Поиск");
           // filter.getSubMenu().getItem(0).setTitle("Город");
           // filter.getSubMenu().getItem(1).setTitle("Только Toll Free");
        }
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Axtar");
        item = menu.findItem(R.id.spinner);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean t = false;
        ArrayList<Objects> filterValues_copy = new ArrayList<>();
        Log.d("----->",item.toString());
        if (!item.toString().equals("Filter")) {
            if (item.toString().equals("Only Tollfree")) {
                if (!item.isChecked()) {
                    b=true;
                    item.setChecked(true);
                }
                else {
                    item.setChecked(false);
                    b=false;
                }
            } else if (!item.toString().equals("Only Tollfree") && !item.toString().equals("Region")) {
                if (item.isChecked()) {
                    for(int i=0;i<search_city_name.size();i++){
                        if(search_city_name.get(i).equals(item.toString()))
                        {
                            search_city_name.remove(i);
                            break;
                        }
                    }
                    item.setChecked(false);
                }
                else {
                    search_city_name.add(item.toString());
                    item.setChecked(true);
                }
            }
            //search_city_name.add()
            if (search_city_name.size() == 0 && b == false) {
                filterValues_copy = allParentValues;
            } else if (search_city_name.size() >= 1 && b == false) {
                Log.d("---->", "Girif");
                for (int i = 0; i < allParentValues.size(); i++) {
                    Objects t2 = new Objects(allParentValues.get(i).Name, allParentValues.get(i).Image, allParentValues.get(i).PID, allParentValues.get(i).City, allParentValues.get(i).tollFree, allParentValues.get(i).addr);
                    for (int k = 0; k < allParentValues.get(i).players.size(); k++) {
                        Log.d("--->>>>>",allParentValues.get(i).cities.get(k)+ " : "+allParentValues.get(i).players.get(k));
                        for (int m = 0; m < search_city_name.size(); m++) {

                            if (allParentValues.get(i).add.get(k).equals(search_city_name.get(m))) {
                                t2.players.add(allParentValues.get(i).players.get(k));
                                t2.cities.add(allParentValues.get(i).cities.get(k));
                                t2.TollFree.add(allParentValues.get(i).TollFree.get(k));
                                t2.add.add(allParentValues.get(i).add.get(k));
                                break;
                            }
                        }
                    }
                    for (int m = 0; m < search_city_name.size(); m++) {
                        if (t2.players.size() != 0 || ((allParentValues.get(i).players.size() == 0) && t2.addr.equals(search_city_name.get(m)))) {
                            filterValues_copy.add(t2);
                            break;
                        }

                    }
                }
            }
            else if (search_city_name.size() == 0 && b == true) {

                for (int i = 0; i < allParentValues.size(); i++) {
                    Objects t2 = new Objects(allParentValues.get(i).Name, allParentValues.get(i).Image, allParentValues.get(i).PID,
                            allParentValues.get(i).City, allParentValues.get(i).tollFree, allParentValues.get(i).addr);
                    for (int k = 0; k < allParentValues.get(i).players.size(); k++) {
                        Log.d("---------?",allParentValues.get(i).TollFree.get(k)+" dsd");
                        if ((!allParentValues.get(i).TollFree.get(k).equals("null"))) {
                            t2.players.add(allParentValues.get(i).players.get(k));
                            t2.cities.add(allParentValues.get(i).cities.get(k));
                            t2.TollFree.add(allParentValues.get(i).TollFree.get(k));
                            t2.add.add(allParentValues.get(i).add.get(k));
                        }
                    }

                    if (t2.players.size() != 0 || ((t2.players.size() == 0) && !t2.tollFree.equals("null"))) {
                        Log.d("------>",t2.players.size()+" : "+t2.Name+" : "+t2.tollFree+"sa");

                        filterValues_copy.add(t2);
                    }

                }

            }
            if (search_city_name.size() > 0 && b == true) {
                Log.d("---->","Girif2");

                for (int i = 0; i < allParentValues.size(); i++) {
                    Objects t2 = new Objects(allParentValues.get(i).Name, allParentValues.get(i).Image, allParentValues.get(i).PID,
                            allParentValues.get(i).City, allParentValues.get(i).tollFree, allParentValues.get(i).addr);
                    for (int k = 0; k < allParentValues.get(i).players.size(); k++) {
                        for (int m = 0; m < search_city_name.size(); m++) {
                            if (((allParentValues.get(i).add.get(k).equals(search_city_name.get(m))
                                    && (!allParentValues.get(i).TollFree.get(k).equals("null"))))) {
                                t2.players.add(allParentValues.get(i).players.get(k));
                                t2.cities.add(allParentValues.get(i).cities.get(k));
                                t2.TollFree.add(allParentValues.get(i).TollFree.get(k));
                                t2.add.add(allParentValues.get(i).add.get(k));
                                break;
                            }
                        }
                    }
                    for (int m = 0; m < search_city_name.size(); m++) {

                        if (t2.players.size() != 0 || (((allParentValues.get(i).players.size() == 0) &&  (t2.addr.equals(search_city_name.get(m)) && !t2.tollFree.equals("null"))))) {

                            filterValues_copy.add(t2);
                            break;
                        }
                    }


                }

            }
        }
            /*

            //check is filterValues empty if yes allparentvalues; check t if false filteredvalues = filteredvalues_copy
            if (item.toString().equals("Only Tollfree")) {

                if (item.isChecked()) {

                    if (search_city_name.size() == 0)
                        filterValues.clear();
                    else {
                        for (int i = 0; i < allParentValues.size(); i++) {
                            Objects t2 = new Objects(allParentValues.get(i).Name, allParentValues.get(i).Image, allParentValues.get(i).PID,
                                    allParentValues.get(i).City, allParentValues.get(i).tollFree,allParentValues.get(i).addr);
                            for (int k = 0; k < allParentValues.get(i).players.size(); k++) {
                                for (int j = 0; j < search_city_name.size(); j++) {
                                    if (allParentValues.get(i).add.get(k).equals(search_city_name.get(j))) {
                                        t2.players.add(allParentValues.get(i).players.get(k));
                                        t2.cities.add(allParentValues.get(i).cities.get(k));
                                        t2.TollFree.add(allParentValues.get(i).TollFree.get(k));
                                        t2.add.add(allParentValues.get(i).add.get(k));
                                        break;
                                    }
                                }
                            }
                            for (int j = 0; j < search_city_name.size(); j++) {
                                if (t2.players.size() != 0 || ((allParentValues.get(i).players.size() == 0) && t2.addr.equals(search_city_name.get(j)))) {
                                    filterValues_copy.add(t2);
                                    break;

                                }
                            }
                        }
                    }
                    b = false;
                    item.setChecked(false);

                } else {
                    b=true;
                    item.setChecked(true);
                    for (int i = 0; i < allParentValues.size(); i++) {
                        Objects t2 = new Objects(allParentValues.get(i).Name, allParentValues.get(i).Image, allParentValues.get(i).PID,
                                allParentValues.get(i).City, allParentValues.get(i).tollFree,allParentValues.get(i).addr);
                        for (int k = 0; k < allParentValues.get(i).players.size(); k++) {

                            if (search_city_name.size() > 0) {
                                for (int j = 0; j < search_city_name.size(); j++) {
                                    if (allParentValues.get(i).add.get(k).equals(search_city_name.get(j))
                                            && (!allParentValues.get(i).TollFree.get(k).equals("null"))) {
                                        t2.players.add(allParentValues.get(i).players.get(k));
                                        t2.cities.add(allParentValues.get(i).cities.get(k));
                                        t2.TollFree.add(allParentValues.get(i).TollFree.get(k));
                                        t2.add.add(allParentValues.get(i).add.get(k));
                                        break;
                                    }
                                }
                            } else {
                                if (!allParentValues.get(i).TollFree.get(k).equals("null") ) {

                                    t2.players.add(allParentValues.get(i).players.get(k));
                                    t2.cities.add(allParentValues.get(i).cities.get(k));
                                    t2.TollFree.add(allParentValues.get(i).TollFree.get(k));
                                    t2.add.add(allParentValues.get(i).add.get(k));
                                    break;
                                }
                            }
                        }
                        if (search_city_name.size() > 0) {
                            for (int j = 0; j < search_city_name.size(); j++) {
                                if (t2.players.size() != 0 || ((allParentValues.get(i).players.size() == 0) && t2.addr.equals(search_city_name.get(j)) && (!t2.tollFree.equals("null")))) {
                                    filterValues_copy.add(t2);

                                    break;

                                }
                            }
                        }

                        else if (t2.players.size() != 0 || ((allParentValues.get(i).players.size() == 0) && ( !t2.tollFree.equals("null")))) {
                            filterValues_copy.add(t2);

                        }

                    }

                }

            }
        }
        */
        if(!item.toString().equals("Filter")&&!item.toString().equals("Region")) {
            if (search_city_name.size() ==  0 && b == false) {
                allParentValues21 = allParentValues;
                allParentValues_copy1 = filterValues_copy;

                CustomAdapter_Objects adapter23 = new CustomAdapter_Objects(getActivity(), filterValues_copy);

                expListView.setAdapter(adapter23);
                setListViewHeight(expListView);


            } else {
                allParentValues21 = filterValues_copy;
                allParentValues_copy1 = filterValues_copy;
                filterValues = filterValues_copy;
                CustomAdapter_Objects adapter23;

                adapter23 = new CustomAdapter_Objects(getActivity(), filterValues_copy);

                expListView.setAdapter(adapter23);
                setListViewHeight(expListView);




            }
        }

        return false;

    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        ArrayList<Objects> allParentValues_copy = new ArrayList<>();
        if (newText == null || newText.trim().isEmpty()) {
            resetSearch();
            return false;
        }
        for (int i = 0; i < allParentValues21.size(); i++) {

            Objects t2 = new Objects(allParentValues21.get(i).Name, allParentValues21.get(i).Image, allParentValues21.get(i).PID, allParentValues21.get(i).City,allParentValues21.get(i).tollFree,allParentValues21.get(i).addr);
            for (int k = 0; k < allParentValues21.get(i).players.size(); k++) {
                String as = allParentValues21.get(i).players.get(k);
                if (as.toLowerCase().contains(newText.toLowerCase())) {
                    t2.players.add(allParentValues21.get(i).players.get(k));
                    t2.cities.add(allParentValues21.get(i).cities.get(k));
                    t2.TollFree.add(allParentValues21.get(i).TollFree.get(k));
                    t2.add.add(allParentValues21.get(i).add.get(k));

                }

            }
            if (allParentValues21.get(i).Name.toLowerCase().contains(newText.toLowerCase()) && t2.players.size() == 0) {
                allParentValues_copy.add(allParentValues21.get(i));
            }
            if (t2.players.size() != 0)
                allParentValues_copy.add(t2);
        }
        CustomAdapter_Objects adapter1 = new CustomAdapter_Objects(getActivity(), allParentValues_copy);

        expListView.setAdapter(adapter1);
        setListViewHeight(expListView);

        return false;
    }


    public void resetSearch() {
        CustomAdapter_Objects adapter1 = new CustomAdapter_Objects(getActivity(), allParentValues21);
        expListView.setAdapter(adapter1);
      // allParentValues21 = allParentValues;
        allParentValues_copy1 = allParentValues21;

        setListViewHeight(expListView);


    }


    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return true;
    }

    private String readFromFile(Context context,String fileName) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(fileName);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

}

