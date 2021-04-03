package com.example.caproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_GET = 2;
    static final int MY_PERMISSIONS_REQUEST_CAMERA = 3;
    Button b1;
    Button rest1Btn;

    Animation animation;

    private CardView burgerbun, china;


    private Button contact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();
        //getting buttons variables
        burgerbun = (CardView)findViewById(R.id.burgerButton);
        china = (CardView)findViewById(R.id.chinaButton);

        //start page animation of fade
        animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade);
        burgerbun.startAnimation(animation);
        china.startAnimation(animation);

        //setting onclicklisteners
        burgerbun.setOnClickListener(this);
        china.setOnClickListener(this);

        contact = (Button) findViewById(R.id.buttonAct2);

        contact.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(),Activity2.class);
                startActivity(intent);
            }
        });

        FloatingActionButton fab = findViewById(R.id.cameraFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                takePicture();
            }
        });
    }




    private void cameraIntentCode() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void takePicture() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
        } else {
            cameraIntentCode();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //if given permission
                    cameraIntentCode();
                } else {
                    Toast.makeText(getApplicationContext(),"Permission denied", Toast.LENGTH_SHORT).show();
                    // if user doesn't allow
                }
                return;
            }
        }

    }

    @Override
    public void onClick(View view) {
        Intent i;

        switch (view.getId()){
            case R.id.burgerButton : i = new Intent(this, restaurantActivity1.class);
            startActivity(i);
            break;

            case R.id.chinaButton : i = new Intent(this, ChinaRest.class);
                startActivity(i);
                break;

            default:break;
        }
    }
}