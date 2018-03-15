package androlite.androidfragmentsearchview;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import androlite.androidfragmentsearchview.Adapters.ViewPageAdapter;
import androlite.androidfragmentsearchview.Menus.AboutUsScreen;
import androlite.androidfragmentsearchview.Menus.AddCompany;
import androlite.androidfragmentsearchview.Menus.SettingScreen;
import androlite.androidfragmentsearchview.Menus.WhatIsTollFree;
import androlite.androidfragmentsearchview.Menus.Wishlist;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Musabir on 4/17/2017.
 */

public class Company_Details extends PreferenceFragment {
    View view;
    Menu menu;

    static boolean h = false;
    static boolean vv = false;
    private FragmentManager fragmentManager;
    private Fragment fragment = null;
    private ProgressDialog pDialog;
    private String facebook_url = "";
    private String twitter_url = "";
    private String instagram_url = "";
    private String google_url = "";
    FragmentManager.BackStackEntry ff;
    public ArrayList<BitmapDrawable> bitmapArrayList;
    AQuery aq ;

    public static ArrayList<String> obj_id = new ArrayList<>();
    public static ArrayList<String> obj_pid = new ArrayList<>();

    ImageView freeCall, phoneNumber, website, address, email, facebook, twitter, instagram, google, logo;
    TextView FreeCallNumber, WebsiteAddress, PhoneNumber, AddressPlace, EmailAddess;
    TextView FreeCalltxt, Websitetxt, Phonetxt, Addresstxt, Emailtxt;
    String  tollfree_arr[] = null, web[] = null;
    ArrayList<String> phnNmbr = new ArrayList<>();
    String unvan = null;
    static String lon = null;
    static String lat = null;
    static String name = null;
    static String icon_path = null;
    static String backgroundImages = null;
    ArrayList<String> contact_type;
    ArrayList<String> contact_list;
    String alLData = "";
    String all_contacts = "";
    String all_addr = "";
    Bitmap myBitmap = null;
    SharedPreferences mSh;
    SharedPreferences.Editor mEd;
    private NavigationView navigationView;
    private Typeface tf;
    LruCache<String, Bitmap> mMemoryCache;
    MapView mapView;
    GoogleMap googleMap;
    private boolean mapsSupported = true;
    private Bundle mBundle;
    ImageView  header;
    int rem = 0;
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    int x = 0;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private RelativeLayout page;
    private String[] backgroundImageList;

    public void expandToolbar(){
        AppBarLayout appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.app_bar);
        appBarLayout.setExpanded(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        tf = Typeface.createFromAsset(getActivity().getAssets(), "clear.otf");

        view = inflater.inflate(R.layout.company_details, container, false);
        bitmapArrayList = new ArrayList<>();
        MainActivity.tag = "Company_Details";
        RelativeLayout rf = (RelativeLayout) getActivity().findViewById(R.id.hs);
        rf.setVisibility(View.GONE);
        setHasOptionsMenu(true);



        navigationView = (NavigationView) view.findViewById(R.id.nav_view);
        ff = getFragmentManager().getBackStackEntryAt(getFragmentManager().getBackStackEntryCount() - 1);
        collapsingToolbarLayout = (CollapsingToolbarLayout) getActivity().findViewById(R.id.collapse_commonview_header);

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "clear.otf");
        collapsingToolbarLayout.setCollapsedTitleTypeface(tf);
        Toolbar toolbar;

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        all_contacts = readFromFile(getActivity(), "AllContacts.txt");
        all_addr = readFromFile(getActivity(), "AllData.txt");
        header = (ImageView) getActivity().findViewById(R.id.header);
        obj_id.add(SubCompanies.object_index1);
        obj_pid.add(SubCompanies.object_index);
        freeCall = (ImageView) view.findViewById(R.id.FreeCallImage);
        phoneNumber = (ImageView) view.findViewById(R.id.PhoneImage);
        website = (ImageView) view.findViewById(R.id.WebsiteImage);
        address = (ImageView) view.findViewById(R.id.AddressImage);
        email = (ImageView) view.findViewById(R.id.emailIcon);
        facebook = (ImageView) view.findViewById(R.id.facebook_icon);
        twitter = (ImageView) view.findViewById(R.id.twitt_icon);
        instagram = (ImageView) view.findViewById(R.id.instagram_icon);
        google = (ImageView) view.findViewById(R.id.google_plus);

        FreeCallNumber = (TextView) view.findViewById(R.id.FreeCallNumber);
        WebsiteAddress = (TextView) view.findViewById(R.id.WebsiteAddress);
        PhoneNumber = (TextView) view.findViewById(R.id.PhoneNumber);
        AddressPlace = (TextView) view.findViewById(R.id.AddressPlace);
        EmailAddess = (TextView) view.findViewById(R.id.emailaddress);

        FreeCalltxt = (TextView) view.findViewById(R.id.FreeCalltxt);
        Websitetxt = (TextView) view.findViewById(R.id.Websitetxt);
        Phonetxt = (TextView) view.findViewById(R.id.Phonetxt);
        Addresstxt = (TextView) view.findViewById(R.id.Addresstxt);
        Emailtxt = (TextView) view.findViewById(R.id.Emailtxt);

        FreeCalltxt.setTypeface(tf);
        Websitetxt.setTypeface(tf);
        Phonetxt.setTypeface(tf);
        Addresstxt.setTypeface(tf);
        Emailtxt.setTypeface(tf);


        FreeCallNumber.setTypeface(tf);
        WebsiteAddress.setTypeface(tf);
        PhoneNumber.setTypeface(tf);
        AddressPlace.setTypeface(tf);
        EmailAddess.setTypeface(tf);

        tollfree_arr = FreeCallNumber.getText().toString().split("[\r\n]+");
        web = WebsiteAddress.getText().toString().split("[\r\n]+");

        new GetContacts().execute();

        FreeCallNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                freeCall();
            }
        });
        FreeCalltxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                freeCall();
            }
        });
        freeCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                freeCall();
            }
        });
        PhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!PhoneNumber.getText().toString().equals(""))
                    phoneNumberCall();
            }
        });
        phoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!PhoneNumber.getText().toString().equals(""))
                    phoneNumberCall();
            }
        });
        Phonetxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!PhoneNumber.getText().toString().equals(""))
                    phoneNumberCall();
            }
        });
        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!WebsiteAddress.getText().toString().equals(""))
                    goToWebsite();
            }
        });
        Websitetxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!WebsiteAddress.getText().toString().equals(""))
                    goToWebsite();
            }
        });
        WebsiteAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!WebsiteAddress.getText().toString().equals(""))
                    goToWebsite();
            }
        });
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AddressPlace.getText().toString().equals("") && !AddressPlace.getText().toString().isEmpty()) {
                    Intent intent = new Intent(getActivity(), Mao.class);
                    startActivity(intent);
                }

            }
        });
        Addresstxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AddressPlace.getText().toString().equals("") && !AddressPlace.getText().toString().isEmpty()) {
                    Intent intent = new Intent(getActivity(), Mao.class);
                    startActivity(intent);
                }

            }
        });
        AddressPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AddressPlace.getText().toString().equals("") && !AddressPlace.getText().toString().isEmpty()) {
                    Intent intent = new Intent(getActivity(), Mao.class);
                    startActivity(intent);
                }

            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!EmailAddess.getText().toString().equals("") && !EmailAddess.getText().toString().isEmpty()) {
                    emailSend(EmailAddess.getText().toString().trim());
                }

            }
        });
        Emailtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!EmailAddess.getText().toString().equals("") && !EmailAddess.getText().toString().isEmpty()) {
                    emailSend(EmailAddess.getText().toString().trim());
                }

            }
        });
        EmailAddess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!EmailAddess.getText().toString().equals("") && !EmailAddess.getText().toString().isEmpty()) {
                    emailSend(EmailAddess.getText().toString().trim());
                }

            }
        });
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!facebook_url.equals("")) {
                    Intent facebookIntent = openFacebook(getActivity());
                    startActivity(facebookIntent);
                }
            }
        });
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!twitter_url.equals("")) {
                    Intent twitterIntent = openTwitter(getActivity());
                    startActivity(twitterIntent);
                }
            }
        });
        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!instagram_url.equals("")) {
                    Intent instagramIntent = openInstagram(getActivity());
                    startActivity(instagramIntent);
                }
            }
        });
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!google_url.equals("")) {
                    Intent googleIntent = openGooglePlus(getActivity());
                    startActivity(googleIntent);
                }
            }
        });

        return view;

    }
    public class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            if(getActivity() == null)
                return;
                getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(x<backgroundImageList.length) {
                        viewPager.setCurrentItem(x++);
                    }
                    else{
                        x=0;
                        viewPager.setCurrentItem(x);
                    }

                }
            });





        }
    }

    private void initializeMap() {
        if (mapsSupported) {
            googleMap = mapView.getMap();
            if(googleMap!=null) {

                 googleMap.getUiSettings().setMyLocationButtonEnabled(false);

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                MapsInitializer.initialize(this.getActivity());
                if (lat.equals("null") || lon.equals("null")) {

                    lat = "0.000000";
                    lon = "0.000000";
                }

                LatLng sydney = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
                googleMap.addMarker(new MarkerOptions()
                        .position(sydney).title(name));


                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));
            }
// end of new code
            }

    }
    public static String getName(){
        return name;
    }
    public static String getLon(){
        return lon;
    }
    public static String getLat(){
        return lat;
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

    private void emailSend(String email){
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto",email, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Send From Contapp");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
        startActivity(Intent.createChooser(emailIntent, "Send Feedback"));
    }
    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            alLData = "-";
            super.onPreExecute();
            contact_type = new ArrayList<>();
            contact_list = new ArrayList<>();
            // Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();


        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();

            String contact = all_contacts;
            String addr = all_addr;
            if (contact != null && addr != null) {
                try {
                    JSONObject contactObj = new JSONObject(contact);
                    JSONObject addrObject = new JSONObject(addr);
                    // Getting JSON Array node
                    JSONArray contacts = contactObj.getJSONArray("server_response");
                    JSONArray adresss = addrObject.getJSONArray("server_response");
                    Log.d("->saaas",SubCompanies.object_index1);


                    for (int i = 0; i < adresss.length(); i++) {
                        JSONObject a = adresss.getJSONObject(i);
                        if (a.getString("obj_id").equals(SubCompanies.object_index1)) {

                            //  String p_mAllValue = a.getString("name");
                            unvan = a.getString("address");
                            lon = a.getString("lon");
                            lat = a.getString("lat");
                            name = a.getString("name");

                            icon_path = a.getString("logo");
                            backgroundImages = a.getString("img");
                            backgroundImageList = backgroundImages.split("\\s*,\\s*");
                            getActivity().setTitle(name);
                            break;
                        }

                    }
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject a = contacts.getJSONObject(i);
                        if (a.getString("obj_id").equals(SubCompanies.object_index1)) {

                            contact_type.add(a.getString("contact_type"));
                            contact_list.add(a.getString("contact"));
                            Log.d("-->contact_type",a.getString("contact_type"));
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            page = (RelativeLayout) view.findViewById(R.id.page);

            if (pDialog != null)
                pDialog.dismiss();
            if(unvan.equals("null"))
                unvan="";
            collapsingToolbarLayout.setTitleEnabled(false);
            Log.d("--->",name);
            Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
            toolbar.setTitle(name);
            viewPager = (ViewPager) view.findViewById(R.id.viewPager);

            if(isValidURL(backgroundImages)) {

            sliderDotspanel = (LinearLayout) view.findViewById(R.id.SliderDots);

            ViewPageAdapter viewPagerAdapter = new ViewPageAdapter(getActivity(), backgroundImages);
            viewPager.setAdapter(viewPagerAdapter);
            dotscount = viewPagerAdapter.getCount();
            dots = new ImageView[dotscount];
            if(dotscount>1) {
                for (int i = 0; i < dotscount; i++) {

                    dots[i] = new ImageView(getActivity());
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.nonactive_dot));

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(8, 0, 8, 0);

                    sliderDotspanel.addView(dots[i], params);

                }

                dots[0].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.active_dot));
            }
            final Timer timer = new Timer();
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    if(dotscount>1) {

                        for (int i = 0; i < dotscount; i++) {
                            dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.nonactive_dot));
                        }

                        dots[position].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.active_dot));
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }

            });

          //  timer.scheduleAtFixedRate(new MyTimerTask(), 2000, 8000);
             //   mapView.setVisibility(View.GONE);

            }
        else if(!lat.equals("0.000000")&&!lon.equals("0.000000")){
               // mapView.setVisibility(View.VISIBLE);
                page.setVisibility(View.GONE);
            }

          else {
               // mapView.setVisibility(View.GONE);
                page.setVisibility(View.GONE);
            }
            AddressPlace.setText(unvan);
            String t="",w="",p="",e="";
            Log.d("-->",contact_type.size()+"sa");
            for(int i=0;i<contact_type.size();i++) {
                if (!contact_list.get(i).equals("null")) {
                    if (contact_type.get(i).equals("0"))
                        p = contact_list.get(i) ;
                    if (contact_type.get(i).equals("1")) {
                        w = contact_list.get(i);
                    }
                    if (contact_type.get(i).equals("2"))
                        t = contact_list.get(i);
                    if (contact_type.get(i).equals("3"))
                        facebook_url = contact_list.get(i);
                    if (contact_type.get(i).equals("4"))
                        twitter_url = contact_list.get(i);
                    if (contact_type.get(i).equals("5"))
                        instagram_url = contact_list.get(i);
                    if (contact_type.get(i).equals("6"))
                        google_url = contact_list.get(i);
                    if (contact_type.get(i).equals("7"))
                        e += contact_list.get(i) +"\n";;
                    //6 youtube

                }
            }
            FreeCallNumber.setText(t);
            WebsiteAddress.setText(w);
            String phones="";

            int x=0;
            Log.d("---->", FreeCallNumber.getText().toString() + "bn");
            String[] phnNmbrcpy = p.split("\\s*,\\s*");
            for (int i=0;i<phnNmbrcpy.length;i++){
                if( !phnNmbrcpy[i].equals("")||!phnNmbrcpy[i].isEmpty())
                    phnNmbr.add(phnNmbrcpy[i]);
            }
            for (int i=0;i<phnNmbr.size();i++){
                if(i<phnNmbr.size()-1 ) {

                        phones += phnNmbr.get(i) + '\n';
                }
                else {

                        phones +=phnNmbr.get(i);
                }
            }
            PhoneNumber.setText(phones);
            EmailAddess.setText(e);

            alLData+=t+"&-&-";
            alLData+=unvan+"&-&-";
            alLData+=name+"&-&-";
            alLData+=SubCompanies.object_index+"&-&-";
            alLData+=SubCompanies.object_index1+"&-&-";
            alLData+=icon_path;


        }
    }
    public final static boolean isValidPhone(CharSequence target) {
        if(!TextUtils.isEmpty(target))
            return Patterns.PHONE.matcher(target).matches();
        return  true;
    }
    public static boolean isValidURL(String urlString)
    {
        try
        {
            URL url = new URL(urlString);
            url.toURI();
            return true;
        } catch (Exception exception)
        {
            return false;
        }
    }

    public Intent openFacebook(Context context) {

            if (!facebook_url.startsWith("www.") && !facebook_url.startsWith("http://") && !facebook_url.startsWith("https://")) {
                facebook_url = "www." + facebook_url;
            }
            if (!facebook_url.startsWith("http://") && !facebook_url.startsWith("https://")) {
                facebook_url = "http://" + facebook_url;
            }


            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse(facebook_url));
    }
    public Intent openTwitter(Context context) {


        if(!twitter_url.equals("")) {

            if (!twitter_url.startsWith("www.") && !twitter_url.startsWith("http://") && !twitter_url.startsWith("https://")) {
                twitter_url = "www." + twitter_url;
            }
            if (!twitter_url.startsWith("http://") && !twitter_url.startsWith("https://")) {
                twitter_url = "http://" + twitter_url;
            }


            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse(twitter_url));
        }
        return null;
    }
    public Intent openInstagram(Context context) {
        if(!instagram_url.equals("")) {

            if (!instagram_url.startsWith("www.") && !instagram_url.startsWith("http://") && !instagram_url.startsWith("https://")) {
                instagram_url = "www." + instagram_url;
            }
            if (!instagram_url.startsWith("http://") && !instagram_url.startsWith("https://")) {
                instagram_url = "http://" + instagram_url;
            }


            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse(instagram_url));
        }
        return null;
    }
    public Intent openGooglePlus(Context context) {

        if(!google_url.equals("")) {
            if (!google_url.startsWith("www.") && !google_url.startsWith("http://") && !google_url.startsWith("https://")) {
                google_url = "www." + google_url;
            }
            if (!google_url.startsWith("http://") && !google_url.startsWith("https://")) {
                google_url = "http://" + google_url;
            }


            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse(google_url));
        }
        return null;
    }
    private void writeToFile(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    private String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("config.txt");

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        this.menu = menu;
        inflater.inflate(R.menu.wish_list, menu);
        MenuItem im = menu.findItem(R.id.action_wishlist);
        mSh = getActivity().getSharedPreferences("Position",MODE_PRIVATE);
        mEd = mSh.edit();
        if (  mSh.getString(SubCompanies.object_index1,"").equals("1"))
        {
            im.setIcon(R.drawable.ic_selected);
        }
        else im.setIcon(R.drawable.ic_wishlist);

        super.onCreateOptionsMenu(menu, inflater);

    }

    public void displayView(int position) {
        fragment = null;
        String fragmentTags = "";

        switch (position) {

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
        fragmentTransaction.replace(R.id.content_frame, fragment, fragmentTags);
        fragmentTransaction.addToBackStack(fragmentTags);
        fragmentTransaction.commit();


        //setTitle(fragment.getTag());

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
       super.onResume();
        getView().setFocusableInTouchMode(true);
        /*
        if(mapView.getVisibility()!=View.GONE) {
            mapView.onResume();
            initializeMap();
        }
*/
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK ) {
                    RelativeLayout rf = (RelativeLayout) getActivity().findViewById(R.id.hs);
                    rf.setVisibility(View.VISIBLE);
                    vv= true;
                    if(obj_id.size()>0) {
                        obj_id.remove(obj_id.size() - 1);
                        obj_pid.remove(obj_pid.size() - 1);
                    }
                    if (getFragmentManager().getBackStackEntryCount() > 0) {
                        Fragment fragment1 = null;

                        ff = getFragmentManager().getBackStackEntryAt(getFragmentManager().getBackStackEntryCount() - 2);


                        getFragmentManager().popBackStack();

                    } else {
                        getActivity().finish();
                    }

                }
                vv = true;
                return true;
            }
        });

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aq = new AQuery(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        /*
        if(mapView.getVisibility()!=View.GONE) {
            mapView.onResume();

        }
        */
    }
    @Override
    public void onDestroy() {
        super.onDestroy();


    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();

    }

    @Override
    public void onStart() {
        super.onStart();
        x = 0;
        expandToolbar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();
        MenuItem im = menu.findItem(R.id.action_wishlist);
        MenuItem icon = menu.findItem(R.drawable.ic_selected);
        //noinspection SimplifiableIfStatement


        if (id == R.id.action_wishlist) {
            if (item.getIcon().getConstantState().equals(
                    getResources().getDrawable(R.drawable.ic_selected).getConstantState()
            )) {
                storeButtonPosition("0");
                deleteValue();
             //   removeBitmapToMemoryCache(SubCompanies.object_index+"");
                item.setIcon(R.drawable.ic_wishlist);

            } else {
                storeValue(alLData);
             //   addBitmapToMemoryCache(SubCompanies.object_index+"",CustomAdapter_Objects.bm.get(rem));
                storeButtonPosition("1");
                item.setIcon(R.drawable.ic_selected);
            }
            return true;
        }
        if (id == R.id.share) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "TollFree Number: " + FreeCallNumber.getText().toString() + "\nPhone Number: " +
                    PhoneNumber.getText().toString() + "\nWebsite Address: " + WebsiteAddress.getText().toString() + "\nAddress: " + AddressPlace.getText().toString();

            String shareSub = "Your subject here";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share using"));
        }

        return super.onOptionsItemSelected(item);
    }
    private void storeValue(String val){
        SharedPreferences mSh = getActivity().getSharedPreferences("Wishlist",MODE_PRIVATE);
        SharedPreferences.Editor mEd = mSh.edit();
        /*
        if(myBitmap!=null) {
            Bitmap realImage = myBitmap;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            realImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();

            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            Log.d("=====>", encodedImage);
            mEd.putString(SubCompanies.object_index1, val + encodedImage);
        }
        else
        */
            mEd.putString(SubCompanies.object_index1, val);

        mEd.apply();
    }
    private void deleteValue(){
        SharedPreferences mSh = getActivity().getSharedPreferences("Wishlist",MODE_PRIVATE);
        SharedPreferences.Editor mEd = mSh.edit();
        mEd.remove(SubCompanies.object_index1);
        mEd.apply();
    }
    private void storeButtonPosition(String val){
        SharedPreferences mSh = getActivity().getSharedPreferences("Position",MODE_PRIVATE);
        SharedPreferences.Editor mEd = mSh.edit();

        mEd.remove(SubCompanies.object_index1);
        mEd.putString(SubCompanies.object_index1,val);
        mEd.apply();


    }
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put("1", bitmap);
        }
    }
    public void removeBitmapToMemoryCache(String key) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.remove(key);
        }
    }
    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }
    public void freeCall() {
            if(!FreeCallNumber.getText().toString().trim().isEmpty()&&!FreeCallNumber.getText().toString().trim().equals("")) {
                Intent myIntent = new Intent(Intent.ACTION_CALL);

                myIntent.setData(Uri.parse("tel:" + FreeCallNumber.getText().toString()));
                startActivity(myIntent);
            }

    }

    public void phoneNumberCall() {
        SubCompanies.requestPermission();
        if(phnNmbr.size()>1) {
        SharedPreferences mSh = getActivity().getSharedPreferences("Value", MODE_PRIVATE);
        String selectedValue = mSh.getString("val", "az");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, phnNmbr);
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
            dialogBuilder.setAdapter(adapter, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    if(!phnNmbr.get(which).isEmpty()) {
                        Intent myIntent = new Intent(Intent.ACTION_CALL);
                        myIntent.setData(Uri.parse("tel:" + phnNmbr.get(which)));
                        startActivity(myIntent);
                    }
                }
            });
        if(selectedValue.equals("az")) {
            dialogBuilder.setTitle("Zəng et");
        }
        else if(selectedValue.equals("en")) {
            dialogBuilder.setTitle("Call");
        }
        else if(selectedValue.equals("ru")) {
            dialogBuilder.setTitle("Вызов");
        }
            AlertDialog dialog = dialogBuilder.create();
            dialog.show();

        }
        else if (phnNmbr.size()==1){
            Intent myIntent = new Intent(Intent.ACTION_CALL);
            Log.d("----->", PhoneNumber.toString());
            myIntent.setData(Uri.parse("tel:" + PhoneNumber.getText().toString()));
            startActivity(myIntent);

        }
     ///   Intent myIntent = new Intent(Intent.ACTION_CALL);
       // Log.d("----->", PhoneNumber.toString());
       // myIntent.setData(Uri.parse("tel:" + PhoneNumber.getText().toString()));
        //startActivity(myIntent);
    }

    public void goToWebsite() {
        Log.d("--->",web.length+"SIZE" + web[0]);
        if(web.length>1) {
            PopupMenu popup = new PopupMenu(getActivity(), website);
            //Inflating the Popup using xml file

            for (int i = 0; i < web.length; i++) {
                popup.getMenu().add(web[i]);
            }
            popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());


            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    //---your menu item action goes here ....
                    Intent myWebLink = new Intent(android.content.Intent.ACTION_VIEW);
                    String url = (item.toString());
                    if (!url.startsWith("http://") && !url.startsWith("https://"))
                        url = "http://" + url;
                    myWebLink.setData(Uri.parse(url));
                    startActivity(myWebLink);
                    return true;
                }
            });
            popup.show();
        }
        else if (web.length==1) {
            Intent myWebLink = new Intent(android.content.Intent.ACTION_VIEW);
            String url = (WebsiteAddress.getText().toString());
            if (!url.startsWith("www.") && !url.startsWith("http://") && !url.startsWith("https://")) {
                url = "www." + url;
            }
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "http://" + url;
            }
            myWebLink.setData(Uri.parse(url));
            startActivity(myWebLink);
        }


    }

}


