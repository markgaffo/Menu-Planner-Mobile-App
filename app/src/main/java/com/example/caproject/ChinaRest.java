package com.example.caproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChinaRest extends AppCompatActivity {

    private ImageButton addBtn;
    private EditText searchFoodEt;
    private TextView test, filteredFoodTv;
    private RecyclerView foodRv;
    private ArrayList<ModelFoodChina> foodList;
    private AdapterFoodChina adapterFoodChina;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_china_rest);
        //toolbar
        getSupportActionBar().setTitle("China Palace");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        filteredFoodTv = findViewById(R.id.filteredFoodTv);

        addBtn = findViewById(R.id.addBtn);
        searchFoodEt = findViewById(R.id.searchFoodEt);
        foodRv = findViewById(R.id.foodRv);

        firebaseAuth = FirebaseAuth.getInstance();

        //function to go to database where items are held
        loadAllFoods();

        //see what user enters in search
        searchFoodEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                //if search bar text is updated
                try{
                    adapterFoodChina.getFilter().filter(s);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                // adding an item to the menu
                Intent intent = new Intent(getApplicationContext(),ChinaAddActivity.class);
                startActivity(intent);
            }
        });

    }


    //get all food items on menu
    private void loadAllFoods() {
        //holds all the food in the database in the array
        foodList = new ArrayList<>();
        //getting database with path
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Menus");
        reference.child(firebaseAuth.getUid()).child("ChinaFood")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //if an item is added into the database a snapshot is taken
                        //the new data is then added into a empty foodlist
                        foodList.clear();
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            ModelFoodChina modelFoodChina = ds.getValue(ModelFoodChina.class);
                            foodList.add(modelFoodChina);
                        }
                        adapterFoodChina = new AdapterFoodChina(ChinaRest.this, foodList);

                        foodRv.setAdapter(adapterFoodChina);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }
}