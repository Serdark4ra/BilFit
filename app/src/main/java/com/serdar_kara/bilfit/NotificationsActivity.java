package com.serdar_kara.bilfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.Timestamp;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.serdar_kara.bilfit.databinding.ActivityNotificationsBinding;
import com.serdar_kara.bilfit.databinding.NotificationRowBinding;
import com.serdar_kara.bilfit.friends.FriendsActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class NotificationsActivity extends AppCompatActivity {

    public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsActivity.NotificationsAdapter.NotificationsViewHolder>{
        private final ArrayList<Map<String, Object>> notifications;
        private final Context context;
        private final FirebaseFirestore db;

        public NotificationsAdapter(Context context, ArrayList<Map<String, Object>> notifications) {
            this.context = context;
            this.notifications = notifications;
            this.db = FirebaseFirestore.getInstance();
        }

        @NonNull
        @Override
        public NotificationsActivity.NotificationsAdapter.NotificationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            NotificationRowBinding binding = NotificationRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new NotificationsActivity.NotificationsAdapter.NotificationsViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull NotificationsActivity.NotificationsAdapter.NotificationsViewHolder holder, int position) {
            String friendUserId = (String) notifications.get(position).get("sender");

            loadUserProfilePhoto(holder.binding, friendUserId);

            db.collection("Users").document(friendUserId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String friendName = documentSnapshot.getString("name_surname");
                            Timestamp timestamp = (Timestamp) notifications.get(position).get("timestamp");
                            holder.bind(friendName, timestamp);
                        } else {
                            Toast.makeText(context, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> Log.d("Friends", "Failed to retrieve friend data"));

            holder.binding.buttonDelete.setOnClickListener(v -> {
                Iterator<Map<String, Object>> iterator = notifications.iterator();
                while (iterator.hasNext()) {
                    Map<String, Object> notification = iterator.next();
                    if (friendUserId.equals(notification.get("sender"))) {
                        // If found, remove the notification from the ArrayList
                        iterator.remove();
                        // Exit the loop since we found and removed the notification
                    }
                }

                db.collection("Users").document(getCurrentUserId())
                        .update("notifications", notifications)
                        .addOnSuccessListener(aVoid -> {
                            // Notification removed successfully from Firestore
                            Toast.makeText(context, "Notification deleted successfully", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            // Error occurred while deleting the notification
                            Toast.makeText(context, "Failed to delete notification", Toast.LENGTH_SHORT).show();
                        });

                // Notify the adapter about the data change
                notifyDataSetChanged();
            });
        }

        @Override
        public int getItemCount() {
            if (notifications == null) {
                return 0;
            }
            return notifications.size();
        }

        private void loadUserProfilePhoto(NotificationRowBinding binding, String userId) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                FirebaseFirestore.getInstance().collection("Users").document(userId)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful() && task.getResult() != null) {
                                DocumentSnapshot doc = task.getResult();
                                if (doc.exists() && doc.contains("profileImage")) {
                                    String photoUrl = doc.getString("profileImage");
                                    if (photoUrl != null && !photoUrl.isEmpty()) {
                                        Glide.with(NotificationsActivity.this)
                                                .load(photoUrl)
                                                .circleCrop() // if you want to apply circle cropping to the image
                                                .into(binding.imageViewNotifications);
                                    }
                                }
                            } else {
                                Toast.makeText(NotificationsActivity.this, "Failed to load user photo.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }

        public class NotificationsViewHolder extends RecyclerView.ViewHolder {
            private NotificationRowBinding binding;

            public NotificationsViewHolder(NotificationRowBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }

            public void bind(String friendName, Timestamp timestamp) {
                long timeDifference = System.currentTimeMillis() - timestamp.toDate().getTime();
                String timeAgo = getTimeAgo(timeDifference);

                binding.textNotificationsName.setText(friendName);
                binding.textTime.setText(timeAgo);
            }

            private String getTimeAgo(long timeDifference) {
                long seconds = timeDifference / 1000;
                if (seconds < 60) {
                    return "Just now";
                }
                long minutes = seconds / 60;
                if (minutes < 60) {
                    return minutes + "m ago";
                }
                long hours = minutes / 60;
                if (hours < 24) {
                    return hours + "h ago";
                }
                return null;
            }
        }
    }

    private RecyclerView recyclerViewNotifications;
    private RecyclerView RecyclerViewNotifications;
    private NotificationsActivity.NotificationsAdapter notificationsAdapter;
    private FirebaseFirestore db;

    private ArrayList<Map<String, Object>> notifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        com.serdar_kara.bilfit.databinding.ActivityNotificationsBinding binding = ActivityNotificationsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Toolbar toolbar = findViewById(R.id.toolbarNotifications);
        setSupportActionBar(toolbar);

        recyclerViewNotifications = findViewById(R.id.recyclerViewNotificaitons);
        recyclerViewNotifications.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        String currentUserId = getCurrentUserId();

        notifications = new ArrayList<>();
        db.collection("Users").document(currentUserId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        notifications = (ArrayList<Map<String,Object>>) documentSnapshot.get("notifications");
                    }
                    notificationsAdapter = new NotificationsActivity.NotificationsAdapter(this, notifications);
                    recyclerViewNotifications.setAdapter(notificationsAdapter);
                });

        toolbar.setNavigationOnClickListener(v -> {
            Log.d("Friends", "B");
            Intent intent = new Intent(NotificationsActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private String getCurrentUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getUid();
        } else {
            Toast.makeText(NotificationsActivity.this, "Something went wrong. Please try again",
                    Toast.LENGTH_SHORT).show();
            return "0000";
        }
    }

}