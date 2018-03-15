package androlite.androidfragmentsearchview;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import androlite.androidfragmentsearchview.Adapters.CustomAdapter;
import androlite.androidfragmentsearchview.Adapters.Sub_Object;
import androlite.androidfragmentsearchview.Menus.SettingScreen;

import static android.content.Context.MODE_PRIVATE;
import static com.google.android.gms.internal.zzir.runOnUiThread;

public class SearchFragment extends ListFragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener, OnBackPressedListener {
    private Fragment fragment = null;
    private FragmentManager fragmentManager;
    TextView emptyTextView;
    private static Bitmap myBitmap = null;
    private static Bitmap myBitmap2 = null;

    static List<String> parent_mAllValues;
    static List<Integer> parent_images;
    static List<String> index;
    int ff = 0;
    static List<String> child_mAllValues;
    List<Integer> child_images;
    static List<String> child_index;
    private static String object_index;
    public static String url2;
    public static String url = "http://contapp.avirtel.az/API/json_get_cat.php?lang=";
    public static String child_url = "http://contapp.avirtel.az/API/json_get_child_cat.php?lang=";
    public static String child_url2;
    private String TAG = MainActivity.class.getSimpleName();
   private String selectedValue ="";
    static String a =null;
    boolean t;
    private static ProgressDialog pDialog;
    private AQuery aq;
    private AQuery aq1;
    ImageView header;
    ExpandableListView expListView;
    static ArrayList<Sub_Object> allParentValues;
    private NavigationView navigationView;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        t = false;
        Log.d("------>","yene basladi");
        super.onCreate(savedInstanceState);
        aq = new AQuery(getActivity());
       // header.setVisibility(View.VISIBLE);

        // new GetContacts().execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!SettingScreen.b) {
            SettingScreen.b = true;


        }

    }
    public void expandToolbar(){
        AppBarLayout appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.app_bar);
        appBarLayout.setExpanded(true,true);
    }
    public void collapseToolbar(){
        AppBarLayout appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.app_bar);
        appBarLayout.setExpanded(false,false);
    }
    @Override
    public void onStart() {
        super.onStart();

        header = (ImageView) getActivity().findViewById(R.id.header);
        header.setImageResource(R.drawable.contapp);
        SharedPreferences mSh =   getActivity().getSharedPreferences("Value",MODE_PRIVATE);
        selectedValue = mSh.getString("val","az");
        Log.d("Dil",selectedValue);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) getActivity().findViewById(R.id.collapse_commonview_header);
        expandToolbar();
        RelativeLayout rf = (RelativeLayout) getActivity().findViewById(R.id.hs);
        rf.setVisibility(View.VISIBLE);
        collapsingToolbarLayout.setTitleEnabled(true);
        collapsingToolbarLayout.setTitle("Contapp");

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "clear.otf");
        collapsingToolbarLayout.setCollapsedTitleTypeface(tf);
        MainActivity.updateOptionChecked();

        //new GetContacts().execute();

        new GetContacts().execute();
    }
    private void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    private void setListViewHeight(ExpandableListView listView,
                                   int group) {
        ExpandableListAdapter listAdapter = listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();
                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }
    private void writeToFile(String data,String fileName,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    @Override
    public void onPause(){
        if(pDialog != null)
            pDialog.dismiss();
        super.onPause();
    }


    public class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            ff++;

            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
          //  if(ff<2)
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            new Thread(new Runnable() {
                public void run() {
                    parent_mAllValues = new ArrayList<>();
                    parent_images = new ArrayList<>();
                    index = new ArrayList<>();
                    allParentValues = new ArrayList<Sub_Object>();
                    SharedPreferences mSh = getActivity().getSharedPreferences("Value", MODE_PRIVATE);
                    selectedValue = mSh.getString("val", "az");
                    aq = new AQuery(getActivity());

                    child_mAllValues = new ArrayList<>();
                    child_index = new ArrayList<>();
                    //    HttpHandler sh = new HttpHandler();
                    String parentReadData = readFromFile(getActivity(), "ParentCategory.txt");
                    String childReadData = readFromFile(getActivity(), "ChildCategory.txt");

                    if (parentReadData != null) {
                        try {
                            JSONObject jsonObj = new JSONObject(parentReadData);
                            JSONObject jsonObj_child = new JSONObject(childReadData);

                            // Getting JSON Array node
                            JSONArray contacts = jsonObj.getJSONArray("server_response");
                            JSONArray contacts_child = jsonObj_child.getJSONArray("server_response");


                            // looping through All Contacts
                            for (int i = 0; i < contacts.length(); i++) {
                                JSONObject c = contacts.getJSONObject(i);

                                //parent_mAllValues.add(c.getString("name"));
                                //  parent_images.add(R.drawable.ic_menu_camera);
                                //  index.add(c.getString("id"));

                                String p_mAllValue = c.getString(("name_") + selectedValue);
                                String p_id = c.getString("id");
                                String icon_path = c.getString("icon_path");
                                Log.d("--->", icon_path);


                                Sub_Object t2 = new Sub_Object(p_mAllValue, icon_path, p_id);
                                for (int k = 0; k < contacts_child.length(); k++) {

                                    JSONObject child = contacts_child.getJSONObject(k);
                                    String icon_path1 = child.getString("icon_path");


                                    if (child.getString("pid").equals(p_id)) {
                                        t2.players.add(child.getString(("name_") + selectedValue) + "&-&-" + child.getString("id"));

                                        t2.img.add(icon_path1);
                                    }

                                }
                                allParentValues.add(t2);


                            }
                        } catch (final JSONException e) {
                            Log.e(TAG, "Json parsing error: " + e.getMessage());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(),
                                            "Json parsing error: " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                            .show();
                                }
                            });

                        }
                    } else {
                        Log.e(TAG, "Couldn't get json from server.");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(),
                                        "Couldn't get json from server. Check LogCat for possible errors!",
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
                    }
                }
                }).start();


            return null;
        }

            @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


                if (pDialog != null) {
                    pDialog.dismiss();
                }
                CustomAdapter adapter = new CustomAdapter(getActivity(), allParentValues);
                expListView.setAdapter(adapter);
                setListViewHeight(expListView);






                //  customList = new CustomList(getActivity(), parent_mAllValues, parent_images,index);
            //setListAdapter(customList);
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
    private void setFragment(){
        fragment = new SubCompanies();
        fragmentManager = getFragmentManager();
        String fragmentTags = "Sub Companies";
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(fragmentTags);
        fragmentTransaction.replace(R.id.content_frame, fragment, fragmentTags);
        fragmentTransaction.commit();

    }
    private void setIndex(String object_index)
    {
        this.object_index = object_index;
    }

    public static String getObject_index()
    {
        return object_index;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.search_fragment, container, false);
        expListView = (ExpandableListView) layout.findViewById(android.R.id.list);

        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {

                setListViewHeight(expandableListView,i);
                if(allParentValues.get(i).players.size()==0) {

                 //   Toast.makeText(getActivity(), allParentValues.get(i).PID, Toast.LENGTH_SHORT).show();
                    setFragment();
                    nn = allParentValues.get(i).PID;
                    setIndex(allParentValues.get(i).PID);

                }
                else{
                    expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                        @Override
                        public boolean onChildClick(ExpandableListView parent, View v, int groupPos,
                                                    int childPos, long id) {

                            String name_decode = allParentValues.get(groupPos).players.get(childPos).toString();
                            String[] arr = name_decode.split("\\&-&-");
                            nn=arr[0];
                       //     Toast.makeText(getActivity(),arr[1], Toast.LENGTH_SHORT).show();
                            setFragment();
                            setIndex(arr[1]);

                            return false;
                        }
                    });
                }

                return false;
            }
        });
        return layout;

    }
    public static String nn = "";
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.filter, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search");
        super.onCreateOptionsMenu(menu, inflater);

    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem searchViewMenuItem = menu.findItem(R.id.action_search);
        SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(searchViewMenuItem);
        int searchImgId = getResources().getIdentifier("android:id/search_button", null, null);
        ImageView v = (ImageView) mSearchView.findViewById(searchImgId);
        v.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        ArrayList<Sub_Object> allParentValues_copy = new ArrayList<>();
        if (newText == null || newText.trim().isEmpty()) {
            resetSearch();
            return false;
        }
        for(int i=0;i<allParentValues.size();i++){

                Sub_Object t2 = new Sub_Object(allParentValues.get(i).Name, allParentValues.get(i).Image, allParentValues.get(i).PID);
                for (int k = 0; k < allParentValues.get(i).players.size(); k++) {
                    String as = allParentValues.get(i).players.get(k);
                    if (as.toLowerCase().contains(newText.toLowerCase())) {
                        t2.players.add(allParentValues.get(i).players.get(k));
                        t2.img.add(allParentValues.get(i).img.get(k));

                    }

            }
            if(allParentValues.get(i).Name.toLowerCase().contains(newText.toLowerCase()) && t2.players.size()==0) {
                allParentValues_copy.add(allParentValues.get(i));
            }
            if (t2.players.size() != 0)
                allParentValues_copy.add(t2);
        }
        CustomAdapter adapter1=new CustomAdapter(getActivity(), allParentValues_copy);
        expListView.setAdapter(adapter1);
        setListViewHeight(expListView);

        for(int i=0;i<allParentValues_copy.size();i++){
            expListView.expandGroup(i);
        }
        return false;
    }

    public void resetSearch() {
     //   customList = new CustomList(getActivity(), parent_mAllValues, parent_images,index);
        CustomAdapter adapter1=new CustomAdapter(getActivity(), allParentValues);
        expListView.setAdapter(adapter1);
        setListViewHeight(expListView);

        //  setListAdapter(customList);
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return true;
    }


    @Override
    public boolean onBackPressed() {

        return false;
    }

}
