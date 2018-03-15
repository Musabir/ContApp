package androlite.androidfragmentsearchview;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.androidquery.AQuery;

import java.io.IOException;
import java.io.OutputStreamWriter;

import androlite.androidfragmentsearchview.Fonts.CustomTypefaceSpan;
import androlite.androidfragmentsearchview.Menus.AboutUsScreen;
import androlite.androidfragmentsearchview.Menus.AddCompany;
import androlite.androidfragmentsearchview.Menus.SettingScreen;
import androlite.androidfragmentsearchview.Menus.WhatIsTollFree;
import androlite.androidfragmentsearchview.Menus.Wishlist;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Fragment fragment = null;
    private FragmentManager fragmentManager;
    public static String tag;
    Fragment g;
    static String lang;
    public static NavigationView navigationView;
    private static ProgressDialog pDialog;
    public static int cnt = 0;
    public static String fragmentTags = "";

    FragmentManager.BackStackEntry ff;
    private SharedPreferences mSharedPreferences;
    String city_url = "http://contapp.avirtel.az/API/json_get_city.php";
    public static String url2;
    public static String url = "http://contapp.avirtel.az/API/json_get_cat.php?lang=";
    public static String child_url = "http://contapp.avirtel.az/API/json_get_child_cat.php?lang=";
    public static String all_data = "http://contapp.avirtel.az/API/json_all_data.php";
    public static String all_contacts = "http://contapp.avirtel.az/API/json_all_contacts.php";
    public static String child_url2;
    AQuery aq;
    private Typeface tf;
    private String selectedValue;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        tf = Typeface.createFromAsset(this.getAssets(), "clear.otf");
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTitle("Contapp");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        aq = new AQuery(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();
        ActionBar bar = getActionBar();
        // bar.setBackgroundDrawable(Color.parseColor("#3EA99F"));

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        MenuItem company_list = menu.findItem(R.id.company_list);
        MenuItem add_the_company = menu.findItem(R.id.add_the_company);
        MenuItem tollfree = menu.findItem(R.id.tollfree);
        MenuItem wishlist = menu.findItem(R.id.wishlist);
        MenuItem settings = menu.findItem(R.id.settings);
        MenuItem about_us = menu.findItem(R.id.about_us);
        SharedPreferences mSh = getSharedPreferences("Value", MODE_PRIVATE);
        selectedValue = mSh.getString("val", "az");

        if(selectedValue.equals("az")){
            company_list.setTitle("Şirkətlərin Siyahısı");
            add_the_company.setTitle("Məkan Əlavə Et");
            tollfree.setTitle("Tollfree Nədir?");
            wishlist.setTitle("Seçilmişlər");
            settings.setTitle("Ayarlar");
            about_us.setTitle("Haqqımızda");
        }
        if(selectedValue.equals("en")){
            company_list.setTitle("Company List");
            add_the_company.setTitle("Add Place");
            tollfree.setTitle("What is Toll Free?");
            wishlist.setTitle("Favourite List");
            settings.setTitle("Settings");
            about_us.setTitle("About us");
        }
        if(selectedValue.equals("ru")){
            company_list.setTitle("Список Компаний");
            add_the_company.setTitle("Добавить Место");
            tollfree.setTitle("Об Toll Free?");
            wishlist.setTitle("Избранное");
            settings.setTitle("Настройки");
            about_us.setTitle("О Нас");
        }
        Menu m = navigationView.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);
            applyFontToMenuItem(mi);

            //for aapplying a font to subMenu ...

            //the method we have create in activity
        }
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        displayView(0,false);

    }
    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences mSh = getSharedPreferences("Value", MODE_PRIVATE);
        selectedValue = mSh.getString("val", "az");
        Log.d("Dil", selectedValue);
        View header = navigationView.getHeaderView(0);
        ImageView mImgView= (ImageView) header.findViewById(R.id.imageView);
        mImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllQueue();
            }
        });
    }


    public void expandToolbar(){
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.setExpanded(true);
    }
    public static void updateOptionChecked(){
        Menu menu = navigationView.getMenu();
        MenuItem company_list = menu.findItem(R.id.menu_none);
        company_list.setChecked(false);
    }
    @Override
    public void onPause(){

        super.onPause();
    }
    public static String getLanguage(){
        return lang;
    }
    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        tag="";
        //  navigationView.getMenu().findItem(ff.getId()).setChecked(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        expandToolbar();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getFragmentManager().getBackStackEntryCount() > 1) {
                Fragment fragment1 = null;

                ff = getFragmentManager().getBackStackEntryAt(getFragmentManager().getBackStackEntryCount() - 2);
                fragment1 = getFragmentManager().findFragmentByTag(ff.getName());
                getFragmentManager().popBackStack();


                // navigationView.getMenu().findItem(0).setChecked(true);
                //setTitle(fragment1.getTag());

            } else {
                finish();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //  getMenuInflater().inflate(R.menu.main, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }
    private void storeValue(String val){
        SharedPreferences mSh = getSharedPreferences("Value",MODE_PRIVATE);
        SharedPreferences.Editor mEd = mSh.edit();
        Log.d("---->DIL",val);
        mEd.putString("val",val);
        mEd.apply();

    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.settings) {
            SharedPreferences mSh = getSharedPreferences("Value", MODE_PRIVATE);
            selectedValue = mSh.getString("val", "az");
            int h = 0;

            AlertDialog.Builder builderSingle = new AlertDialog.Builder(
                    MainActivity.this);
            String cancel = "Cancel";
            if(selectedValue.equals("az")) {
                builderSingle.setTitle("Dillər");
                h = 0;
                cancel="Ləgv et";
            }
            else if(selectedValue.equals("en")) {
                builderSingle.setTitle("Languages");
                h = 1;
                cancel = "Cancel";
            }
            else if(selectedValue.equals("ru")) {
                builderSingle.setTitle("Языки");
                h = 2;
                cancel="Oтменить";
            }
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    MainActivity.this,
                    android.R.layout.select_dialog_singlechoice);
            arrayAdapter.add("Az");
            arrayAdapter.add("En");
            arrayAdapter.add("Ru");

            builderSingle.setNegativeButton(cancel,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            builderSingle.setSingleChoiceItems(arrayAdapter, h, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) storeValue("az");
                    if (which == 1) storeValue("en");
                    if (which == 2) storeValue("ru");
                    clearAllQueue();
                }

            });
            builderSingle.create();
            builderSingle.show();
        }
        if (id == R.id.about_us) {
            displayView(2,false);
        }
        if (id == R.id.tollfree) {
            displayView(3,false);
        }
        if (id == R.id.wishlist) {
            displayView(4,false);
        }
        if (id == R.id.add_the_company) {
            displayView(5,false);
        }
        if (id == R.id.company_list) {
            displayView(6,false);
        }
        //displayView(0); // call search fragment.

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void clearAllQueue(){
        Fragment fr = new SearchFragment();
        fragmentTags = "Contapp";
        if (fr != null) {
            fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentManager.popBackStack("Contapp",FragmentManager.POP_BACK_STACK_INCLUSIVE);

            //fragmentManager.popBackStack(1, FragmentManager.POP_BACK_STACK_INCLUSIVE);

            fragmentTransaction.addToBackStack(fragmentTags);
            tag="";
            fragmentTransaction.replace(R.id.content_frame, fr, fragmentTags);
            fragmentTransaction.commit();
            fragmentManager.executePendingTransactions();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            Menu menu = navigationView.getMenu();
            MenuItem company_list = menu.findItem(R.id.company_list);
            MenuItem add_the_company = menu.findItem(R.id.add_the_company);
            MenuItem tollfree = menu.findItem(R.id.tollfree);
            MenuItem wishlist = menu.findItem(R.id.wishlist);
            MenuItem settings = menu.findItem(R.id.settings);
            MenuItem about_us = menu.findItem(R.id.about_us);
            SharedPreferences mSh = getSharedPreferences("Value", MODE_PRIVATE);
            selectedValue = mSh.getString("val", "az");

            if(selectedValue.equals("az")){
                company_list.setTitle("Şirkətlərin Siyahısı");
                add_the_company.setTitle("Məkan Əlavə Et");
                tollfree.setTitle("Tollfree Nədir?");
                wishlist.setTitle("Seçilmişlər");
                settings.setTitle("Ayarlar");
                about_us.setTitle("Haqqımızda");
            }
            if(selectedValue.equals("en")){
                company_list.setTitle("Company List");
                add_the_company.setTitle("Add Place");
                tollfree.setTitle("What is Toll Free?");
                wishlist.setTitle("Favourite List");
                settings.setTitle("Settings");
                about_us.setTitle("About us");
            }
            if(selectedValue.equals("ru")){
                company_list.setTitle("Список Компаний");
                add_the_company.setTitle("Добавить Место");
                tollfree.setTitle("Об Toll Free?");
                wishlist.setTitle("Избранное");
                settings.setTitle("Настройки");
                about_us.setTitle("О Нас");
            }
            Menu m = navigationView.getMenu();
            for (int i=0;i<m.size();i++) {
                MenuItem mi = m.getItem(i);
                applyFontToMenuItem(mi);

                //for aapplying a font to subMenu ...

                //the method we have cre
            }



            //setTitle(fragment.getTag());

        }
    }

    public void displayView(int position,boolean f) {
        fragment = null;

        switch (position) {
            case 0:
                fragment = new SearchFragment();
                fragmentTags = "Contapp";
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
            case 6:
                fragment = new SearchFragment();
                fragmentTags = "Contapp";
                break;

            default:
                break;
        }
        if (fragment != null && !fragmentTags.equals(tag)) {
            fragmentManager = getFragmentManager();

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.addToBackStack(fragmentTags);
            fragmentTransaction.replace(R.id.content_frame, fragment, fragmentTags);


            tag = fragmentTags;

            fragmentTransaction.commit();
            fragmentManager.executePendingTransactions();
            Log.d("-->.",fragmentManager.getBackStackEntryCount()+"");

            //setTitle(fragment.getTag());

        }
    }

    private void writeToFile(String data,String fileName,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            outputStreamWriter.write(data+data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    private void applyFontToMenuItem(MenuItem mi) {
        Log.d("--->",mi.getTitle()+" ");
        Typeface font = Typeface.createFromAsset(getAssets(), "clear.otf");

        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

}
