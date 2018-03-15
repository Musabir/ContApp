package androlite.androidfragmentsearchview.Adapters;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;

import java.util.ArrayList;

import androlite.androidfragmentsearchview.R;
import androlite.androidfragmentsearchview.SubCompanies;

/**
 * Created by Musabir on 6/16/2017.
 */

public class CustomAdapter_Objects extends BaseExpandableListAdapter {
    private final Typeface tf;
    private Context c;
    private ArrayList<Objects> team;
    private LayoutInflater inflater;
    AQuery aq ;
    RelativeLayout row;


    private LruCache<String, Bitmap> mMemoryCache;

    public CustomAdapter_Objects(Context c, ArrayList<Objects> team) {

        this.c = c;
        this.team = team;
        tf = Typeface.createFromAsset(c.getAssets(), "clear.otf");

        aq = new AQuery(c);

    }

    //GET A SINGLE PLAYER
    @Override
    public Object getChild(int groupPos, int childPos) {
        // TODO Auto-generated method stub
        return team.get(groupPos).players.get(childPos);
    }

    public Object getChildCity(int groupPos, int childPos) {
        // TODO Auto-generated method stub
        return team.get(groupPos).cities.get(childPos);
    }
    public Object getChildTollFree(int groupPos, int childPos) {
        // TODO Auto-generated method stub
        return team.get(groupPos).TollFree.get(childPos);
    }

    //GET PLAYER ID
    @Override
    public long getChildId(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return 0;
    }

    //GET PLAYER ROW
    @Override
    public View getChildView(int groupPos, int childPos, boolean isLastChild, View convertView,
                             ViewGroup parent) {
        //ONLY INFLATER XML ROW LAYOUT IF ITS NOT PRESENT,OTHERWISE REUSE IT
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.object_sub_object, null);
        }

                //GET CHILD/PLAYER NAME
        String child_name = (String) getChild(groupPos, childPos);
        String child_city = (String) getChildCity(groupPos, childPos);

        final String child_tollfree = (String) getChildTollFree(groupPos, childPos);

        //SET CHILD NAME
        TextView nameTv = (TextView) convertView.findViewById(R.id.object_name);
        TextView address = (TextView) convertView.findViewById(R.id.txt23);
        nameTv.setTypeface(tf);
        address.setTypeface(tf);
        ImageView imageButton = (ImageView) convertView.findViewById(R.id.tollfreebutton1);
        if(child_tollfree.equals("null"))
            imageButton.setVisibility(View.INVISIBLE);
        else imageButton.setVisibility(View.VISIBLE);
            String[] arr = child_name.split("\\&-&-");
        if(!child_city.equals("")&&!child_city.equals("null"))
        address.setText(child_city);
        else
            address.setVisibility(View.INVISIBLE);
        nameTv.setText(arr[0]);
        //GET TEAM NAME
        if(!child_tollfree.equals("null")) {
            if (imageButton.getVisibility() == View.VISIBLE) {
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SubCompanies.requestPermission();
                        phoneNumberCall(child_tollfree);
                    }
                });
            }
        }

        //ASSIGN IMAGES TO PLAYERS ACCORDING TO THEIR NAMES AN TEAMS

        return convertView;
    }

    //GET NUMBER OF PLAYERS
    @Override
    public int getChildrenCount(int groupPosw) {
        // TODO Auto-generated method stub
        return team.get(groupPosw).players.size();
    }

    //GET TEAM
    @Override
    public Object getGroup(int groupPos) {
        // TODO Auto-generated method stub
        return team.get(groupPos);
    }

    //GET NUMBER OF TEAMS
    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return team.size();
    }

    //GET TEAM ID
    @Override
    public long getGroupId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }
    public static ArrayList<Bitmap> bm = new ArrayList<>();
    public static ArrayList<Integer> bb = new ArrayList<>();

    //GET TEAM ROW
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {


        //ONLY INFLATE XML TEAM ROW MODEL IF ITS NOT PRESENT,OTHERWISE REUSE IT
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.object_object, null);
        }
        //GET GROUP/TEAM ITEM
        final Objects t = (Objects) getGroup(groupPosition);
        TextView nameTv = (TextView) convertView.findViewById(R.id.txt);
       // TextView invibleObjectName = (TextView) convertView.findViewById(R.id.invisible_object_name);
        TextView address = (TextView) convertView.findViewById(R.id.addresss);
        TextView invibleObjectName = (TextView) convertView.findViewById(R.id.invisible_textview);
        row = (RelativeLayout) convertView.findViewById(R.id.rl);


        ImageView imageButton = (ImageView) convertView.findViewById(R.id.tollfreebutton);
        ImageView indicator = (ImageView) convertView.findViewById(R.id.indicator);
        address.setTypeface(tf);
        invibleObjectName.setTypeface(tf);
        ImageView logo = (ImageView) convertView.findViewById(R.id.img34);
        Bitmap bitmap = aq.getCachedImage(t.Image);
        if(bitmap!=null) {
            logo.setImageBitmap(bitmap);

        }
        else  aq.id(logo).image(t.Image, true, true, 0 , R.drawable.name);
//        addBitmapToMemoryCache(t.PID,aq.id(logo).getCachedImage(t.Image));
        CustomAdapter_Objects.bm.add( aq.id(logo).getCachedImage(t.Image));
        CustomAdapter_Objects.bb.add(Integer.parseInt(t.PID));


        String add = t.City;
        String name = t.Name;

        //String path = t.Image;
        // if(t.Image!=null)
       //logo.setImageBitmap(t.Image);
        // logo.setImageResource(R.drawable.ic_menu_camera);
        //img.setImageResource();


        if (getChildrenCount(groupPosition) > 0 ) {
            if(isExpanded){
                row.setBackgroundResource(R.drawable.list_background);
            }
            else row.setBackgroundResource(R.drawable.none_background);
            imageButton.setVisibility(View.INVISIBLE);
            indicator.setVisibility(View.VISIBLE);
            indicator.setImageResource(R.drawable.down);
            address.setVisibility(View.INVISIBLE);
            nameTv.setVisibility(View.INVISIBLE);
            invibleObjectName.setVisibility(View.VISIBLE);
            invibleObjectName.setText(name);

            if (indicator != null)
            {


                indicator.setVisibility(View.VISIBLE);
                    int stateSetIndex = (isExpanded ? 1 : 0);

                    if(stateSetIndex == 1){
                        indicator.setImageResource(R.drawable.ic_expand_00012);


                    }
                    else if(stateSetIndex == 0){
                        indicator.setImageResource(R.drawable.ic_collapse_00012);

                }
            }
        } else {  invibleObjectName.setVisibility(View.INVISIBLE);
            nameTv.setVisibility(View.VISIBLE);
            indicator.setVisibility(View.INVISIBLE);
            address.setVisibility(View.VISIBLE);
            if(!t.tollFree.equals("null"))
            imageButton.setVisibility(View.VISIBLE);
            else imageButton.setVisibility(View.INVISIBLE);
            nameTv.setText(name);
            Log.d("__>",add);
            if(!add.equals("")&&!add.equals("null"))
                address.setText(add);
            else
                address.setVisibility(View.INVISIBLE);
            address.setText(add);
            if (!t.tollFree.equals("null") ){
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        phoneNumberCall(t.tollFree);
                    }
                });

            }
        }



        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isChildSelectable(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return true;
    }

    public void phoneNumberCall(String PhoneNumber) {
        Intent myIntent = new Intent(Intent.ACTION_CALL);
        myIntent.setData(Uri.parse("tel:" + PhoneNumber.toString()));
        if (ActivityCompat.checkSelfPermission(c, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        c.startActivity(myIntent);
    }
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }
}
