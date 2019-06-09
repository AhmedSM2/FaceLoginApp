package com.example.test;

import android.content.Context;
import android.content.Intent;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class MainSecondActivity extends AppCompatActivity {
    private RecyclerView postList;
    private DatabaseReference PostsRef,LikesRef;
    boolean LikeChecker = false;
    public Context context;
    private FirebaseAuth mAuth;
    private String CurrentUser_id,User_id;
    public List<PostSecond> post_list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_second);

        postList = (RecyclerView) findViewById(R.id.all_users_post_list);
        postList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postList.setLayoutManager(linearLayoutManager);
        PostsRef = FirebaseDatabase.getInstance().getReference().child("Post");
        LikesRef =FirebaseDatabase.getInstance().getReference().child("Likes");
        mAuth = FirebaseAuth.getInstance();
        CurrentUser_id = mAuth.getCurrentUser().getUid();


    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<PostSecond> options=
                new FirebaseRecyclerOptions.Builder<PostSecond>()
                        .setQuery(PostsRef,PostSecond.class)
                        .build();
        final FirebaseRecyclerAdapter<PostSecond,PostsViewHolder> adapter =
                new FirebaseRecyclerAdapter<PostSecond, PostsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final PostsViewHolder holder, int position, @NonNull PostSecond model) {
//Get PostId:
                        final String PostKey=getRef(position).getKey();
                        PostsRef.child(PostKey).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           PostSecond postSecond = dataSnapshot.getValue(PostSecond.class);
                           String userid= postSecond.getUid();
                                FirebaseDatabase.getInstance().getReference("users")
                                        .child(userid).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        user_class user = dataSnapshot.getValue(user_class.class);
                                        String  profile_image = user.getImageUrl();
                                        holder.setProfile_Image(profile_image);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        holder.userName.setText(model.getUsername());
                        holder.date.setText(model.getDate());
                        holder.time.setText(model.getTime());
                        holder.contents.setText(model.getDesc());
                        String image_uri = model.getImage_uri();
                        holder.setImage(image_uri);
                        /*FirebaseDatabase.getInstance().getReference("users")
                                .child(CurrentUser_id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                user_class user = dataSnapshot.getValue(user_class.class);
                              String  profile_image = user.getImageUrl();
                              holder.setProfile_Image(profile_image);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });*/

//Add Like:
                        holder.setLikeButtonStatues(PostKey);
//Send user To post Activity with post id  :
                        holder.mview.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent clickPostIntent=new Intent(MainSecondActivity.this,ClickPostActivity.class);
//Send Id:
                                clickPostIntent.putExtra("PostKey",PostKey);
                                startActivity(clickPostIntent);
                            }
                        });
//When click on comment button go to comment activity
                        holder.commentPostButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent commentIntent = new Intent(MainSecondActivity.this,CommentSecondActivity.class);
                                commentIntent.putExtra("PostKey",PostKey);

                                startActivity(commentIntent);
                            }
                        });
//Check if user already liked a post or not
                        holder.likePostButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                LikeChecker = true;
                                LikesRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (LikeChecker==true) {
                                            if (dataSnapshot.child(PostKey).hasChild(CurrentUser_id)) {
                                                LikesRef.child(PostKey).child(CurrentUser_id).removeValue();
                                                LikeChecker = false;
                                            } else {
                                                LikesRef.child(PostKey).child(CurrentUser_id).setValue(true);
                                                LikeChecker = false;
                                            }
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        });
                    }
                    @NonNull
                    @Override
                    public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_post_layout,parent,false);
                        context = parent.getContext();
                        PostsViewHolder  viewHolder= new PostsViewHolder(view);
                        return viewHolder;
                    }
                };
        postList.setAdapter(adapter);
        adapter.startListening();
    }
    public  class PostsViewHolder extends RecyclerView.ViewHolder{
        View mview;
        //Initialize User Name And description of post :
        TextView userName,contents,date,time;
        ImageView postImage,profile_Img;
        //Initialize like And comment  :
        ImageButton likePostButton,commentPostButton;
        TextView displayNoOfLikes;
        //Count Likes :
        int countLikes;
        String currentUserId;
        DatabaseReference LikesRef;
        public PostsViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.post_user_name);
            contents = itemView.findViewById(R.id.post_description);
            date = itemView.findViewById(R.id.post_date);
            time= itemView.findViewById(R.id.post_time);
            likePostButton =itemView.findViewById(R.id.like_button);
            commentPostButton =itemView.findViewById(R.id.comment_button);
            displayNoOfLikes =itemView.findViewById(R.id.display_no_of_likes);

            LikesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
            currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            mview=itemView;
        }
        public void setLikeButtonStatues(final String postKey ){
            LikesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(postKey).hasChild(currentUserId)){
                        countLikes = (int) dataSnapshot.child(postKey).getChildrenCount();
                        likePostButton.setImageResource(R.drawable.like);
                        displayNoOfLikes.setText(Integer.toString(countLikes)+(" Likes"));
                    }
                    else
                    {
                        countLikes = (int) dataSnapshot.child(postKey).getChildrenCount();
                        likePostButton.setImageResource(R.drawable.dislike);
                        displayNoOfLikes.setText(Integer.toString(countLikes)+(" Likes"));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        public void setImage(String imageDesc)
        {
            postImage = mview.findViewById(R.id.post_image);
            if(imageDesc != null)
            {
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(R.drawable.profile);
                Glide.with(context).applyDefaultRequestOptions(requestOptions).load(imageDesc).into(postImage);
            }

        }

        public void setProfile_Image(String imageDesc)
        {
            profile_Img = mview.findViewById(R.id.post_profile_image);
            if(imageDesc != null)
            {
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(R.drawable.profile);
                Glide.with(context).applyDefaultRequestOptions(requestOptions).load(imageDesc).into(profile_Img);
            }

        }

    }
}
