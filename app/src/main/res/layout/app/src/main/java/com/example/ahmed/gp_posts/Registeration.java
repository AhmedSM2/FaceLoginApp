package com.example.ahmed.gp_posts;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Registeration extends AppCompatActivity  {

    private EditText inputEmail, inputPassword, inputUsername;

    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;


    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    String lat;
    String provider;
    private double latitude, longitude;
    protected boolean gps_enabled, network_enabled;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);
/*
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
*/

        auth = FirebaseAuth.getInstance();

        btnSignIn = (Button) findViewById(R.id.sign_in_button);
      //  btnAddress = (Button) findViewById(R.id.address);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputUsername = (EditText)findViewById(R.id.username);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registeration.this, ResetPasswordActivity.class));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /*btnAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Registeration.this, MapsActivity.class);
                startActivity(intent);
                finish();
                /*Toast.makeText(Registeration.this,latitude+" && "+
                        longitude, Toast.LENGTH_LONG).show();

            }
        });*/
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = inputEmail.getText().toString().trim();
                final String username = inputUsername.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(getApplicationContext(), "Enter username!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Registeration.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(Registeration.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(Registeration.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {

                                  final   User user = new User("default",email,FirebaseAuth.getInstance().getCurrentUser().getUid(),2,username,"offline");
                                    HashMap UserMap = new HashMap();
                                    UserMap.put("email", email);
                                    UserMap.put("status","offline");
                                    UserMap.put("user_type", 2);
                                    UserMap.put("ImageUrl", "default");
                                    UserMap.put("uid",FirebaseAuth.getInstance().getCurrentUser().getUid() );
                                    UserMap.put("username",username);

                                    FirebaseDatabase.getInstance().getReference("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(UserMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            progressBar.setVisibility(View.GONE);
                                            if (task.isSuccessful()) {
                                                FirebaseDatabase.getInstance().getReference("Users")
                                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        User user1 = dataSnapshot.getValue(User.class);
                                                        int i = user1.getUser_type();
                                                        if(i == 2){
                                                            Toast.makeText(Registeration.this,"Hello Regular user", Toast.LENGTH_LONG).show();
                                                            gotoMain();
                                                        }
                                                        else if(i==3) {
                                                            Toast.makeText(Registeration.this,"Hello Bronzo user", Toast.LENGTH_LONG).show();
                                                            gotoMain();
                                                        }
                                                        else if (i==4){
                                                            Toast.makeText(Registeration.this,"Hello silver user", Toast.LENGTH_LONG).show();
                                                            gotoMain();
                                                        }
                                                        else if (i==5){
                                                            Toast.makeText(Registeration.this,"Hello golden user", Toast.LENGTH_LONG).show();
                                                            gotoMain();
                                                        }
                                                        else{
                                                            Toast.makeText(Registeration.this,"Hello Admin", Toast.LENGTH_LONG).show();
                                                            Intent intent = new Intent(Registeration.this, AdminActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });
                                                //int i = user.getUser_type();

                                            } else {
                                                //display a failure message
                                                Toast.makeText(Registeration.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();

                                            }
                                        }
                                    });

                                }
                                }

                        });

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
    public void gotoMain(){
        Intent intent = new Intent(Registeration.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

   /* @Override
    public void onLocationChanged(Location location) {
       latitude =  location.getLatitude();
    longitude = location.getLongitude();
        Toast.makeText(Registeration.this,latitude+" && "+
                longitude, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.d("Latitude","status");
    }

    @Override
    public void onProviderEnabled(String s) {

        Log.d("Latitude","enable");
    }


    @Override
    public void onProviderDisabled(String s) {
        Log.d("Latitude","disable");
    }
*/
}
