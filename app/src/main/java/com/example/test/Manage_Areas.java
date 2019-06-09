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
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Manage_Areas extends AppCompatActivity {

    DatabaseReference AreaRef;
    private Toolbar manage_a_toolbar;
    private ImageView addAreaLink;
    final List<Area_class> areas = new ArrayList<Area_class>();
    ListView All_Areas;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_areas);

        manage_a_toolbar = findViewById(R.id.manage_area_toolbar);

        All_Areas = findViewById(R.id.allAreas);

        addAreaLink = findViewById(R.id.add_area_link);

        AreaRef = FirebaseDatabase.getInstance().getReference().child("AbuEl3orifDB").child("Areas");

//        setSupportActionBar(manage_a_toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setTitle("Manage Area");

        AreaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                areas.clear();
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    Area_class a = areaSnapshot.getValue(Area_class.class);
                    areas.add(a);

                }

                manage_Area_List_Item adapter = new manage_Area_List_Item(Manage_Areas.this, areas);
                All_Areas.setAdapter(adapter);

                All_Areas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Area_class area = areas.get(position);

                        Intent a = new Intent(Manage_Areas.this, Edit_Delete_All_Areas.class);
                        a.putExtra("areaName", area.getArea_name());
                        a.putExtra("areaLatitude", area.getArea_latitude());
                        a.putExtra("areaLongitude", area.getArea_longitude());
                        startActivity(a);


                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        addAreaLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendToAddAreaActivity();
            }
        });
    }

    private void SendToAddAreaActivity() {
        Intent AddAreaActivity = new Intent(Manage_Areas.this, Add_Area_Activirt.class);
        startActivity(AddAreaActivity);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            SendUserToMapActivtit();
        }
        return super.onOptionsItemSelected(item);
    }

    private void SendUserToMapActivtit() {
        Intent map_intent = new Intent(Manage_Areas.this, GoogleMapsActivity.class);
        startActivity(map_intent);

    }
}
