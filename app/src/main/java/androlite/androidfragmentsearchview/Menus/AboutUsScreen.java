package androlite.androidfragmentsearchview.Menus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import androlite.androidfragmentsearchview.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Musabir on 4/11/2017.
 */

public class AboutUsScreen extends PreferenceFragment {


    private Typeface tf;
    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tf = Typeface.createFromAsset(getActivity().getAssets(), "clear.otf");
        RelativeLayout rf = (RelativeLayout) getActivity().findViewById(R.id.hs);
        rf.setVisibility(View.VISIBLE);
        View view = inflater.inflate(R.layout.about_us, container, false);
        textView = (TextView) view.findViewById(R.id.about_us_txt);
        textView.setTypeface(tf);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","info@contapp.az", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Send From Contapp");
                startActivity(Intent.createChooser(emailIntent, "Send Feedback"));
            }
        });


        return view;
    }

    public void expandToolbar(){
        AppBarLayout appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.app_bar);
        appBarLayout.setExpanded(true);
    }
    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences mSh = getActivity().getSharedPreferences("Value", MODE_PRIVATE);
        String selectedValue = mSh.getString("val", "az");

        String childReadData = readFromFile(getActivity(), "about_us.txt");
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) getActivity().findViewById(R.id.collapse_commonview_header);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "clear.otf");
        collapsingToolbarLayout.setCollapsedTitleTypeface(tf);
        collapsingToolbarLayout.setTitleEnabled(true);
         mSh =   getActivity().getSharedPreferences("Value",MODE_PRIVATE);
        expandToolbar();
        selectedValue = mSh.getString("val","az");
        if(selectedValue.equals("az"))
            collapsingToolbarLayout.setTitle("Haqqımızda");
        else if(selectedValue.equals("en"))
            collapsingToolbarLayout.setTitle("About Us");
        else if(selectedValue.equals("ru"))
            collapsingToolbarLayout.setTitle("О Нас");

        ImageView header = (ImageView) getActivity().findViewById(R.id.header);
        header.setImageResource(R.drawable.about_us);
        if (childReadData != null) {
            try {
                JSONObject jsonObj = new JSONObject(childReadData);
                JSONObject jsonObj_child = new JSONObject(childReadData);

                // Getting JSON Array node
                JSONArray contacts = jsonObj.getJSONArray("server_response");


                // looping through All Contacts
                for (int i = 0; i < contacts.length(); i++) {
                    JSONObject c = contacts.getJSONObject(i);
                    textView.setText(c.getString("about_"+selectedValue));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private String readFromFile(Context context, String fileName) {

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
