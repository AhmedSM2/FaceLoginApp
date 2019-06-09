package com.example.test;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class InfoActivity extends AppCompatActivity {
    CircleImageView profile_img;
    TextView username;
    Button block,unblock;

    FirebaseUser firebaseUser;
    DatabaseReference reference,Blocked;
    String userid,email,statue;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        profile_img = (CircleImageView) findViewById(R.id.profile_img);
        username = (TextView)findViewById(R.id.username);
        block = (Button)findViewById(R.id.btn_blockUser);
        unblock = (Button)findViewById(R.id.btn_unblockUser);
        intent = getIntent();
        userid = intent.getStringExtra("userid");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(userid);
        Blocked = FirebaseDatabase.getInstance().getReference().child("Blocked").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user_class user = dataSnapshot.getValue(user_class.class);
                statue=user.getIsblocked();
                if (statue.equals("true")){
                    unblock.setVisibility(View.VISIBLE);
                    block.setVisibility(View.INVISIBLE);
                }
                else {
                    unblock.setVisibility(View.INVISIBLE);
                    block.setVisibility(View.VISIBLE);
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("isblocked","true");
                reference.updateChildren(hashMap);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        user_class user = dataSnapshot.getValue(user_class.class);
                        email=user.getUser_fullname();
                        BlockedUser blockedUser = new BlockedUser(email,userid);
                        HashMap UserMap = new HashMap();
                        UserMap.put("email", email);
                        UserMap.put("uid", userid);

                        Blocked.setValue(UserMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(InfoActivity.this,"Blocked Successfully",Toast.LENGTH_SHORT).show();
                                gotoAllUsersActivity();
                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });
        unblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("isblocked","false");
                reference.updateChildren(hashMap);
                Toast.makeText(InfoActivity.this,"UnBlocked Successfully",Toast.LENGTH_SHORT).show();
                gotoAllUsersActivity();



            }
        });


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user_class user = dataSnapshot.getValue(user_class.class);
                username.setText(user.getUser_username());
                if (user.getImageUrl().equals("default")){

                    profile_img.setImageResource(R.mipmap.ic_launcher);


                }
                else {
                    Glide.with(getApplicationContext()).load(user.getImageUrl()).into(profile_img);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void gotoAllUsersActivity(){
        Intent intent = new Intent(InfoActivity.this, AllUsersActivity.class);
        startActivity(intent);
        finish();
    }
}
