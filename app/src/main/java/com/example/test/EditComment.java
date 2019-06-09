package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditComment extends AppCompatActivity {
    private DatabaseReference mComment ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_comment);
        Button button = findViewById(R.id.eidtComment_btn);
        final EditText editText = findViewById(R.id.editComment_text);

        Intent intent = getIntent();
        final String id = intent.getStringExtra("CommentId");
        final String content = intent.getStringExtra("CommentContent");
        final String post_id = intent.getStringExtra("PostId");
        mComment = FirebaseDatabase.getInstance().getReference().child("Post/"+post_id).child("Comment");


        editText.setText(content);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newComment= editText.getText().toString();
                mComment.child(id).child("/comment_body").setValue(newComment);
                finish();
                sendUserToPCommentsActivity();
            }
        });
    }
    private  void sendUserToPCommentsActivity()
    {
        Intent editCommentActivity = new Intent(getApplicationContext(),PostsActivity.class);
        startActivity(editCommentActivity);
    }
}
