package com.example.ahmed.gp_posts;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsersFragment extends Fragment {

    private RecyclerView recyclerView ;

    private UsersAdapter usersAdapter;
    private List<User> mUsers;
    EditText search_user;

    public UsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_users, container, false);
       recyclerView = view.findViewById(R.id.recycler_View);
       recyclerView.setHasFixedSize(true);
       recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
       mUsers = new ArrayList<>();
       search_user = view.findViewById(R.id.search_users);
       search_user.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

           }

           @Override
           public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            searchUser(charSequence.toString().toLowerCase());

           }

           @Override
           public void afterTextChanged(Editable editable) {

           }
       });
        readUsers();
        return view;
    }

    private void searchUser(String toString) {
      final FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("username")
                .startAt(toString)
                .endAt(toString+"/uf%ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren() ){
                    User user = snapshot.getValue(User.class);
                    // assert user != null;
                    //assert firebaseUser !=null;
                    if (!user.getUid().equals(firebaseUser.getUid())){
                        mUsers.add(user);
                    }
                    //mUsers.add(user);
                }
                usersAdapter = new UsersAdapter(getContext(),mUsers,false);
                recyclerView.setAdapter(usersAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void readUsers(){
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (search_user.getText().toString().equalsIgnoreCase("")) {
                    mUsers.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);
                        // assert user != null;
                        //assert firebaseUser !=null;
                        if (!user.getUid().equals(firebaseUser.getUid())) {
                            mUsers.add(user);
                        }
                        //mUsers.add(user);
                    }
                    usersAdapter = new UsersAdapter(getContext(), mUsers, false);
                    recyclerView.setAdapter(usersAdapter);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
