package androlite.androidfragmentsearchview.Menus;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androlite.androidfragmentsearchview.MainActivity;
import androlite.androidfragmentsearchview.R;
import androlite.androidfragmentsearchview.SearchFragment;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Musabir on 4/11/2017.
 */

public class SettingScreen extends PreferenceFragment {
    private static ListPreference mListPreference;
    public static String lang;
   public static boolean b=true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.screen_setting);
        mListPreference = (ListPreference) getPreferenceManager().findPreference("languages");

        mListPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue.equals("1")) {
                    SearchFragment.url2 = SearchFragment.url + "az";
                    lang = "az";
                    mListPreference.setTitle("Dillər");
                    storeValue("az");
                }
                if (newValue.equals("2")) {
                    SearchFragment.url2 = SearchFragment.url + "en";
                    lang = "en";
                    mListPreference.setTitle("Language");
                    storeValue("en");
                }
                if (newValue.equals("3")) {
                    SearchFragment.url2 = SearchFragment.url + "ru";
                    lang = "ru";
                    mListPreference.setTitle("Язык");
                    storeValue("ru");
                }
                b = false;
                return true;
            }
        });
    }
        private void storeValue(String val){
            SharedPreferences mSh = getActivity().getSharedPreferences("Value",MODE_PRIVATE);
            SharedPreferences.Editor mEd = mSh.edit();
            mEd.putString("val",val);
            mEd.apply();

            mSh =   getActivity().getSharedPreferences("Value",MODE_PRIVATE);
            String selectedValue = mSh.getString("val","az");
            if(selectedValue.equals("az"))
                getActivity().setTitle("Dillər");
            else if(selectedValue.equals("en"))
                getActivity().setTitle("Languages");
            else if(selectedValue.equals("ru"))
                getActivity().setTitle("Языки");

    }


}
