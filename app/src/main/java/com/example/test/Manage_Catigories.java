package com.example.test;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Manage_Catigories extends AppCompatActivity {


    DatabaseReference area_category_ref;
    final List<String> categories = new ArrayList<String>();

    ListView All_Area_Categories;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_categories);

        All_Area_Categories = findViewById(R.id.all_area_cat);

        Bundle bundle = getIntent().getExtras();
        final String AreaName = bundle.getString("areaName");

        area_category_ref = FirebaseDatabase.getInstance().getReference().child("AbuEl3orifDB").child("Areas").child(AreaName).child("categories");


        area_category_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                categories.clear();
                for (DataSnapshot catSnapshot: dataSnapshot.getChildren())
                {
                    String category = catSnapshot.getKey();
                    categories.add(category);
                }

                ArrayAdapter catAdapter = new ArrayAdapter(Manage_Catigories.this , android.R.layout.simple_list_item_1 , categories);
                All_Area_Categories.setAdapter(catAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        All_Area_Categories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String category_child_name = categories.get(position);

                Intent toCategory_child_info = new Intent(Manage_Catigories.this , Manage_categories_childern.class);

                toCategory_child_info.putExtra("catName" , category_child_name);
                toCategory_child_info.putExtra("areaName" , AreaName);

                startActivity(toCategory_child_info);

            }
        });



    }
}
