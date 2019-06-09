package com.example.test;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

public class Edit_Delete_All_Areas extends AppCompatActivity {

    DatabaseReference areaRef;


    public TextView areaName, area_lat, area_long;
    public Button editBtn, backbtn, delBtnArea, btn_area_categories;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_delete_all_areas);

        editBtn = findViewById(R.id.btn_edit_area);
        backbtn = findViewById(R.id.btn_back);
        areaName = findViewById(R.id.areaName);
        area_lat = findViewById(R.id.are_lat);
        area_long = findViewById(R.id.area_long);
        delBtnArea = findViewById(R.id.btn_del_area);
        btn_area_categories = findViewById(R.id.Btn_area_categories);

        Bundle bundle = getIntent().getExtras();
        final String AreaName = bundle.getString("areaName");
        final double AreaLatitude = bundle.getDouble("areaLatitude");
        final double AreaLongitude = bundle.getDouble("areaLongitude");

        areaRef = FirebaseDatabase.getInstance().getReference().child("AbuEl3orifDB").child("Areas").child(AreaName);
        areaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    String arname = dataSnapshot.child("area_name").getValue().toString();
                    String arlat = dataSnapshot.child("area_latitude").getValue().toString();
                    String arlong = dataSnapshot.child("area_longitude").getValue().toString();

                    areaName.setText(arname);
                    area_lat.setText("Latitude : " + arlat);
                    area_long.setText("Longitude : " + arlong);


                }
                editBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent a = new Intent(Edit_Delete_All_Areas.this, Edit_specific_area.class);
                        a.putExtra("areaName", AreaName);
                        a.putExtra("areaLatitude", AreaLatitude);
                        a.putExtra("areaLongitude", AreaLongitude);
                        startActivity(a);
                    }
                });


                backbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent a = new Intent(Edit_Delete_All_Areas.this, Manage_Areas.class);
                        startActivity(a);
                    }
                });

                delBtnArea.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        areaRef.removeValue();
                        sendToManageArea();
                    }
                });

                btn_area_categories.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent ar_cat = new Intent(Edit_Delete_All_Areas.this, Manage_Catigories.class);
                        ar_cat.putExtra("areaName", AreaName);
                        startActivity(ar_cat);
                    }
                });


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendToManageArea() {
        Intent manageAreaactivity = new Intent(Edit_Delete_All_Areas.this, Manage_Areas.class);
        startActivity(manageAreaactivity);
    }
}



