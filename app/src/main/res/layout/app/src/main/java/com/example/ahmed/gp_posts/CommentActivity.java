package com.example.ahmed.gp_posts;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class CommentActivity extends AppCompatActivity {
    private ImageButton post_comment;
    private EditText comment_input;
    private RecyclerView commentList;
    private DatabaseReference UserRef,PostRef;
    private String Post_key,CurrentUser_id;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        PostRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(Post_key).child("comments");
    post_comment =(ImageButton)findViewById(R.id.post_comment_btn);
    comment_input = (EditText) findViewById(R.id.comment_input);
    commentList = (RecyclerView) findViewById(R.id.comments_list);
Post_key = getIntent().getExtras().get("PostKey").toString();
mAuth = FirebaseAuth.getInstance();
CurrentUser_id = mAuth.getCurrentUser().getUid();


    commentList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        commentList.setLayoutManager(linearLayoutManager);

        post_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                UserRef.child(CurrentUser_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            String userName = dataSnapshot.child("username").getValue().toString();
                            ValidateComment(userName);
                            comment_input.setText("");

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

    }

    private void ValidateComment(String userName) {
        String commentText = comment_input.getText().toString();
        if (TextUtils.isEmpty(commentText)){
            Toast.makeText(this,"Please write comment",Toast.LENGTH_SHORT).show();

        }
        else{
            Calendar calFordDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
           final String saveCurrentDate = currentDate.format(calFordDate.getTime());

            Calendar calFordTime = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
          final String  saveCurrentTime = currentTime.format(calFordDate.getTime());
        final String RandomKey = CurrentUser_id + saveCurrentDate + saveCurrentTime;
            HashMap commentMap = new HashMap();
            commentMap.put("Uid",CurrentUser_id);
            commentMap.put("comment",commentText);
            commentMap.put("date",saveCurrentDate);
            commentMap.put("time",saveCurrentTime);
            commentMap.put("Username",userName);
            PostRef.child(RandomKey).updateChildren(commentMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()){
                        Toast.makeText(CommentActivity.this,"You have commented successfully",Toast.LENGTH_SHORT).show();

                    }
                    else {
                        Toast.makeText(CommentActivity.this,"Error",Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
}
