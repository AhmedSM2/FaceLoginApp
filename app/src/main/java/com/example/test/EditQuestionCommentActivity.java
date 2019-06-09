package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EditQuestionCommentActivity extends AppCompatActivity
{
   private Button btnSavedComment ;
   private DatabaseReference UserRef ,commentRef;
   private FirebaseAuth mAuth;
   private  String currentUserId;
   private ArrayList<String> savedCommentsList ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_question_comment);

        /*_________________________intent data_______________________*/
        Bundle bundle=getIntent().getExtras();
        final String commentId =bundle.getString("QuestionCommentKey");
        final String selectedArea =bundle.getString("SelectedArea");
        final String selectedCategory =bundle.getString("SelectedCategory");
        final  String questionId =bundle.getString("QuestionKey");
        /*___________________________________________________________________*/

        commentRef=FirebaseDatabase.getInstance().getReference().child("AbuEl3orifDB").child("Questions").child(selectedArea).child(selectedCategory).child("categoryQuestions").child(questionId).child("QuestionCategoryComments").child(commentId);

        UserRef= FirebaseDatabase.getInstance().getReference().child("users");
        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();

        /*________________________________________Save Comment ___________________________________*/

        savedCommentsList =new ArrayList<String>();

        btnSavedComment=findViewById(R.id.Button_save_comment);
        btnSavedComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!savedCommentsList.contains(commentId))
                {


                    commentRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String content =dataSnapshot.child("questionCommentContent").getValue().toString();
                            String date =dataSnapshot.child("questionCommentDate").getValue().toString();
                            // String disLikes =dataSnapshot.child("questionCommentDislikeCount").getValue().toString();
                            //String likes =dataSnapshot.child("questionCommentLikeCount").getValue().toString();
                            String time =dataSnapshot.child("questionCommentTime").getValue().toString();
                            // String userId =dataSnapshot.child("questionCommentUserId").getValue().toString();
                            String userName =dataSnapshot.child("questionCommentUserName").getValue().toString();
                            String pp =dataSnapshot.child("questionCommentUserPP").getValue().toString();



                            UserRef.child(currentUserId).child("SavedQuestionComment").child(commentId).child("questionCommentContent").setValue(content);
                            UserRef.child(currentUserId).child("SavedQuestionComment").child(commentId).child("questionCommentDate").setValue(date);
                            //UserRef.child(currentUserId).child("SavedQuestionComment").child("questionCommentDislikeCount").setValue(disLikes);
                            //UserRef.child(currentUserId).child("SavedQuestionComment").child("questionCommentLikeCount").setValue(likes);
                            UserRef.child(currentUserId).child("SavedQuestionComment").child(commentId).child("questionCommentTime").setValue(time);
                            //UserRef.child(currentUserId).child("SavedQuestionComment").child("questionCommentUserId").setValue(userId);
                            UserRef.child(currentUserId).child("SavedQuestionComment").child(commentId).child("questionCommentUserName").setValue(userName);
                            UserRef.child(currentUserId).child("SavedQuestionComment").child(commentId).child("questionCommentUserPP").setValue(pp);





                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    savedCommentsList.add(commentId);
                    Toast.makeText(EditQuestionCommentActivity.this, "New comment is saved ...", Toast.LENGTH_SHORT).show();



                }
                else {
                    Toast.makeText(EditQuestionCommentActivity.this, "this comment is already saved ..", Toast.LENGTH_SHORT).show();
                }

                sendUsertoQuestionCommentActicity();


            }
        });

        /*_______________________________________________________________________________________________________*/
    }

    private void sendUsertoQuestionCommentActicity() {
        Intent questionCommentActicity=new Intent(getApplicationContext() ,QuestionCommentsActivity.class);
        startActivity(questionCommentActicity);
    }
}
