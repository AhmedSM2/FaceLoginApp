package com.example.test;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FavoriteTemp extends AppCompatActivity {
    private final ArrayList<CommentClass> commentList = new ArrayList<>();
    private final ArrayList<Post> postsList = new ArrayList<>();
    private final ArrayList<String> favList = new ArrayList<>();

    DatabaseReference mPost, mComment,mFavorite,temp ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_temp);
        final ListView favoritesList = findViewById(R.id.favoritesList);
        Log.d("rwerwerew","Abo nesma");
        mComment = FirebaseDatabase.getInstance().getReference().child("").child("Comment");
        mPost = FirebaseDatabase.getInstance().getReference().child("Post");
        mFavorite = FirebaseDatabase.getInstance().getReference().child("Favorite");

        mPost.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Post posts = dataSnapshot.getValue(Post.class);
                postsList.add(posts);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mComment.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                CommentClass comments = dataSnapshot.getValue(CommentClass.class);
                commentList.add(comments);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mFavorite.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String fav = dataSnapshot.getValue(String.class);
                favList.add(fav);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        temp = FirebaseDatabase.getInstance().getReference().child("-LbYJRsYb1DEc-WDPsgk");
        String s = temp.toString();
        Log.d("aaaaaaaaaaaa",s);


    }
}
