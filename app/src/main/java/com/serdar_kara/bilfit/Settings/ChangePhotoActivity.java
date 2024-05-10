
package com.serdar_kara.bilfit.Settings;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.serdar_kara.bilfit.R;
import com.serdar_kara.bilfit.databinding.ActivityChangePhotoBinding;

import java.util.HashMap;
import java.util.Map;

public class ChangePhotoActivity extends AppCompatActivity {

    private ImageView profileImageView;
    private Uri imageData;
    private ActivityResultLauncher<Intent> galleryActivityResultLauncher;
    private ActivityResultLauncher<String> permissionLauncher;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firebaseFirestore;

    private ActivityChangePhotoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePhotoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        profileImageView = findViewById(R.id.imageButton6); // Assume this ID for your ImageView

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        registerLauncher();

        binding.profileButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImageToFirebase();
            }
        });

        binding.toolbar.setNavigationOnClickListener(v -> {
            finish();
        });

    }

    public void imageClicked(View view){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_MEDIA_IMAGES)){
                    Snackbar.make(view,"Permission needed to add images",Snackbar.LENGTH_INDEFINITE).setAction("Give Permission", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //req
                            permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
                        }
                    }).show();
                }else{
                    //permiss req
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                }

            }else{
                //gallery
                Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryActivityResultLauncher.launch(intentToGallery);
            }
        }else{
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                    Snackbar.make(view,"Permission needed to add images",Snackbar.LENGTH_INDEFINITE).setAction("Give Permission", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //req
                            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                        }
                    }).show();
                }else{
                    //permiss req
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                    System.out.println("2");
                }

            }else{
                //gallery
                Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryActivityResultLauncher.launch(intentToGallery);
            }
        }


    }

    private void registerLauncher(){
        galleryActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                if (o.getResultCode() == RESULT_OK){
                    Intent intentFromResult = o.getData();
                    if (intentFromResult != null){
                        imageData = intentFromResult.getData();
                        binding.imageButton6.setImageURI(imageData);
                    }
                }
            }
        });
        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean o) {
                if (o){
                    Intent intentToGallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    galleryActivityResultLauncher.launch(intentToGallery);
                }else{
                    Toast.makeText(ChangePhotoActivity.this,"Permission Needed!",Toast.LENGTH_LONG).show();

                }
            }
        });
    }





    private void uploadImageToFirebase() {
        if (imageData != null) {
            String userId = firebaseAuth.getCurrentUser().getUid();
            StorageReference fileRef = firebaseStorage.getReference().child("ProfilePhotos/" + userId + ".jpg");

            UploadTask uploadTask = fileRef.putFile(imageData);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("Firebase", "Upload failed, retrying...", e);
                    uploadImageToFirebase(); // Retry uploading
                }
            }).addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                updateFirestoreUserProfileImage(uri.toString());
            })).addOnFailureListener(e -> {
                Toast.makeText(ChangePhotoActivity.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else {
            Toast.makeText(this, "No image selected to upload.", Toast.LENGTH_LONG).show();
        }
    }



    private void updateFirestoreUserProfileImage(String imageUrl) {
        Map<String, Object> userUpdates = new HashMap<>();
        userUpdates.put("profileImage", imageUrl);

        firebaseFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid())
                .update(userUpdates)
                .addOnSuccessListener(aVoid -> Toast.makeText(ChangePhotoActivity.this, "Profile Image Updated", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(ChangePhotoActivity.this, "Failed to update profile image.", Toast.LENGTH_SHORT).show());
    }
}

