package com.example.test;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class


SavedCommentActivity extends AppCompatActivity {

    private RecyclerView savedCommentsRecyclerView ;
    private DatabaseReference savedCommentRef  ,specificCommentsRef;
    private FirebaseRecyclerAdapter savedCommentRecyclerAdapter;
    public Context context;
    private FirebaseAuth mAuth;
    private  String currentUserId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_comment);

        /*Comment Data */
        /*_____________________________________________________________________________________________________________________________*/

//        Bundle bundle =getIntent().getExtras();
//        final String QuestionId =bundle.getString("QuestionId");
        // final String CommentId =bundle.getString("CommentId");


        mAuth= FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();

//                                      /*Save Comment FirebaseDatabase Reference*/
        /*_____________________________________________________________________________________________________________________________*/


       // savedQuestionsRef = FirebaseDatabase.getInstance().getReference().child("AbuEl3orifDB").child("SavedQuestionComment").child(currentUserId);
        savedCommentRef=FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId).child("SavedQuestionComment");

        /*RecyclerView*/
        /*_____________________________________________________________________________________________________________________________*/

        savedCommentsRecyclerView =findViewById(R.id.RecyclerSavedCommentsView);

        /*LinearLayoutManager*/
        /*_________________________________________________________________________________*/
        LinearLayoutManager layoutManager =new LinearLayoutManager(SavedCommentActivity.this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        /*_________________________________________________________________________________*/


        savedCommentsRecyclerView.setLayoutManager(layoutManager);
        savedCommentsRecyclerView.setHasFixedSize(true);
        /*_____________________________________________________________________________________________________________________________*/


        displayAllSavedComments();





    }

    private void displayAllSavedComments() {
        /*Options*/
        /*_____________________________________________________________________________________________________________________________*/

        FirebaseRecyclerOptions<QuestionComments> options = new FirebaseRecyclerOptions.Builder<QuestionComments>()
                .setQuery(savedCommentRef, new SnapshotParser<QuestionComments>() {
                    @NonNull
                    @Override
                    public QuestionComments parseSnapshot(@NonNull DataSnapshot snapshot) {

                        QuestionComments commentOpject = new QuestionComments(snapshot.child("questionCommentUserName").getValue().toString() ,snapshot.child("questionCommentTime").getValue().toString() ,snapshot.child("questionCommentDate").getValue().toString() , snapshot.child("questionCommentContent").getValue().toString() ,snapshot.child("questionCommentUserPP").getValue().toString()) ;

                        return commentOpject;

                    }
                }).build();



        /*FirebaseRecyclerAdapter*/
        /*_____________________________________________________________________________________________________________________________*/


        savedCommentRecyclerAdapter =new FirebaseRecyclerAdapter<QuestionComments ,savedCommentViewHolder>(options) {

            /*onBindViewHolder*/
            /*_____________________________________________________________________________________________________________________________*/

            @Override
            protected void onBindViewHolder(@NonNull final savedCommentViewHolder holder, int position, @NonNull QuestionComments model) {
                holder.setSavedCommentContent(model.getQuestion_comments_description());
                holder.setSavedCommentDate(model.getQuestion_comment_date());
                holder.setSavedCommentTime(model.getQuestion_comment_time());
                holder.setSavedCommentUserName(model.getQuestion_comment_uFullName());
                // holder.setSavedCommentUserNamePP(model.getQuestion_comment_uPP());

                holder.mView.findViewById(R.id.like_icon_btn).setVisibility(savedCommentsRecyclerView.INVISIBLE);
                holder.mView.findViewById(R.id.dislike_icon_btn).setVisibility(savedCommentsRecyclerView.INVISIBLE);


                /*___________________________________________UnSaved Comment ______________________________________*/

                holder.Root.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        final String CommentId =getRef(holder.getAdapterPosition()).getKey();
                        specificCommentsRef =savedCommentRef.child(CommentId);
                        PopupMenu popupMenu=new PopupMenu(holder.mView.getContext() , findViewById(R.id.root));  // 2 parameters Context , view
                        popupMenu.inflate(R.menu.menu_unsaved);

                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId())
                                {
                                    case R.id.unSaved :
                                        specificCommentsRef.removeValue();
                                        break;


                                }

                                return true;
                            }
                        });

                        popupMenu.show();


                        return true;
                    }


                });
            }
            /*_____________________________________________________________________________________________________________________________*/

            /*onCreateViewHolder*/
            /*_____________________________________________________________________________________________________________________________*/

            @NonNull
            @Override
            public savedCommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.all_question_comments_layout,parent,false);
                return new savedCommentViewHolder(view);
            }
        };

        savedCommentsRecyclerView.setAdapter(savedCommentRecyclerAdapter);





    }

    /*View Holder static class*/
    /*_____________________________________________________________________________________________________________________________*/

    public  static class  savedCommentViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
        LinearLayout Root;


        public savedCommentViewHolder(@NonNull View itemView)
        {

            super(itemView);
            mView=itemView;
            Root=mView.findViewById(R.id.root);
        }

        public  void setSavedCommentContent (String Content)
        {
            TextView ContentTextView =mView.findViewById(R.id.Question_description);
            ContentTextView.setText(Content);

        }
        public  void setSavedCommentTime (String Time)
        {
            TextView TimeTextView =mView.findViewById(R.id.Question_time);
            TimeTextView.setText(Time);


        }
        public  void setSavedCommentDate (String Date)
        {
            TextView DateTextView =mView.findViewById(R.id.Question_date);
            DateTextView.setText(Date);

        }
        public  void setSavedCommentUserName (String UserName)
        {
            TextView UserNameTextView =mView.findViewById(R.id.Question_username);
            UserNameTextView.setText(UserName);


        }

//        public  void setSavedCommentUserNamePP (String UserName_PP)
//        {
//            CircleImageView UserNamePP_ImageView =mView.findViewById(R.id.Question_user_pp);
//            RequestOptions requestOptions=new RequestOptions();
//            requestOptions.placeholder(R.drawable.profile_icon);
//
//            Glide.with(context).applyDefaultRequestOptions(requestOptions).load(UserName_PP).into(UserNamePP_ImageView);
//        }
    }
    /*_____________________________________________________________________________________________________________________________*/


    /*On Start method*/
    /*_____________________________________________________________________________________________________________________________*/


    @Override
    protected void onStart() {
        super.onStart();
        savedCommentRecyclerAdapter.startListening();
    }

    /*On Stop method*/
    /*_____________________________________________________________________________________________________________________________*/

    @Override
    protected void onStop() {
        super.onStop();
        savedCommentRecyclerAdapter.stopListening();
    }
}
