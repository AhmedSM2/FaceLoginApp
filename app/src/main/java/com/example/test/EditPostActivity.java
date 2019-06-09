package com.example.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditPostActivity extends AppCompatActivity {
    private DatabaseReference mPost ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        final EditText editText = findViewById(R.id.editPost_text);
        Button button = findViewById(R.id.eidtPost_btn);
        mPost = FirebaseDatabase.getInstance().getReference().child("Post");
        Intent intent = getIntent();
        final String id = intent.getStringExtra("PostId");
        final String content = intent.getStringExtra("Content");
        editText.setText(content);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPost= editText.getText().toString();
                mPost.child(id).child("/content").setValue(newPost);
                sendUserToPostsActivity();
            }
        });
    }
    private  void sendUserToPostsActivity()
    {
        Intent editPostActivity = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(editPostActivity);
        finish();
    }
}
