package com.example.ryhma4.taskimatti.utility;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.ryhma4.taskimatti.R;
import com.example.ryhma4.taskimatti.model.Routine;
import com.example.ryhma4.taskimatti.model.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by veeti on 14.2.2018.
 */

public class ExapandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<Type> listDataHeader;
    private HashMap<Type, ArrayList<Routine>> listHashMap;

    public ExapandableListAdapter(Context context, List<Type> listDataHeader, HashMap<Type, ArrayList<Routine>> listHashMap) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listHashMap = listHashMap;
    }

    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return listHashMap.get(listDataHeader.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return listDataHeader.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return listHashMap.get(listDataHeader.get(i)).get(i1); // i = Group item, i1 = Children
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        Type header = (Type) getGroup(i);

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_group, null);
        }
        TextView lblListHeader = view.findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setPadding(100,60,60,60);
        lblListHeader.setTextSize(17);
//        lblListHeader.setText("Miksi tulee null object getType().getName():ssa");
        lblListHeader.setText(header.getName());
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        final Routine child = (Routine) getChild(i,i1);
        if (view == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item, null);
        }
        TextView txtListChild = view.findViewById(R.id.lblListItem);
        txtListChild.setPadding(60,60,60,60);
        txtListChild.setText(child.getName());
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}


