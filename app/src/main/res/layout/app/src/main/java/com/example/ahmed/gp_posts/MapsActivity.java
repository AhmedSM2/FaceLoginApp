package com.example.ahmed.gp_posts;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import com.google.android.gms.location.LocationListener;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,  GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private DatabaseReference  UsersRef,AddressRef,db;
    private FirebaseAuth mAuth;
    private String current_user_id;

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker marker;
    private EditText addressField;
    private static final int Request_User_Location_Code = 97;
    private double latitude,longitude;
    private Button close,add,search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();

        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        db = FirebaseDatabase.getInstance().getReference().child("LocationsFirebase");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            checkUserLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        close = (Button) findViewById(R.id.close);
        add = (Button) findViewById(R.id.test_btn);
        search = (Button) findViewById(R.id.search_btn);
        addressField = (EditText) findViewById(R.id.location_search);
        search.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                //final String[] latitude = new String[1];
                //final String[] longitude = new String[1];
                //final double[] lat = new double[1];
                //final double[] lng = new double[1];
                final String address = addressField.getText().toString();
              //  List<Address> addressList = null;
                final MarkerOptions userMarkerOptions = new MarkerOptions();
                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            String name = dataSnapshot.child("Name").getValue().toString();
                            if (address.equals(name)){

                                double lat = (double) dataSnapshot.child("latitude").getValue();
                                double lng = (double) dataSnapshot.child("longitude").getValue();
                               // double latt = lat;
                                Toast.makeText(MapsActivity.this,"Matched "+name +" "+lat+" "+lng,Toast.LENGTH_LONG).show();
                                LatLng latLng = new LatLng(lat, lng);
                                userMarkerOptions.position(latLng);
                                userMarkerOptions.title(address);
                                userMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
                                if(marker !=null){
                                    marker.remove();
                                }

                                mMap.addMarker(userMarkerOptions);
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));

                            }
                            else {
                                Toast.makeText(MapsActivity.this,"Not Matched", Toast.LENGTH_LONG).show();
                            }
                        }
                        else {
                            Toast.makeText(MapsActivity.this,"Error", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoMain();
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap AddressMap = new HashMap();
                LocationsFirebase locationsFirebase2 = new LocationsFirebase("Alexandria",29.924526,31.205753);
                AddressMap.put("Name","Alexandria");
                AddressMap.put("latitude",31.205753 );
                AddressMap.put("longitude", 29.924526);
                //userAddress userAddress = new userAddress(latitude,longitude);
                db.updateChildren(AddressMap);
            }
        });

    }

    public void onClick(View v)
    {
        String hospital = "hospital",school = "school",restaurant = "restaurant";
        Object transferData[] = new Object[2];
        final String[] latitude = new String[1];
        final String[] longitude = new String[1];
        final double[] lat = new double[1];
        final double[] lng = new double[1];

        //  GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces();

        switch (v.getId()) {
        /*    case R.id.test:
                db = FirebaseDatabase.getInstance().getReference().child("LocationsFirebase");
                HashMap AddressMap = new HashMap();
                AddressMap.put("Name", "Alexandria");
                AddressMap.put("latitude", 31.205753);
                AddressMap.put("longitude", 29.924526);
                //userAddress userAddress = new userAddress(latitude,longitude);
                db.setValue(AddressMap);
                break;*/
            case R.id.search:
                EditText addressField = (EditText) findViewById(R.id.location_search);
                final String address = addressField.getText().toString();
                List<Address> addressList = null;
                final MarkerOptions userMarkerOptions = new MarkerOptions();
                db.orderByChild("Name").equalTo(address).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                           // System.out.println(userSnapshot.getKey());
                           latitude[0] = (String) userSnapshot.child("latitude").getValue();
                            longitude[0] =userSnapshot.child("longitude").getValue(String.class);
                            lat[0] = Double.parseDouble(latitude[0]);
                            lng[0] = Double.parseDouble(longitude[0]);

                            LatLng latLng = new LatLng(lat[0], lng[0]);
                            userMarkerOptions.position(latLng);
                            userMarkerOptions.title(address);
                            userMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

                            mMap.addMarker(userMarkerOptions);
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        throw databaseError.toException();
                    }
                });

                /*if (!TextUtils.isEmpty(address)) {
                    Geocoder geocoder = new Geocoder(this);
                    try {
                        addressList = geocoder.getFromLocationName(address, 6);
                        if (addressList != null) {
                            for (int i = 0; i < addressList.size(); i++) {
                                Address userAddress = addressList.get(i);
                                LatLng latLng = new LatLng(userAddress.getLatitude(), userAddress.getLongitude());
                                userMarkerOptions.position(latLng);
                                userMarkerOptions.title(address);
                                userMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                                mMap.addMarker(userMarkerOptions);
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                            }
                        } else {
                            Toast.makeText(this, "location not found", Toast.LENGTH_SHORT).show();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } else {
                    Toast.makeText(this, "write any location", Toast.LENGTH_SHORT).show();
                }*/
                break;

        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
       /* mMap = googleMap;


        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            // TODO: Consider calling
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    final MarkerOptions userMarkerOptions = new MarkerOptions();
                    Toast.makeText(MapsActivity.this,latLng.latitude+"&&& "+latLng.longitude,Toast.LENGTH_SHORT).show();
                    LatLng latLng1 = new LatLng(latLng.latitude, latLng.longitude);
                    userMarkerOptions.position(latLng1);
                    userMarkerOptions.title("Ay 7aga");
                    userMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                    if(marker !=null){
                        marker.remove();
                    }

                    mMap.addMarker(userMarkerOptions);
                }
            });

        }
    }

    public boolean checkUserLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) !=PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION))

            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Request_User_Location_Code);
            }
            else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Request_User_Location_Code);
            }
            return false;
        }
        else {
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case Request_User_Location_Code:
                if (grantResults.length >0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
                    {
                        if (googleApiClient==null){
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else {
                    Toast.makeText(this,"Permission Denied...",Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }
    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }
    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        lastLocation = location;
      /*  AddressRef = FirebaseDatabase.getInstance().getReference().child("UserAddress");
        userAddress userAddress = new userAddress(latitude,longitude);
        AddressRef.setValue(userAddress);
*/
        HashMap AddressMap = new HashMap();
        AddressMap.put("latitude", latitude);
        AddressMap.put("longitude", longitude);
        userAddress userAddress = new userAddress(latitude,longitude);
        UsersRef.child(current_user_id).updateChildren(AddressMap);

      /*  userAddress userAddress = new userAddress();
        userAddress.setLatitude(location.getLatitude());
        userAddress.setLongitude(location.getLongitude());
        User user = new User();
        user.userAddress=userAddress;
        FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(user);

        */
       // Toast.makeText(MapsActivity.this,latitude+" && "+
         //       longitude, Toast.LENGTH_LONG).show();
        if(marker !=null){
            marker.remove();
        }
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("You Are Here");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        marker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(14));

        if(googleApiClient != null){
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);
        }

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1100);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            // TODO: Consider calling
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest,this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    public void gotoMain(){
        Intent intent = new Intent(MapsActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
