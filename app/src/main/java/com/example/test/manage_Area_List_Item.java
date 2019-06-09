package com.example.test;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class manage_Area_List_Item extends ArrayAdapter {

    private Activity context;
    private List<Area_class> AreaList;

    public manage_Area_List_Item(Activity context, List<Area_class> Areas) {
        super(context, R.layout.manage_areas, Areas);
        this.context = context;
        this.AreaList = Areas;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View ListViewItem = inflater.inflate(R.layout.manage_area_list_item, null, true);

        TextView area_name = ListViewItem.findViewById(R.id.areaName);

        final Area_class area_obj = AreaList.get(position);

        area_name.setText(area_obj.getArea_name());

        return ListViewItem;

    }
}
