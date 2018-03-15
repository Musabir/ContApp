package androlite.androidfragmentsearchview.Menus;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androlite.androidfragmentsearchview.Adapters.CustomAdapter_Objects;
import androlite.androidfragmentsearchview.Company_Details;
import androlite.androidfragmentsearchview.CustomList;
import androlite.androidfragmentsearchview.R;
import androlite.androidfragmentsearchview.SubCompanies;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Musabir on 6/19/2017.
 */

public class Wishlist extends ListFragment {
    private CustomList customList;
    List<String> names;
    List<Bitmap> images;
    List<String> address;
    List<String> tollfree;
    List<String> object_index;
    List<String> object_index_par;
    List<String> icon_path;
    String object_det = "";
    private Fragment fragment = null;
    private FragmentManager fragmentManager;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.wishlist, container, false);
        ListView listView = (ListView) view.findViewById(android.R.id.list);
        RelativeLayout rf = (RelativeLayout) getActivity().findViewById(R.id.hs);
        rf.setVisibility(View.VISIBLE);
        object_index = new ArrayList<>();
        object_index_par = new ArrayList<>();
        names = new ArrayList<>();
        images = new ArrayList<>();
        address = new ArrayList<>();
        tollfree = new ArrayList<>();
        object_index = new ArrayList<>();
        icon_path = new ArrayList<>();
        if(Company_Details.obj_id.size()>0) {
            SubCompanies.object_index1 = Company_Details.obj_id.get(Company_Details.obj_id.size() - 1);
            SubCompanies.object_index = Company_Details.obj_pid.get(Company_Details.obj_pid.size() - 1);
        }
        getActivity().setTitle("Wishlist");

        SharedPreferences mSh = getActivity().getSharedPreferences("Wishlist",MODE_PRIVATE);
        Map<String, ?> allEntries = mSh.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            object_det = entry.getValue().toString();
            Log.d("-->object_det",object_det);
            String[] list = object_det.split("\\&-&-");
            if(!list[0].equals("-"))
            tollfree.add(list[0]);
            else tollfree.add("null");
            address.add(list[1]);
            names.add(list[2]);
            Log.d("::+?",list[4]+" "+list[3]);
            object_index.add(list[4]);
            object_index_par.add(list[3]);
            icon_path.add(list[5]);
            int rem = 0;

            for(int i = 0; i< CustomAdapter_Objects.bb.size(); i++){

                if(CustomAdapter_Objects.bb.get(i) == Integer.parseInt(list[3]))
                {

                    images.add(CustomAdapter_Objects.bm.get(i));
                    break;
                }
            }


        }

        customList = new CustomList(getActivity(), names, address, images,tollfree,object_index_par,icon_path);
        setListAdapter(customList);
        return view;

    }

    @Override
    public void onListItemClick(ListView listView, View v, int position, long id) {
        fragment = new Company_Details();
        fragmentManager = getFragmentManager();
        String str = listView.getItemAtPosition(position).toString();
        SubCompanies.object_index = object_index_par.get(position);
        SubCompanies.object_index1 = object_index.get(position);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack("Company_Details");
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();

    }
    public void expandToolbar(){
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) getActivity().findViewById(R.id.collapse_commonview_header);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "clear.otf");
        collapsingToolbarLayout.setCollapsedTitleTypeface(tf);
        collapsingToolbarLayout.setTitleEnabled(true);
        AppBarLayout appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.app_bar);
        appBarLayout.setExpanded(true);
        SharedPreferences mSh =   getActivity().getSharedPreferences("Value",MODE_PRIVATE);
        String selectedValue = mSh.getString("val","az");
        if(selectedValue.equals("az"))
            collapsingToolbarLayout.setTitle("Seçilmişlər");
        else if(selectedValue.equals("en"))
            collapsingToolbarLayout.setTitle("Favourite List");
        else if(selectedValue.equals("ru"))
            collapsingToolbarLayout.setTitle("Избранное");
    }
    @Override
    public void onStart() {
        super.onStart();


        expandToolbar();

        ImageView header = (ImageView) getActivity().findViewById(R.id.header);
        header.setImageResource(R.drawable.wishlist);
    }
}