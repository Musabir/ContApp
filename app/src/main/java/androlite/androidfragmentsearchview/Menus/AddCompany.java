package androlite.androidfragmentsearchview.Menus;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceFragment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.vlk.multimager.activities.GalleryActivity;
import com.vlk.multimager.utils.Constants;
import com.vlk.multimager.utils.Image;
import com.vlk.multimager.utils.Params;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import androlite.androidfragmentsearchview.DBHelper;
import androlite.androidfragmentsearchview.R;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Musabir on 4/17/2017.
 */

public class AddCompany extends PreferenceFragment  {
    RequestQueue requestQueue;
    String insertUrl = "http://contapp.avirtel.az/API/json_add_object.php";
    private int t = 0;
    String selectedValue="";
    final int PLACE_PICKER = 1;
    final int PICTURE_SELECTED = 2;
    private LinearLayout mLayout;
    private ImageView ic;
    ArrayList<String> city_id;
    ImageView right1,right2,right3,right4,right5;
    private DBHelper mydb ;

    //  RelativeLayout.LayoutParams lparams;
    String city_selected_item,category_selected_item,object_seleted_item;
    String city_selected_item_id,category_selected_item_id,object_seleted_item_id;

    private final static int MY_PERMISSION_FINE_LOCATION = 101;
    String address;
    int c=0;
    int p=0;
    int e = 0;


    TextView citytxt,categorytxt,sub_categorytxt,profiletxt,addresstxt,imagetxt,imagetxt1;
    TextInputEditText placeName,phonenum1txt,phonenum2txt,phonenum3txt,emailtxt, tollfreetxt,
            websitetxt,fbtxt,twittertxt,instagramtxt,googletxt;
    RelativeLayout rl;
    RelativeLayout city_lyt,category_lyt,profile_lyt,company_lyt,address_lyt,image_lyt,image_lyt1;
    private String lnglat;
    Typeface tf;
    private ProgressDialog pDialog;
    private InputMethodManager im ;
    static boolean b1 =false;
    ArrayList<String> cont = new ArrayList<>();
    private Handler h;
    String selectedItem = "Musa";
    String concatOfAllImageNames ="";
    private String sLat=null;
    private String sLng=null;

    private ArrayAdapter<String> company_adapter;
    private ArrayAdapter<String> subobject_adapter;

    private ArrayAdapter<String> spinnerArrayAdapter_city;
    LinearLayout phonelayout,profilelayout,companylayout;
    private DeviceUuidFactory dv;

    Button submit;
    ImageView header;
    String imageEncoded;
    ArrayList<String> imagesEncodedList;
    Button GetImageFromGalleryButton, UploadImageOnServerButton;

    ImageView ShowSelectedImage;

    EditText GetImageName;

    Bitmap FixBitmap;

    String ImageTag = "image_tag" ;

    String ImageName = "image_data" ;

    String ServerUploadPath ="http://androidblog.esy.es/upload-image-server.php" ;

    ProgressDialog progressDialog ;

    ByteArrayOutputStream byteArrayOutputStream ;

    byte[] byteArray ;

    String ConvertImage ="";
    ArrayList<String> backgroundConvertedImage;

    String GetImageNameFromEditText;

    HttpURLConnection httpURLConnection ;

    URL url;

    OutputStream outputStream;

    BufferedWriter bufferedWriter ;

    int RC ;

    BufferedReader bufferedReader ;

    StringBuilder stringBuilder;

    boolean check = true;
    private HashMap<String, String> parameters;
    private View view;
    private String picturePath;
    private ArrayList<String> picturePathList;
    private ArrayList<Image> imagesList;
    private boolean back = false,log=false;
    int city_rem=-1,cat_rem=-1,sub_cat_rem=-1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tf = Typeface.createFromAsset(getActivity().getAssets(), "clear.otf");
        RelativeLayout rf = (RelativeLayout) getActivity().findViewById(R.id.hs);
        rf.setVisibility(View.VISIBLE);
        imagesList = new ArrayList<>();
        backgroundConvertedImage =new ArrayList<>();
        picturePathList = new ArrayList<>();
        picturePath=null;
        imagesEncodedList = new ArrayList<>();
        parameters = new HashMap<>();
        requestQueue = Volley.newRequestQueue(getActivity());
        view = inflater.inflate(R.layout.add_company, container, false);

        mLayout = (LinearLayout) view.findViewById(R.id.Layout);
        city_lyt = (RelativeLayout) view.findViewById(R.id.city_lyt);
        category_lyt = (RelativeLayout) view.findViewById(R.id.category_lyt);
        profile_lyt = (RelativeLayout) view.findViewById(R.id.profile_lyt);
        rl = (RelativeLayout) view.findViewById(R.id.layout);
        address_lyt = (RelativeLayout) view.findViewById(R.id.address_lyt);
        image_lyt = (RelativeLayout) view.findViewById(R.id.image_lyt);
        image_lyt1 = (RelativeLayout) view.findViewById(R.id.image_lyt1);

        ic = (ImageView) view.findViewById(R.id.address_icon);
        object_seleted_item="";
        category_selected_item="";
        city_selected_item="";
        SharedPreferences mSh = getActivity().getSharedPreferences("Value", MODE_PRIVATE);
        String selectedValue = mSh.getString("val","az");


        placeName = (TextInputEditText) view.findViewById(R.id.placeName);
        citytxt = (TextView) view.findViewById(R.id.txt2);
        categorytxt = (TextView) view.findViewById(R.id.txt3);
        sub_categorytxt = (TextView) view.findViewById(R.id.txt4);
        phonenum1txt = (TextInputEditText) view.findViewById(R.id.phoneName);
        phonenum2txt = (TextInputEditText) view.findViewById(R.id.phoneName1);
        phonenum3txt = (TextInputEditText) view.findViewById(R.id.phoneName2);
        profiletxt = (TextView) view.findViewById(R.id.profiletxt);
        tollfreetxt = (TextInputEditText) view.findViewById(R.id.tollfreetxt);
        websitetxt = (TextInputEditText) view.findViewById(R.id.websitetxt);
        fbtxt = (TextInputEditText) view.findViewById(R.id.fbtxt);
        twittertxt = (TextInputEditText) view.findViewById(R.id.twittertxt);
        instagramtxt = (TextInputEditText) view.findViewById(R.id.instagramtxt);
        googletxt = (TextInputEditText) view.findViewById(R.id.googletxt);
        addresstxt = (TextView) view.findViewById(R.id.addresstxt);
        imagetxt = (TextView) view.findViewById(R.id.imagetxt);
        imagetxt1 = (TextView) view.findViewById(R.id.imagetxt1);
        emailtxt = (TextInputEditText) view.findViewById(R.id.emailtxt);


        submit = (Button) view.findViewById(R.id.submit);


        right1 = (ImageView) view.findViewById(R.id.right1);
        right2 = (ImageView) view.findViewById(R.id.right2);
        right3 = (ImageView) view.findViewById(R.id.right3);
        right4 = (ImageView) view.findViewById(R.id.right4);
        right5 = (ImageView) view.findViewById(R.id.right5);

        phonelayout = (LinearLayout) view.findViewById(R.id.phonelayout);
        profilelayout = (LinearLayout) view.findViewById(R.id.profilelayout);
        companylayout = (LinearLayout) view.findViewById(R.id.companylayout);

        placeName.setTypeface(tf);
        citytxt.setTypeface(tf);
        categorytxt.setTypeface(tf);
        sub_categorytxt.setTypeface(tf);
        phonenum1txt.setTypeface(tf);
        phonenum2txt.setTypeface(tf);
        phonenum3txt.setTypeface(tf);
        emailtxt.setTypeface(tf);
        tollfreetxt.setTypeface(tf);
        websitetxt.setTypeface(tf);
        fbtxt.setTypeface(tf);
        instagramtxt.setTypeface(tf);
        googletxt.setTypeface(tf);
        addresstxt.setTypeface(tf);
        imagetxt.setTypeface(tf);
        imagetxt1.setTypeface(tf);
        submit.setTypeface(tf);

        mydb = new DBHelper(getActivity());

        right4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(phonelayout.getVisibility()==View.GONE) {
                    phonelayout.setVisibility(View.VISIBLE);
                    right4.setImageResource(R.drawable.y);
                }
                else {
                    phonelayout.setVisibility(View.GONE);
                    right4.setImageResource(R.drawable.asag);
                }
            }
        });
        profile_lyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(profilelayout.getVisibility()==View.GONE) {
                    profilelayout.setVisibility(View.VISIBLE);
                    right5.setImageResource(R.drawable.y);

                }
                else {
                    profilelayout.setVisibility(View.GONE);
                    right5.setImageResource(R.drawable.asag);

                }
            }
        });

        city_id = new ArrayList<>();



        final ArrayList<String> cat_id = new ArrayList<>();
        final ArrayList<String> cat = new ArrayList<>();
        final ArrayList<String> sub_list = new ArrayList<>();
        final ArrayList<String> sub_list_id = new ArrayList<>();

        mSh =   getActivity().getSharedPreferences("Value",MODE_PRIVATE);
        selectedValue = mSh.getString("val","az");

        if(selectedValue.equals("az"))
        {
            sub_list.add("Digər");
            sub_list_id.add("-1");
            submit.setText("Göndər");

        }
        else if(selectedValue.equals("en"))
        {
            sub_list.add("Other");
            sub_list_id.add("-1");
            submit.setText("Submit");


        }
        else if(selectedValue.equals("ru"))
        {
            sub_list_id.add("-1");
            sub_list.add("Другой");
            submit.setText("Потвердить");

        }


        final ArrayList<String> cities = new ArrayList<>();
        String city_names = readFromFile(getActivity(),"city.txt");
        if (city_names != null) {
            try {

                JSONObject jsonCityObj = new JSONObject(city_names);
                JSONArray cityArray = jsonCityObj.getJSONArray("server_response");


                for (int i = 0; i < cityArray.length(); i++) {
                    JSONObject c = cityArray.getJSONObject(i);

                    city_id.add(c.getString("id"));
                    cities.add(c.getString(("name_") + selectedValue));
                }

            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        String cityTitle = "",categoryTitle = "",companyTitle = "";
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(
                getActivity());
        String cancel = "Choose";
        if(selectedValue.equals("az")) {
            cityTitle = "Şəhərlər";
            categoryTitle = "Kateqoriyalar";
            companyTitle = "Şirkətlər";
            cancel="Seç";
        }
        else if(selectedValue.equals("en")) {
            cityTitle = "Cities";
            categoryTitle = "Categories";
            companyTitle = "Companies";
            cancel = "Choose";
        }
        else if(selectedValue.equals("ru")) {
            cityTitle = "Города";
            categoryTitle = "Kатегории";
            companyTitle = "Kомпании";
            cancel="Bыберите";
        }

        final String finalCityTitle = cityTitle;
        final String finalCancel = cancel;
        final String finalCategoryTitle = categoryTitle;

        final String parentReadData = readFromFile(getActivity(),"ParentCategory.txt");
        final String childReadData = readFromFile(getActivity(),"ChildCategory.txt");
        final String subReadData = readFromFile(getActivity(),"AllData.txt");
        try {
            JSONObject parent = new JSONObject(parentReadData);
            JSONObject child = new JSONObject(childReadData);
            JSONArray p = parent.getJSONArray("server_response");
            JSONArray ch = child.getJSONArray("server_response");

            if(selectedValue.equals("az"))
            {
                cat.add("Digər");
                cat_id.add("-1");

            }
            else if(selectedValue.equals("en"))
            {
                cat.add("Other");
                cat_id.add("-1");

            }
            else if(selectedValue.equals("ru"))
            {
                cat.add("Другой");
                cat_id.add("-1");
            }
            for (int i = 0; i < p.length(); i++) {
                String pp ="";
                JSONObject a = p.getJSONObject(i);

                //  String p_mAllValue = a.getString("name");
                pp=a.getString("name_"+selectedValue);
                String p_id = a.getString("id");
                boolean g = false;
                for (int j = 0; j < ch.length(); j++) {
                    JSONObject b = ch.getJSONObject(j);

                    //  String p_mAllValue = a.getString("name");
                    if (b.getString("pid").equals(p_id)) {
                        cat.add(b.getString("name_"+selectedValue));
                        cat_id.add(b.getString("id"));
                        g = true;
                    }
                }
                if(!g) {
                    cat.add(pp);
                    cat_id.add(p_id);
                }

            }
        } catch (JSONException e2) {
            e2.printStackTrace();
        }


        final ArrayList<String> finalCities = cities;
        city_lyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        getActivity(),
                        android.R.layout.select_dialog_singlechoice, finalCities);
                builderSingle.setTitle(finalCityTitle);
                builderSingle.setNegativeButton(finalCancel,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                builderSingle.setSingleChoiceItems(arrayAdapter, city_rem,new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        city_selected_item = finalCities.get(which);
                        citytxt.setText(city_selected_item);
                        city_rem = which;
                        dialog.dismiss();


                    }

                });
                builderSingle.create();
                builderSingle.show();
            }
        });
        final String finalSelectedValue3 = selectedValue;
        category_lyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        getActivity(),
                        android.R.layout.select_dialog_singlechoice,cat);
                builderSingle.setTitle(finalCategoryTitle);
                builderSingle.setNegativeButton(finalCancel,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                builderSingle.setSingleChoiceItems(arrayAdapter, cat_rem,new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        category_selected_item = cat.get(which);
                        categorytxt.setText(category_selected_item);
                        cat_rem = which;

                        if(which!=0)
                            companylayout.setVisibility(View.VISIBLE);
                        else {
                            if(which==0)
                                object_seleted_item="r";
                            companylayout.setVisibility(View.GONE);
                        }
                        sub_list.clear();
                        if(finalSelectedValue3.equals("az"))
                        {
                            sub_list.add("Digər");

                        }
                        else if(finalSelectedValue3.equals("en"))
                        {
                            sub_list.add("Other");


                        }
                        else if(finalSelectedValue3.equals("ru"))
                        {
                            sub_list.add("Другой");

                        }

                        try {

                            for (int i = 0; i < cat.size(); i++) {
                                Log.d("--->",cat.size()+"   "+selectedItem);
                                if (cat.get(i).equals(category_selected_item)) {
                                    String sub_id = cat_id.get(i);

                                    JSONObject sub = new JSONObject(subReadData);
                                    JSONArray s = sub.getJSONArray("server_response");
                                    for (int j = 0; j < s.length(); j++) {
                                        JSONObject a = s.getJSONObject(j);
                                        if (a.getString("obj_pid").equals("null"))

                                            if (a.getString("obj_pid").equals("null") && a.getString("cat_id").equals(sub_id)) {
                                                sub_list.add(a.getString("name"));
                                            }

                                    }
                                    break;
                                }


                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();

                        }
                        dialog.dismiss();

                    }

                });
                builderSingle.create();
                builderSingle.show();
            }
        });


        final String finalCompanyTitle = companyTitle;
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        getActivity(),
                        android.R.layout.select_dialog_singlechoice,sub_list);
                builderSingle.setTitle(finalCompanyTitle);
                builderSingle.setNegativeButton(finalCancel,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                builderSingle.setSingleChoiceItems(arrayAdapter, sub_cat_rem,new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sub_cat_rem=-1;
                        object_seleted_item = sub_list.get(which);
                        sub_categorytxt.setText(object_seleted_item);
                        dialog.dismiss();

                    }

                });
                builderSingle.create();
                builderSingle.show();
            }
        });


        //cat.remove(0);
        company_adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_text,cat ){
            public View getView(int position, View convertView, ViewGroup parent)
            {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(tf);

                return v;
            }
            public View getDropDownView(int position, View convertView, ViewGroup parent)
            {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(tf);//Typeface for dropdown view
                return v;
            }
        };



        final String nameText[] = {"Insert place name","Məkan adı daxil edin","Введите название места"};
        final String cityText[] = {"Select city","Şəhər seçin","Выберите город"};
        final String categoryText[] = {"Select category","Kateqoriya seçin","Выберите категорию"};
        final String companyText[] = {"Select company","Şirkət seçin","Выберите компанию"};
        final String phoneText1[] = {"Invalid phone number","Yanlış nömrə","Неправельный номер"};
        final String phoneText2[] = {"Invalid phone number 2","Yanlış nömrə 2","Неправельный номер 2"};
        final String phoneText3[] = {"Invalid phone number 3","Yanlış nömrə 3","Неправельный номер 3"};
        final String phoneText[] = {"Insert phone number","Nömrə daxil edin","Введите номер"};
        final String tollfreeText[] = {"Invalid toll free number","Yanlış Toll Free nömrə","Неправельный toll free номер"};
        final String emailText[] = {"Invalid e-mail","Yanlış e-mail address","Неправельный почта"};
        final String websiteText[] = {"Invalid website url","Yanlış website address","Неправельный адрес сайта"};
        final String facebookText[] = {"Invalid Facebook url","Yanlış Facebook address","Неправельный фейсбук адрес"};
        final String twitterText[] = {"Invalid Twitter url","Yanlış Twitter address","Неправельный твиттер адрес"};
        final String instagramText[] = {"Invalid Instagram url","Yanlış Instagram address","Неправельный Инстаграм адрес"};
        final String googleText[] = {"Invalid Google+ url","Yanlış Google+ address","Неправельный Гугл+ адрес"};
        final String backgroundText[] = {"Select background image","Arxa fon şəkili seçin","Выберите фоновое изображение"};
        final String logoText[] = {"Select logo","Logo daxil edin","Выбрать логотип"};
        final String addressText[] = {"Select Address","Address daxil edin","Введите адрес"};

        final String finalSelectedValue1 = selectedValue;
        final String finalSelectedValue = selectedValue;
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final boolean[] t = {false};
                String name = placeName.getText().toString().trim();
                if (name == null || name.equals("") || name.equals("null")) {
                    if (finalSelectedValue1.equals("az"))
                        Toast.makeText(getActivity().getApplicationContext(), nameText[1], Toast.LENGTH_SHORT).show();
                    else if (finalSelectedValue1.equals("en"))
                        Toast.makeText(getActivity().getApplicationContext(), nameText[0], Toast.LENGTH_SHORT).show();
                    else if (finalSelectedValue1.equals("ru"))
                        Toast.makeText(getActivity().getApplicationContext(), nameText[2], Toast.LENGTH_SHORT).show();
                    t[0] = true;
                } else if (city_selected_item.equals("")) {
                    if (finalSelectedValue1.equals("az"))
                        Toast.makeText(getActivity().getApplicationContext(), cityText[1], Toast.LENGTH_SHORT).show();
                    else if (finalSelectedValue1.equals("en"))
                        Toast.makeText(getActivity().getApplicationContext(), cityText[0], Toast.LENGTH_SHORT).show();
                    else if (finalSelectedValue1.equals("ru"))
                        Toast.makeText(getActivity().getApplicationContext(), cityText[2], Toast.LENGTH_SHORT).show();
                    t[0] = true;
                } else if (category_selected_item.equals("")) {
                    if (finalSelectedValue1.equals("az"))
                        Toast.makeText(getActivity().getApplicationContext(), categoryText[1], Toast.LENGTH_SHORT).show();
                    else if (finalSelectedValue1.equals("en"))
                        Toast.makeText(getActivity().getApplicationContext(), categoryText[0], Toast.LENGTH_SHORT).show();
                    else if (finalSelectedValue1.equals("ru"))
                        Toast.makeText(getActivity().getApplicationContext(), categoryText[2], Toast.LENGTH_SHORT).show();
                    t[0] = true;

                } else if (object_seleted_item.equals("")) {
                    if (finalSelectedValue1.equals("az"))
                        Toast.makeText(getActivity().getApplicationContext(), companyText[1], Toast.LENGTH_SHORT).show();
                    else if (finalSelectedValue1.equals("en"))
                        Toast.makeText(getActivity().getApplicationContext(), companyText[0], Toast.LENGTH_SHORT).show();
                    else if (finalSelectedValue1.equals("ru"))
                        Toast.makeText(getActivity().getApplicationContext(), companyText[2], Toast.LENGTH_SHORT).show();
                    t[0] = true;

                } else if (phonenum1txt.getText().toString().trim().isEmpty() && phonenum2txt.getText().toString().trim().isEmpty() && phonenum3txt.getText().toString().trim().isEmpty()) {
                    if (finalSelectedValue1.equals("az"))
                        Toast.makeText(getActivity().getApplicationContext(), phoneText[1], Toast.LENGTH_SHORT).show();
                    else if (finalSelectedValue1.equals("en"))
                        Toast.makeText(getActivity().getApplicationContext(), phoneText[0], Toast.LENGTH_SHORT).show();
                    else if (finalSelectedValue1.equals("ru"))
                        Toast.makeText(getActivity().getApplicationContext(), phoneText[2], Toast.LENGTH_SHORT).show();
                    t[0] = true;

                } else if (!isValidPhone(phonenum1txt.getText().toString().trim())) {
                    if (finalSelectedValue1.equals("az"))
                        Toast.makeText(getActivity().getApplicationContext(), phoneText1[1], Toast.LENGTH_SHORT).show();
                    else if (finalSelectedValue1.equals("en"))
                        Toast.makeText(getActivity().getApplicationContext(), phoneText1[0], Toast.LENGTH_SHORT).show();
                    else if (finalSelectedValue1.equals("ru"))
                        Toast.makeText(getActivity().getApplicationContext(), phoneText1[2], Toast.LENGTH_SHORT).show();
                    t[0] = true;
                } else if (!isValidPhone(phonenum2txt.getText().toString().trim())) {
                    if (finalSelectedValue1.equals("az"))
                        Toast.makeText(getActivity().getApplicationContext(), phoneText2[1], Toast.LENGTH_SHORT).show();
                    else if (finalSelectedValue1.equals("en"))
                        Toast.makeText(getActivity().getApplicationContext(), phoneText2[0], Toast.LENGTH_SHORT).show();
                    else if (finalSelectedValue1.equals("ru"))
                        Toast.makeText(getActivity().getApplicationContext(), phoneText2[2], Toast.LENGTH_SHORT).show();
                    t[0] = true;

                } else if (!isValidPhone(phonenum3txt.getText().toString().trim())) {
                    if (finalSelectedValue1.equals("az"))
                        Toast.makeText(getActivity().getApplicationContext(), phoneText3[1], Toast.LENGTH_SHORT).show();
                    else if (finalSelectedValue1.equals("en"))
                        Toast.makeText(getActivity().getApplicationContext(), phoneText3[0], Toast.LENGTH_SHORT).show();
                    else if (finalSelectedValue1.equals("ru"))
                        Toast.makeText(getActivity().getApplicationContext(), phoneText3[2], Toast.LENGTH_SHORT).show();
                    t[0] = true;
                } else if (!isValidPhone(tollfreetxt.getText().toString().trim())) {
                    if (finalSelectedValue1.equals("az"))
                        Toast.makeText(getActivity().getApplicationContext(), tollfreeText[1], Toast.LENGTH_SHORT).show();
                    else if (finalSelectedValue1.equals("en"))
                        Toast.makeText(getActivity().getApplicationContext(), tollfreeText[0], Toast.LENGTH_SHORT).show();
                    else if (finalSelectedValue1.equals("ru"))
                        Toast.makeText(getActivity().getApplicationContext(), tollfreeText[2], Toast.LENGTH_SHORT).show();
                    t[0] = true;
                } else if (!isValidEmail(emailtxt.getText().toString().trim())) {
                    if (finalSelectedValue1.equals("az"))
                        Toast.makeText(getActivity().getApplicationContext(), emailText[1], Toast.LENGTH_SHORT).show();
                    else if (finalSelectedValue1.equals("en"))
                        Toast.makeText(getActivity().getApplicationContext(), emailText[0], Toast.LENGTH_SHORT).show();
                    else if (finalSelectedValue1.equals("ru"))
                        Toast.makeText(getActivity().getApplicationContext(), emailText[2], Toast.LENGTH_SHORT).show();
                    t[0] = true;

                } else if (!isValidUrl(websitetxt.getText().toString().trim())) {
                    if (finalSelectedValue1.equals("az"))
                        Toast.makeText(getActivity().getApplicationContext(), websiteText[1], Toast.LENGTH_SHORT).show();
                    else if (finalSelectedValue1.equals("en"))
                        Toast.makeText(getActivity().getApplicationContext(), websiteText[0], Toast.LENGTH_SHORT).show();
                    else if (finalSelectedValue1.equals("ru"))
                        Toast.makeText(getActivity().getApplicationContext(), websiteText[2], Toast.LENGTH_SHORT).show();
                    t[0] = true;
                } else if (!isValidUrl(fbtxt.getText().toString().trim())) {
                    if (finalSelectedValue1.equals("az"))
                        Toast.makeText(getActivity().getApplicationContext(), facebookText[1], Toast.LENGTH_SHORT).show();
                    else if (finalSelectedValue1.equals("en"))
                        Toast.makeText(getActivity().getApplicationContext(), facebookText[0], Toast.LENGTH_SHORT).show();
                    else if (finalSelectedValue1.equals("ru"))
                        Toast.makeText(getActivity().getApplicationContext(), facebookText[2], Toast.LENGTH_SHORT).show();
                    t[0] = true;
                } else if (!isValidUrl(instagramtxt.getText().toString().trim())) {
                    if (finalSelectedValue1.equals("az"))
                        Toast.makeText(getActivity().getApplicationContext(), instagramText[1], Toast.LENGTH_SHORT).show();
                    else if (finalSelectedValue1.equals("en"))
                        Toast.makeText(getActivity().getApplicationContext(), instagramText[0], Toast.LENGTH_SHORT).show();
                    else if (finalSelectedValue1.equals("ru"))
                        Toast.makeText(getActivity().getApplicationContext(), instagramText[2], Toast.LENGTH_SHORT).show();
                    t[0] = true;
                } else if (!isValidUrl(twittertxt.getText().toString().trim())) {
                    if (finalSelectedValue1.equals("az"))
                        Toast.makeText(getActivity().getApplicationContext(), twitterText[1], Toast.LENGTH_SHORT).show();
                    else if (finalSelectedValue1.equals("en"))
                        Toast.makeText(getActivity().getApplicationContext(), twitterText[0], Toast.LENGTH_SHORT).show();
                    else if (finalSelectedValue1.equals("ru"))
                        Toast.makeText(getActivity().getApplicationContext(), twitterText[2], Toast.LENGTH_SHORT).show();
                    t[0] = true;
                } else if (!isValidUrl(googletxt.getText().toString().trim())) {
                    if (finalSelectedValue1.equals("az"))
                        Toast.makeText(getActivity().getApplicationContext(), googleText[1], Toast.LENGTH_SHORT).show();
                    else if (finalSelectedValue1.equals("en"))
                        Toast.makeText(getActivity().getApplicationContext(), googleText[0], Toast.LENGTH_SHORT).show();
                    else if (finalSelectedValue1.equals("ru"))
                        Toast.makeText(getActivity().getApplicationContext(), googleText[2], Toast.LENGTH_SHORT).show();
                    t[0] = true;
                } else if (addresstxt.getText().toString().trim().isEmpty()) {
                    if (finalSelectedValue1.equals("az"))
                        Toast.makeText(getActivity().getApplicationContext(), addressText[1], Toast.LENGTH_SHORT).show();
                    else if (finalSelectedValue1.equals("en"))
                        Toast.makeText(getActivity().getApplicationContext(), addressText[0], Toast.LENGTH_SHORT).show();
                    else if (finalSelectedValue1.equals("ru"))
                        Toast.makeText(getActivity().getApplicationContext(), addressText[2], Toast.LENGTH_SHORT).show();
                    t[0] = true;
                }
                else if (back==false) {
                    if (finalSelectedValue1.equals("az"))
                        Toast.makeText(getActivity().getApplicationContext(), backgroundText[1], Toast.LENGTH_SHORT).show();
                    else if (finalSelectedValue1.equals("en"))
                        Toast.makeText(getActivity().getApplicationContext(), backgroundText[0], Toast.LENGTH_SHORT).show();
                    else if (finalSelectedValue1.equals("ru"))
                        Toast.makeText(getActivity().getApplicationContext(), backgroundText[2], Toast.LENGTH_SHORT).show();
                    t[0] = true;
                }
                else if (log==false) {
                    if (finalSelectedValue1.equals("az"))
                        Toast.makeText(getActivity().getApplicationContext(), logoText[1], Toast.LENGTH_SHORT).show();
                    else if (finalSelectedValue1.equals("en"))
                        Toast.makeText(getActivity().getApplicationContext(), logoText[0], Toast.LENGTH_SHORT).show();
                    else if (finalSelectedValue1.equals("ru"))
                        Toast.makeText(getActivity().getApplicationContext(), logoText[2], Toast.LENGTH_SHORT).show();
                    t[0] = true;
                }
                Log.d("--->", finalSelectedValue);


                if (!t[0]) {
                    parameters.put("dil", finalSelectedValue);
                    parameters.put("company_name", placeName.getText().toString().trim());
                    if(category_selected_item.equals(cat.get(0)))
                        parameters.put("category", "r");
                    else
                        parameters.put("category", category_selected_item);
                    if(category_selected_item.equals(sub_list.get(0)))//error
                        parameters.put("object", "r");
                    else  parameters.put("object", object_seleted_item);
                    Log.d("-->",cat.get(0)+" : "+sub_list.get(0));
                    parameters.put("city", city_selected_item);
                    parameters.put("website", websitetxt.getText().toString().trim());
                    parameters.put("tollfree", tollfreetxt.getText().toString().trim());
                    parameters.put("facebook", fbtxt.getText().toString().trim());
                    parameters.put("twitter", twittertxt.getText().toString().trim());
                    parameters.put("instagram", instagramtxt.getText().toString().trim());
                    parameters.put("google", googletxt.getText().toString().trim());
                    parameters.put("phone", phonenum1txt.getText().toString().trim()+", "+phonenum2txt.getText().toString().trim()+", "+phonenum3txt.getText().toString().trim());
                    parameters.put("email", emailtxt.getText().toString().trim());
                    parameters.put("address", addresstxt.getText().toString().trim());
                    parameters.put("lat", sLat);
                    parameters.put("lng", sLng);
                    parameters.put("id", String.valueOf(dv.getDeviceUuid()));

                    if(back==true&&log==true)
                        UploadImageToServer();
                }
            }
        });



        im = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(addresstxt.getWindowToken(), 0);

        final String finalSelectedValue2 = selectedValue;


        spinnerArrayAdapter_city = new ArrayAdapter<String>(
                getActivity(), R.layout.spinner_text, cities
        ){
            public View getView(int position, View convertView, ViewGroup parent)
            {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(tf);

                return v;
            }
            public View getDropDownView(int position, View convertView, ViewGroup parent)
            {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(tf);//Typeface for dropdown view
                return v;
            }
        };

        image_lyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GalleryActivity.class);
                Params params = new Params();
                params.setCaptureLimit(5);
                params.setPickerLimit(6);
                params.setToolbarColor(R.color.colorPrimary);

                intent.putExtra(Constants.KEY_PARAMS, params);
                startActivityForResult(intent, Constants.TYPE_MULTI_PICKER);
            }
        });
        image_lyt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICTURE_SELECTED);
            }
        });


        address_lyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                Intent intent;

                try {
                    intent = builder.build(getActivity());

                    startActivityForResult(intent, PLACE_PICKER);

                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }


            }
        });

        if(selectedValue.equals("az"))
        {
            placeName.setHint("Məkanın adı");
            citytxt.setHint("Şəhər");
            categorytxt.setHint("Kateqoriya");
            sub_categorytxt.setHint("Şirkət");
            imagetxt1.setHint("Logo");
            imagetxt.setHint("Arxa fon şəkili");
            phonenum1txt.setHint("Nömrə");
            phonenum2txt.setHint("Nömrə 2");
            phonenum3txt.setHint("Nömrə 3");
            emailtxt.setHint("Email");
            profiletxt.setHint("Profil");
            tollfreetxt.setHint("Toll free");
            websitetxt.setHint("Veb sayt");
            fbtxt.setHint("Facebook");
            twittertxt.setHint("Twitter");
            instagramtxt.setHint("Instagram");
            googletxt.setHint("Google Plus");
        }
        else if(selectedValue.equals("en"))
        {
            placeName.setHint("Place name");
            citytxt.setHint("City");
            categorytxt.setHint("Category");
            sub_categorytxt.setHint("Company");
            imagetxt1.setHint("Logo");
            imagetxt.setHint("Background image");
            phonenum1txt.setHint("Phone number");
            phonenum2txt.setHint("Phone number 2");
            phonenum3txt.setHint("Phone number 3");
            emailtxt.setHint("Email");
            profiletxt.setHint("Profile");
            tollfreetxt.setHint("Toll free");
            websitetxt.setHint("Website");
            fbtxt.setHint("Facebook");
            twittertxt.setHint("Twitter");
            instagramtxt.setHint("Instagram");
            googletxt.setHint("Google Plus");
        }
        else if(selectedValue.equals("ru"))
        {
            placeName.setHint("Название места");
            citytxt.setHint("Город");
            categorytxt.setHint("Категории");
            sub_categorytxt.setHint("Категории");
            imagetxt1.setHint("Логотип");//bura
            imagetxt.setHint("Background image");//bura
            phonenum1txt.setHint("Номер");//bura
            phonenum2txt.setHint("Номер 2");//bura
            phonenum3txt.setHint("Номер 3");//bura
            emailtxt.setHint("Эл. адрес");//bura
            profiletxt.setHint("Профиль");//bura
            tollfreetxt.setHint("Toll free");//bura
            websitetxt.setHint("Веб-сайт");//bura
            fbtxt.setText("Facebook");//bura
            twittertxt.setHint("Твиттер");//bura
            instagramtxt.setHint("Instagram");//bura
            googletxt.setHint("Google Plus");//bura
        }



        return view;



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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_PICKER) {
            if (resultCode == getActivity().RESULT_OK) {

                Place place = PlacePicker.getPlace(data, getActivity());

                lnglat = place.getLatLng().toString();
                String[] latLng = lnglat.substring(10, lnglat.length() - 1).split(",");
                sLat = latLng[0];
                sLng = latLng[1];
                if (place != null) {
                    address = (String) place.getAddress();
                    addresstxt.setText(address);


                }
            }
        }
        if (requestCode == PICTURE_SELECTED && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            try {
                log = true;

                FixBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                Uri selectedImage = data.getData();

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                picturePath = cursor.getString(columnIndex);
                Log.d("--->Paht",picturePath);
                File f = new File(picturePath);
                if (!f.getName().isEmpty())
                    imagetxt1.setText(f.getName());
                cursor.close();

            } catch (IOException e) {

                e.printStackTrace();
            }
        }

        // When an Image is picked
        if (requestCode == Constants.TYPE_MULTI_PICKER && resultCode == getActivity().RESULT_OK
                && null != data) {
            back  = true;
            concatOfAllImageNames="";
            // Get the Image from data
            imagesList = data.getParcelableArrayListExtra(Constants.KEY_BUNDLE_LIST);
            for(int i=0;i<imagesList.size();i++) {
                Log.d("eee", imagesList.get(i).imagePath);
                String filename=imagesList.get(i).imagePath.substring(imagesList.get(i).imagePath.lastIndexOf("/")+1);

                concatOfAllImageNames +=filename + " ";

            }
            imagetxt.setText(concatOfAllImageNames);


        }

        super.onActivityResult(requestCode, resultCode, data);

    }
    protected int byteSizeOf(Bitmap data) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
            return data.getRowBytes() * data.getHeight();
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return data.getByteCount();
        } else {
            return data.getAllocationByteCount();
        }
    }
    public void UploadImageToServer() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        BitmapFactory.Options options = null;
        options = new BitmapFactory.Options();

        options.inSampleSize = 3;
        ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();
        Bitmap bm = BitmapFactory.decodeFile(picturePath,options);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, stream);

        byteArray = stream.toByteArray();
        ConvertImage = Base64.encodeToString(byteArray, 0);
        for(int i=0;i<imagesList.size();i++) {
            Bitmap bm1 = BitmapFactory.decodeFile(imagesList.get(i).imagePath, options);

            ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
            bm1.compress(Bitmap.CompressFormat.PNG, 100, stream1);

            byteArray = stream1.toByteArray();
            backgroundConvertedImage.add(Base64.encodeToString(byteArray, 0));

        }
        class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();

            }


            @Override
            protected String doInBackground(Void... params) {

                ImageProcessClass imageProcessClass = new ImageProcessClass();

                HashMap<String, String> HashMapParams = new HashMap<String, String>();

                HashMapParams.put(ImageName, ConvertImage);
                HashMapParams.put("Size",backgroundConvertedImage.size()+"");
                for(int i=0;i<backgroundConvertedImage.size();i++){
                    HashMapParams.put("image"+i,backgroundConvertedImage.get(i));
                }
                HashMapParams.putAll(parameters);

                String FinalData = imageProcessClass.ImageHttpRequest(insertUrl, HashMapParams);

                return FinalData;
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);
                if (progressDialog != null)
                    progressDialog.dismiss();

                Toast.makeText(getActivity(), string1, Toast.LENGTH_SHORT).show();
            }

        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
        AsyncTaskUploadClassOBJ.execute();

    }


    public class ImageProcessClass{

        public String ImageHttpRequest(String requestURL,HashMap<String, String> PData) {

            StringBuilder stringBuilder = new StringBuilder();

            try {
                url = new URL(requestURL);

                httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(20000);

                httpURLConnection.setConnectTimeout(20000);

                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setDoInput(true);

                httpURLConnection.setDoOutput(true);

                outputStream = httpURLConnection.getOutputStream();

                bufferedWriter = new BufferedWriter(

                        new OutputStreamWriter(outputStream, "UTF-8"));

                bufferedWriter.write(bufferedWriterDataFN(PData));

                bufferedWriter.flush();

                bufferedWriter.close();

                outputStream.close();

                RC = httpURLConnection.getResponseCode();

                if (RC == HttpsURLConnection.HTTP_OK) {

                    bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                    stringBuilder = new StringBuilder();

                    String RC2;

                    while ((RC2 = bufferedReader.readLine()) != null){

                        stringBuilder.append(RC2);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {

            stringBuilder = new StringBuilder();

            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {
                if (check)
                    check = false;
                else
                    stringBuilder.append("&");

                stringBuilder.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));

                stringBuilder.append("=");

                stringBuilder.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }

            return stringBuilder.toString();
        }

    }



    private void requestPermission() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_FINE_LOCATION);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences mSh =   getActivity().getSharedPreferences("Value",MODE_PRIVATE);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) getActivity().findViewById(R.id.collapse_commonview_header);
        collapsingToolbarLayout.setCollapsedTitleTypeface(tf);
        collapsingToolbarLayout.setTitleEnabled(true);
        expandToolbar();
        selectedValue = mSh.getString("val","az");
        if(selectedValue.equals("az"))
            collapsingToolbarLayout.setTitle("Məkan Əlavə Et");
        else if(selectedValue.equals("en"))
            collapsingToolbarLayout.setTitle("Add Place");
        else if(selectedValue.equals("ru"))
            collapsingToolbarLayout.setTitle("Добавить Место");

        header = (ImageView) getActivity().findViewById(R.id.header);
        header.setImageResource(R.drawable.addcompany);
        dv = new DeviceUuidFactory(getActivity());


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSION_FINE_LOCATION:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity().getApplicationContext(), "This app requires location permissions to be granted", Toast.LENGTH_SHORT).show();
                    requestPermission();
                }
                break;
        }
    }




    public void expandToolbar(){
        AppBarLayout appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.app_bar);
        appBarLayout.setExpanded(true);
    }
    public final static boolean isValidEmail(CharSequence target) {
        if(target.toString().isEmpty()) return true;
        if(!TextUtils.isEmpty(target))
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        return  true;
    }
    public final static boolean isValidPhone(CharSequence target) {
        if(target.toString().isEmpty()) return true;
        if(!TextUtils.isEmpty(target))
            return Patterns.PHONE.matcher(target).matches();
        return  true;
    }


    public static boolean isValidUrl(String urlString) {
        if(urlString.isEmpty()) return true;

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
    private void setNull(){
        placeName = (TextInputEditText) view.findViewById(R.id.placeName);
        citytxt = (TextView) view.findViewById(R.id.txt2);
        categorytxt = (TextView) view.findViewById(R.id.txt3);
        sub_categorytxt = (TextView) view.findViewById(R.id.txt4);
        phonenum1txt = (TextInputEditText) view.findViewById(R.id.phoneName);
        phonenum2txt = (TextInputEditText) view.findViewById(R.id.phoneName1);
        phonenum3txt = (TextInputEditText) view.findViewById(R.id.phoneName2);
        profiletxt = (TextView) view.findViewById(R.id.profiletxt);
        tollfreetxt = (TextInputEditText) view.findViewById(R.id.tollfreetxt);
        websitetxt = (TextInputEditText) view.findViewById(R.id.websitetxt);
        fbtxt = (TextInputEditText) view.findViewById(R.id.fbtxt);
        twittertxt = (TextInputEditText) view.findViewById(R.id.twittertxt);
        instagramtxt = (TextInputEditText) view.findViewById(R.id.instagramtxt);
        googletxt = (TextInputEditText) view.findViewById(R.id.googletxt);
        addresstxt = (TextView) view.findViewById(R.id.addresstxt);
        imagetxt = (TextView) view.findViewById(R.id.imagetxt);
        imagetxt1 = (TextView) view.findViewById(R.id.imagetxt1);
        emailtxt = (TextInputEditText) view.findViewById(R.id.emailtxt);


        rl.setVisibility(View.GONE);
        profilelayout.setVisibility(View.GONE);
        phonelayout.setVisibility(View.GONE);
        right4.setImageResource(R.drawable.asag);
        right5.setImageResource(R.drawable.asag);


    }

}
