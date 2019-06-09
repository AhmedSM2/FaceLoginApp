package com.example.test;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Register_Activity extends AppCompatActivity
{
    private EditText user_email , user_password , user_confirm_password;
    private Button register_form_btn;
    private TextView login_link;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    private Animation logoAnim;
    private ImageView registerLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_);

        user_email = findViewById(R.id.register_email_input);
        user_password = findViewById(R.id.register_password_input);
        user_confirm_password = findViewById(R.id.register_confirmPassword_input);
        register_form_btn = findViewById(R.id.register_btn);
        login_link = findViewById(R.id.register_login_link);
        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);

        login_link.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                goToLoginActivity();
            }
        });

        register_form_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                createNewAccount();
            }
        });

        /*___________________________animation Logo __________________________*/

        registerLogo=findViewById(R.id.register_logo);
        logoAnim= AnimationUtils.loadAnimation(this,R.anim.logo_anim);
        registerLogo.startAnimation(logoAnim);
        /*_____________________________________________________________________*/
    }

    private void goToLoginActivity()
    {
        Intent loginActivityIntent = new Intent(getApplicationContext(),Login_Activity.class);
        startActivity(loginActivityIntent);
        finish();

    }

    private void createNewAccount()
    {
        final String email = user_email.getText().toString();
        String password = user_password.getText().toString();
        String confirm_password= user_confirm_password.getText().toString();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "please fill your email field ", Toast.LENGTH_SHORT).show();
        }


       else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "please fill your password field ", Toast.LENGTH_SHORT).show();
        }


       else if(TextUtils.isEmpty(confirm_password))
        {
            Toast.makeText(this, "please fill your confirm password field ", Toast.LENGTH_SHORT).show();
        }
        else if(!password.equals(confirm_password))
        {
            Toast.makeText(this, "password is not matching with confirm password", Toast.LENGTH_SHORT).show();
        }
        else
        {


         loadingBar.setTitle("Creating your new Account");
         loadingBar.setMessage("Please wait we are setup your new Account now ....");
         loadingBar.show();
         loadingBar.setCanceledOnTouchOutside(true);

         mAuth.createUserWithEmailAndPassword(email , password)
                 .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                 {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task)
                     {
                         if(task.isSuccessful())
                         {
                         //  user_class user =new user_class("", "", "", 2,"false");
                             HashMap UserMap = new HashMap();
                             UserMap.put("email", email);
                             UserMap.put("isblocked","false");
                             UserMap.put("status","offline");
                             UserMap.put("user_type", 2);
                             UserMap.put("username","username");
                             UserMap.put("fullname","fullname");
                             UserMap.put("address","address");
                             UserMap.put("ImageUrl", "default");
                             UserMap.put("UserArea", "default");
                             UserMap.put("Bio","");
                             UserMap.put("phone","");
                             UserMap.put("uid",FirebaseAuth.getInstance().getCurrentUser().getUid() );
                             //UserMap.put("username",username);
                             FirebaseDatabase.getInstance().getReference("users")
                                     .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                     .setValue(UserMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                 @Override
                                 public void onComplete(@NonNull Task<Void> task) {
                                     if (task.isSuccessful()) {
                                         FirebaseDatabase.getInstance().getReference("users")
                                                 .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                             @Override
                                             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                 user_class user1 = dataSnapshot.getValue(user_class.class);
                                                 int i = user1.getUser_type();
                                                 if(i != 1){
                                                     //Toast.makeText(Registeration.this,"Hello Regular user", Toast.LENGTH_LONG).show();
                                                    // gotoMain();
                                                     sendUserTosetupActivity();
                                                     Toast.makeText(Register_Activity.this, "your Account created successfully", Toast.LENGTH_SHORT).show();
                                                     loadingBar.dismiss();
                                                 }

                                             }

                                             @Override
                                             public void onCancelled(@NonNull DatabaseError databaseError) {

                                             }
                                         });

                                     }

                                 }
                             });
                             /*sendUserTosetupActivity();
                             Toast.makeText(Register_Activity.this, "your Account created successfully", Toast.LENGTH_SHORT).show();
                             loadingBar.dismiss();*/

                         }
                         else
                         {
                             String message = task.getException().getMessage();
                             Toast.makeText(Register_Activity.this, "an error occured : "+ message, Toast.LENGTH_SHORT).show();
                             loadingBar.dismiss();
                         }

                     }
                 });

        }
    }

    //send user to setup activity method---------------------
    private void sendUserTosetupActivity()
    {
        Intent setupIntent = new Intent(getApplicationContext() , setup_user_Activity.class);
       // setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        //finish();
    }
    //-------------------------------------------------------
}
