package com.example.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class AdminActivity extends AppCompatActivity {
    private Button btn_Manage,btn_showUsers,btn_logout;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        btn_Manage =(Button)findViewById(R.id.Manage_Map);
        btn_showUsers = (Button)findViewById(R.id.all_Uesrs);
        btn_logout = (Button)findViewById(R.id.logout_admin);
        mAuth = FirebaseAuth.getInstance();
        btn_showUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent AllUsers = new Intent(AdminActivity.this , AllUsersActivity.class);
                startActivity(AllUsers);
            }
        });
        btn_Manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent manageA = new Intent(AdminActivity.this , Manage_Areas.class);
                startActivity(manageA);
            }
        });
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                sendUserToLoginActivity();
            }
        });
    }
    private void sendUserToLoginActivity()
    {
        Intent goToLoginActivity = new Intent(getApplicationContext(),Login_Activity.class);
        goToLoginActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(goToLoginActivity);
        finish();
    }
}
