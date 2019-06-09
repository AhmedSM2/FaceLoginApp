package com.example.test;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity
{
    private EditText profile_username , profile_email , profile_phone,profile_address ,profile_bio;
   // private CircleImageView user_pp;
    private Button profile_save_btn;


    private ProgressDialog loadingBar;
    final static int Gallery_pick = 1; // to used as second parametar in open phone gallery method
    private StorageReference userProfileImageRef; // to use to store the link of pp to firebase storage
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef ,areaRef;
    private double areaLat , areaLng;
    private Spinner choose_area;
    private String selectedArea ,currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);



        /*__________________________________________Intent data ________________________________*/

        Bundle bundle =getIntent().getExtras();
        final String userName=bundle.getString("username");
        final String Email=bundle.getString("email");
        final String Phone=bundle.getString("phone");
        final String Address=bundle.getString("address");
        final String Bio=bundle.getString("bio");
        /*_________________________________________________________________________________________*/

        profile_username = findViewById(R.id.edit_profile_username_id);
        profile_username.setText(userName);

        profile_email = findViewById(R.id.profile_edit_email_id);
        profile_email.setText(Email);

        profile_phone = findViewById(R.id.profile_edit_phone_id);
        profile_phone.setText(Phone);

        profile_address = findViewById(R.id.profile_edit_address_id);
        profile_address.setText(Address);

        profile_bio = findViewById(R.id.profile_edit_bio_id);
        profile_bio.setText(Bio);

        profile_save_btn = findViewById(R.id.save_edit_profile_btn);

        loadingBar = new ProgressDialog(this);


        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserID);

        //create folder to store images in firebase storage
       // userProfileImageRef = FirebaseStorage.getInstance().getReference().child("users prifile images");


        //---------button action to save user informatiom method ---------------------------

        profile_save_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                save_profile_user_info();
            }
        });

        //------------------------------------------------------------------------------------

        //-----button action to open phone gallery method -------------------------------------
//        user_pp.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                Intent openGallery = new Intent();
//                openGallery.setAction(Intent.ACTION_GET_CONTENT);
//                openGallery.setType("image/*");
//                startActivityForResult(openGallery , Gallery_pick);
//            }
//        });
        //--------------------------------------------------------------------------------------



        /*___________________________________ area spinner ____________________________________*/
        areaRef=FirebaseDatabase.getInstance().getReference().child("AbuEl3orifDB").child("Areas");
        choose_area=findViewById(R.id.area_spinner);
        final ArrayList<String> areaList=new ArrayList<>();

        areaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot areaSnapshot : dataSnapshot.getChildren())
                {
                    Area_class areaObj=areaSnapshot.getValue(Area_class.class);
                    String areaName=areaObj.getArea_name();
                    areaList.add(areaName);

                }

                ArrayAdapter<String> areaAdapter =new ArrayAdapter<String>(EditProfileActivity.this, android.R.layout.simple_spinner_item ,areaList);
                choose_area.setAdapter(areaAdapter);

                choose_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        selectedArea=areaList.get(position);
                        Toast.makeText(EditProfileActivity.this, ""+selectedArea, Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*__________________________________________________________________________________________*/



        //--------------------------------------------------------------------------------------
        //-----------to display the user profile image------------------------------------------
//        usersRef.addValueEventListener(new ValueEventListener()
//        {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
//            {
//                if(dataSnapshot.exists())
//                {
//                    final String userImage = dataSnapshot.child("profileImage").getValue().toString();
////                    Picasso.get()
////                            .load(userImage)
////                            .placeholder(R.drawable.profile_icon)
////                            .error(R.drawable.home_icon2)
////                            .resize(50, 50)
////                            .centerCrop()
////                            .networkPolicy(NetworkPolicy.NO_CACHE)
////                            .into(user_pp);
//                    Glide.with(getApplicationContext())
//                    .load(userImage)
//                    .apply(new RequestOptions()
//                    .placeholder(R.drawable.profile_icon)
//                    .fitCenter())
//                    .into(user_pp);
//
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError)
//            {
//
//            }
//        });
        //--------------------------------------------------------------------------------------
    }


    //---------------method of croping image-----------------------------------------------------
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode == Gallery_pick && resultCode == RESULT_OK && data != null)
//        {
//            Uri ImageUri = data.getData();
//
//            CropImage.activity()
//                    .setGuidelines(CropImageView.Guidelines.ON)
//                    .setAspectRatio(1,1)
//                    .start(this);
//        }
//
//        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
//        {
//            CropImage.ActivityResult result =CropImage.getActivityResult(data);
//
//            if(resultCode == RESULT_OK)
//            {
//                loadingBar.setTitle("profile image uploading");
//                loadingBar.setMessage("please wait until your profile image uploading complete...");
//                loadingBar.show();
//                loadingBar.setCanceledOnTouchOutside(true);
//
//                Uri resultUri = result.getUri();
//
//                StorageReference filePath = userProfileImageRef.child(currentUserID + ".jpg");
//
//                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>()
//                {
//                    @Override
//                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
//                    {
//                        if(task.isSuccessful())
//                        {
//                            Toast.makeText(EditProfileActivity.this, "profile image stored successfully to firebase storage", Toast.LENGTH_SHORT).show();
//
//                            final String downloadUrl =task.getResult().getStorage().getDownloadUrl().toString();
//                            usersRef.child("profileImage").setValue(downloadUrl)
//                                    .addOnCompleteListener(new OnCompleteListener<Void>()
//                                    {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task)
//                                        {
//                                            if(task.isSuccessful())
//                                            {
//                                                Intent selfSetupIntent = new Intent(getApplicationContext(), setup_user_Activity.class);
//                                                startActivity(selfSetupIntent);
//
//                                                Toast.makeText(EditProfileActivity.this, "profile image stored to firebase database successfully", Toast.LENGTH_SHORT).show();
//                                                loadingBar.dismiss();
//                                            }
//                                            else
//                                            {
//                                                String message = task.getException().getMessage();
//                                                Toast.makeText(EditProfileActivity.this, "Error occured : "+message, Toast.LENGTH_SHORT).show();
//                                                loadingBar.dismiss();
//                                            }
//                                        }
//                                    });
//                        }
//                    }
//                });
//            }
//            else
//            {
//                Toast.makeText(EditProfileActivity.this, "Error occured : Image can't cropped , try Again", Toast.LENGTH_SHORT).show();
//                loadingBar.dismiss();
//            }
//        }
//    }

    //-------------------------------------------------------------------------------------------

    private void save_profile_user_info()
    {
        String usernameStr = profile_username.getText().toString();
        String emailStr = profile_email.getText().toString();
        String phoneStr = profile_phone.getText().toString();
        String addressStr=profile_address.getText().toString();
        String bioStr=profile_bio.getText().toString();


        if(TextUtils.isEmpty(usernameStr))
        {
            Toast.makeText(this, "please fill username field", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(emailStr))
        {
            Toast.makeText(this, "please fill email field", Toast.LENGTH_SHORT).show();
        }


        else
        {
            loadingBar.setTitle("complete Edit profile");
            loadingBar.setMessage("please wait until your profile change complete...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);


            usersRef.child("username").setValue(usernameStr);

            usersRef.child("email").setValue(emailStr);

            usersRef.child("phone").setValue(phoneStr);

            usersRef.child("address").setValue(addressStr);

            usersRef.child("Bio").setValue(bioStr);

            usersRef.child("UserArea").setValue(selectedArea);

            loadingBar.dismiss();

            sendUserToProfileActivity();







//            user_class userClassObject = new user_class(usernameStr , emailStr , areaStr,phoneStr ,addressStr);
//            HashMap userMap = new HashMap();
//            userMap.put("username",userClassObject.getUser_username());
//            userMap.put("email",userClassObject.getEmail());
//            userMap.put("area",userClassObject.getArea());
//            userMap.put("phone",userClassObject.getPhone());
//            userMap.put("address",userClassObject.getAddress());
//
//
//            usersRef.updateChildren(userMap)
//                    .addOnCompleteListener(new OnCompleteListener()
//                    {
//                        @Override
//                        public void onComplete(@NonNull Task task)
//                        {
//                            if(task.isSuccessful())
//                            {
//                                sendUserToMapActivity();
//                                Toast.makeText(EditProfileActivity.this, "your account setup finished successfully", Toast.LENGTH_SHORT).show();
//                                loadingBar.dismiss();
//                            }
//                            else
//                            {
//                                String message = task.getException().getMessage();
//                                Toast.makeText(EditProfileActivity.this, "error occured : "+message, Toast.LENGTH_SHORT).show();
//                                loadingBar.dismiss();
//                            }
//                        }
//                    });




        }
    }

    //send user to main activity after account is situped-------------------------
    private void sendUserToMainActivity()
    {
        Intent mainActivityIntent = new Intent(getApplicationContext() , MainActivity.class);
        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainActivityIntent);
        finish();
    }

    private void sendUserToMapActivity()
    {
        Intent mapActivityIntent = new Intent(getApplicationContext() , GoogleMapsActivity.class);
        mapActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mapActivityIntent);
        finish();
    }
    private void sendUserToProfileActivity()
    {
        Intent profileActivityIntent = new Intent(getApplicationContext() , UserProfileActivity.class);
        //profileActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(profileActivityIntent);
       // finish();
    }
    //-----------------------------------------------------------------------------
//    @Override
//    public void onDestroy(){
//        super.onDestroy();
//        if ( loadingBar!=null && loadingBar.isShowing() ){
//            loadingBar.cancel();
//        }
//    }
}
