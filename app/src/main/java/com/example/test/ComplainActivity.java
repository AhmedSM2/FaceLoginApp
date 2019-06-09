package com.example.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ComplainActivity extends AppCompatActivity {

    Button btnCompalin;
    EditText complainText;

    private DatabaseReference complain ;
    private DatabaseReference UserRef;
    private String CurrentUser_id,PostKey;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain);

        mAuth = FirebaseAuth.getInstance();
        CurrentUser_id = mAuth.getCurrentUser().getUid();

        btnCompalin = findViewById(R.id.btnComplain);
        complainText = findViewById(R.id.complainText);
//Get Id Of The Post :
        Intent intent = getIntent();

        PostKey = getIntent().getExtras().get("PostKey").toString();


        complain = FirebaseDatabase.getInstance().getReference().child("Complains");

        btnCompalin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String complainContent =complainText.getText().toString();
//Get Id of our complain :
                String complainId = complain.child("Complains").push().getKey();
                Complain comp = new Complain(complainId,complainContent,PostKey,CurrentUser_id);
                complain.child(complainId).setValue(comp);
                Toast.makeText(ComplainActivity.this, "Your Complain Added Successfully", Toast.LENGTH_SHORT).show();
                goToMainActivity();
            }
            private void goToMainActivity() {
                Intent goToMain = new Intent(getApplicationContext(),MainSecondActivity.class);
                startActivity(goToMain);
            }
        });





    }
}
