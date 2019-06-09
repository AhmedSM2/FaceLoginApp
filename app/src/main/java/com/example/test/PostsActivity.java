package com.example.test;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PostsActivity extends AppCompatActivity {

    private DatabaseReference mPost ;
    ArrayList<Post> list = new ArrayList<>();
    String postTempId;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    String currentUserID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        final ListView listView = findViewById(R.id.main_list);
        mPost = FirebaseDatabase.getInstance().getReference().child("Post");
        final EditText postCont = findViewById(R.id.postContent);
        final EditText postUser = findViewById(R.id.postName);
        Button btn_send = findViewById(R.id.send);
        Button te = findViewById(R.id.temp_btn);
        final CustomeAdpater customeAdpater = new CustomeAdpater();
        te.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Temp();
            }
        });
        // Add Post
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = mPost.child("Post").push().getKey();
                Post post = new Post(postCont.getText().toString(),postUser.getText().toString(),id);
                mPost.child(id).setValue(post);
                postCont.setText("");
                postUser.setText("");
            }
        });

        listView.setAdapter(customeAdpater);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView p_conent = view.findViewById(R.id.post_content);
                Bundle bundle = new Bundle();
                bundle.putString("post",p_conent.getText().toString());
                Intent comment_intent = new Intent(getApplication(),Comment.class);
                comment_intent.putExtra("postId",list.get(position).id);
                comment_intent.putExtras(bundle);
                startActivity(comment_intent);
            }
        });
        // read the post from firebase
        mPost.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Post postData = dataSnapshot.getValue(Post.class);
                list.add(0,postData);
                customeAdpater.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//                Post c = dataSnapshot.getValue(Post.class);
//                list.remove(c);
//                Log.d("TAG",c.name);
//                customeAdpater.notifyDataSetChanged();
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        mPost.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot com_snapshot : dataSnapshot.getChildren())
//                {
//                    Post c = com_snapshot.getValue(Post.class);
//                    list.add(0,c);
//                }
//                customeAdpater.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        /////////////////////////////////////////////////////////

    }

    // Custome list view
    class CustomeAdpater extends BaseAdapter {
        boolean checklike = false;
        @Override
        public int getCount() {
            return list.size();
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
            convertView = getLayoutInflater().inflate(R.layout.post_content,null);
            TextView p_name = convertView.findViewById(R.id.post_name);
            TextView p_conent = convertView.findViewById(R.id.post_content);
            mAuth = FirebaseAuth.getInstance();
            currentUserID = mAuth.getCurrentUser().getUid();
            usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserID);

            final TextView t_likes= convertView.findViewById(R.id.likeCount);
            final TextView t_disLikes= convertView.findViewById(R.id.disLikeCount);
            final ImageButton like = convertView.findViewById(R.id.like_btn);
            final ImageButton Menu = convertView.findViewById(R.id.postMenu);
            final ImageButton dislike = convertView.findViewById(R.id.dislike_btn);
            final String postId =mPost.child("/"+list.get(position).id).getKey();
            // Post Menu that include edit , remove and report
            Menu.setOnClickListener(new View.OnClickListener() {

                String postContent=  list.get(position).content;
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(getApplicationContext(),v);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.item1:
                                    sendUserToEditPostActivity(postContent,postId);
                                    return true;
                                case R.id.item2:
                                    mPost.child(postId).removeValue();
                                    Post p = new Post();
                                    p = list.get(position);
                                    list.remove(p);
                                    notifyDataSetChanged();
                                    return true;
                                case R.id.item3:
                                    Toast.makeText(PostsActivity.this, "Item3 :)",
                                            Toast.LENGTH_SHORT).show();
                                    return true;
                                default: return  false;
                            }
                        }
                    });
                    popupMenu.inflate(R.menu.menu_post);
                    popupMenu.show();
                }
            });
            final int[] likecount = {0};
            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dislike.setColorFilter(getResources().getColor(R.color.colorLikeFaceDefault));
                    like.setColorFilter(getResources().getColor(R.color.colorLikeFace));
                    checklike = true;
                    likecount[0] = likecount[0] +1;
                    t_likes.setText(Integer.toString(likecount[0]));
                    mPost.child(postId).child("Likes").push().setValue("true");
                    checklike = true;
                }
            });
            final int[] dislikecount = {0};
            dislike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    like.setColorFilter(getResources().getColor(R.color.colorLikeFaceDefault));
                    dislike.setColorFilter(getResources().getColor(R.color.colorLikeFace));
                    checklike = false;
                    likecount[0] = likecount[0] -1;
                    dislikecount[0] =dislikecount[0]+1;
                    t_disLikes.setText(Integer.toString(dislikecount[0]));
                    t_likes.setText(Integer.toString(likecount[0]));
                    mPost.child(postId).child("DisLikes").push().setValue("false");
                }
            });
            p_name.setText(list.get(position).name);
            p_conent.setText(list.get(position).content);
            return convertView;
        }

    }

    private  void sendUserToEditPostActivity(String content, String id)
    {
        Intent editPostActivity = new Intent(getApplicationContext(),EditPostActivity.class);
        editPostActivity.putExtra("Content",content);
        editPostActivity.putExtra("PostId",id);
        startActivity(editPostActivity);
    }
    private  void Temp()
    {
        Intent tt = new Intent(getApplicationContext(),FavoriteTemp.class);
        startActivity(tt);
    }
}
