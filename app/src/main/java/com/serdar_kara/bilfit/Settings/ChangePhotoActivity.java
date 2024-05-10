<<<<<<< Updated upstream
package com.serdar_kara.bilfit.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.serdar_kara.bilfit.FeedbackActivity;
import com.serdar_kara.bilfit.MainActivity;
import com.serdar_kara.bilfit.R;
import com.serdar_kara.bilfit.get_info_activities.UserInfoHolder;
import com.serdar_kara.bilfit.get_info_activities.UserInfoManager;

public class ChangePhotoActivity extends AppCompatActivity {

    private String getCurrentUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getUid();
        } else {
            Toast.makeText(ChangePhotoActivity.this, "Something went wrong. Please try again",
                    Toast.LENGTH_SHORT).show();
            return "0000";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_photo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        UserInfoHolder userInfoHolder = UserInfoManager.getInstance().getUserInfo();
        // double gender = userInfoHolder.getPower();
    }



}
=======
package com.serdar_kara.bilfit.Settings;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_photo);
        profileImageView = findViewById(R.id.imageButton6); // Assume this ID for your ImageView

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        registerGalleryActivityResultLauncher();
        registerPermissionLauncher();

        profileImageView.setOnClickListener(v -> attemptToOpenGallery());
    }

    private void attemptToOpenGallery() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            } else {
                Snackbar.make(profileImageView, "Permission denied permanently. Please enable it from settings.", Snackbar.LENGTH_LONG)
                        .setAction("Settings", v -> {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        }).show();
            }
        }
    }

    private void registerGalleryActivityResultLauncher() {
        galleryActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                imageData = result.getData().getData();
                profileImageView.setImageURI(imageData);
                uploadImageToFirebase();
            }
        });
    }

    private void registerPermissionLauncher() {
        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                openGallery();
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Snackbar.make(profileImageView, "Permission needed to access gallery", Snackbar.LENGTH_INDEFINITE)
                            .setAction("Allow", v -> permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE))
                            .show();
                } else {
                    Snackbar.make(profileImageView, "Permission denied permanently. Please enable it from settings.", Snackbar.LENGTH_LONG)
                            .setAction("Settings", v -> {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            }).show();
                }
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryActivityResultLauncher.launch(intent);
    }

    private void uploadImageToFirebase() {
        if (imageData != null) {
            String userId = firebaseAuth.getCurrentUser().getUid();
            StorageReference fileRef = firebaseStorage.getReference().child("ProfilePhotos/" + userId + ".jpg");
            fileRef.putFile(imageData).addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                updateFirestoreUserProfileImage(uri.toString());
            })).addOnFailureListener(e -> Toast.makeText(ChangePhotoActivity.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show());
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
>>>>>>> Stashed changes
