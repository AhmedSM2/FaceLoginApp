package com.example.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile3 extends AppCompatActivity {
    private Button logout_btn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile3);
        logout_btn = findViewById(R.id.logout_profile3_btn);
        mAuth = FirebaseAuth.getInstance();

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                LoginManager.getInstance().logOut();
                updateUI();
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            updateUI();
        }
    }
    private void updateUI() {
        Toast.makeText(getApplicationContext(),"you are logged in now",Toast.LENGTH_LONG).show();
        sendUserToMainActiity();
    }
    private  void sendUserToMainActiity()
    {
        Intent goToRegisterActivity = new Intent(getApplicationContext(),Login_Activity.class);
        startActivity(goToRegisterActivity);
    }
}
