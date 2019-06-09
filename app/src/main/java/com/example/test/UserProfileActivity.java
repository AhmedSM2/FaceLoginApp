package com.example.test;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserProfileActivity extends AppCompatActivity {

    private TextView bio_txt ,username ,email,address,area ,phone_txt;
    private PopupWindow bio_popupWindow;
    private Button save_bio_btn ,cancel_bio_btn ,editProfileBtn ;
    private EditText bio_edit_txt;
    private DatabaseReference userRef ;
    private FirebaseAuth mAuth;
    private String currentUserId ,userNameStr ,emailStr, addressStr ,bioStr ,areaStr ,phoneStr,userType;
    private Button follow2 = findViewById(R.id.profile_follow_btn2);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        userRef=FirebaseDatabase.getInstance().getReference().child("users");
        mAuth=FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();


        /*______________________________retrieve user data____________________________*/
        username=findViewById(R.id.profile_username_id);
        email=findViewById(R.id.profile_email_id);
        address=findViewById(R.id.profile_address_id);
        area=findViewById(R.id.profile_area_id);
        bio_txt=findViewById(R.id.profile_bio_id);
        phone_txt=findViewById(R.id.profile_phone_id);



        userRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                     userNameStr =dataSnapshot.child("username").getValue().toString();
                     emailStr =dataSnapshot.child("email").getValue().toString();
                     addressStr=dataSnapshot.child("address").getValue().toString();
                     areaStr =dataSnapshot.child("UserArea").getValue().toString();
                     bioStr=dataSnapshot.child("Bio").getValue().toString();
                     phoneStr=dataSnapshot.child("phone").getValue().toString();
                     userType=dataSnapshot.child("user_type").getValue().toString();

                    username.setText(userNameStr);
                    email.setText(emailStr);
                    address.setText(addressStr);
                    area.setText(areaStr);
                    bio_txt.setText(bioStr);
                    phone_txt.setText(phoneStr);




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*___________________________________________________________________*/

        /*_____________________      Follow      _____________________________*/
        if(userType !="2"){
            follow2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        /*___________________________________________________________________*/

        /*______________________________edit user profile____________________________*/
        editProfileBtn=findViewById(R.id.edit_profile_btn);
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent editProfileActivityIntent =new Intent(getApplicationContext(),EditProfileActivity.class);
                editProfileActivityIntent.putExtra("username" ,userNameStr);
                editProfileActivityIntent.putExtra("email" ,emailStr);
                editProfileActivityIntent.putExtra("phone" ,phoneStr);
                editProfileActivityIntent.putExtra("area" ,areaStr);
                editProfileActivityIntent.putExtra("address" ,addressStr);
                editProfileActivityIntent.putExtra("bio" ,bioStr);
                startActivity(editProfileActivityIntent);

            }
        });









    }
}
