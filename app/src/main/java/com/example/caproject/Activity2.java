package com.example.caproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Activity2 extends AppCompatActivity {

    private ImageView facebook, twitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        //setting up top nav bar
        getSupportActionBar().setTitle("Contact Page");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        facebook = findViewById(R.id.facebook);
        twitter = findViewById(R.id.twitter);

        //setting on click for the images
        facebook.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                goToFacebookPage("102275898487369");
            }
        });
        twitter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                goToTwitterPage();
            }
        });
    }
    //launch twitter page
    private void goToTwitterPage() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/Menu_planner"));
        startActivity(intent);
    }
    //launch the facebook page
    private void goToFacebookPage(String id){
        try{
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/"+id));
            startActivity(intent);
        }catch(ActivityNotFoundException e){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/"+id));
        }
    }
}