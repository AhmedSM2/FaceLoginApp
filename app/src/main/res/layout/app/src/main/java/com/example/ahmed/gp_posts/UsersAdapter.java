package com.example.ahmed.gp_posts;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UsersAdapter  extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {
    private Context mContext;
    private List<User> mUsers;
    private  boolean ischat;
    String theLastMessage;
    public UsersAdapter(Context mContext,List<User> mUsers,boolean ischat){
       this.mUsers = mUsers;
       this.mContext =mContext;
       this.ischat= ischat;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(mContext).inflate(R.layout.user_item,parent,false);


        return new UsersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final User user = mUsers.get(position);
        holder.username.setText(user.getUsername());
        if (user.getImageUrl().equals("default")){

            holder.profile_img.setImageResource(R.mipmap.ic_launcher);


        }
        else {
            Glide.with(mContext).load(user.getImageUrl()).into(holder.profile_img);
        }
        if (ischat){
            lastMessage(user.getUid(),holder.last_msg);

        }else {
         holder.last_msg.setVisibility(View.GONE);

        }
        if (ischat){
            if (user.getStatus().equals("online")){
             holder.img_on.setVisibility(View.VISIBLE);
             holder.img_off.setVisibility(View.GONE);
            }
           else {
                holder.img_off.setVisibility(View.VISIBLE);
                holder.img_on.setVisibility(View.GONE);
            }
        }
        else {
            holder.img_on.setVisibility(View.GONE);
            holder.img_off.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(mContext,MessageActivity.class);
               intent.putExtra("userid",user.getUid());
               mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class  ViewHolder extends  RecyclerView.ViewHolder{

        public TextView username;
        public ImageView profile_img;
        private ImageView img_on;
        private ImageView img_off;
        public TextView last_msg;

        public ViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            profile_img = itemView.findViewById(R.id.user_profile);
            img_on = itemView.findViewById(R.id.img_on);
            img_off = itemView.findViewById(R.id.img_off);
        last_msg = itemView.findViewById(R.id.last_msg);

        }
    }
   private  void lastMessage(final String userid, final TextView last_msg){
    theLastMessage ="default";
       final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
       DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
       reference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
              for (DataSnapshot snapshot: dataSnapshot.getChildren()){
               Chat chat = snapshot.getValue(Chat.class);
               if(chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
                  chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid())){
                   theLastMessage = chat.getMessage();

               }
              }
              switch (theLastMessage){
                  case "default":
                      last_msg.setText("No Message");
                    break;
                   default:
                     last_msg.setText(theLastMessage);
                    break;
              }
              theLastMessage = "default";
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });

   }
}
