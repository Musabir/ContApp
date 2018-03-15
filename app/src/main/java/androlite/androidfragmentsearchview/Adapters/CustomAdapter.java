package androlite.androidfragmentsearchview.Adapters;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.androidquery.AQuery;

import java.util.ArrayList;

import androlite.androidfragmentsearchview.R;

public class CustomAdapter extends BaseExpandableListAdapter {
    private final Typeface tf;
    private Context c;
    private ArrayList<Sub_Object> team;
    private LayoutInflater inflater;
    private final static int MY_PERMISSION_C = 101;

    private AQuery aq ;
    TableRow row ;
    public CustomAdapter(Context c,ArrayList<Sub_Object> team)
    {
        this.c=c;
        this.team=team;
        aq = new AQuery(c);
        tf = Typeface.createFromAsset(c.getAssets(), "clear.otf");



    }
    //GET A SINGLE PLAYER
    @Override
    public Object getChild(int groupPos, int childPos) {
        // TODO Auto-generated method stub
        return team.get(groupPos).players.get(childPos);
    }
    public Object getChildImage(int groupPos, int childPos) {
        // TODO Auto-generated method stub
        return team.get(groupPos).img.get(childPos);
    }
    //GET PLAYER ID
    @Override
    public long getChildId(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return 0;
    }
    //GET PLAYER ROW
    @Override
    public View getChildView(final int groupPos, final int childPos, boolean isLastChild, View convertView,
                             ViewGroup parent) {
        //ONLY INFLATER XML ROW LAYOUT IF ITS NOT PRESENT,OTHERWISE REUSE IT
        inflater=(LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView==null)
        {
            convertView=inflater.inflate(R.layout.sub_objects, null);
        }
        final View finalConvertView = convertView;

        //GET CHILD/PLAYER NAME
        String  child=(String) getChild(groupPos, childPos);
        String logo =(String) getChildImage(groupPos, childPos);
        //SET CHILD NAME
        TextView nameTv=(TextView) finalConvertView.findViewById(R.id.textview1);
        nameTv.setTypeface(tf);
        ImageView im = (ImageView) finalConvertView.findViewById(R.id.img1);
       // ImageView img=(ImageView) convertView.findViewById(R.id.imageView1);
        String[] arr = child.split("\\&-&-");
                final Bitmap[] bitmap = {null};
        nameTv.setText(arr[0]);
                new Thread(new Runnable() {
                    public void run() {
        bitmap[0] = aq.getCachedImage(team.get(groupPos).img.get(childPos));
                    }
                }).start();
        if(bitmap[0] !=null) {
            im.setImageBitmap(bitmap[0]);

        }
        else
        aq.id(im).image(team.get(groupPos).img.get(childPos), true, true, 0,R.drawable.building);

      //  im.setImageBitmap(logo);
        //GET TEAM NAME
        String teamName= getGroup(groupPos).toString();

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
    //GET TEAM ROW
    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
        //ONLY INFLATE XML TEAM ROW MODEL IF ITS NOT PRESENT,OTHERWISE REUSE IT

        inflater=(LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null)
        {
            convertView=inflater.inflate(R.layout.list_single, null);
        }
        row = (TableRow) convertView.findViewById(R.id.tb);
        final View finalConvertView = convertView;
        ImageView img=(ImageView) finalConvertView.findViewById(R.id.img);

        //GET GROUP/TEAM ITEM
        Sub_Object t=(Sub_Object) getGroup(groupPosition);
        //SET GROUP NAME
        TextView nameTv=(TextView) finalConvertView.findViewById(R.id.txt);
        nameTv.setTypeface(tf);
        final Bitmap[] bitmap = {null};
        // img.setBackgroundResource(R.drawable.address);
        String name=t.Name;
        new Thread(new Runnable() {
            public void run() {
                bitmap[0] = aq.getCachedImage(team.get(groupPosition).Image);
            }
        }).start();
        if(bitmap[0] !=null) {
            Log.d("--->"," not Null");
            img.setImageBitmap(bitmap[0]);

        }
        else {
            aq.id(img).image(team.get(groupPosition).Image, true, true, 0, R.drawable.building);
            aq.id(img).image(team.get(groupPosition).Image, true, true, 0, R.drawable.building);
            aq.id(img).image(team.get(groupPosition).Image, true, true, 0, R.drawable.building);
            aq.id(img).image(team.get(groupPosition).Image, true, true, 0, R.drawable.building);
            Log.d("--->","Null");
        }

       // else img.setImageResource(R.drawable.ic_menu_camera);
        //img.setImageResource();

        String[] arr = name.split("\\&-&-");
        nameTv.setText(arr[0]);
        View ind = finalConvertView.findViewById(R.id.group_indicator);

        if (ind != null)
        {
            ImageView indicator = (ImageView) ind;
            if (getChildrenCount(groupPosition) == 0)
            {
                indicator.setVisibility(View.INVISIBLE);

            }
            else
            {
                if(isExpanded){
                    row.setBackgroundResource(R.drawable.list_background);
                }
                else row.setBackgroundResource(R.drawable.none_background);

                indicator.setVisibility(View.VISIBLE);
                int stateSetIndex = (isExpanded ? 1 : 0);

                if(stateSetIndex == 1){
                    indicator.setImageResource(R.drawable.ic_expand_00012);


                }
                else if(stateSetIndex == 0){
                    indicator.setImageResource(R.drawable.ic_collapse_00012);

                }
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
}