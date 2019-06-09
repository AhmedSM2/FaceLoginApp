package com.example.test;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class NewPostActivity extends AppCompatActivity {




    private Toolbar newPostToolbar;
    private ImageView newPostImg;
    private EditText newPostTxt;
    private Button newPostBtn;
    private ProgressDialog loadingBar;
    private Uri postImgUri = null;
    private String saveCurrentDate, saveCurrentTime, postRandomName,profile_image, downloadUrl, current_user_id;
    private String Description,username;
    private DatabaseReference ComplainRef, PostsRef;
    private FirebaseAuth auth;
    private StorageReference firebaseStorage;
    private static final int MAX_LENGTH = 100;
    //  private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
//        progressBar = findViewById(R.id.new_Post_Progress);
//        progressBar.setVisibility(View.INVISIBLE);

        loadingBar = new ProgressDialog(this);
        loadingBar.setTitle("creating post..");
        loadingBar.setMessage("wait until saving your new post...");
        loadingBar.setCanceledOnTouchOutside(true);


        firebaseStorage = FirebaseStorage.getInstance().getReference();
        PostsRef = FirebaseDatabase.getInstance().getReference().child("Post");
        ComplainRef = FirebaseDatabase.getInstance().getReference().child("Complains");
        auth = FirebaseAuth.getInstance();


        current_user_id = auth.getCurrentUser().getUid();

        newPostToolbar = findViewById(R.id.new_Post_Toolbar);
//        setSupportActionBar(newPostToolbar);
       // getSupportActionBar().setTitle("Add New Post ");

        newPostImg = findViewById(R.id.new_Post_Img);
        newPostTxt = findViewById(R.id.new_Post_Txt);
        newPostBtn = findViewById(R.id.new_Post_Btn);

        newPostImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .setMinCropResultSize(512,512)
                        .start(NewPostActivity.this);
            }
        });
        newPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidatePostInfo();
            }
        });
    }

    private void ValidatePostInfo() {
        Description = newPostTxt.getText().toString();
        if(TextUtils.isEmpty(Description)&& postImgUri==null)
        {
            Toast.makeText(this, "no thing to save", Toast.LENGTH_SHORT).show();
            loadingBar.dismiss();
        }
        else if(!TextUtils.isEmpty(Description)&&postImgUri==null)
        {
            loadingBar.show();
            //progressBar.setVisibility(View.VISIBLE);
//Need Random Names For Images Name :
            Calendar calFordDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
            saveCurrentDate = currentDate.format(calFordDate.getTime());

            Calendar calFordTime = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
            saveCurrentTime = currentTime.format(calFordDate.getTime());

            postRandomName = saveCurrentDate + saveCurrentTime;

            HashMap<String,Object>postMap = new HashMap<>();
            /*FirebaseDatabase.getInstance().getReference("users")
                    .child(current_user_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    user_class user = dataSnapshot.getValue(user_class.class);
                   username= user.getUser_username();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });*/
            postMap.put("desc",Description);
            postMap.put("date", saveCurrentDate);
            postMap.put("time", saveCurrentTime);
            postMap.put("uid", current_user_id);


            PostsRef.push().setValue(postMap);
            Toast.makeText(NewPostActivity.this,"Saved ",Toast.LENGTH_LONG).show();
            goTOSecondMainActivity();
            // progressBar.setVisibility(View.INVISIBLE);
            loadingBar.dismiss();

        }else if(TextUtils.isEmpty(Description)&&postImgUri!=null)
        {
            Toast.makeText(this, "Can't Post Photo Only", Toast.LENGTH_SHORT).show();
            loadingBar.dismiss();

        }
        else
        {
            StoringImageToFirebaseStorage();
        }


//        else if(postImgUri==null){
//            Toast.makeText(NewPostActivity.this,"Shoudl Choose Image ",Toast.LENGTH_LONG).show();
//           // progressBar.setVisibility(View.INVISIBLE);
//            loadingBar.dismiss();
//
//        }
//        else if(TextUtils.isEmpty(Description)){
//            Toast.makeText(NewPostActivity.this,"Shoudl Enter Post",Toast.LENGTH_LONG).show();
//           // progressBar.setVisibility(View.INVISIBLE);
//            loadingBar.dismiss();
//
//        }
    }

    private void StoringImageToFirebaseStorage()
    {
        loadingBar.show();
        //progressBar.setVisibility(View.VISIBLE);
//Need Random Names For Images Name :
        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calFordDate.getTime());

        Calendar calFordTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currentTime.format(calFordDate.getTime());

        postRandomName = saveCurrentDate + saveCurrentTime;
        //  String randomNumber = random();
        //Add To Storage
        final StorageReference filePath = firebaseStorage.child("post_images").child(postImgUri.getLastPathSegment()+postRandomName +".jpg");
        filePath.putFile(postImgUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
        {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()){
                    String error = task.getException().getMessage();
                    Toast.makeText(NewPostActivity.this,"errprssQQQ"+error,Toast.LENGTH_LONG).show();
                }
                return filePath.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>()
        {
            @Override
            public void onComplete(@NonNull Task<Uri> task)
            {
                if (task.isSuccessful()){
                    String downloadImageUri = task.getResult().toString();
                    /*FirebaseDatabase.getInstance().getReference("users")
                            .child(current_user_id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            user_class user = dataSnapshot.getValue(user_class.class);
                            username= user.getUser_username();
                            profile_image = user.getImageUrl();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });**/
                    HashMap<String,Object>postMap = new HashMap<>();
                    postMap.put("image_uri",downloadImageUri);
                    //  postMap.put("content", contentOfComplain);
                    postMap.put("desc",Description);
                    postMap.put("date", saveCurrentDate);
                    postMap.put("time", saveCurrentTime);
                    postMap.put("uid", current_user_id);
                    // Log.e("sssssssssssqqqqqqqqqqq",current_user_id);
                    //                                postMap.put("user_id",1);
                    //    postMap.put("time_stamp",FieldValue.serverTimestamp());

                    PostsRef.push().setValue(postMap);
                    Toast.makeText(NewPostActivity.this,"Saved ",Toast.LENGTH_LONG).show();
                    goTOSecondMainActivity();
                    // progressBar.setVisibility(View.INVISIBLE);
                    loadingBar.dismiss();
                }
            }
        });
        /*
        String compKey = ComplainRef.push().getKey();
        Log.d("DDDDDDDDDDDDDDDDDD",compKey);

        ComplainRef.child(compKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    final String contentOfComplain = dataSnapshot.child("content").getValue().toString();
                    Log.d("DDDDDDDDDDDDDDDDDDssss",contentOfComplain);
                    String id = dataSnapshot.child("id").getValue().toString();
                  //  Log.d("DDDDDDDDDDDDDDDDDDaaaaaaa",id);
                    //Save TO Firebase storage :
                    filePath.putFile(postImgUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()){
                                String error = task.getException().getMessage();
                                Toast.makeText(NewPostActivity.this,"errprssQQQ"+error,Toast.LENGTH_LONG).show();
                            }
                            return filePath.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()){
                                String downloadImageUri = task.getResult().toString();
                                HashMap<String,Object>postMap = new HashMap<>();
                                postMap.put("image_uri",downloadImageUri);
                                 postMap.put("content", contentOfComplain);
                                postMap.put("desc",Description);
                                postMap.put("date", saveCurrentDate);
                                postMap.put("time", saveCurrentTime);
                                //                                postMap.put("user_id",1);
                                //    postMap.put("time_stamp",FieldValue.serverTimestamp());

                                PostsRef.push().setValue(postMap);
                                Toast.makeText(NewPostActivity.this,"Saved ",Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.INVISIBLE);

                            }
                        }
                    });
             }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        */




    }

    private void goTOSecondMainActivity() {
        Intent mainIntent = new Intent(NewPostActivity.this, MainSecondActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                postImgUri  = result.getUri();
                newPostImg.setImageURI(postImgUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(MAX_LENGTH);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
}
