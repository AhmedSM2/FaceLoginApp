package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Manage_categories_childern extends AppCompatActivity {

    DatabaseReference area_category_children_ref;
    final List<Category_class> categories_children = new ArrayList<Category_class>();

    ListView All_Area_Categories_children;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_categories_childern);

        All_Area_Categories_children = findViewById(R.id.all_area_cat_children);


        Bundle bundle = getIntent().getExtras();
        final String AreaName = bundle.getString("areaName");
        final String categoryName = bundle.getString("catName");

        area_category_children_ref = FirebaseDatabase.getInstance().getReference().child("AbuEl3orifDB").child("Areas").child(AreaName).child("categories").child(categoryName);


        area_category_children_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                categories_children.clear();

                for (DataSnapshot catSnapshot : dataSnapshot.getChildren()) {
                    Category_class category_child = catSnapshot.getValue(Category_class.class);
                    categories_children.add(category_child);

                }

                manage_Category_list_item adapter = new manage_Category_list_item(Manage_categories_childern.this, categories_children);
                All_Area_Categories_children.setAdapter(adapter);

                All_Area_Categories_children.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Category_class cat = categories_children.get(position);

                        Intent toPlace = new Intent(Manage_categories_childern.this, Edit_Delete_All_Categories.class);

                        toPlace.putExtra("category_place_name", cat.getCategory_name());
                        toPlace.putExtra("category_place_lat", cat.getCategory_latitude());
                        toPlace.putExtra("category_place_long", cat.getCategory_longitude());
                        toPlace.putExtra("category_place_type", cat.getCategory_type());
                        toPlace.putExtra("category_place_rate", cat.getCategory_rate());
                        toPlace.putExtra("areaName", AreaName);
                        toPlace.putExtra("catName", categoryName);


                        startActivity(toPlace);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
