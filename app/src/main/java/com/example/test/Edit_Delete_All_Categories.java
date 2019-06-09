package com.example.test;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Edit_Delete_All_Categories extends AppCompatActivity {

    DatabaseReference categoryPlaceRef;


    public TextView cat_place_name, cat_place_lat, cat_Place_long, cat_place_type, cat_place_rate;
    public Button edit_place_Btn, back_btn, del_place_Btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_delete_all_categories);

        cat_place_name = findViewById(R.id.cat_place_name);
        cat_place_lat = findViewById(R.id.cat_place_lat);
        cat_Place_long = findViewById(R.id.cat_place_long);
        cat_place_type = findViewById(R.id.cat_place_type);
        cat_place_rate = findViewById(R.id.cat_place_rate);
        edit_place_Btn = findViewById(R.id.btn_edit_cat_place);
        del_place_Btn = findViewById(R.id.btn_del_cat_place);
        back_btn = findViewById(R.id.btn_back);

        Bundle bundle = getIntent().getExtras();
        final String AreaName = bundle.getString("areaName");
        final String categoryName = bundle.getString("catName");
        final String placeName = bundle.getString("category_place_name");
        final double placeLatitude = bundle.getDouble("category_place_lat");
        final double placeLongitude = bundle.getDouble("category_place_long");
        final int placeType = bundle.getInt("category_place_type");
        final int placeRate = bundle.getInt("category_place_rate");

        categoryPlaceRef = FirebaseDatabase.getInstance().getReference().child("AbuEl3orifDB").child("Areas").child(AreaName).child("categories").child(categoryName).child(placeName);

        categoryPlaceRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    String place_name = dataSnapshot.child("category_name").getValue().toString();
                    String place_latitude = dataSnapshot.child("category_latitude").getValue().toString();
                    String place_longitude = dataSnapshot.child("category_longitude").getValue().toString();
                    int place_type = Integer.parseInt(dataSnapshot.child("category_type").getValue().toString());
                    int place_rate = Integer.parseInt(dataSnapshot.child("category_rate").getValue().toString());

                    cat_place_name.setText(place_name);
                    cat_place_lat.setText("Latitude : " + place_latitude);
                    cat_Place_long.setText("Longitude : " + place_longitude);
                    cat_place_type.setText("Type : " + place_type);
                    cat_place_rate.setText("Rate : " + place_rate);

                }


                edit_place_Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent editPlace = new Intent(Edit_Delete_All_Categories.this, Edit_Specific_Category_Place.class);
                        editPlace.putExtra("place_name", placeName);
                        editPlace.putExtra("place_latitude", placeLatitude);
                        editPlace.putExtra("place_longitude", placeLongitude);
                        editPlace.putExtra("place_type", placeType);
                        editPlace.putExtra("place_rate", placeRate);
                        editPlace.putExtra("AreaName", AreaName);
                        editPlace.putExtra("CategoryName", categoryName);

                        startActivity(editPlace);

                    }
                });


                back_btn.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent a = new Intent(Edit_Delete_All_Categories.this , Manage_categories_childern.class);
                        startActivity(a);
                    }
                });

                del_place_Btn.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        categoryPlaceRef.removeValue();
                        Intent back = new Intent(Edit_Delete_All_Categories.this,Manage_Areas.class);
                        startActivity(back);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
