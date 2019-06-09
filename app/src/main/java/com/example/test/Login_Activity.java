package com.example.test;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class Login_Activity extends AppCompatActivity
{

    private EditText user_email_login , user_password_login;
    private Button login_button,profile_btn_1,profile_btn_2,profile_btn_3;
    private TextView register_link;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;

    private Animation loginLogoAnim ,translateAnim;
    private LinearLayout loginByLayout;
    private ImageView loginLogo,loginFace_btn;

    // login with facebook
    private CallbackManager mCallbackManager;
    private String TAG="";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);

        user_email_login = findViewById(R.id.login_email_input);
        user_password_login = findViewById(R.id.login_password_input);
        login_button = findViewById(R.id.login_btn);
//        profile_btn_1 = findViewById(R.id.profile1_btn);
//        profile_btn_2 = findViewById(R.id.profile2_btn);
//        profile_btn_3 = findViewById(R.id.profile3_btn);
        register_link = findViewById(R.id.login_register_link);
        loginFace_btn = findViewById(R.id.face_logo);
        mAuth = FirebaseAuth.getInstance();

        loadingBar = new ProgressDialog(this);

        //link to send user to register activity--------------------------------
        register_link.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sendUserToRegisterActiity();
            }
        });
        //-----------------------------------------------------------------------

        //--------login button action--------------------------------------------
        login_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                user_login_method();
            }
        });
        //------------------------------------------------------------------------


        /*_________________________________login logo animation _____________________________________*/
        loginLogo=findViewById(R.id.login_logo);
        loginLogoAnim= AnimationUtils.loadAnimation(this ,R.anim.logo_anim);
        loginLogo.startAnimation(loginLogoAnim);
        /*_________________________________loginBy layout animation _____________________________________*/
        loginByLayout=findViewById(R.id.loginBy_layout);
        translateAnim= AnimationUtils.loadAnimation(this ,R.anim.translat_anim);
        loginByLayout.startAnimation(translateAnim);

        /*_________________________________________________________________________________________________*/
        loginFace_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWithFace();
            }
        });
        //link to send user to Profiles activity--------------------------------
//        profile_btn_1.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                sendUserToProfile1Actiity();
//            }
//        });
//        profile_btn_2.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                sendUserToProfile2Actiity();
//            }
//        });
//        profile_btn_3.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                sendUserToProfile3Actiity();
//            }
//        });
        //-----------------------------------------------------------------------
    }
    //-------------facebook login method-----------------------------//////////////////////////////////////////////////////

    // login with facebook
    public void loginWithFace(){
        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions(Login_Activity.this, Arrays.asList("email","public_profile"));
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            updateUI();
        }
    }

    private void updateUI() {
        sendUserToProfile3Actiity();
        Toast.makeText(getApplicationContext(),"you are logged in now",Toast.LENGTH_LONG).show();
    }
    private void handleFacebookAccessToken(AccessToken token) {
     //   Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                    //        Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                          //  Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
    //-------------user login method-----------------------------
    private void user_login_method()
    {
        String email = user_email_login.getText().toString();
        String password = user_password_login.getText().toString();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "please fill email feild", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "please fill password feild", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Login to Account");
            loadingBar.setMessage("please wait until you login to your account...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            mAuth.signInWithEmailAndPassword(email , password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if(task.isSuccessful())
                            {
                                FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            user_class user1 = dataSnapshot.getValue(user_class.class);
                                            int type = user1.getUser_type();
                                            if (type==1){
                                                Intent intent = new Intent(Login_Activity.this, AdminActivity.class);
                                                startActivity(intent);
                                                finish();

                                            }
                                          else  if ( user1.getIsblocked().equals("true")) {
                                                Toast.makeText(Login_Activity.this,"You Are Blocked ",Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                sendUserToMapActivity();

                                                Toast.makeText(Login_Activity.this, "you are logged in successfully ", Toast.LENGTH_SHORT).show();
                                                loadingBar.dismiss();
                                            }

                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }
                            else
                            {
                                String message = task.getException().getMessage();
                                Toast.makeText(Login_Activity.this, "error occured : "+message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });
        }

    }

    //-------------send user to login activity ---------------------
    private void sendUserToMainActivity()
    {
        Intent mainActivityIntent = new Intent(getApplicationContext() , MainActivity.class);
        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainActivityIntent);
        finish();
    }
    //--------------------------------------------------------------

    private void sendUserToMapActivity()
    {
        Intent MapActivityIntent = new Intent(getApplicationContext() , GoogleMapsActivity.class);
        MapActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(MapActivityIntent);
        finish();
    }

    //-----------------------------------------------------------

    //-------------link to register Activity----------------------
    private  void sendUserToRegisterActiity()
    {
        Intent goToRegisterActivity = new Intent(getApplicationContext(),Register_Activity.class);
        startActivity(goToRegisterActivity);
        //finish();
    }
    //------------------------------------------------------------

//          Profiles -----------------------------------------------------------
    //-------------link to Profiles Activity----------------------
    private  void sendUserToProfile1Actiity()
    {
        Intent goToRegisterActivity = new Intent(getApplicationContext(),Profile1.class);
        startActivity(goToRegisterActivity);
    //    finish();
    }
    private  void sendUserToProfile2Actiity()
    {
        Intent goToRegisterActivity = new Intent(getApplicationContext(),Profile2.class);
        startActivity(goToRegisterActivity);
        //    finish();
    }
    private  void sendUserToProfile3Actiity()
    {
        Intent goToRegisterActivity = new Intent(getApplicationContext(),Profile3.class);
        startActivity(goToRegisterActivity);
            finish();
    }
    //------------------------------------------------------------

}
