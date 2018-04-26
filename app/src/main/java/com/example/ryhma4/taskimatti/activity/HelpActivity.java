package com.example.ryhma4.taskimatti.activity;

import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.ryhma4.taskimatti.R;

public class HelpActivity extends MainActivity{


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_date);

        helpSettings();
    }

    public void helpSettings(){
        FrameLayout fl = findViewById(R.id.contaner);
        fl.removeAllViews();
        SettingsActivity.HelpPreferenceFragment helpFragment = new SettingsActivity.HelpPreferenceFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.contaner, helpFragment);
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            super.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        super.finish();
    }

}
