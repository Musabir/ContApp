package androlite.androidfragmentsearchview.Menus;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import androlite.androidfragmentsearchview.R;

/**
 * Created by Musabir on 7/21/2017.
 */

public class FindLocation extends PreferenceFragment {
    final int PLACE_PICKER = 1;
    private String sLng,sLat;
    private String tm;
    private TextView distance,duration;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.find_location, container, false);
        Button button = (Button) view.findViewById(R.id.button);
        distance = (TextView) view.findViewById(R.id.distance);
        duration = (TextView) view.findViewById(R.id.duration);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // requestPermission();
                //  lparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
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
        return view;

    }
    public class GetDistanceAsyncTask extends AsyncTask<Void,Void,String>{
        double lng1,lng2,lat1,lat2;
        public GetDistanceAsyncTask(double lng1,double lng2,double lat1,double lat2){
            this.lat1 = lat1;
            this.lat2 = lat2;
            this.lng1 = lng1;
            this.lng2 = lng2;
            this.execute();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String distance ="";
            String time = "";
           String url =  "https://maps.googleapis.com/maps/api/directions/xml?origin=" +lat1+","+lng1+ "&destination=" + lat2 + "," +lng2 + "&sensor=false&units=metric&mode=walkingalternatives=true";
            String tag[] = {"text"};
            URL url1  =null;
            try {
                url1 = new URL(url);
                HttpURLConnection connection = null;
                connection = (HttpURLConnection) url1.openConnection();
                connection.setReadTimeout(30000);
                connection.setConnectTimeout(30000);
                connection.setRequestMethod("GET");
                connection.setDoOutput(true);
                connection.connect();

                InputStream is = connection.getInputStream();
                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document doc = builder.parse(is);
                if(doc!=null)
                {
                    NodeList nl;
                    ArrayList<String> arg = new ArrayList();
                    for(String s :tag){
                        nl = doc.getElementsByTagName(s);
                        if(nl.getLength()>0) {

                            Node node = nl.item(nl.getLength() - 1);
                            Node node1 = nl.item(nl.getLength() - 2);
                            arg.add(node.getTextContent());
                            arg.add(node1.getTextContent());
                        }
                        else {
                            arg.add(" - ");
                            arg.add(" - ");
                        }

                        }
                        distance = String.format("%s",arg.get(0));
                        tm = String.format("%s",arg.get(1));
                    }
                    else System.out.print("Doc is null");


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
            return distance;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s!=null) {
                distance.setText(s);
                duration.setText(tm);
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==PLACE_PICKER){
            if(resultCode==getActivity().RESULT_OK){

                Place place = PlacePicker.getPlace(data,getActivity());

                String lnglat = place.getLatLng().toString();
                String[] latLng = lnglat.substring(10, lnglat.length() - 1).split(",");
                sLat = latLng[0];
                sLng = latLng[1];
                if(place!=null) {

                    new GetDistanceAsyncTask(50.07362366,Double.parseDouble(sLng),40.37270514,Double.parseDouble(sLat));


                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}
