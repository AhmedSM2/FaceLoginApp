package com.example.test;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class custompopupActivity extends AppCompatActivity
{

    private Spinner sp_area , sp_category; //spinner
    private DatabaseReference AbuEl3orif_DB_Ref;
    private Button btn_filter;
    String selected_Area , selected_category;
    private Dialog AskQueDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custompopup1);

        btn_filter = findViewById(R.id.btn_map_filter);
        final List<String> areas = new ArrayList<String>();
        final List<String> categories = new ArrayList<String>();
        AbuEl3orif_DB_Ref  = FirebaseDatabase.getInstance().getReference().child("AbuEl3orifDB");
        AskQueDialog = new Dialog(this);
        AskQueDialog.setContentView(R.layout.custompopup1);


        //-------------------------------for spinner areas----------------------------------------------------


        AbuEl3orif_DB_Ref.child("Areas").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                areas.clear();

                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren())
                {
                    Area_class area_object = areaSnapshot.getValue(Area_class.class);
                    String Area_name = area_object.getArea_name();
                    areas.add(Area_name);
                }

                sp_area = AskQueDialog.findViewById(R.id.sp_Qarea_filter);
                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(custompopupActivity.this, android.R.layout.simple_spinner_item, areas);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_area.setAdapter(areasAdapter);

                sp_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                    {
                        selected_Area = areas.get(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //--------------------------------------------------------------------------------------------------------------


        //-------------------------------for spinner category----------------------------------------------------


        AbuEl3orif_DB_Ref.child("categories").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                areas.clear();

                for (DataSnapshot categorySnapshot: dataSnapshot.getChildren())
                {

                    String category_key = categorySnapshot.getKey();
                    categories.add(category_key);
                }

                sp_category = AskQueDialog.findViewById(R.id.sp_Qcategory_filter);
                ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<String>(custompopupActivity.this, android.R.layout.simple_spinner_item, categories);
                categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_category.setAdapter(categoriesAdapter);

                sp_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                    {
                        selected_category = categories.get(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //--------------------------------------------------------------------------------------------------------------



    }
}
