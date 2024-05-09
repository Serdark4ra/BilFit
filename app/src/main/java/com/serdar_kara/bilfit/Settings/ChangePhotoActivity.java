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