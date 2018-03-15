package androlite.androidfragmentsearchview;

/**
 * Created by Musabir on 3/30/2017.
 */

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.androidquery.AQuery;

import java.util.List;

public class CustomList extends ArrayAdapter<String> {

    private final Activity context;
    private final List<String> web;
    private final List<String> address;
    private final List<String> tollfree;
    private final List<Bitmap> image;
    private final List<String> ID;
    private final List<String> icon_path;
    private LruCache<String, Bitmap> mMemoryCache;
    AQuery aq;
    View rowView;
    ImageView call;
    String phnNmbr[] = null;

    public CustomList(Activity context,
                      List<String> web, List<String> address, List<Bitmap> image, List<String> tollfree,List<String> ID, List<String> icon_path) {
        super(context, R.layout.list_single, web);
        this.context = context;
        this.web = web;
        this.image = image;
        this.address = address;
        this.tollfree = tollfree;
        this.ID = ID;
        this.icon_path = icon_path;
        aq = new AQuery(context);


    }


    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        rowView = inflater.inflate(R.layout.object_sub_object, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.object_name);
        TextView addresss = (TextView) rowView.findViewById(R.id.txt23);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
        Bitmap bitmap = aq.getCachedImage(icon_path.get(position));
        if(bitmap!=null)
            imageView.setImageBitmap(bitmap);
        else imageView.setImageDrawable(ActivityCompat.getDrawable(getContext(),
               R.drawable.path));
        call = (ImageView) rowView.findViewById(R.id.tollfreebutton1);
        txtTitle.setText(web.get(position));
        addresss.setText(address.get(position));

        if (!tollfree.get(position).equals("null")) {
            call.setVisibility(View.VISIBLE);
            phnNmbr = tollfree.get(position).split("[\r\n]+");
            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    freeCall(tollfree.get(position));
                }
            });
        }
        else
            call.setVisibility(View.INVISIBLE);

        return rowView;
    }

    public void freeCall(String phonenumber) {
        if (phnNmbr.length > 1) {
            PopupMenu popup = new PopupMenu(context, call);
            //Inflating the Popup using xml file

            for (int i = 0; i < phnNmbr.length; i++) {
                popup.getMenu().add(phnNmbr[i]);
            }


            popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());


            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    //---your menu item action goes here ....
                    Intent myIntent = new Intent(Intent.ACTION_CALL);

                    myIntent.setData(Uri.parse("tel:" + item.toString()));
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        context.startActivity(myIntent);
                        return false;
                    }

                    return true;
                }
            });
            popup.show();
        }
        else if (phnNmbr.length==1){
            Intent myIntent = new Intent(Intent.ACTION_CALL);
            myIntent.setData(Uri.parse("tel:" + phonenumber.toString()));
            context.startActivity(myIntent);

        }
    }
    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }
}