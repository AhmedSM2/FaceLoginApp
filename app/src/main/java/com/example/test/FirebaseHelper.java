package com.example.test;

import android.support.annotation.NonNull;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseHelper {

    DatabaseReference db;
    Boolean saved=null;

    //ArrayList<String> Areas;



    public FirebaseHelper(DatabaseReference db)
    {
        this.db = db;
    }

    //--------------- Save Area Method-----------------------------------------
    public  Boolean save_Area_DB(Area_class Area_object)
    {
        if(Area_object==null)
        {
            saved=false;
        }
        else
        {
            try
            {
                String AreaName = Area_object.getArea_name();
                double AreaLat = Area_object.getArea_latitude();
                double AreaLng = Area_object.getArea_longitude();

                Area_object = new Area_class(AreaName , AreaLat , AreaLng );

                db.child("Areas").child(AreaName).setValue(Area_object);

                saved=true;

            }
            catch (DatabaseException e)
            {
                e.printStackTrace();

                saved=false;
            }
        }

        return saved;
    }


    //------------------------------------------------------------------------------------------

    //---------------rerieve All Areas Method---------------------------------------------------

    public ArrayList<String> retrieve_All_Areas_DB()
    {

         final ArrayList<String> Areas = new ArrayList<String>();

         db.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot)
             {
                 for(DataSnapshot AreaSnapshot : dataSnapshot.getChildren())
                 {
                     Area_class area_object = AreaSnapshot.getValue(Area_class.class);
                     String Area_name = area_object.getArea_name();
                     Areas.add(Area_name);
                 }

             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });

//        db.addListenerForSingleValueEvent(new ValueEventListener()
//        {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
//            {
//                Area_class area_class = dataSnapshot.getValue(Area_class.class);
//                String Area_name = area_class.getArea_name();
//                Areas.add(Area_name);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError)
//            {
//
//            }
//        });
//
////        db.addChildEventListener(new ChildEventListener()
////        {
////            @Override
////            public void onChildAdded(DataSnapshot dataSnapshot, String s)
////            {
////
////            }
////
////            @Override
////            public void onChildChanged(DataSnapshot dataSnapshot, String s)
////            {
////                //fetchData_Areas(dataSnapshot,Areas);
////
////            }
////
////            @Override
////            public void onChildRemoved(DataSnapshot dataSnapshot)
////            {
////
////            }
////
////            @Override
////            public void onChildMoved(DataSnapshot dataSnapshot, String s)
////            {
////
////            }
////
////            @Override
////            public void onCancelled(@NonNull DatabaseError databaseError)
////            {
////
////            }
////
////        });

       return Areas;
    }
//---------------------------------------------------------------------------------------------------



    //---------------rerieve All Areas Method---------------------------------------------------

    public ArrayList<String> retrieve_All_Categories_DB2()
    {

        final ArrayList<String> Categories = new ArrayList<String>();

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for(DataSnapshot AreaSnapshot : dataSnapshot.getChildren())
                {
                   Category_class category_object = AreaSnapshot.getValue(Category_class.class);
                    String category_name = category_object.getCategory_name();
                    Categories.add(category_name);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        db.addListenerForSingleValueEvent(new ValueEventListener()
//        {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
//            {
//                Area_class area_class = dataSnapshot.getValue(Area_class.class);
//                String Area_name = area_class.getArea_name();
//                Areas.add(Area_name);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError)
//            {
//
//            }
//        });
//
////        db.addChildEventListener(new ChildEventListener()
////        {
////            @Override
////            public void onChildAdded(DataSnapshot dataSnapshot, String s)
////            {
////
////            }
////
////            @Override
////            public void onChildChanged(DataSnapshot dataSnapshot, String s)
////            {
////                //fetchData_Areas(dataSnapshot,Areas);
////
////            }
////
////            @Override
////            public void onChildRemoved(DataSnapshot dataSnapshot)
////            {
////
////            }
////
////            @Override
////            public void onChildMoved(DataSnapshot dataSnapshot, String s)
////            {
////
////            }
////
////            @Override
////            public void onCancelled(@NonNull DatabaseError databaseError)
////            {
////
////            }
////
////        });

        return Categories;
    }
//---------------------------------------------------------------------------------------------------


    //-----------------------fetch areas method---------------------------------------------------------
    private void fetchData_Areas(DataSnapshot snapshot,ArrayList<String> Areas)
    {
        Areas.clear();
        for (DataSnapshot ds:snapshot.getChildren())
        {
            String Area_name = ds.getValue(Area_class.class).getArea_name();

            Areas.add(Area_name);
        }

    }
//--------------------------------------------------------------------------------------------------

//--------------- Save Area Method------------------------------------------------------------------
    public  Boolean save_Category_DB(Category_class Category_object)
    {
        if(Category_object==null)
        {
            saved=false;
        }
        else
        {
            try
            {
                String CategoryName = Category_object.getCategory_name();
                int CategoryType = Category_object.getCategory_type();
                int CategoryRate = Category_object.getCategory_rate();
                double CategoryLat = Category_object.getCategory_latitude();
                double CategoryLng = Category_object.getCategory_longitude();

                Category_object = new Category_class(CategoryName , CategoryRate , CategoryType , CategoryLat , CategoryLng );

                db.child("Categories").push().setValue(Category_object);

                saved=true;

            }
            catch (DatabaseException e)
            {
                e.printStackTrace();

                saved=false;
            }
        }

        return saved;
    }

    //------------------------------------------------------------------------------------------
    public  Boolean save_Category_DB2(Category_class Category_object , String Area_name)
    {
        if(Category_object==null)
        {
            saved=false;
        }
        else
        {
            try
            {
                Area_class Area_object = new Area_class();
                Area_name = Area_object.getArea_name();

                String CategoryName = Category_object.getCategory_name();
                int CategoryType = Category_object.getCategory_type();
                int CategoryRate = Category_object.getCategory_rate();
                double CategoryLat = Category_object.getCategory_latitude();
                double CategoryLng = Category_object.getCategory_longitude();

                Category_object = new Category_class(CategoryName , CategoryRate , CategoryType , CategoryLat , CategoryLng );

                db.child("Categories").push().setValue(Category_object);

                saved=true;

            }
            catch (DatabaseException e)
            {
                e.printStackTrace();

                saved=false;
            }
        }

        return saved;
    }

    //---------------rerieve All Categories Method---------------------------------------------------
    public ArrayList<String> retrieve_All_Categories_DB()
    {
        final ArrayList<String> Categories=new ArrayList<>();

        db.child("Categories").addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                fetchData_Categories(dataSnapshot,Categories);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s)
            {
                fetchData_Categories(dataSnapshot,Categories);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot)
            {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s)
            {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }

        });

        return Categories;
    }
//---------------------------------------------------------------------------------------------------

    //-----------------------fetch areas method---------------------------------------------------------
    private void fetchData_Categories(DataSnapshot snapshot,ArrayList<String> Categories)
    {
        Categories.clear();
        for (DataSnapshot ds:snapshot.getChildren())
        {
            Category_class category_object =  ds.getValue(Category_class.class);
            String category_name = category_object.getCategory_name();
            Categories.add(category_name);

        }

    }
//--------------------------------------------------------------------------------------------------

}
