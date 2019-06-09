package com.example.test;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;

public class manage_Category_list_item extends ArrayAdapter {


    private Activity context;
    public List<Category_class> CategoriesList;

    public manage_Category_list_item(Activity context, List<Category_class> categoriesList) {
        super(context, R.layout.manage_categories_childern, categoriesList);
        this.context = context;
        CategoriesList = categoriesList;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View ListViewItem = inflater.inflate(R.layout.manage_category_list_item, null, true);

        TextView Cat_name = ListViewItem.findViewById(R.id.categoryName);

        final Category_class category_obj = CategoriesList.get(position);

        Cat_name.setText(category_obj.getCategory_name());
        return ListViewItem;
    }

}
