package com.example.test;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Add_Area_Activirt extends AppCompatActivity
{

    private EditText area_name ,area_lng;
    private Button btn_pick_area_location , area_save_btn , btn_intent_save_category;

    private DatabaseReference AbuEl3orif_DB_Ref ;

    double areaLat , areaLng;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__area__activirt);

        area_name = findViewById(R.id.area_name_txt);
        btn_pick_area_location = findViewById(R.id.area_lat_txt);
        area_save_btn = findViewById(R.id.btn_save_area);
        btn_intent_save_category = findViewById(R.id.btn_save_category_intent);

        btn_pick_area_location.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                AlertDialog alertDialog = new AlertDialog.Builder(Add_Area_Activirt.this).create();
                alertDialog.setTitle("Pick Area Location");
                alertDialog.setIcon(R.drawable.location_icon);
                LayoutInflater layoutInflater = LayoutInflater.from(Add_Area_Activirt.this);
                View promptView = layoutInflater.inflate(R.layout.dialogmap, null);
                alertDialog.setView(promptView);

                MapView mMapView =  promptView.findViewById(R.id.mapView);
                MapsInitializer.initialize(Add_Area_Activirt.this);

                mMapView.onCreate(alertDialog.onSaveInstanceState());
                mMapView.onResume();


                mMapView.getMapAsync(new OnMapReadyCallback()
                {
                    @Override
                    public void onMapReady(final GoogleMap googleMap)
                    {
                        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                            @Override
                            public void onMapClick(LatLng latLng)
                            {
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

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Add Location", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {

                        Toast.makeText(Add_Area_Activirt.this, "Location has been picked successuflly...", Toast.LENGTH_LONG).show();

                    }

                });


                alertDialog.show();

                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
                //alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources().getColor());


                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0,0,150,0);

                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setLayoutParams(params);

            }
        });

        AbuEl3orif_DB_Ref  = FirebaseDatabase.getInstance().getReference().child("AbuEl3orifDB");

        area_save_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                save_area_to_DB();
            }
        });

        btn_intent_save_category.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                go_to_save_category_activity();
            }
        });


    }

    private void go_to_save_category_activity()
    {
     Intent save_category_intent = new Intent(getApplicationContext() , Add_Category_Activity.class);
     startActivity(save_category_intent);
     finish();

    }

    private void save_area_to_DB()
    {
        String AreaName = area_name.getText().toString();
        double AreaLat = areaLat;
        double AreaLng = areaLng;

        Area_class Area_object = new Area_class(AreaName , AreaLat , AreaLng);

        FirebaseHelper fh = new FirebaseHelper(AbuEl3orif_DB_Ref);

        if(fh.save_Area_DB(Area_object))
        {
            Toast.makeText(this, "Area Saved to Database Successfully ...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Error occuired...", Toast.LENGTH_SHORT).show();
        }
    }
}
