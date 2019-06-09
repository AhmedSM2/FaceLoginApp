package com.example.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.test.Notifications.Client;
import com.example.test.Notifications.Data;
import com.example.test.Notifications.MyResponse;
import com.example.test.Notifications.Sender;
import com.example.test.Notifications.Token;
import com.google.android.gms.common.api.Api;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {
    CircleImageView profile_img;
    TextView username,txt_seen;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    Intent intent;
     String userid;
    ImageButton send_btn ;
    EditText text_send;

    MessageAdapter messageAdapter;
    List<Chat> mChat;

    RecyclerView recyclerView;
    ValueEventListener seenListener;

    APIService apiService;
    boolean notifiy=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recycler_View_message);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              startActivity(new Intent(MessageActivity.this,StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

            }
        });
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);


        profile_img = (CircleImageView) findViewById(R.id.profile_imge);
        username = (TextView)findViewById(R.id.username_chat);
        text_send = (EditText) findViewById(R.id.text_send);
        send_btn = (ImageButton)findViewById(R.id.btn_send);

        intent = getIntent();
         userid = intent.getStringExtra("userid");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifiy=true;
               String msg = text_send.getText().toString();
               if (!msg.equals("")){
                  send(firebaseUser.getUid(),userid,msg);
                  text_send.setText("");
               }
               else {
                   Toast.makeText(MessageActivity.this,"Please type a message",Toast.LENGTH_SHORT).show();

               }

            }
        });


        reference = FirebaseDatabase.getInstance().getReference().child("users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user_class user = dataSnapshot.getValue(user_class.class);
                username.setText(user.getUser_username());
                if (user.getImageUrl().equals("default")){

                    profile_img.setImageResource(R.mipmap.ic_launcher);


                }
                else {
                    Glide.with(getApplicationContext()).load(user.getImageUrl()).into(profile_img);
                }
                readMessage(firebaseUser.getUid(),userid,user.getImageUrl());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        seenMessage(userid);

    }
    private void seenMessage(final String userid){
     reference = FirebaseDatabase.getInstance().getReference("Chats");
     seenListener = reference.addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(DataSnapshot dataSnapshot) {
          for(DataSnapshot snapshot:dataSnapshot.getChildren()){
            Chat chat = snapshot.getValue(Chat.class);
            if(chat.getReceiver().equals(firebaseUser.getUid())&& chat.getSender().equals(userid)){
                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("isseen",true);
                snapshot.getRef().updateChildren(hashMap);


            }
          }
         }

         @Override
         public void onCancelled(DatabaseError databaseError) {

         }
     });
    }

    private void send(String sender, final String receiver, final String mssg){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> map = new HashMap<>();
        map.put("sender",sender);
        map.put("receiver",receiver);
        map.put("message",mssg);
        map.put("isseen",false);

        reference.child("Chats").push().setValue(map);

            final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist")
                    .child(firebaseUser.getUid()).child(userid);
            chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()){
                       chatRef.child("id").setValue(userid);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        final String msg = mssg;
        reference =FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user_class user =dataSnapshot.getValue(user_class.class);
               if (notifiy) {
                   sendNotification(receiver, user.getUser_username(), mssg);
               }
               notifiy=false;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void sendNotification(String receiver, final String username, final String mssg) {
    DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                Token token = snapshot.getValue(Token.class);
                Data data = new Data(firebaseUser.getUid(),R.mipmap.ic_launcher,username+": "+mssg,"New Message",
                        userid);
                Sender sender = new Sender(data,token.getToken());
                apiService.sendNotification(sender)
                .enqueue(new Callback<MyResponse>() {
                    @Override
                    public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                       if (response.code()==200){
                           if (response.body().success!=1){
                              Toast.makeText(MessageActivity.this,"Failed",Toast.LENGTH_SHORT).show();

                           }
                       }
                    }

                    @Override
                    public void onFailure(Call<MyResponse> call, Throwable t) {

                    }
                });
            }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void readMessage(final String myid , final String userid,final String imageUrl){
      mChat = new ArrayList<>();
      reference = FirebaseDatabase.getInstance().getReference("Chats");
      reference.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {
              mChat.clear();
              for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                 Chat chat = snapshot.getValue(Chat.class);
                 if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid)||
                 chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){
                    mChat.add(chat);
                 }
                 messageAdapter = new MessageAdapter(MessageActivity.this,mChat,imageUrl);
             recyclerView.setAdapter(messageAdapter);
              }
          }

          @Override
          public void onCancelled(DatabaseError databaseError) {


          }
      });
    }
    private void status(String status){
        reference  = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("status",status);
        reference.updateChildren(hashMap);

    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(seenListener);
        status("offline");

    }
}
