package com.serdar_kara.bilfit.friends;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.serdar_kara.bilfit.EditFriendsActivity;
import com.serdar_kara.bilfit.MainActivity;
import com.serdar_kara.bilfit.R;
import com.serdar_kara.bilfit.databinding.ActivityFriendsBinding;
import com.serdar_kara.bilfit.databinding.FriendRequestRowBinding;
import com.serdar_kara.bilfit.databinding.FriendRowBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class FriendsActivity extends AppCompatActivity {
    public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder> {
        private static final String PREFS_NAME = "ButtonTimestamp";
        private static final String TIMESTAMP_KEY_PREFIX = "ButtonTimestamp_";
        private final List<String> friendsList;
        private final Context context;
        private final FirebaseFirestore db;
        private final SharedPreferences sharedPreferences;

        public FriendsAdapter(Context context, List<String> friendsList) {
            this.context = context;
            this.friendsList = friendsList;
            this.db = FirebaseFirestore.getInstance();
            this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }

        @NonNull
        @Override
        public FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            FriendRowBinding binding = FriendRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new FriendsViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull FriendsViewHolder holder, int position) {

            String friendUserId = friendsList.get(position);
            loadUserProfilePhoto(holder.binding, friendUserId);

            String timestampKey = TIMESTAMP_KEY_PREFIX + friendUserId;
            long buttonClickTimestamp = sharedPreferences.getLong(timestampKey, 0);

            if (buttonClickTimestamp != 0 && isWithinTenMinutes(buttonClickTimestamp)) {
                long elapsedTime = System.currentTimeMillis() - buttonClickTimestamp;
                if (TimeUnit.MILLISECONDS.toMinutes(elapsedTime) < 10) {
                    // Button was clicked within the last 10 minutes, disable it and start the timer
                    holder.binding.buttonGoTogether.setEnabled(false);
                    startTimer(holder, buttonClickTimestamp, friendUserId);
                }
            }

            db.collection("Users").document(friendUserId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String friendName = documentSnapshot.getString("name_surname");
                            holder.bind(friendName);
                        } else {
                            Toast.makeText(context, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> Log.d("Friends", "Failed to retrieve friend data"));

            holder.binding.buttonGoTogether.setOnClickListener(v -> {
                sendNotificationToFriend(friendUserId);
                holder.binding.buttonGoTogether.setEnabled(false);
                startTimer(holder, System.currentTimeMillis(), friendUserId);
            });

            holder.binding.buttonCheckProfile.setOnClickListener(v -> {
                db.collection("Users").document(friendUserId).get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                String friendName = documentSnapshot.getString("name_surname");
                                long points = documentSnapshot.getLong("points");
                                Toast.makeText(context, "Points of " + friendName + ": " + points, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(context, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(e -> Log.d("Friends", "Failed to retrieve friend data"));
            });

        }

        @Override
        public int getItemCount() {
            return friendsList.size();
        }

        private void startTimer(FriendsViewHolder holder, long startTime, String friendUserId) {
            new CountDownTimer(TimeUnit.MINUTES.toMillis(10) - (System.currentTimeMillis() - startTime), 1000) {
                public void onTick(long millisUntilFinished) {
                    String timeLeft = String.format(Locale.getDefault(), "%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                    holder.binding.buttonGoTogether.setText(timeLeft);
                }

                @SuppressLint("SetTextI18n")
                public void onFinish() {
                    holder.binding.buttonGoTogether.setEnabled(true);
                    holder.binding.buttonGoTogether.setText("Go Together");
                }
            }.start();

            // Save timestamp when the button is clicked
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong(TIMESTAMP_KEY_PREFIX + friendUserId, startTime);
            editor.apply();
        }
        private boolean isWithinTenMinutes(long timestamp) {
            long elapsedTime = System.currentTimeMillis() - timestamp;
            return TimeUnit.MILLISECONDS.toMinutes(elapsedTime) < 10;
        }

        private void sendNotificationToFriend(String friendUserId) {
            Map<String, Object> notificationData = goTogether();
            db.collection("Users").document(friendUserId).update("notifications", FieldValue.arrayUnion(notificationData))
                    .addOnSuccessListener(documentReference ->
                            Toast.makeText(context, "Notification sent successfully!!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e ->
                            Toast.makeText(context, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show());
        }

        private Map<String, Object> goTogether() {
            Map<String, Object> goTogetherNotification = new HashMap<>();
            goTogetherNotification.put("sender", getCurrentUserId());
            goTogetherNotification.put("timestamp", new Date());
            return goTogetherNotification;
        }

        public class FriendsViewHolder extends RecyclerView.ViewHolder {
            private FriendRowBinding binding;

            public FriendsViewHolder(FriendRowBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }

            public void bind(String friendName) {
                binding.textFriendName.setText(friendName);
                binding.textFriendUsername.setText(friendName);
            }
        }

        private void loadUserProfilePhoto(FriendRowBinding binding, String userId) {
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
                                        Glide.with(FriendsActivity.this)
                                                .load(photoUrl)
                                                .circleCrop() // if you want to apply circle cropping to the image
                                                .into(binding.imageViewFriend);
                                    }
                                }
                            } else {
                                Toast.makeText(FriendsActivity.this, "Failed to load user photo.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }

    private class FriendRequestsAdapter extends RecyclerView.Adapter<FriendRequestsAdapter.FriendRequestsViewHolder> {
        private final List<String> friendRequestsList;

        public FriendRequestsAdapter(List<String> friendRequestsList) {
            this.friendRequestsList = friendRequestsList;
        }

        @NonNull
        @Override
        public FriendRequestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            FriendRequestRowBinding binding = FriendRequestRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
            return new FriendRequestsViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull FriendRequestsViewHolder holder, int position) {
            String friendUserId = String.valueOf(friendRequestsList.get(position));
            loadUserProfilePhoto(holder.binding, friendUserId);
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Load user data based on friendUserId
            db.collection("Users").document(friendUserId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Set user data to views
                            holder.binding.textRequestName.setText(documentSnapshot.getString("name_surname"));
                            holder.binding.textRequestUsername.setText(documentSnapshot.getString("username"));
                            holder.binding.imageViewRequest.setImageResource(R.drawable.ic_launcher_background);
                        } else {
                            Toast.makeText(FriendsActivity.this, "User not found. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(FriendsActivity.this, "Failed to load user data. Please try again.", Toast.LENGTH_SHORT).show());

            // Accept button click listener
            holder.binding.buttonAccept.setOnClickListener(v -> {
                // Remove request from request list in Firestore
                db.collection("Users").document(getCurrentUserId())
                        .update("friendRequests", FieldValue.arrayRemove(friendUserId))
                        .addOnSuccessListener(aVoid -> {
                            db.collection("Users").document(friendUserId)
                                    .update("friends", FieldValue.arrayUnion(getCurrentUserId()))
                                    .addOnFailureListener(e -> Toast.makeText(FriendsActivity.this, "Failed to accept friend request. Please try again.", Toast.LENGTH_SHORT).show());
                            // Add user to friend list in Firestore
                            db.collection("Users").document(getCurrentUserId())
                                    .update("friends", FieldValue.arrayUnion(friendUserId))
                                    .addOnSuccessListener(aVoid1 -> {
                                        // Remove item from RecyclerView and update UI
                                        friendRequestsList.remove(position);
                                        FriendsActivity.this.recreate();
                                        Toast.makeText(FriendsActivity.this, "Friend request accepted.", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> Toast.makeText(FriendsActivity.this, "Failed to accept friend request. Please try again.", Toast.LENGTH_SHORT).show());
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(FriendsActivity.this, "Failed to accept friend request. Please try again.", Toast.LENGTH_SHORT).show());
            });

            // Deny button click listener
            holder.binding.buttonDeny.setOnClickListener(v -> {
                // Remove request from request list in Firestore
                db.collection("Users").document(getCurrentUserId())
                        .update("friendRequests", FieldValue.arrayRemove(friendUserId))
                        .addOnSuccessListener(aVoid -> {
                            // Remove item from RecyclerView and update UI
                            friendRequestsList.remove(position);
                            FriendsActivity.this.recreate();
                            Toast.makeText(FriendsActivity.this, "Friend request denied.", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> Toast.makeText(FriendsActivity.this, "Failed to deny friend request. Please try again.", Toast.LENGTH_SHORT).show());
            });
        }

        @Override
        public int getItemCount() {

            return friendRequestsList.size();
        }

        private void loadUserProfilePhoto(FriendRequestRowBinding binding, String userId) {
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
                                        Glide.with(FriendsActivity.this)
                                                .load(photoUrl)
                                                .circleCrop() // if you want to apply circle cropping to the image
                                                .into(binding.imageViewRequest);
                                    }
                                }
                            } else {
                                Toast.makeText(FriendsActivity.this, "Failed to load user photo.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }

        public class FriendRequestsViewHolder extends RecyclerView.ViewHolder {
            private final FriendRequestRowBinding binding;
            public FriendRequestsViewHolder(FriendRequestRowBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }

    private RecyclerView recyclerViewFriends;
    private RecyclerView recyclerViewFriendRequests;
    private FriendsAdapter friendsAdapter;
    private FriendRequestsAdapter friendRequestsAdapter;
    private ArrayList<String> friendsList;
    private ArrayList<String> friendRequestsList;
    private FirebaseFirestore db;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Friends", "A");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        com.serdar_kara.bilfit.databinding.ActivityFriendsBinding binding = ActivityFriendsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        Toolbar toolbar = findViewById(R.id.toolbarFriend);
        setSupportActionBar(toolbar);

        // Initialize RecyclerViews and layout managers
        recyclerViewFriends = findViewById(R.id.recyclerViewFriends);
        recyclerViewFriends.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewFriendRequests = findViewById(R.id.recyclerViewFriendRequests);
        recyclerViewFriendRequests.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        String currentUserId = getCurrentUserId();

        // Add an onClickListener to the navigation icon
        toolbar.setNavigationOnClickListener(v -> {
            Log.d("Friends", "B");
            Intent intent = new Intent(FriendsActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
        Log.d("Friends", "C");

        friendsList = new ArrayList<>();
        db.collection("Users").document(currentUserId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        friendsList = (ArrayList<String>) documentSnapshot.get("friends");
                    }
                    friendsAdapter = new FriendsAdapter(this, friendsList);
                    recyclerViewFriends.setAdapter(friendsAdapter);
                });

        Log.d("Friends", "Good so far");

        friendRequestsList = new ArrayList<>();
        db.collection("Users").document(currentUserId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        friendRequestsList = (ArrayList<String>) documentSnapshot.get("friendRequests");
                        Log.d("Friends", "Still good");
                        if (friendRequestsList.isEmpty()) {
                            // If empty, set the height of recyclerViewFriendRequests to wrap_content
                            ViewGroup.LayoutParams params = recyclerViewFriendRequests.getLayoutParams();
                            params.height = 0;
                            recyclerViewFriendRequests.setLayoutParams(params);
                        } else {
                            Log.d("Friends", "Not good");
                            // Initialize and set adapter for friend requests RecyclerView
                            friendRequestsAdapter = new FriendRequestsAdapter(friendRequestsList);
                            recyclerViewFriendRequests.setAdapter(friendRequestsAdapter);

                            // If not empty, set a fixed height for recyclerViewFriendRequests
                            int desiredHeight = 500;
                            ViewGroup.LayoutParams params = recyclerViewFriendRequests.getLayoutParams();
                            params.height = desiredHeight;
                            recyclerViewFriendRequests.setLayoutParams(params);

                        }
                        Log.d("Friends", "BBB");
                    } else {
                        Toast.makeText(FriendsActivity.this, "Error in showing requests", Toast.LENGTH_SHORT).show();
                    }
                });

        binding.addFriendButton.setOnClickListener(v -> showAddFriendDialog(friendsList));
        binding.editFriendButton.setOnClickListener(v -> {
            if (friendsList.size() == 0)
            {
                Toast.makeText(FriendsActivity.this, "No friends to edit", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(FriendsActivity.this, EditFriendsActivity.class);
                startActivity(intent);
            }
        });
    }
    private String getCurrentUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getUid();
        } else {
            Toast.makeText(FriendsActivity.this, "Something went wrong. Please try again",
                    Toast.LENGTH_SHORT).show();
            return "0000";
        }
    }
    private void showAddFriendDialog(ArrayList<String> friendsList) {
        AtomicBoolean isFound = new AtomicBoolean(false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Friend");

        // Set up the layout for the dialog
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(16, 0, 16, 0);

        EditText editTextUsername = new EditText(this);
        editTextUsername.setHint("Enter Username");
        layout.addView(editTextUsername, params);

        builder.setView(layout);

        // Set up the buttons
        builder.setPositiveButton("Add", (dialog, which) -> {
            String username = editTextUsername.getText().toString().trim();


            // Find the user
            db.collection("Users")
                    .whereEqualTo("name_surname", username)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            QueryDocumentSnapshot documentSnapshot = (QueryDocumentSnapshot) queryDocumentSnapshots.getDocuments().get(0); // Get the first document
                            String userId = documentSnapshot.getId();

                            if ( userId.equals(getCurrentUserId())) {
                                Toast.makeText(FriendsActivity.this, "You cannot send a request to yourself!",Toast.LENGTH_SHORT).show();
                            }else{

                                    for (int i = 0; i < friendsList.size() && !isFound.get(); i++) {
                                        if (friendsList.get(i).equals(userId)) {
                                            isFound.set(true);
                                        }
                                    }

                                if ( isFound.equals(true) ) {
                                    Toast.makeText(FriendsActivity.this, username + " is already in the friend list!", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    // Add friend to the request list of the target user
                                    DocumentReference targetUserRef = db.collection("Users").document(userId);

                                    targetUserRef.update("friendRequests", FieldValue.arrayUnion(getCurrentUserId()))
                                            .addOnSuccessListener(aVoid -> {
                                                // Successful
                                                Toast.makeText(FriendsActivity.this, "Friend request sent to user ", Toast.LENGTH_SHORT).show();
                                            })
                                            .addOnFailureListener(e -> {
                                                // Error
                                                Toast.makeText(FriendsActivity.this, "Failed to send friend request. Please try again.", Toast.LENGTH_SHORT).show();
                                            });
                                }
                            }
                        } else {
                            // No user
                            Toast.makeText(FriendsActivity.this, "User not found with username: " + username, Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Handle any errors
                        Toast.makeText(FriendsActivity.this, "Error searching for user. Please try again.", Toast.LENGTH_SHORT).show();
                    });
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}