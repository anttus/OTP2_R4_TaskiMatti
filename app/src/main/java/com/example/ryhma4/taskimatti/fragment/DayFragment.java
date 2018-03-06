package com.example.ryhma4.taskimatti.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ryhma4.taskimatti.R;

/**
 * Created by mikae on 6.3.2018.
 */

public class DayFragment extends Fragment {
    private TextView mTextView;

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
            mTextView = (TextView) view.findViewById(R.id.fragment_main_text);
            return view;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState){
            super.onActivityCreated(savedInstanceState);

            Bundle args = getArguments();
            int day = args.getInt("day");

            switch (day) {
                case 0:
                    mTextView.setText("Maanantai");
                    break;
                case 1:
                    mTextView.setText("Tiistai");
                    break;
                case 2:
                    mTextView.setText("Keskiviikko");
                    break;
                case 3:
                    mTextView.setText("Torstai");
                    break;
                case 4:
                    mTextView.setText("Perjantai");
                    break;
                case 5:
                    mTextView.setText("Lauantai");
                    break;
                case 6:
                    mTextView.setText("Sunnuntai");
                    break;
            }

        }

}
