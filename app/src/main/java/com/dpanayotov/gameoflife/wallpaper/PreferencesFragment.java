package com.dpanayotov.gameoflife.wallpaper;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import com.dpanayotov.gameoflife.R;
import com.dpanayotov.gameoflife.util.CellRadiusSeekBarPreference;

/**
 * Created by Dean Panayotov Local on 2.9.2015
 */
public class PreferencesFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);

        // add a validator to the "numberofCircles" preference so that it only
        // accepts numbers
        Preference circlePreference = getPreferenceScreen().findPreference("numberOfCircles");

        // add the validator
        circlePreference.setOnPreferenceChangeListener(numberCheckListener);

        Preference cellRadiusPreference = getPreferenceScreen().findPreference("circle_radius");

        cellRadiusPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {


            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(preference instanceof CellRadiusSeekBarPreference){
                    CellRadiusSeekBarPreference cellRadiusSeekBarPreference = (CellRadiusSeekBarPreference) preference;
                    cellRadiusSeekBarPreference.setSummary(newValue.toString()+" pixels");
                }
                return true;
            }
        });
    }

    /**
     * Checks that a preference is a valid numerical value
     */

    Preference.OnPreferenceChangeListener numberCheckListener = new Preference.OnPreferenceChangeListener() {

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            // check that the string is an integer
            if (newValue != null && newValue.toString().length() > 0
                    && newValue.toString().matches("\\d*")) {
                return true;
            }
            // If now create a message to the user
            if(getActivity() != null){
                Toast.makeText(getActivity(), "Invalid Input", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    };
}
