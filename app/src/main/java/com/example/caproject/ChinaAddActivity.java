package com.example.caproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class ChinaAddActivity extends AppCompatActivity {

    private ImageView addPicIcon;
    private Button addBtn;
    private TextView addtitleTv, addDescTv, addPriceTv;
    private EditText titleEt, descEt, priceEt;
    //codes for popup permissions
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 300;
    //codes for popup permissions
    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;

    private String[] cameraPermissions;
    private String[] storagePermissions;
    //firebase connection wth pictures uri path
    private Uri image_uri;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_china_add);
        //toolbar
        getSupportActionBar().setTitle("Add To Menu");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addBtn = findViewById(R.id.addToMenuBtn);
        addPicIcon = findViewById(R.id.addPicIcon);
        titleEt = findViewById(R.id.titleEditText);
        descEt = findViewById(R.id.descEditText);
        priceEt = findViewById(R.id.priceEditText);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Wait");
        progressDialog.setCanceledOnTouchOutside(false);

        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        //picture at the top of page
        addPicIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //show the pop up to pick camera or gallery
                showImagePicked();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                inputData();
            }
        });
    }

    private String foodTitle, foodDescritpion, foodPrice;
    private void inputData(){
        //getting items in the fields
        foodTitle = titleEt.getText().toString().trim();
        foodDescritpion = descEt.getText().toString().trim();
        foodPrice = priceEt.getText().toString().trim();

        //check if data entered is valid
        if(TextUtils.isEmpty(foodTitle)){
            Toast.makeText(this, "Name is needed", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(foodDescritpion)){
            Toast.makeText(this, "Description is needed", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(foodPrice)){
            Toast.makeText(this, "price is needed", Toast.LENGTH_SHORT).show();
            return;
        }
        addItem();
    }
    //needed for adding to database
    private void addItem(){
        progressDialog.setMessage("Adding to menu");
        progressDialog.show();
        //timestamp is taking as the items id
        String timestamp = ""+System.currentTimeMillis();
        if(image_uri == null){
            //no picture
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("foodId",""+timestamp);
            hashMap.put("foodTitle",""+foodTitle);
            hashMap.put("foodDescritpion",""+foodDescritpion);
            hashMap.put("foodPrice",""+foodPrice);
            hashMap.put("foodIcon","");
            hashMap.put("timestamp",""+timestamp);
            hashMap.put("uid",""+firebaseAuth.getUid());
            //inserting into firebase
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Menus");
            reference.child(firebaseAuth.getUid()).child("ChinaFood").child(timestamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>(){
                @Override
                public void onSuccess(Void aVoid){
                    //message displays if added
                    progressDialog.dismiss();
                    Toast.makeText(ChinaAddActivity.this, "Added to menu", Toast.LENGTH_SHORT).show();
                    clearData();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e){
                    //message displays if something failed
                    progressDialog.dismiss();
                    Toast.makeText(ChinaAddActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            //setting path in database to store with picture
            String filePathName = "food_images/"+""+timestamp;
            StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathName);
            storageReference.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //if the picture gets uploaded this displays image
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while(!uriTask.isSuccessful());
                    Uri downloadImageUri = uriTask.getResult();

                    if(uriTask.isSuccessful()){
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("foodId",""+timestamp);
                        hashMap.put("foodTitle",""+foodTitle);
                        hashMap.put("foodDescritpion",""+foodDescritpion);
                        hashMap.put("foodPrice",""+foodPrice);
                        hashMap.put("foodIcon",""+downloadImageUri);
                        hashMap.put("timestamp",""+timestamp);
                        hashMap.put("uid",""+firebaseAuth.getUid());
                        //inserting into firebase menu path
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Menus");
                        reference.child(firebaseAuth.getUid()).child("ChinaFood").child(timestamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>(){
                            @Override
                            public void onSuccess(Void aVoid){
                                progressDialog.dismiss();
                                Toast.makeText(ChinaAddActivity.this, "Added to menu", Toast.LENGTH_SHORT).show();
                                clearData();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e){
                                progressDialog.dismiss();
                                Toast.makeText(ChinaAddActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(ChinaAddActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private void clearData(){
        //getting text areas to blank
        titleEt.setText("");
        descEt.setText("");
        priceEt.setText("");
        addPicIcon.setImageResource(R.drawable.ic_add_pic_iv);
        image_uri = null;
    }

    private void showImagePicked() {
        //getting permissions
        String[] options = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Image").setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which){
                if(which == 0){
                    //Camera selected
                    if(checkCameraPermission()){
                        //camera allowed
                        pickFromCamera();
                    }
                    else{
                        //no permission
                        requestCameraPermission();
                    }
                }
                else{
                    //Gallery selected
                    if(checkStoragePermission()){
                        //permission given
                        pickFromGallery();
                    }
                    else{
                        // no permission
                        requestStoragePermission();
                    }
                }
            }
        }).show();
    }

    //taking picture from gallery
    private void pickFromGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }
    //taking picture from camera
    private void pickFromCamera(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_Image_Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Image_Description");

        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }
    // permissions handlers
    private boolean checkStoragePermission(){
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }
    // permissions handlers
    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }
    // permissions handlers
    private boolean checkCameraPermission(){
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }
    // permissions handlers
    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }


    //permissions requests
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        switch (requestCode){
            //camera request
            case CAMERA_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean cameraAccepted= grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted= grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && storageAccepted){
                        //when user accepts both
                        pickFromCamera();
                    }
                    else{
                        //if one or more are not accepted.
                        Toast.makeText(this, "Camera and Storage is required!", Toast.LENGTH_LONG).show();
                    }
                }
            }
            //storage request
            case STORAGE_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean storageAccepted= grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(storageAccepted){
                        pickFromGallery();
                    }
                    else{
                        Toast.makeText(this, "Storage is required!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        if(resultCode == RESULT_OK){
            if(requestCode == IMAGE_PICK_GALLERY_CODE){
                //setting an image picked from gallery to the icon
                image_uri = data.getData();
                addPicIcon.setImageURI(image_uri);
            }
            else if(requestCode == IMAGE_PICK_CAMERA_CODE){
                //setting an image picked from Camera to the icon
                addPicIcon.setImageURI(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}