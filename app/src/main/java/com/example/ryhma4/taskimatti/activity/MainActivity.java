package com.example.ryhma4.taskimatti.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.ryhma4.taskimatti.R;
import com.example.ryhma4.taskimatti.fragment.DayFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static Resources globalRes;
    public MainActivity() {
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalRes = getResources();
        setContentView(R.layout.activity_main);

        ViewPager viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        // Get current day of week and set the tab layout to that day
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        TabLayout.Tab t = tabLayout.getTabAt(getDayIndex(calendar.get(Calendar.DAY_OF_WEEK)));
        System.out.println(calendar.get(Calendar.DAY_OF_WEEK));
        if (t != null) {
            t.select();
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createRoutineActivity();
            }
        });
    }

    public void createRoutineActivity() {
        Intent intent = new Intent(this, CreateRoutineActivity.class);
        startActivity(intent);
    }

    /**
     * Sets up the week day fragments
     * @param viewPager The element where the tabs are set
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        String[] weekdays = {
                getResources().getString(R.string.day_monday), getResources().getString(R.string.day_tuesday),
                getResources().getString(R.string.day_wednesday), getResources().getString(R.string.day_thursday),
                getResources().getString(R.string.day_friday), getResources().getString(R.string.day_saturday),
                getResources().getString(R.string.day_sunday)};

        //Get the dates for the current week.
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        SimpleDateFormat weekDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        for(int i = 0; i < weekdays.length; i++) {
            DayFragment fragment = new DayFragment();
            Bundle args = new Bundle();
            args.putString("weekDate",weekDateFormat.format(calendar.getTime())); //Pass date of the weekday.
            fragment.setArguments(args);
            adapter.addFrag(fragment, weekdays[i]);
            calendar.add(Calendar.DAY_OF_WEEK, 1);
        }
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.action_setTasks:
                startActivity(new Intent(this, SetTaskActivity.class));
                return true;
            case R.id.action_routines:
                startActivity(new Intent(this, ShowRoutinesActivity.class));
                return true;
            case R.id.action_signOut:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                setContentView(R.layout.login_main);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**
     * Used to get the current day of week correctly
     * @param weekday The integer value of current day of week
     * @return Returns the index of the current week day
     */
    public int getDayIndex(int weekday) {
        int index;
        if (weekday == 1) {
            index = 6;
        } else {
            index = weekday - 2;
        }
        return index;
    }
}
