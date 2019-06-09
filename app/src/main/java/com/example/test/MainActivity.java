package com.example.test;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
{
   FragmentActivity fA;
   private NavigationView navigationView;
   private DrawerLayout drawerLayout;
   private RecyclerView post_list;
   private Toolbar mToolbar;
   private ActionBarDrawerToggle actionBarDrawerToggle;

   private CircleImageView navProfileImage;
   private TextView navUsername;

   private String CurrentUserId ;
   private DatabaseReference userRef;
   private FirebaseAuth mAuth;//to check user authentication loggedin or not

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.main_page_toolbar);//call toolbar
        drawerLayout = findViewById(R.id.darw_layout);
        navigationView = findViewById(R.id.nav_view);


        userRef = FirebaseDatabase.getInstance().getReference().child("users");
        mAuth = FirebaseAuth.getInstance();//to check user authentication loggedin or not

        CurrentUserId = mAuth.getCurrentUser().getUid();

        //--------to open side menue otmatically-----------------------------//
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout,mToolbar ,R.string.drawer_open,R.string.drawer_close)
        {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };


        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        //-----------------------------------------------------------------------
        View nav_view=navigationView.inflateHeaderView(R.layout.navigation_header);//to get view of navigation header

        navProfileImage = nav_view.findViewById(R.id.nav_profile_img);
        navUsername = nav_view.findViewById(R.id.nav_username);

        userRef.child(CurrentUserId).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    String userFullName = dataSnapshot.child("fullname").getValue().toString();
                   // String userProfileImage = dataSnapshot.child("profileImage").getValue().toString();

                    navUsername.setText(userFullName);

                   // Picasso.with(MainActivity.this).load(userProfileImage).placeholder(R.drawable.profile_icon).into(navProfileImage);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //-----------------------------------------------------------------------

        //--------------------------------------------------------------------
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Home");//to set title to toolbar
        //---------------------------------------------------------------------


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                userMenueSelected(item);
                return false;
            }
        });

    }


    //-----------//to check user authentication loggedin or not//------------------------

    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null)
        {
            sendUserToLoginActivity();
        }
    }

    private void sendUserToLoginActivity()
    {
        Intent goToLoginActivity = new Intent(getApplicationContext(),Login_Activity.class);
        goToLoginActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(goToLoginActivity);
        finish();
    }

    //-----------------------------------------------------------------------------------


    //--------------------------to make drwaer open when click on side button--------------


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(actionBarDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //------------------------------------------------------------------------------------------


  //--------------to make action to each chosse ------------------------------------------------
    public void userMenueSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.nav_profile:
                Toast.makeText(this, "profile icon Action", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nave_home:
                Toast.makeText(this, "Home icon Action", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_saved_comments:
                Toast.makeText(this, "saved comment icon Action", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_followers:
                Toast.makeText(this, "followers icon Action", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_settings:
                Toast.makeText(this, "settings icon Action", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_logout:

                mAuth.signOut();
                sendUserToLoginActivity();
                break;
        }

    }

    //---------------------------------------------------------------------------------------------------
}
