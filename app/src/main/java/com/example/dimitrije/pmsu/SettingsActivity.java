package com.example.dimitrije.pmsu;

import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.os.Bundle;

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        CheckBoxPreference cbDate = (CheckBoxPreference) findPreference(getString(R.string.sortPostByDate));
        CheckBoxPreference cbPopularity = (CheckBoxPreference) findPreference(getString(R.string.sortPostByPopularity));
        if (cbDate.isChecked() && cbDate.isEnabled()){
            cbPopularity.setEnabled(false);
        }
        else if (cbPopularity.isChecked() && cbPopularity.isEnabled()){
            cbDate.setEnabled(false);
        }

        CheckBoxPreference cbComDate = (CheckBoxPreference) findPreference(getString(R.string.sortCommentByDate));
        CheckBoxPreference cbComPop = (CheckBoxPreference) findPreference(getString(R.string.sortCommentByPopularity));
        if (cbComDate.isChecked() && cbComDate.isEnabled()){
            cbComPop.setEnabled(false);
        }
        else if (cbComPop.isChecked() && cbComPop.isEnabled()){
            cbComDate.setEnabled(false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
