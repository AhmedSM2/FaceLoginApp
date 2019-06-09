package com.example.test;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class Edit_Specific_Category_Place extends AppCompatActivity {


    DatabaseReference placeRef, placeRef2;
    EditText place_Name, place_Rate;
    Button savePlace, pickPlace;
    double placeLat, placeLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_specific_category_place);


        Bundle bundle = getIntent().getExtras();
        final String AreaName = bundle.getString("AreaName");
        final String CategoryName = bundle.getString("CategoryName");
        final String placeName = bundle.getString("place_name");
        final double placeLatitude = bundle.getDouble("place_latitude");
        final double placeLongitude = bundle.getDouble("place_longitude");
        final int placeType = bundle.getInt("place_type");
        final int placeRate = bundle.getInt("place_rate");

        place_Name = findViewById(R.id.place_name);
        place_Rate = findViewById(R.id.place_rate);
        savePlace = findViewById(R.id.btn_save_place);
        pickPlace = findViewById(R.id.pick_place);

        placeRef = FirebaseDatabase.getInstance().getReference().child("AbuEl3orifDB").child("Areas").child(AreaName).child("categories").child(CategoryName).child(placeName);
        placeRef2 = FirebaseDatabase.getInstance().getReference().child("AbuEl3orifDB").child("Areas").child(AreaName).child("categories").child(CategoryName);

        placeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    String place__name = dataSnapshot.child("category_name").getValue().toString();
                    int place__rate = Integer.parseInt(dataSnapshot.child("category_rate").getValue().toString());

                    place_Name.setText(place__name);
                    place_Rate.setText(place__rate);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        pickPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(Edit_Specific_Category_Place.this).create();
                alertDialog.setTitle("Pick Place Location");
                alertDialog.setIcon(R.drawable.location_icon);
                LayoutInflater layoutInflater = LayoutInflater.from(Edit_Specific_Category_Place.this);
                View promptView = layoutInflater.inflate(R.layout.dialogmap, null);
                alertDialog.setView(promptView);

                MapView mMapView = promptView.findViewById(R.id.mapView);
                MapsInitializer.initialize(Edit_Specific_Category_Place.this);

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

                                placeLat = latLng.latitude;
                                placeLong = latLng.longitude;

                            }
                        });
                    }
                });

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Add Location", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(Edit_Specific_Category_Place.this, "Location has been picked successuflly...", Toast.LENGTH_LONG).show();

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


        savePlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeRef.removeValue();

                double PlaceLat, PlaceLong;

                int Placerate = Integer.parseInt(place_Rate.getText().toString());
                String PlaceName = place_Name.getText().toString();

                if (placeLat == 0 && placeLong == 0) {
                    PlaceLat = placeLatitude;
                    PlaceLong = placeLongitude;
                } else {
                    PlaceLat = placeLat;
                    PlaceLong = placeLong;
                }

                HashMap placeobj = new HashMap();
                placeobj.put("category_latitude", PlaceLat);
                placeobj.put("category_longitude", PlaceLong);
                placeobj.put("category_name", PlaceName);
                placeobj.put("category_type", placeType);
                placeobj.put("category_rate", Placerate);


                placeRef2.child(PlaceName).updateChildren(placeobj);

                Intent a = new Intent(Edit_Specific_Category_Place.this, Edit_Delete_All_Categories.class);
                startActivity(a);
            }
        });

    }
}
