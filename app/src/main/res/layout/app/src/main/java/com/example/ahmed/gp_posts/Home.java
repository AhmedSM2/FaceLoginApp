package com.example.ahmed.gp_posts;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {
    private RecyclerView postList;
    private DatabaseReference postRef,LikesRef;
    boolean LikeChecker = false;
    private FirebaseAuth mAuth;
    private String CurrentUser_id,User_id;
    // Creating DatabaseReference.
    DatabaseReference databaseReference;

    // Creating RecyclerView.
    RecyclerView recyclerView;

    // Creating RecyclerView.Adapter.
    RecyclerView.Adapter adapter ;

    // Creating Progress dialog
    ProgressDialog progressDialog;

    // Creating List of ImageUploadInfo class.
    List<Posts> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        LikesRef =FirebaseDatabase.getInstance().getReference().child("Likes");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Posts");
       /*\\ postList =(RecyclerView) findViewById(R.id.recyclerView);
        postList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postList.setLayoutManager(linearLayoutManager);



        DisplayAllUserPosts();*/


        mAuth = FirebaseAuth.getInstance();
        CurrentUser_id = mAuth.getCurrentUser().getUid();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

// Setting RecyclerView size true.
        recyclerView.setHasFixedSize(true);

// Setting RecyclerView layout as LinearLayout.
        recyclerView.setLayoutManager(new LinearLayoutManager(Home.this));
        progressDialog = new ProgressDialog(Home.this);

// Setting up message in Progress dialog.
        progressDialog.setMessage("Loading Images From Firebase.");

// Showing progress dialog.
        progressDialog.show();
        // Adding Add Value Event Listener to databaseReference.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    Posts imageUploadInfo = postSnapshot.getValue(Posts.class);

                    list.add(imageUploadInfo);
                }

                adapter = new RecyclerViewAdapter(getApplicationContext(), list);



                recyclerView.setAdapter(adapter);
                /*recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        //final String PostKey = get(position).getKey();
                        Intent clickPostIntent = new Intent(Home.this,ClickPostActivity.class);
                        //clickPostIntent.putExtra("PostKey",PostKey);

                        startActivity(clickPostIntent);

                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));*/

                // Hiding the progress dialog.
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                // Hiding the progress dialog.
                progressDialog.dismiss();

            }
        });
    }

   private void DisplayAllUserPosts() {
        FirebaseRecyclerOptions<Posts> options =
                new FirebaseRecyclerOptions.Builder<Posts>()
                .setQuery(postRef,Posts.class)
                .build();
        FirebaseRecyclerAdapter<Posts,PostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Posts, PostViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull Posts model) {

                final String PostKey = getRef(position).getKey();
                holder.setFullname(model.getFullname());
                holder.setDescription(model.getDescription());
                holder.setDate(model.getDate());
                holder.setTime(model.getTime());
                holder.setLikeButtonStatues(PostKey);
                holder.setPostimage(getApplicationContext(),model.getPostimage());
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                      Intent clickPostIntent = new Intent(Home.this,ClickPostActivity.class);
                      clickPostIntent.putExtra("PostKey",PostKey);

                      startActivity(clickPostIntent);

                    }
                });
                holder.CommentButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent commentIntent = new Intent(Home.this,CommentActivity.class);
                     commentIntent.putExtra("PostKey",PostKey);

                        startActivity(commentIntent);
                    }
                });
                holder.LikeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LikeChecker = true;
                        LikesRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                              if (LikeChecker==true) {
                                  if (dataSnapshot.child(PostKey).hasChild(CurrentUser_id)) {
                                      LikesRef.child(PostKey).child(CurrentUser_id).removeValue();
                                      LikeChecker = false;


                                  } else {
                                      LikesRef.child(PostKey).child(CurrentUser_id).setValue(true);
                                  }
                              }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });
            }

            @NonNull
            @Override
            public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_all_posts,parent,false);
                PostViewHolder viewHolder = new PostViewHolder(view);
                return viewHolder;
            }
        };
        postList.setAdapter(firebaseRecyclerAdapter);
    }
    public static class PostViewHolder extends RecyclerView.ViewHolder{
    View mView;

    ImageButton LikeButton,CommentButton;
    TextView DisplayNumOfLikes;
    int countLikes;
    String currentUserId;
    DatabaseReference LikesRef;
        public PostViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            LikeButton = (ImageButton)mView.findViewById(R.id.btn_dislike);
            CommentButton = (ImageButton)mView.findViewById(R.id.btn_comment);
            DisplayNumOfLikes = (TextView) mView.findViewById(R.id.display_no_of_likes);
            LikesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
           currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        }
        public void setLikeButtonStatues(final String postKey ){
        LikesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(postKey).hasChild(currentUserId)){
                    countLikes = (int) dataSnapshot.child(postKey).getChildrenCount();
                    LikeButton.setImageResource(R.drawable.like);
                    DisplayNumOfLikes.setText(Integer.toString(countLikes)+(" Likes"));


                }
                else
                {
                    countLikes = (int) dataSnapshot.child(postKey).getChildrenCount();
                    LikeButton.setImageResource(R.drawable.dislike);
                    DisplayNumOfLikes.setText(Integer.toString(countLikes)+(" Likes"));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        }

        public  void setFullname(String fullname){
            TextView username = (TextView) mView.findViewById(R.id.post_username);
        username.setText(fullname);
        }
        public void setTime(String time){
            TextView postTime = (TextView) mView.findViewById(R.id.post_time);
            postTime.setText("   "+time);
        }
        public void setDate(String date){
            TextView postDate = (TextView) mView.findViewById(R.id.post_date);
            postDate.setText("   "+date);
        }
        public void setDescription(String description){
            TextView postDesc = (TextView) mView.findViewById(R.id.post_desc);
            postDesc.setText(description);
        }
        public void setPostimage(Context cox, String postimage){
            ImageView image = (ImageView) mView.findViewById(R.id.post_image);
            Picasso.with(cox).load(String.valueOf(image)).into(image);

        }
    }
}
