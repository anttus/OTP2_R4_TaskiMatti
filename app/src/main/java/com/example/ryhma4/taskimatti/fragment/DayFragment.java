package com.example.ryhma4.taskimatti.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ryhma4.taskimatti.R;
import com.example.ryhma4.taskimatti.activity.MainActivity;

import org.w3c.dom.Text;

import java.util.Date;

/**
 * Created by mikae on 6.3.2018.
 */

public class DayFragment extends Fragment {
    private TextView mainRoutineText, mainTimeText;
    private LinearLayout mainTimesLayout, mainRoutinesLayout;

    public DayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_day,container, false);
        mainRoutineText = view.findViewById(R.id.mainRoutineText);
        mainTimeText = view.findViewById(R.id.mainTimeText);
        mainRoutinesLayout = view.findViewById(R.id.mainRoutinesLayout);
        mainTimesLayout = view.findViewById(R.id.mainTimesLayout);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        Bundle args = getArguments();
        int day = args.getInt("day");
        Date date = new Date();

        switch (day) {
            case 0:
                mainRoutineText.setText("Maanantai");
                mainTimeText.setText("asdf");
                break;
            case 1:
                mainRoutineText.setText("Tiistai");
                break;
            case 2:
                mainRoutineText.setText("Keskiviikko");
                break;
            case 3:
                mainRoutineText.setText("Torstai");
                break;
            case 4:
                mainRoutineText.setText("Perjantai");
                break;
            case 5:
                mainRoutineText.setText("Lauantai");
                break;
            case 6:
                mainRoutineText.setText("Sunnuntai");
                break;
        }

    }

}
