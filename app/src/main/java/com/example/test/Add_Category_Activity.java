package com.example.test;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.List;

public class Add_Category_Activity extends AppCompatActivity
{
    private EditText ca_name  , ca_rate /*, ca_lat*/ , ca_lng;
    private Button save_ca_btn , btn_show_ca;
    private DatabaseReference AbuEl3orif_DB_Ref , AbuEl3orif_DB_Ref2;
    private Spinner ca_type , ca_area; //spinner
    private ArrayAdapter<String> arrayAdapter ;
    private String  categories[] = {"Resturants" , "Hospitals" , "Coffe Shops"};
    private Button ca_location;
    int category_t ;
    String selected_Area;

    double category_lat , category_lng;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__category_);

        ca_name = findViewById(R.id.category_name_txt);
        ca_type = findViewById(R.id.category_type_txt);
        ca_rate = findViewById(R.id.category_rate_txt);
        ca_location = findViewById(R.id.category_lat_txt);
        save_ca_btn = findViewById(R.id.btn_save_category);
        final List<String> areas = new ArrayList<String>();

        ca_location.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


                AlertDialog alertDialog = new AlertDialog.Builder(Add_Category_Activity.this).create();
                alertDialog.setTitle("Pick Category Location");
                alertDialog.setIcon(R.drawable.location_icon);
                LayoutInflater layoutInflater = LayoutInflater.from(Add_Category_Activity.this);
                View promptView = layoutInflater.inflate(R.layout.dialogmap, null);
                alertDialog.setView(promptView);

                MapView mMapView =  promptView.findViewById(R.id.mapView);
                MapsInitializer.initialize(Add_Category_Activity.this);

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

                                category_lat = latLng.latitude;
                                category_lng = latLng.longitude;

                            }
                        });
                    }
                });

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Add Location", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {

                        Toast.makeText(Add_Category_Activity.this, "Location has been picked successuflly...", Toast.LENGTH_LONG).show();

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
                //alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setLayoutParams(params);

            }
        });


        AbuEl3orif_DB_Ref  = FirebaseDatabase.getInstance().getReference().child("AbuEl3orifDB");

        AbuEl3orif_DB_Ref2  = FirebaseDatabase.getInstance().getReference().child("AbuEl3orifDB").child("Areas").child("Categories");


        //----spinner for category type-----------------------------------------//
        arrayAdapter = new ArrayAdapter<String>(Add_Category_Activity.this , android.R.layout.simple_spinner_item , categories);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ca_type.setAdapter(arrayAdapter);


        ca_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                if(position == 0)
                {
                    category_t =1;
                }
                else if (position == 1)
                {
                    category_t =2;
                }
                else if (position == 2)
                {
                    category_t =3;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        //-------------------------------------------------------------------------------------------------------------

        //-------------------------------for spinner category areas----------------------------------------------------


        AbuEl3orif_DB_Ref.child("Areas").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                areas.clear();
                // Is better to use a List, because you don't know the size
                // of the iterator returned by dataSnapshot.getChildren() to
                // initialize the array

                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren())
                {
                    Area_class area_object = areaSnapshot.getValue(Area_class.class);
                    String Area_name = area_object.getArea_name();
                    areas.add(Area_name);
                }

                ca_area = findViewById(R.id.category_Area);
                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(Add_Category_Activity.this, android.R.layout.simple_spinner_item, areas);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ca_area.setAdapter(areasAdapter);

                ca_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                    {
                        selected_Area = areas.get(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //--------------------------------------------------------------------------------------------------------------




        save_ca_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                //save_category_DB();
                add_new_category();

            }
        });



    }
//--------------------------------Add new category method-------------------------------------------------------//
    private void add_new_category()
    {
        final ArrayList<Area_class> Areas_objects = new ArrayList<Area_class>();




        AbuEl3orif_DB_Ref.child("Areas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for(DataSnapshot AreaSnapshot : dataSnapshot.getChildren())
                {
                    Area_class area_object = AreaSnapshot.getValue(Area_class.class);
                    Areas_objects.add(area_object);

                }

                for(int i = 0 ; i <= Areas_objects.size() ; i++ )
                {
                    if(Areas_objects.get(i).getArea_name().equals(selected_Area) && category_t == 1)
                    {
                       // AbuEl3orif_DB_Ref.child(Areas_objects.get(i).getArea_name());
                        String categoryName = ca_name.getText().toString();
                        int categoryType = category_t;
                        int categoryRate = Integer.parseInt(ca_rate.getText().toString());

                        double categoryLat = category_lat;
                        double categoryLng = category_lng;

                        Category_class category_object = new Category_class(categoryName , categoryRate , categoryType , categoryLat ,categoryLng);
                        AbuEl3orif_DB_Ref.child("Areas").child(selected_Area).child("categories").child("resturants").child(categoryName).setValue(category_object);
                        AbuEl3orif_DB_Ref.child("categories").child("resturants").child(categoryName).setValue(category_object);
                        AbuEl3orif_DB_Ref.child("AllPlaces").child(categoryName).setValue(category_object);


                        i=Areas_objects.size();

                    }
                   else if(Areas_objects.get(i).getArea_name().equals(selected_Area) && category_t == 2)
                    {
                        // AbuEl3orif_DB_Ref.child(Areas_objects.get(i).getArea_name());
                        String categoryName = ca_name.getText().toString();
                        int categoryType = category_t;
                        int categoryRate = Integer.parseInt(ca_rate.getText().toString());
                        double categoryLat = category_lat;
                        double categoryLng = category_lng;

                        Category_class category_object = new Category_class(categoryName , categoryRate , categoryType , categoryLat ,categoryLng);
                        AbuEl3orif_DB_Ref.child("Areas").child(selected_Area).child("categories").child("hospitals").child(categoryName).setValue(category_object);
                        AbuEl3orif_DB_Ref.child("categories").child("hospitals").child(categoryName).setValue(category_object);
                        AbuEl3orif_DB_Ref.child("AllPlaces").child(categoryName).setValue(category_object);


                        i=Areas_objects.size();

                    }
                  else  if(Areas_objects.get(i).getArea_name().equals(selected_Area) && category_t == 3)
                    {
                        // AbuEl3orif_DB_Ref.child(Areas_objects.get(i).getArea_name());
                        String categoryName = ca_name.getText().toString();
                        int categoryType =category_t;
                        int categoryRate = Integer.parseInt(ca_rate.getText().toString());
                        double categoryLat = category_lat;
                        double categoryLng = category_lng;

                        Category_class category_object = new Category_class(categoryName , categoryRate , categoryType , categoryLat ,categoryLng);
                        AbuEl3orif_DB_Ref.child("Areas").child(selected_Area).child("categories").child("cofeShops").child(categoryName).setValue(category_object);
                        AbuEl3orif_DB_Ref.child("categories").child("cofeShops").child(categoryName).setValue(category_object);
                        AbuEl3orif_DB_Ref.child("AllPlaces").child(categoryName).setValue(category_object);


                        i=Areas_objects.size();

                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
   //---------------------------------------------------------------------------------------------------------------------------


    private void show_all_areas()
    {
        final ArrayList<String> Areas = new ArrayList<String>();

        AbuEl3orif_DB_Ref.child("Areas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for(DataSnapshot AreaSnapshot : dataSnapshot.getChildren())
                {
                    Area_class area_object = AreaSnapshot.getValue(Area_class.class);
                    String Area_name = area_object.getArea_name();
                    Areas.add(Area_name);
                }

                Toast.makeText(Add_Category_Activity.this, "size : " + Areas.size(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void show_all_categories()
    {
//        final ArrayList<String> Areas = new ArrayList<String>();
//
//        AbuEl3orif_DB_Ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
//            {
//                for(DataSnapshot AreaSnapshot : dataSnapshot.getChildren())
//                {
//                    Area_class area_object = AreaSnapshot.getValue(Area_class.class);
//                    String Area_name = area_object.getArea_name();
//                    Areas.add(Area_name);
//                }
//
//                Toast.makeText(Add_Category_Activity.this, "size : " + Areas.size(), Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


        final ArrayList<String> Categories = new ArrayList<String>();

        Categories.clear();

        AbuEl3orif_DB_Ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for(DataSnapshot CategoySnapshot : dataSnapshot.getChildren())
                {
                    Category_class category_object = CategoySnapshot.getValue(Category_class.class);

                    String Category_name = category_object.getCategory_name();

                    if(category_object.getCategory_type() == 1)
                    {
                        Categories.add(Category_name);

                    }

                }

                //Toast.makeText(Add_Category_Activity.this, "size : " + Categories.size(), Toast.LENGTH_SHORT).show();

                for(int i =0 ; i < Categories.size() ; i++)
                {
                    Toast.makeText(Add_Category_Activity.this, "category names of type 1 : " + Categories.get(i), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

//    private void save_category_DB()
//    {
//        String categoryName = ca_name.getText().toString();
//        int categoryType = Integer.parseInt(ca_type.getText().toString());
//        int categoryRate = Integer.parseInt(ca_rate.getText().toString());
//        double categoryLat = Double.parseDouble(ca_lat.getText().toString());
//        double categoryLng = Double.parseDouble(ca_lng.getText().toString());
//
//        Category_class category_object = new Category_class(categoryName , categoryRate , categoryType , categoryLat ,categoryLng);
//        FirebaseHelper fh = new FirebaseHelper(AbuEl3orif_DB_Ref.child("Areas"));
//
//        if(fh.save_Category_DB(category_object))
//        {
//            Toast.makeText(this, "Category Saved to DB Successfully...", Toast.LENGTH_SHORT).show();
//        }
//        else
//        {
//            Toast.makeText(this, "Error occuried", Toast.LENGTH_SHORT).show();
//        }
//
//    }
}
