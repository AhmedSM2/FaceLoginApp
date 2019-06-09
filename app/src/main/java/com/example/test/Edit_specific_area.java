package com.example.test;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Edit_specific_area extends AppCompatActivity {
    DatabaseReference areaRef, areaRef2;
    EditText areaName;
    Button saveArea, pickArea;
    double areaLat, areaLng;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_specific_area);

        Bundle bundle = getIntent().getExtras();
        final String AreaName = bundle.getString("areaName");
        final double AreaLatitude = bundle.getDouble("areaLatitude");
        final double AreaLongitude = bundle.getDouble("areaLongitude");


        areaName = findViewById(R.id.area_name);
        pickArea = findViewById(R.id.pickArea);

        saveArea = findViewById(R.id.btn_save_area);


        areaRef = FirebaseDatabase.getInstance().getReference().child("AbuEl3orifDB").child("Areas").child(AreaName);
        areaRef2 = FirebaseDatabase.getInstance().getReference().child("AbuEl3orifDB").child("Areas");

        areaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    String arname = dataSnapshot.child("area_name").getValue().toString();

                    areaName.setText(arname);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        pickArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(Edit_specific_area.this).create();
                alertDialog.setTitle("Pick Area Location");
                alertDialog.setIcon(R.drawable.location_icon);
                LayoutInflater layoutInflater = LayoutInflater.from(Edit_specific_area.this);
                View promptView = layoutInflater.inflate(R.layout.dialogmap, null);
                alertDialog.setView(promptView);

                MapView mMapView = promptView.findViewById(R.id.mapView);
                MapsInitializer.initialize(Edit_specific_area.this);

                mMapView.onCreate(alertDialog.onSaveInstanceState());
                mMapView.onResume();


                mMapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(final GoogleMap googleMap) {
                        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                            @Override
                            public void onMapClick(LatLng latLng) {
                                // Creating a marker
                                MarkerOptions markerOptions = new MarkerOptions();

                                // Setting the position for the marker
                                markerOptions.position(latLng);

                                // Setting the title for the marker.
                                // This will be displayed on taping the marker
                                markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                                // Clears the previously touched position
                                googleMap.clear();

                                // Animating to the touched position
                                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                                // Placing a marker on the touched position
                                googleMap.addMarker(markerOptions);

                                areaLat = latLng.latitude;
                                areaLng = latLng.longitude;

                            }
                        });
                    }
                });

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Add Location", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(Edit_specific_area.this, "Location has been picked successuflly...", Toast.LENGTH_LONG).show();

                    }

                });


                alertDialog.show();

                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
                //alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources().getColor());


                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, 150, 0);

                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setLayoutParams(params);

            }
        });

        saveArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                areaRef.removeValue();
                double AreaLat, AreaLng;
                String AreaName = areaName.getText().toString();
                if (areaLat == 0 && areaLng == 0) {
                    AreaLat = AreaLatitude;
                    AreaLng = AreaLongitude;
                } else {
                    AreaLat = areaLat;
                    AreaLng = areaLng;
                }

                HashMap areaobj = new HashMap();
                areaobj.put("area_name", AreaName);
                areaobj.put("area_latitude", AreaLat);
                areaobj.put("area_longitude", AreaLng);


                areaRef2.child(AreaName).updateChildren(areaobj);

                Intent a = new Intent(Edit_specific_area.this, Manage_Areas.class);
                startActivity(a);
            }
        });

    }


}
