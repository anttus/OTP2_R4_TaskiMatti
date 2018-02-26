package com.example.ryhma4.taskimatti.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.example.ryhma4.taskimatti.utility.ExapandableListAdapter;
import com.example.ryhma4.taskimatti.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShowRoutinesActivity extends MainActivity {
    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_routines);

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

        List<String> siivous_genre = new ArrayList<>();
        siivous_genre.add("Imuroi");
        siivous_genre.add("Pese vessa");

        List<String> liikunta_genre = new ArrayList<>();
        liikunta_genre.add("Lenkkeile");
        liikunta_genre.add("Salitreeni");

        List<String> rentoutuminen_genre = new ArrayList<>();
        rentoutuminen_genre.add("Lue kirjaa");
        rentoutuminen_genre.add("Paivaunet");

        List<List<String>> kaikkiRutiinit = new ArrayList<List<String>>();
        kaikkiRutiinit.add(siivous_genre);
        kaikkiRutiinit.add(liikunta_genre);
        kaikkiRutiinit.add(rentoutuminen_genre);

        ArrayList<String> temp = new ArrayList<>();

        for (int i = 0; i < kaikkiRutiinit.size(); i++){
            for(int j = 0; j < kaikkiRutiinit.get(i).size(); j++){
                temp.add(kaikkiRutiinit.get(i).get(j));
            }

        }

        listHashMap.put(listDataHeader.get(0), temp);
        listHashMap.put(listDataHeader.get(1), siivous_genre);
        listHashMap.put(listDataHeader.get(2), liikunta_genre);
        listHashMap.put(listDataHeader.get(3), rentoutuminen_genre);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }

}
