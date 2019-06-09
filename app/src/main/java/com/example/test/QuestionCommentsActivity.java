package com.example.test;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class QuestionCommentsActivity extends AppCompatActivity
{
    private Toolbar update_q_comment_toolbar;
    private DatabaseReference userRef  , QuestionRef  ,CategoryQuestionsCommentsRef , ReactsRef ;
    private FirebaseAuth mAuth ;
    private String CurrentuserId , CommentuserPP , CommentuserUsername , QuestionCommentStringContent , currentQuestionCommentDate , currentQuestionCommentTime ,PostCommentRandomKey ,userArea ;
    private EditText questionCommentContent;
    private Button Question_comment_submit_btn;
    private ProgressDialog loadingBar;
    private RecyclerView Question_Comments_CategoryList;
    private FirebaseRecyclerAdapter<QuestionComments, QuestionsCommentViewHolder> adapter;
    public Context context;
    private CircleImageView q_Upp;
    Boolean likeChacker = false;
    Boolean dislikeChecker = false;
    int likeCounter = 0;
    int dislikeCounter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_comments);

//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_question);

        update_q_comment_toolbar = findViewById(R.id.update_question_comment_toolbar);
        Question_Comments_CategoryList = findViewById(R.id.all_users_category_questions_comments);
        Question_Comments_CategoryList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        Question_Comments_CategoryList.setLayoutManager(linearLayoutManager);

        userRef = FirebaseDatabase.getInstance().getReference().child("users");
        QuestionRef =FirebaseDatabase.getInstance().getReference().child("AbuEl3orifDB").child("Questions");
        ReactsRef = FirebaseDatabase.getInstance().getReference().child("AbuEl3orifDB").child("QuestionReacts");
        mAuth = FirebaseAuth.getInstance();
        CurrentuserId = mAuth.getCurrentUser().getUid();

        questionCommentContent = findViewById(R.id.add_question_comment_content);
        Question_comment_submit_btn = findViewById(R.id.add_question_comment_btn);
        loadingBar = new ProgressDialog(this);


//        //---------------------INTENT DATA-------------------------
//        Bundle bundle=getIntent().getExtras();
//        final String selectedArea = bundle.getString("SELAREA");
//        final String selectedCategory = bundle.getString("SELCATEGORY");

        //---------------------INTENT DATA-------------------------
        Bundle bundle=getIntent().getExtras();
        final String selectedArea = bundle.getString("SelectedArea");
        final String selectedCategory = bundle.getString("SelectedCategory");
        final String QuestionKey = bundle.getString("QuestionKey");



        //---------------------------------------------------------
        CategoryQuestionsCommentsRef = FirebaseDatabase.getInstance().getReference().child("AbuEl3orifDB").child("Questions").child(selectedArea).child(selectedCategory).child("categoryQuestions").child(QuestionKey).child("QuestionCategoryComments");


        Question_comment_submit_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //SaveQuestionMethod();

                Calendar calendarForDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
                currentQuestionCommentDate = currentDate.format(calendarForDate.getTime());

                Calendar calendarForTime = Calendar.getInstance();
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
                currentQuestionCommentTime = currentTime.format(calendarForTime.getTime());

                QuestionCommentStringContent =questionCommentContent.getText().toString();

                PostCommentRandomKey = currentQuestionCommentDate + currentQuestionCommentTime ;

                if(TextUtils.isEmpty(QuestionCommentStringContent))
                {
                    Toast.makeText(QuestionCommentsActivity.this, "comment Content is Empty..", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    userRef.child(CurrentuserId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            userArea = dataSnapshot.child("UserArea").getValue().toString();
                            if(userArea.equals(selectedArea))
                            {

                                HashMap QuestionMap = new HashMap();
                                QuestionMap.put("questionCommentUserId" , CurrentuserId);
                                QuestionMap.put("questionCommentUserName" , CommentuserUsername);
                                QuestionMap.put("questionCommentUserPP" , CommentuserPP);
                                QuestionMap.put("questionCommentDate" , currentQuestionCommentDate);
                                QuestionMap.put("questionCommentTime" , currentQuestionCommentTime);
                                QuestionMap.put("questionCommentContent" , QuestionCommentStringContent);
                                QuestionMap.put("questionCommentLikeCount" , likeCounter);
                                QuestionMap.put("questionCommentDislikeCount" , dislikeCounter);


                                loadingBar.setTitle("Add Comment...");
                                loadingBar.setMessage("please wait until your comment saving complete...");
                                loadingBar.show();
                                loadingBar.setCanceledOnTouchOutside(true);

                                QuestionRef.child(selectedArea).child(selectedCategory).child("categoryQuestions").child(QuestionKey).child("QuestionCategoryComments").child(CurrentuserId+"@"+PostCommentRandomKey).updateChildren(QuestionMap).addOnCompleteListener(new OnCompleteListener()
                                {
                                    @Override
                                    public void onComplete(@NonNull Task task)
                                    {
                                        if(task.isSuccessful())
                                        {
                                            questionCommentContent.setText("");
                                            //SendUserToQuestionsActivity();

                                            Toast.makeText(QuestionCommentsActivity.this, "Comment is stored Successfully...", Toast.LENGTH_SHORT).show();
                                            loadingBar.dismiss();
                                        }
                                        else
                                        {
                                            Toast.makeText(QuestionCommentsActivity.this, "Error occuring during saving Comment", Toast.LENGTH_SHORT).show();
                                            loadingBar.dismiss();
                                        }

                                    }
                                });

                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Sorry ! you can't replay in this area !! ", Toast.LENGTH_SHORT).show();
                            }






                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });



                }

            }
        });

        setSupportActionBar(update_q_comment_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Add Comment");


        //====ref to users node======//
        userRef.child(CurrentuserId).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    CommentuserPP = dataSnapshot.child("profileImage").getValue().toString();
                    CommentuserUsername = dataSnapshot.child("username").getValue().toString();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        //==================================QUESTION LIST APPEAR HERE========================================//
//        FirebaseRecyclerAdapter<Questions , QuestionsViewHolder> firebaseRecyclerAdapter =
//                new FirebaseRecyclerAdapter<Questions, QuestionsViewHolder>
//                        (
//                                Questions.class,
//                                R.layout.all_questions_layout,
//                                QuestionsViewHolder.class,
//                                CategoryQuestions
//                        )
//                {
//                    @Override
//                    protected void onBindViewHolder(@NonNull QuestionsViewHolder holder, int position, @NonNull Questions model)
//                    {
//
//                    }
//
//                    @NonNull
//                    @Override
//                    public QuestionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
//                    {
//                        return null;
//                    }
//                };
        //=====================Dispaly All Users here====================================//

        //        FirebaseRecyclerOptions<Questions> options =
//                new FirebaseRecyclerOptions.Builder<Questions>()
//                        .setQuery(CategoryQuestionsRef, Questions.class)
//                        .build();

        //DisplayAllUserQuestions();

        FirebaseRecyclerOptions<QuestionComments> options = new FirebaseRecyclerOptions.Builder<QuestionComments>()
                .setQuery(CategoryQuestionsCommentsRef.orderByChild("questionCommentLikeCount"), new SnapshotParser<QuestionComments>()
                {
                    @NonNull
                    @Override
                    public QuestionComments parseSnapshot(@NonNull DataSnapshot snapshot)
                    {
                        QuestionComments questionComments = new QuestionComments(snapshot.child("questionCommentUserName").getValue().toString() ,snapshot.child("questionCommentTime").getValue().toString()  , snapshot.child("questionCommentDate").getValue().toString() ,snapshot.child("questionCommentContent").getValue().toString() ,  snapshot.child("questionCommentUserPP").getValue().toString());
                        return questionComments;

                    }
                })
                .build();

        adapter = new FirebaseRecyclerAdapter<QuestionComments, QuestionsCommentViewHolder>(options)
        {


            @Override
            protected void onBindViewHolder(@NonNull QuestionsCommentViewHolder holder, int position, @NonNull QuestionComments model)
            {
                final String QuestionCommentKey = getRef(position).getKey();

                holder.setQuestion_comment_uFullName(model.question_comment_uFullName);
                holder.setQuestion_comment_time(model.question_comment_time);
                holder.setQuestion_comment_date(model.question_comment_date);
                holder.setQuestion_comments_description(model.question_comments_description);

                String imageUri = model.question_comment_uPP;
                holder.setQuestion_comment_uPP(imageUri);

                //======Reacts Status==================//
                holder.setQuestionReactsStatus(QuestionKey,QuestionCommentKey , selectedArea , selectedCategory);

                //================when click the quetion=================//
                holder.mView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent ClickQuestionCommentIntent = new Intent(QuestionCommentsActivity.this , EditQuestionCommentActivity.class);
                        ClickQuestionCommentIntent.putExtra("QuestionCommentKey" , QuestionCommentKey);
                        ClickQuestionCommentIntent.putExtra("SelectedArea" , selectedArea);
                        ClickQuestionCommentIntent.putExtra("SelectedCategory", selectedCategory);
                        ClickQuestionCommentIntent.putExtra("QuestionKey", QuestionKey);

                        startActivity(ClickQuestionCommentIntent);

                    }
                });

                //====like btn=================
                holder.like_Question_btn.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        likeChacker = true;

                        ReactsRef.addValueEventListener(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                            {
                                if(likeChacker.equals(true))
                                {
                                    if(!dataSnapshot.child("QuestionCommentLikes").child(QuestionKey).child(QuestionCommentKey).hasChild(CurrentuserId) && !dataSnapshot.child("QuestionCommentDisLikes").child(QuestionKey).child(QuestionCommentKey).hasChild(CurrentuserId))
                                    {
                                        ReactsRef.child("QuestionCommentLikes").child(QuestionKey).child(QuestionCommentKey).child(CurrentuserId).setValue(true);
                                        // like_Question_btn.setColorFilter(getResources().getColor(R.color.colorLike));
                                        likeChacker = false;

                                    }

                                    else if(dataSnapshot.child("QuestionCommentLikes").child(QuestionKey).child(QuestionCommentKey).hasChild(CurrentuserId) && !dataSnapshot.child("QuestionCommentDisLikes").child(QuestionKey).child(QuestionCommentKey).hasChild(CurrentuserId))
                                    {
                                        ReactsRef.child("QuestionCommentLikes").child(QuestionKey).child(QuestionCommentKey).child(CurrentuserId).removeValue();
                                        likeChacker = false;

                                    }

                                    else if(!dataSnapshot.child("QuestionCommentLikes").child(QuestionKey).child(QuestionCommentKey).hasChild(CurrentuserId) && dataSnapshot.child("QuestionCommentDisLikes").child(QuestionKey).child(QuestionCommentKey).hasChild(CurrentuserId))
                                    {
                                        ReactsRef.child("QuestionCommentDisLikes").child(QuestionKey).child(QuestionCommentKey).child(CurrentuserId).removeValue();
                                        ReactsRef.child("QuestionCommentLikes").child(QuestionKey).child(QuestionCommentKey).child(CurrentuserId).setValue(true);

                                        likeChacker = false;
                                        dislikeChecker = false ;

                                    }
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError)
                            {

                            }
                        });

                    }
                });

                //====Dislike btn=================
                holder.dislike_Question_btn.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        dislikeChecker = true;

                        ReactsRef.addValueEventListener(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                            {
                                if(dislikeChecker.equals(true))
                                {
                                    if(!dataSnapshot.child("QuestionCommentLikes").child(QuestionKey).child(QuestionCommentKey).hasChild(CurrentuserId) && !dataSnapshot.child("QuestionCommentDisLikes").child(QuestionKey).child(QuestionCommentKey).hasChild(CurrentuserId))
                                    {
                                        ReactsRef.child("QuestionCommentDisLikes").child(QuestionKey).child(QuestionCommentKey).child(CurrentuserId).setValue(true);
                                        dislikeChecker = false;

                                    }

                                    else if(!dataSnapshot.child("QuestionCommentLikes").child(QuestionKey).child(QuestionCommentKey).hasChild(CurrentuserId) && dataSnapshot.child("QuestionCommentDisLikes").child(QuestionKey).child(QuestionCommentKey).hasChild(CurrentuserId))
                                    {
                                        ReactsRef.child("QuestionCommentDisLikes").child(QuestionKey).child(QuestionCommentKey).child(CurrentuserId).removeValue();
                                        dislikeChecker = false;

                                    }

                                    else if(dataSnapshot.child("QuestionCommentLikes").child(QuestionKey).child(QuestionCommentKey).hasChild(CurrentuserId) && !dataSnapshot.child("QuestionCommentDisLikes").child(QuestionKey).child(QuestionCommentKey).hasChild(CurrentuserId))
                                    {
                                        ReactsRef.child("QuestionCommentDisLikes").child(QuestionKey).child(QuestionCommentKey).child(CurrentuserId).setValue(true);
                                        ReactsRef.child("QuestionCommentLikes").child(QuestionKey).child(QuestionCommentKey).child(CurrentuserId).removeValue();

                                        likeChacker = false;
                                        dislikeChecker = false ;

                                    }
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError)
                            {

                            }
                        });

                    }
                });

            }

            @NonNull
            @Override
            public QuestionCommentsActivity.QuestionsCommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.all_question_comments_layout, parent, false);
                context = parent.getContext();

                return new QuestionCommentsActivity.QuestionsCommentViewHolder(view);
            }



        };


        Question_Comments_CategoryList.setAdapter(adapter);

        //===================================================================================================
        //================================================================================
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        adapter.startListening();

    }

    @Override
    protected void onStop()
    {
        super.onStop();

        adapter.stopListening();
    }

    public class QuestionsCommentViewHolder extends RecyclerView.ViewHolder
    {

        View mView;

        ImageButton like_Question_btn , dislike_Question_btn , comments_Question_btn;
        TextView likecount , dislikeCount , commentsCount;
        String current_uid;
        DatabaseReference ReactsRef2 ,QuestionRef2 ;

        public QuestionsCommentViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mView = itemView;

            like_Question_btn = (ImageButton) mView.findViewById(R.id.like_icon_btn);
            dislike_Question_btn =(ImageButton) mView.findViewById(R.id.dislike_icon_btn);
            comments_Question_btn = (ImageButton) mView.findViewById(R.id.comments_icon_btn);
            likecount = (TextView) mView.findViewById(R.id.likes_count);
            dislikeCount = (TextView) mView.findViewById(R.id.dislikes_count);
            commentsCount = (TextView) mView.findViewById(R.id.Question_comments_count);
            QuestionRef2 =FirebaseDatabase.getInstance().getReference().child("AbuEl3orifDB").child("Questions");
            ReactsRef2 = FirebaseDatabase.getInstance().getReference().child("AbuEl3orifDB").child("QuestionReacts");
            current_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        }

        public void setQuestionReactsStatus(final String QuestionKey , final String QuestionCommentKey , final String SelArea , final String SelCategory)
        {
            ReactsRef2.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if(!dataSnapshot.child("QuestionCommentLikes").child(QuestionKey).child(QuestionCommentKey).hasChild(CurrentuserId) && !dataSnapshot.child("QuestionCommentDisLikes").child(QuestionKey).child(QuestionCommentKey).hasChild(CurrentuserId))
                    {
                        likeCounter = (int) dataSnapshot.child("QuestionCommentLikes").child(QuestionKey).child(QuestionCommentKey).getChildrenCount();
                        dislikeCounter = (int) dataSnapshot.child("QuestionCommentDisLikes").child(QuestionKey).child(QuestionCommentKey).getChildrenCount();
                        like_Question_btn.setColorFilter(getResources().getColor(R.color.DefaultColorLike));
                        dislike_Question_btn.setColorFilter(getResources().getColor(R.color.DefaultColorLike));
                        likecount.setText(Integer.toString(likeCounter));
                        dislikeCount.setText(Integer.toString(dislikeCounter));
                        QuestionRef2.child(SelArea).child(SelCategory).child("categoryQuestions").child(QuestionKey).child("QuestionCategoryComments").child(QuestionCommentKey).child("questionCommentLikeCount").setValue(likeCounter);
                        QuestionRef2.child(SelArea).child(SelCategory).child("categoryQuestions").child(QuestionKey).child("QuestionCategoryComments").child(QuestionCommentKey).child("questionCommentDislikeCount").setValue(dislikeCounter);


                    }

                    else if(!dataSnapshot.child("QuestionCommentLikes").child(QuestionKey).child(QuestionCommentKey).hasChild(CurrentuserId) && dataSnapshot.child("QuestionCommentDisLikes").child(QuestionKey).child(QuestionCommentKey).hasChild(CurrentuserId))
                    {
                        likeCounter = (int) dataSnapshot.child("QuestionCommentLikes").child(QuestionKey).child(QuestionCommentKey).getChildrenCount();
                        dislikeCounter = (int) dataSnapshot.child("QuestionCommentDisLikes").child(QuestionKey).child(QuestionCommentKey).getChildrenCount();
                        like_Question_btn.setColorFilter(getResources().getColor(R.color.DefaultColorLike));
                        dislike_Question_btn.setColorFilter(getResources().getColor(R.color.colorDislike));
                        likecount.setText(Integer.toString(likeCounter));
                        dislikeCount.setText(Integer.toString(dislikeCounter));
                        QuestionRef2.child(SelArea).child(SelCategory).child("categoryQuestions").child(QuestionKey).child("QuestionCategoryComments").child(QuestionCommentKey).child("questionCommentLikeCount").setValue(likeCounter);
                        QuestionRef2.child(SelArea).child(SelCategory).child("categoryQuestions").child(QuestionKey).child("QuestionCategoryComments").child(QuestionCommentKey).child("questionCommentDislikeCount").setValue(dislikeCounter);


                    }

                    else if(dataSnapshot.child("QuestionCommentLikes").child(QuestionKey).child(QuestionCommentKey).hasChild(CurrentuserId) && !dataSnapshot.child("QuestionCommentDisLikes").child(QuestionKey).child(QuestionCommentKey).hasChild(CurrentuserId))
                    {
                        likeCounter = (int) dataSnapshot.child("QuestionCommentLikes").child(QuestionKey).child(QuestionCommentKey).getChildrenCount();
                        dislikeCounter = (int) dataSnapshot.child("QuestionCommentDisLikes").child(QuestionKey).child(QuestionCommentKey).child(QuestionCommentKey).getChildrenCount();
                        like_Question_btn.setColorFilter(getResources().getColor(R.color.colorLike));
                        dislike_Question_btn.setColorFilter(getResources().getColor(R.color.DefaultColorLike));
                        likecount.setText(Integer.toString(likeCounter));
                        dislikeCount.setText(Integer.toString(dislikeCounter));
                        QuestionRef2.child(SelArea).child(SelCategory).child("categoryQuestions").child(QuestionKey).child("QuestionCategoryComments").child(QuestionCommentKey).child("questionCommentLikeCount").setValue(likeCounter);
                        QuestionRef2.child(SelArea).child(SelCategory).child("categoryQuestions").child(QuestionKey).child("QuestionCategoryComments").child(QuestionCommentKey).child("questionCommentDislikeCount").setValue(dislikeCounter);


                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {

                }
            });
        }

        public void setQuestion_comment_uFullName(String question_c_uFullName)
        {
            TextView q_c_UfullName = (TextView) mView.findViewById(R.id.Question_username);
            q_c_UfullName.setText(question_c_uFullName);
        }

        public void setQuestion_comment_time(String question_c_time)
        {
            TextView q_c_Time = (TextView) mView.findViewById(R.id.Question_time);
            q_c_Time.setText("  " + question_c_time);
        }

        public void setQuestion_comment_date(String question_c_date)
        {
            TextView q_c_Date = (TextView) mView.findViewById(R.id.Question_date);
            q_c_Date.setText("  " + question_c_date);
        }

        public void setQuestion_comments_description(String question_c_description)
        {
            TextView q_c_Description = (TextView) mView.findViewById(R.id.Question_description);
            q_c_Description.setText(question_c_description);
        }

        public void setQuestion_comment_uPP(String question_c_uPP)
        {
            q_Upp = mView.findViewById(R.id.Question_user_pp);
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.profile_icon);

            //Picasso.with(ctx).load(question_uPP).into(q_Upp);
            Glide.with(context).applyDefaultRequestOptions(requestOptions).load(question_c_uPP).into(q_Upp);
            // Picasso.with(ctx).load(question_uPP).placeholder(R.drawable.profile_icon).into(q_Upp);
        }


    }

    private void SaveQuestionMethod()
    {
//        Calendar calendarForDate = Calendar.getInstance();
//        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
//        currentQuestionDate = currentDate.format(calendarForDate.getTime());
//
//        Calendar calendarForTime = Calendar.getInstance();
//        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
//        currentQuestionTime = currentTime.format(calendarForTime.getTime());
//
//        QuestionStringContent =questionContent.getText().toString();
//
//        PostRandomKey = currentQuestionDate + currentQuestionTime ;
//
//
//
//        if(TextUtils.isEmpty(QuestionStringContent))
//        {
//            Toast.makeText(this, "Quetion Content is Empty..", Toast.LENGTH_SHORT).show();
//        }
//        else
//        {
//            HashMap QuestionMap = new HashMap();
//            QuestionMap.put("questionUserId" , CurrentuserId);
//            QuestionMap.put("questionUserName" , userUsername);
//            QuestionMap.put("questionUserPP" , userPP);
//            QuestionMap.put("questionDate" , currentQuestionDate);
//            QuestionMap.put("questionTime" , currentQuestionTime);
//            QuestionMap.put("questionContent" , QuestionStringContent);
//
//            loadingBar.setTitle("Asking Question...");
//            loadingBar.setMessage("please wait until your question saving complete...");
//            loadingBar.show();
//            loadingBar.setCanceledOnTouchOutside(true);
//
//            QuestionRef.child(selectedArea).child(selectedCategory).child("categoryQuestions").child(CurrentuserId + PostRandomKey).updateChildren(QuestionMap).addOnCompleteListener(new OnCompleteListener()
//            {
//                @Override
//                public void onComplete(@NonNull Task task)
//                {
//                    if(task.isSuccessful())
//                    {
//                        SendUserToQuestionsActivity();
//                        Toast.makeText(QuestionActivity.this, "Question is stored Successfully...", Toast.LENGTH_SHORT).show();
//                        loadingBar.dismiss();
//                    }
//                    else
//                    {
//                        Toast.makeText(QuestionActivity.this, "Error occuring during saving question", Toast.LENGTH_SHORT).show();
//                        loadingBar.dismiss();
//                    }
//
//                }
//            });
//
//
//        }
    }

    private void SendUserToQuestionCommentsActivity()
    {
        Intent QuestionIntent = new Intent(QuestionCommentsActivity.this , QuestionCommentsActivity.class);
        startActivity(QuestionIntent);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(id == android.R.id.home)
        {
            SendUserToEditQuestionActivtity();
        }
        return super.onOptionsItemSelected(item);
    }

    private void SendUserToEditQuestionActivtity()
    {
        Intent EditQuetion_intent = new Intent(QuestionCommentsActivity.this , EditQuestionActivity.class);
        startActivity(EditQuetion_intent);

    }
}

