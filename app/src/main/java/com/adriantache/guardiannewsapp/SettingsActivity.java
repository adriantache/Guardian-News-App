package com.adriantache.guardiannewsapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat implements android.support.v7.preference.Preference.OnPreferenceChangeListener {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.preferences);
            Preference number_of_posts = findPreference(getString(R.string.posts_to_fetch_key));
            bindPreferenceSummaryToValue(number_of_posts);
            Preference show_all = findPreference(getString(R.string.show_all_key));
            bindPreferenceSummaryToValue(show_all);
            Preference section = findPreference(getString(R.string.section_key));
            bindPreferenceSummaryToValue(section);
        }

        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(),preference.getKey().replace("key","default"));
            onPreferenceChange(preference, preferenceString);
        }


        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String stringValue = newValue.toString();
            preference.setSummary(stringValue);
            return true;
        }
    }
}
