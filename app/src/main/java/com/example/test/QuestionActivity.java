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

public class QuestionActivity extends AppCompatActivity
{
    private Toolbar update_q_toolbar;
    private DatabaseReference userRef  , QuestionRef  ,CategoryQuestionsRef , ReactsRef ;
    private FirebaseAuth mAuth ;
    private String CurrentuserId , userPP , userUsername , QuestionStringContent , currentQuestionDate , currentQuestionTime ,PostRandomKey ;
    private EditText questionContent;
    private Button Question_submit_btn;
    private ProgressDialog loadingBar;
    private RecyclerView QuestionCategoryList;
    private FirebaseRecyclerAdapter<Questions, QuestionsViewHolder> adapter;
    public Context context;
    private  CircleImageView q_Upp;
    Boolean likeChacker = false;
    Boolean dislikeChecker = false;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        update_q_toolbar = findViewById(R.id.update_question_toolbar);
        QuestionCategoryList = findViewById(R.id.all_users_category_questions);
        QuestionCategoryList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        QuestionCategoryList.setLayoutManager(linearLayoutManager);

        userRef = FirebaseDatabase.getInstance().getReference().child("users");
        QuestionRef =FirebaseDatabase.getInstance().getReference().child("AbuEl3orifDB").child("Questions");
        ReactsRef = FirebaseDatabase.getInstance().getReference().child("AbuEl3orifDB").child("QuestionReacts");
        mAuth = FirebaseAuth.getInstance();
        CurrentuserId = mAuth.getCurrentUser().getUid();

        questionContent = findViewById(R.id.ask_question_content);
        Question_submit_btn = findViewById(R.id.ask_question_btn);
        loadingBar = new ProgressDialog(this);


        //---------------------INTENT DATA-------------------------
        Bundle bundle=getIntent().getExtras();
        final String selectedArea = bundle.getString("SELAREA");
        final String selectedCategory = bundle.getString("SELCATEGORY");


        //---------------------------------------------------------
        CategoryQuestionsRef = FirebaseDatabase.getInstance().getReference().child("AbuEl3orifDB").child("Questions").child(selectedArea).child(selectedCategory).child("categoryQuestions");


        Question_submit_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //SaveQuestionMethod();

                Calendar calendarForDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
                currentQuestionDate = currentDate.format(calendarForDate.getTime());

                Calendar calendarForTime = Calendar.getInstance();
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
                currentQuestionTime = currentTime.format(calendarForTime.getTime());

                QuestionStringContent =questionContent.getText().toString();

                PostRandomKey = currentQuestionDate + currentQuestionTime ;



                if(TextUtils.isEmpty(QuestionStringContent))
                {
                    Toast.makeText(QuestionActivity.this, "Quetion Content is Empty..", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    HashMap QuestionMap = new HashMap();
                    QuestionMap.put("questionUserId" , CurrentuserId);
                    QuestionMap.put("questionUserName" , userUsername);
                    QuestionMap.put("questionUserPP" , userPP);
                    QuestionMap.put("questionDate" , currentQuestionDate);
                    QuestionMap.put("questionTime" , currentQuestionTime);
                    QuestionMap.put("questionContent" , QuestionStringContent);

                    loadingBar.setTitle("Asking Question...");
                    loadingBar.setMessage("please wait until your question saving complete...");
                    loadingBar.show();
                    loadingBar.setCanceledOnTouchOutside(true);

                    QuestionRef.child(selectedArea).child(selectedCategory).child("categoryQuestions").child(CurrentuserId +"@"+  PostRandomKey).updateChildren(QuestionMap).addOnCompleteListener(new OnCompleteListener()
                    {
                        @Override
                        public void onComplete(@NonNull Task task)
                        {
                            if(task.isSuccessful())
                            {
                                questionContent.setText("");
                                //SendUserToQuestionsActivity();

                                Toast.makeText(QuestionActivity.this, "Question is stored Successfully...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                            else
                            {
                                Toast.makeText(QuestionActivity.this, "Error occuring during saving question", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }

                        }
                    });


                }

            }
        });

        setSupportActionBar(update_q_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Ask Question");


        //====ref to users node======//
        userRef.child(CurrentuserId).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                     userPP = dataSnapshot.child("profileImage").getValue().toString();
                     userUsername = dataSnapshot.child("username").getValue().toString();

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

        FirebaseRecyclerOptions<Questions> options = new FirebaseRecyclerOptions.Builder<Questions>()
                .setQuery(CategoryQuestionsRef, new SnapshotParser<Questions>()
                {
                    @NonNull
                    @Override
                    public Questions parseSnapshot(@NonNull DataSnapshot snapshot)
                    {
                        Questions questions = new Questions(snapshot.child("questionUserName").getValue().toString() ,snapshot.child("questionTime").getValue().toString()  , snapshot.child("questionDate").getValue().toString() ,snapshot.child("questionContent").getValue().toString() ,  snapshot.child("questionUserPP").getValue().toString());
                        return questions;

                    }
                })
                .build();

        adapter = new FirebaseRecyclerAdapter<Questions, QuestionsViewHolder>(options)
        {


            @NonNull
            @Override
            public QuestionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.all_questions_layout, parent, false);
                context = parent.getContext();

                return new QuestionsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull QuestionsViewHolder holder, int position, @NonNull Questions model)
            {
                final String QuestionKey = getRef(position).getKey();

                holder.setQuestion_uFullName(model.getQuestion_uFullName());
                holder.setQuestion_time(model.getQuestion_time());
                holder.setQuestion_date(model.getQuestion_date());
                holder.setQuestion_description(model.getQuestion_description());

                String imageUri = model.getQuestion_uPP();
                holder.setQuestion_uPP(imageUri);

                //======Reacts Status==================//
                holder.setQuestionReactsStatus(QuestionKey);

                //======show comments Counter=============//
                holder.setQuestionCommentsCount(QuestionKey,selectedArea ,selectedCategory);

                //================when click the quetion=================//
                holder.mView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent ClickQuestionIntent = new Intent(QuestionActivity.this , EditQuestionActivity.class);
                        ClickQuestionIntent.putExtra("QuestionKey" , QuestionKey);
                        ClickQuestionIntent.putExtra("SelectedArea" , selectedArea);
                        ClickQuestionIntent.putExtra("SelectedCategory", selectedCategory);

                        startActivity(ClickQuestionIntent);

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
                                    if(!dataSnapshot.child("QuestionLikes").child(QuestionKey).hasChild(CurrentuserId) && !dataSnapshot.child("QuestionDisLikes").child(QuestionKey).hasChild(CurrentuserId))
                                    {
                                        ReactsRef.child("QuestionLikes").child(QuestionKey).child(CurrentuserId).setValue(true);
                                        // like_Question_btn.setColorFilter(getResources().getColor(R.color.colorLike));
                                        likeChacker = false;

                                    }

                                    else if(dataSnapshot.child("QuestionLikes").child(QuestionKey).hasChild(CurrentuserId) && !dataSnapshot.child("QuestionDisLikes").child(QuestionKey).hasChild(CurrentuserId))
                                    {
                                        ReactsRef.child("QuestionLikes").child(QuestionKey).child(CurrentuserId).removeValue();
                                        likeChacker = false;

                                    }

                                    else if(!dataSnapshot.child("QuestionLikes").child(QuestionKey).hasChild(CurrentuserId) && dataSnapshot.child("QuestionDisLikes").child(QuestionKey).hasChild(CurrentuserId))
                                    {
                                        ReactsRef.child("QuestionDisLikes").child(QuestionKey).child(CurrentuserId).removeValue();
                                        ReactsRef.child("QuestionLikes").child(QuestionKey).child(CurrentuserId).setValue(true);

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
                                    if(!dataSnapshot.child("QuestionLikes").child(QuestionKey).hasChild(CurrentuserId) && !dataSnapshot.child("QuestionDisLikes").child(QuestionKey).hasChild(CurrentuserId))
                                    {
                                        ReactsRef.child("QuestionDisLikes").child(QuestionKey).child(CurrentuserId).setValue(true);
                                        dislikeChecker = false;

                                    }

                                    else if(!dataSnapshot.child("QuestionLikes").child(QuestionKey).hasChild(CurrentuserId) && dataSnapshot.child("QuestionDisLikes").child(QuestionKey).hasChild(CurrentuserId))
                                    {
                                        ReactsRef.child("QuestionDisLikes").child(QuestionKey).child(CurrentuserId).removeValue();
                                        dislikeChecker = false;

                                    }

                                    else if(dataSnapshot.child("QuestionLikes").child(QuestionKey).hasChild(CurrentuserId) && !dataSnapshot.child("QuestionDisLikes").child(QuestionKey).hasChild(CurrentuserId))
                                    {
                                        ReactsRef.child("QuestionDisLikes").child(QuestionKey).child(CurrentuserId).setValue(true);
                                        ReactsRef.child("QuestionLikes").child(QuestionKey).child(CurrentuserId).removeValue();

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

        };


        QuestionCategoryList.setAdapter(adapter);

        //===================================================================================================
     //================================================================================


    }

   private void DisplayAllUserQuestions()
    {
////        FirebaseRecyclerOptions<Questions> options =
////                new FirebaseRecyclerOptions.Builder<Questions>()
////                        .setQuery(CategoryQuestionsRef, Questions.class)
////                        .build();
//
//
//        FirebaseRecyclerOptions<Questions> options = new FirebaseRecyclerOptions.Builder<Questions>()
//                .setQuery(CategoryQuestionsRef, new SnapshotParser<Questions>()
//                {
//                    @NonNull
//                    @Override
//                    public Questions parseSnapshot(@NonNull DataSnapshot snapshot)
//                    {
//                        Questions questions = new Questions(snapshot.child("questionUserName").getValue().toString() ,snapshot.child("questionTime").getValue().toString()  , snapshot.child("questionDate").getValue().toString() ,snapshot.child("questionContent").getValue().toString() ,  snapshot.child("questionUserPP").getValue().toString());
//                        return questions;
//
//                    }
//                })
//                .build();
//
//         adapter = new FirebaseRecyclerAdapter<Questions, QuestionsViewHolder>(options)
//        {
//
//
//            @NonNull
//            @Override
//            public QuestionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
//            {
//                View view = LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.all_questions_layout, parent, false);
//                context = parent.getContext();
//
//                return new QuestionsViewHolder(view);
//            }
//
//            @Override
//            protected void onBindViewHolder(@NonNull QuestionsViewHolder holder, int position, @NonNull Questions model)
//            {
//               final String QuestionKey = getRef(position).getKey();
//
//                holder.setQuestion_uFullName(model.getQuestion_uFullName());
//                holder.setQuestion_time(model.getQuestion_time());
//                holder.setQuestion_date(model.getQuestion_date());
//                holder.setQuestion_description(model.getQuestion_description());
//
//                String imageUri = model.getQuestion_uPP();
//                holder.setQuestion_uPP(imageUri);
//
//                //================when click the quetion=================//
//                holder.mView.setOnClickListener(new View.OnClickListener()
//                {
//                    @Override
//                    public void onClick(View v)
//                    {
//                        Intent ClickQuestionIntent = new Intent(QuestionActivity.this , EditQuestionActivity.class);
//                        ClickQuestionIntent.putExtra("QuestionKey" , QuestionKey);
//
//                        startActivity(ClickQuestionIntent);
//
//                    }
//                });
//            }
//
//        };
//
//
//        QuestionCategoryList.setAdapter(adapter);
//
//        //===================================================================================================
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

    public class QuestionsViewHolder extends RecyclerView.ViewHolder
    {

        View mView;

        ImageButton like_Question_btn , dislike_Question_btn , comments_Question_btn;
        TextView likecount , dislikeCount , commentsCount;
        int likeCounter , dislikeCounter , QuestionCommentsCounter;
        String current_uid;
        DatabaseReference ReactsRef2 , QuestionComentsRef2;

        public QuestionsViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mView = itemView;

            like_Question_btn = (ImageButton) mView.findViewById(R.id.like_icon_btn);
            dislike_Question_btn =(ImageButton) mView.findViewById(R.id.dislike_icon_btn);
            comments_Question_btn = (ImageButton) mView.findViewById(R.id.comments_icon_btn);
            likecount = (TextView) mView.findViewById(R.id.likes_count);
            dislikeCount = (TextView) mView.findViewById(R.id.dislikes_count);
            commentsCount = (TextView) mView.findViewById(R.id.Question_comments_count);

            ReactsRef2 = FirebaseDatabase.getInstance().getReference().child("AbuEl3orifDB").child("QuestionReacts");
            current_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            QuestionComentsRef2 = FirebaseDatabase.getInstance().getReference().child("AbuEl3orifDB").child("Questions");
        }

        public void setQuestionReactsStatus(final String QuestionKey)
        {
            ReactsRef2.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if(!dataSnapshot.child("QuestionLikes").child(QuestionKey).hasChild(CurrentuserId) && !dataSnapshot.child("QuestionDisLikes").child(QuestionKey).hasChild(CurrentuserId))
                    {
                        likeCounter = (int) dataSnapshot.child("QuestionLikes").child(QuestionKey).getChildrenCount();
                        dislikeCounter = (int) dataSnapshot.child("QuestionDisLikes").child(QuestionKey).getChildrenCount();
                        like_Question_btn.setColorFilter(getResources().getColor(R.color.DefaultColorLike));
                        dislike_Question_btn.setColorFilter(getResources().getColor(R.color.DefaultColorLike));
                        likecount.setText(Integer.toString(likeCounter));
                        dislikeCount.setText(Integer.toString(dislikeCounter));

                    }

                    else if(!dataSnapshot.child("QuestionLikes").child(QuestionKey).hasChild(CurrentuserId) && dataSnapshot.child("QuestionDisLikes").child(QuestionKey).hasChild(CurrentuserId))
                    {
                        likeCounter = (int) dataSnapshot.child("QuestionLikes").child(QuestionKey).getChildrenCount();
                        dislikeCounter = (int) dataSnapshot.child("QuestionDisLikes").child(QuestionKey).getChildrenCount();
                        like_Question_btn.setColorFilter(getResources().getColor(R.color.DefaultColorLike));
                        dislike_Question_btn.setColorFilter(getResources().getColor(R.color.colorDislike));
                        likecount.setText(Integer.toString(likeCounter));
                        dislikeCount.setText(Integer.toString(dislikeCounter));


                    }

                    else if(dataSnapshot.child("QuestionLikes").child(QuestionKey).hasChild(CurrentuserId) && !dataSnapshot.child("QuestionDisLikes").child(QuestionKey).hasChild(CurrentuserId))
                    {
                        likeCounter = (int) dataSnapshot.child("QuestionLikes").child(QuestionKey).getChildrenCount();
                        dislikeCounter = (int) dataSnapshot.child("QuestionDisLikes").child(QuestionKey).getChildrenCount();
                        like_Question_btn.setColorFilter(getResources().getColor(R.color.colorLike));
                        dislike_Question_btn.setColorFilter(getResources().getColor(R.color.DefaultColorLike));
                        likecount.setText(Integer.toString(likeCounter));
                        dislikeCount.setText(Integer.toString(dislikeCounter));


                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {

                }
            });
        }


        public void setQuestionCommentsCount(final String QuestionKey , final String selArea , final String selCategory)
        {
            QuestionComentsRef2.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {

                        QuestionCommentsCounter = (int) dataSnapshot.child(selArea).child(selCategory).child("categoryQuestions").child(QuestionKey).child("QuestionCategoryComments").getChildrenCount();
                        commentsCount.setText(Integer.toString(QuestionCommentsCounter));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {

                }
            });
        }

        public void setQuestion_uFullName(String question_uFullName)
        {
            TextView q_UfullName = (TextView) mView.findViewById(R.id.Question_username);
            q_UfullName.setText(question_uFullName);
        }

        public void setQuestion_time(String question_time)
        {
            TextView qTime = (TextView) mView.findViewById(R.id.Question_time);
            qTime.setText("  " + question_time);
        }

        public void setQuestion_date(String question_date)
        {
            TextView qDate = (TextView) mView.findViewById(R.id.Question_date);
            qDate.setText("  " + question_date);
        }

        public void setQuestion_description(String question_description)
        {
            TextView qDescription = (TextView) mView.findViewById(R.id.Question_description);
            qDescription.setText(question_description);
        }

        public void setQuestion_uPP(String question_uPP)
        {
            q_Upp = mView.findViewById(R.id.Question_user_pp);
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.profile_icon);

            //Picasso.with(ctx).load(question_uPP).into(q_Upp);
            Glide.with(context).applyDefaultRequestOptions(requestOptions).load(question_uPP).into(q_Upp);
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

    private void SendUserToQuestionsActivity()
    {
        Intent QuestionIntent = new Intent(QuestionActivity.this , QuestionActivity.class);
        startActivity(QuestionIntent);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(id == android.R.id.home)
        {
            SendUserToMapActivtit();
        }
        return super.onOptionsItemSelected(item);
    }

    private void SendUserToMapActivtit()
    {
        Intent map_intent = new Intent(QuestionActivity.this , GoogleMapsActivity.class);
        startActivity(map_intent);

    }
}
