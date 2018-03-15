package androlite.androidfragmentsearchview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;


import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;


public class Splashscreen extends Activity {

    Thread splashTread;
    String city_url = "http://contapp.avirtel.az/API/json_get_city.php";
    public static String url2;
    public static String url = "http://contapp.avirtel.az/API/json_get_cat.php";
    public static String child_url = "http://contapp.avirtel.az/API/json_get_child_cat.php";
    public static String all_data = "http://contapp.avirtel.az/API/json_all_data.php";
    public static String all_contacts = "http://contapp.avirtel.az/API/json_all_contacts.php";
    public static String what_is_toll_free = "http://contapp.avirtel.az/API/what_is_toll_free.php";
    public static String about_us = "http://contapp.avirtel.az/API/about_us.php";
    public static String all_countries = "http://contapp.avirtel.az/API/json_all_countries.php";
    private static ProgressDialog pDialog;
    AQuery aq;
    boolean h =false;
    DBHelper mydb;

    // ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
      //  imageView = (ImageView)findViewById(R.id.imageView2);
      //  imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        mydb = new DBHelper(this);

        int[] ids = new int[]{R.drawable.group};
        Random randomGenerator = new Random();
        int r= randomGenerator.nextInt(ids.length);
        aq = new AQuery(this);

        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    boolean t=true;
                    // Splash screen pause time
                    while (t) {
                        sleep(2000);
                        t=false;
                        if(Connectivity.isConnectedFast(getBaseContext())){
                           // new GetContacts().execute();
                        }
                    }

                    Intent intent = new Intent(Splashscreen.this,
                            MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    Splashscreen.this.finish();


                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    Splashscreen.this.finish();
                }

            }
        };
        if(Connectivity.isConnectedFast(getBaseContext())){
            new GetContacts().execute();


        }
        splashTread.start();

    }
    public class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {


            getAllData(city_url,"city.txt");
            getAllData(url,"ParentCategory.txt");
            getAllData(child_url,"ChildCategory.txt");
            getAllData(all_data,"AllData.txt");
            getAllData(all_contacts,"AllContacts.txt");
            getAllData(what_is_toll_free,"WTF.txt");
            getAllData(about_us,"about_us.txt");
            getAllData(all_countries,"AllCountries.txt");

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
    public String getAllData(String url,String fileName){
        String line = null;
        try {
            URL Url = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) Url.openConnection();
            InputStream is = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            line = sb.toString();
            connection.disconnect();
            is.close();
            writeToFile(line,fileName,this);

            sb.delete(0, sb.length());
        } catch (Exception e) {

        }
        return line;
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