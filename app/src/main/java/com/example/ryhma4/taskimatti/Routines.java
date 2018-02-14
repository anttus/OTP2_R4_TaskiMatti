package com.example.ryhma4.taskimatti;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Routines extends MainActivity {
    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routines);

        listView = (ExpandableListView) findViewById(R.id.lvExp);
        initData();
        listAdapter = new ExapandableListAdapter(this,listDataHeader,listHashMap);
        listView.setAdapter(listAdapter);
    }

    private void initData() {
        listDataHeader = new ArrayList<>();
        listHashMap = new HashMap<>();

        listDataHeader.add("KAIKKI RUTIINIT");
        listDataHeader.add("Siivous");
        listDataHeader.add("Liikunta");
        listDataHeader.add("Rentoutuminen");

        List<String> kaikkiRutiinit = new ArrayList<>();
        kaikkiRutiinit.add("Imuroi");
        kaikkiRutiinit.add("Pyyhi polyt");
        kaikkiRutiinit.add("Lenkkeily");

        List<String> siivous_genre = new ArrayList<>();
        siivous_genre.add("Imuroi");
        siivous_genre.add("Pese vessa");

        List<String> liikunta_genre = new ArrayList<>();
        liikunta_genre.add("Lenkkeile");
        liikunta_genre.add("Salitreeni");

        List<String> rentoutuminen_genre = new ArrayList<>();
        rentoutuminen_genre.add("Lue kirjaa");
        rentoutuminen_genre.add("Paivaunet");

        listHashMap.put(listDataHeader.get(0), kaikkiRutiinit);
        listHashMap.put(listDataHeader.get(1), siivous_genre);
        listHashMap.put(listDataHeader.get(2), liikunta_genre);
        listHashMap.put(listDataHeader.get(3), rentoutuminen_genre);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_main, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//
//            case R.id.action_main:
//                startActivity(new Intent(this, MainActivity.class));
//                Toast.makeText(this, "Main", Toast.LENGTH_SHORT).show();
//                return true;
//            case R.id.action_settings:
//                startActivity(new Intent(this, Settings.class));
//                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
//                return true;
//            case R.id.action_setTasks:
//                startActivity(new Intent(this, SetTasks.class));
//                Toast.makeText(this, "Already in Set Tasks", Toast.LENGTH_SHORT).show();
//                return true;
//            case R.id.action_routines:
//                startActivity(new Intent(this, Routines.class));
//                Toast.makeText(this, "Routines", Toast.LENGTH_SHORT).show();
//                return true;
//            case R.id.action_signOut:
//                FirebaseAuth.getInstance().signOut();
//                startActivity(new Intent(this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
//                setContentView(R.layout.login_main);
//                finish();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
}
