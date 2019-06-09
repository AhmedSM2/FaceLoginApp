package com.example.test;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditQuestionActivity extends AppCompatActivity
{
    //private String QuestionKey ,SelectedArea , SelectedCategory ;
    private DatabaseReference QuestionCategoryRef;
    private CircleImageView userQuestionPP;
    private TextView userQuestionName , QuestionDate , QuestionTime , QuestionDescription;
    //private EditText QuestionEditContent;
    private Button DeleteQuestionButton , EditQuestionButton , showCommentsQuestionButton , CancelQuestionButton;
    private FirebaseAuth mAuth;
    private String currentUserId , DBuserId , UserPP , UserName , QuestionDateStr , QuestionTimeStr , QuestionDescriptionStr ;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_question);

        userQuestionPP = findViewById(R.id.Question_user_pp_edit);
        userQuestionName = findViewById(R.id.Question_username_edit);
        QuestionDate = findViewById(R.id.Question_date_edit);
        QuestionTime = findViewById(R.id.Question_time_edit);
        QuestionDescription = findViewById(R.id.Question_description_edit);
        //QuestionEditContent = findViewById(R.id.question_edit_content);
        DeleteQuestionButton = findViewById(R.id.Button_delete_updated_quetion);
        EditQuestionButton = findViewById(R.id.Button_save_updated_quetion);
        showCommentsQuestionButton = findViewById(R.id.Button_comments_updated_quetion);
        CancelQuestionButton = findViewById(R.id.Button_cancel_updated_quetion);

        EditQuestionButton.setVisibility(View.INVISIBLE);
        DeleteQuestionButton.setVisibility(View.INVISIBLE);



        //---------------------INTENT DATA-------------------------
        Bundle bundle=getIntent().getExtras();
        final String selectedArea = bundle.getString("SelectedArea");
        final String selectedCategory = bundle.getString("SelectedCategory");
        final String QuestionKey = bundle.getString("QuestionKey");

        QuestionCategoryRef = FirebaseDatabase.getInstance().getReference().child("AbuEl3orifDB").child("Questions").child(selectedArea).child(selectedCategory).child("categoryQuestions").child(QuestionKey);
        mAuth = FirebaseAuth.getInstance();

        currentUserId = mAuth.getCurrentUser().getUid();


        QuestionCategoryRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                     UserPP = dataSnapshot.child("questionUserPP").getValue().toString();
                     UserName = dataSnapshot.child("questionUserName").getValue().toString();
                     QuestionDateStr = dataSnapshot.child("questionDate").getValue().toString();
                     QuestionTimeStr= dataSnapshot.child("questionTime").getValue().toString();
                     QuestionDescriptionStr = dataSnapshot.child("questionContent").getValue().toString();
                    DBuserId = dataSnapshot.child("questionUserId").getValue().toString();

                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.placeholder(R.drawable.profile_icon);
                    //Picasso.with(ctx).load(question_uPP).into(q_Upp);
                    Glide.with(getApplicationContext()).applyDefaultRequestOptions(requestOptions).load(UserPP).into(userQuestionPP);

                    userQuestionName.setText(UserName);
                    QuestionDate.setText("  "+QuestionDateStr);
                    QuestionTime.setText("  "+QuestionTimeStr);
                    QuestionDescription.setText(QuestionDescriptionStr);
                   // QuestionEditContent.setText(QuestionDescriptionStr);

                    if(currentUserId.equals(DBuserId))
                    {
                        EditQuestionButton.setVisibility(View.VISIBLE);
                        DeleteQuestionButton.setVisibility(View.VISIBLE);

                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

        DeleteQuestionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DeleteCurrentQuestion();
                SendUserToQuestionsActivity();
                Toast.makeText(EditQuestionActivity.this, "Question Deleted", Toast.LENGTH_SHORT).show();
            }
        });

        EditQuestionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                EditCurrentQuestion(QuestionDescriptionStr);
            }
        });

        CancelQuestionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SendUserToQuestionsActivity();
            }
        });

        showCommentsQuestionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent QuestionCommentsIntent = new Intent(EditQuestionActivity.this , QuestionCommentsActivity.class);
                QuestionCommentsIntent.putExtra("QuestionKey" , QuestionKey);
                QuestionCommentsIntent.putExtra("SelectedArea" , selectedArea);
                QuestionCommentsIntent.putExtra("SelectedCategory", selectedCategory);
                startActivity(QuestionCommentsIntent);
            }
        });
    }



    private void EditCurrentQuestion(String questionDescriptionStr)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditQuestionActivity.this);
        builder.setTitle("Edit Question");

        final EditText inputFeild = new EditText(EditQuestionActivity.this);
        inputFeild.setText(questionDescriptionStr);
        builder.setView(inputFeild);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                QuestionCategoryRef.child("questionContent").setValue(inputFeild.getText().toString());
                Toast.makeText(EditQuestionActivity.this, "Question has been Updated", Toast.LENGTH_SHORT).show();

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();

            }
        });

        Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.holo_blue_bright);

    }

    private void DeleteCurrentQuestion()
    {
        QuestionCategoryRef.removeValue();

    }

    private void SendUserToQuestionsActivity()
    {
        Intent QuestionIntent = new Intent(EditQuestionActivity.this , QuestionActivity.class);
        startActivity(QuestionIntent);
        finish();

    }


}
