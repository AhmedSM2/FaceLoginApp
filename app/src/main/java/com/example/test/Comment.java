package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Comment extends AppCompatActivity {
    final ArrayList<CommentClass> commentList = new ArrayList<>();
    private DatabaseReference mComment,mFavoriteTemp ;
    String postId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
     //   ArrayList<Post> arrPosts= new ArrayList<>();
        TextView postView = findViewById(R.id.c_Post);
        final TextView hint = findViewById(R.id.hint);
        final LinearLayout commentLayout =findViewById(R.id.commentLayout);
        final ListView listView = findViewById(R.id.commentList);
        Intent intent = getIntent();
        final EditText commentText = findViewById(R.id.commnet_content);
        Button addCom = findViewById(R.id.comment_btn);
        String postContent = getIntent().getExtras().getString("post");
        postView.setText(postContent);
        postId=intent.getStringExtra("postId");
        mComment = FirebaseDatabase.getInstance().getReference().child("Post/"+postId).child("Comment");
        addCom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment_id = mComment.child("Comment").push().getKey();
                String StrCom = commentText.getText().toString();
                CommentClass new_comment = new CommentClass(StrCom,comment_id,"");
          //      mComment.push().setValue(new_comment);
                mComment.child(comment_id).setValue(new_comment);

            }
        });
        final CustomComment commentAdapter = new CustomComment();
        listView.setAdapter(commentAdapter);
        //Save favorit comment
        mFavoriteTemp = FirebaseDatabase.getInstance().getReference().child("Favorite");
        final ArrayList<String> favoriteCheck = new ArrayList<>();
        mFavoriteTemp.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String id = dataSnapshot.getValue(String.class);
                favoriteCheck.add(0,id);
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String comment_id =   commentList.get(position).getComment_id();
                if(favoriteCheck.contains(comment_id) == false){
                    mFavoriteTemp.push().setValue(comment_id);
                    Toast.makeText(Comment.this, "Edit :)"+comment_id,
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(Comment.this, "No :)",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        mComment.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                CommentClass Comment = dataSnapshot.getValue(CommentClass.class);
                commentList.add(Comment);
                commentAdapter.notifyDataSetChanged();
                listView.setSelection(commentAdapter.getCount()-1);
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
    }

    // Comment custom adapter
    class CustomComment extends BaseAdapter {

        @Override
        public int getCount() {
            return commentList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            convertView = getLayoutInflater().inflate(R.layout.comment_content,null);
            TextView textView = convertView.findViewById(R.id.comment_txt);
            textView.setText(commentList.get(position).getComment_body());


            //final ImageButton Menu = convertView.findViewById(R.id.commentMenu);
            // Post Menu that include edit , remove and report
           /* Menu.setOnClickListener(new View.OnClickListener() {

                String comment_id=  commentList.get(position).getComment_id();
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(getApplicationContext(),v);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.item1:
                                    sendUserToEditCommentActivity(commentList.get(position).getComment_body(),comment_id,postId);
                                    Toast.makeText(Comment.this, "Edit :)",
                                            Toast.LENGTH_SHORT).show();
                                    return true;
                                case R.id.item2:
                                    mComment.child(postId).removeValue();
                                    CommentClass c = new CommentClass();
                                    c = commentList.get(position);
                                    commentList.remove(c);
                                    notifyDataSetChanged();
                                    return true;
                                case R.id.item3:
                                    Toast.makeText(Comment.this, "Item3 :)",
                                            Toast.LENGTH_SHORT).show();
                                    return true;
                                default: return  false;
                            }
                        }
                    });
                    popupMenu.inflate(R.menu.menu_post);
                    popupMenu.show();
                }
            });*/
            return convertView;
        }
    }
    private  void sendUserToEditCommentActivity(String content, String id, String post_id)
    {
        Intent editCommentActivity = new Intent(getApplicationContext(),EditComment.class);
        editCommentActivity.putExtra("CommentContent",content);
        editCommentActivity.putExtra("CommentId",id);
        editCommentActivity.putExtra("PostId",post_id);
        startActivity(editCommentActivity);
    }
}
