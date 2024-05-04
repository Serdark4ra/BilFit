package com.serdar_kara.bilfit.friends;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.serdar_kara.bilfit.MainActivity;
import com.serdar_kara.bilfit.R;
import com.serdar_kara.bilfit.databinding.ActivityFriendsBinding;
import com.serdar_kara.bilfit.databinding.FriendRequestRowBinding;
import com.serdar_kara.bilfit.databinding.FriendRowBinding;

import java.util.ArrayList;
import java.util.List;
public class FriendsActivity extends AppCompatActivity {

    private class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder> {

        private List<String> friendsList;

        public FriendsAdapter(List<String> friendsList) {
            Log.d("Item", String.valueOf(friendsList.size()));
            this.friendsList = friendsList;
        }

        @NonNull
        @Override
        public FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            FriendRowBinding binding = FriendRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
            return new FriendsViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull FriendsViewHolder holder, int position) {
            System.out.println("Here");
            String friendUserId = String.valueOf(friendsList.get(position));
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Users").document(friendUserId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            //FirebaseUser friend = documentSnapshot.toObject(FirebaseUser.class);
                            holder.binding.textFriendName.setText(documentSnapshot.getString("name_surname"));
                            holder.binding.textFriendUsername.setText(documentSnapshot.getString("name_surname"));
                            holder.binding.imageViewFriend.setImageResource(R.drawable.ic_launcher_background);
                            Log.d("Friends", friendUserId);
                        } else {
                            Toast.makeText(FriendsActivity.this, "Something went wrong. Please try again",
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> Log.d("Friends", "Fail"));

            holder.binding.buttonGoTogether.setOnClickListener(v -> {
                // TODO
            });

            holder.binding.buttonCheckProfile.setOnClickListener(v -> {
                //TODO
            });
        }

        @Override
        public int getItemCount() {
            return friendsList.size();
        }

        public class FriendsViewHolder extends RecyclerView.ViewHolder {
            private FriendRowBinding binding;
            public FriendsViewHolder(FriendRowBinding binding) {
                super(binding.getRoot());
                this.binding = binding;

            }
        }
    }

    private class FriendRequestsAdapter extends RecyclerView.Adapter<FriendRequestsAdapter.FriendRequestsViewHolder> {
        private List<String> friendRequestsList;

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
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Users").document(friendUserId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            holder.binding.textRequestName.setText(documentSnapshot.getString("name_surname"));
                            holder.binding.textRequestUsername.setText(documentSnapshot.getString("name_surname"));
                            holder.binding.imageViewRequest.setImageResource(R.drawable.ic_launcher_background);
                        } else {
                            Toast.makeText(FriendsActivity.this, "Something went wrong. Please try again",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

            holder.binding.buttonAccept.setOnClickListener(v -> {
                //TODO
            });

            holder.binding.buttonDeny.setOnClickListener(v -> {
                //TODO
            });
        }

        @Override
        public int getItemCount() {

            return friendRequestsList.size();
        }

        public class FriendRequestsViewHolder extends RecyclerView.ViewHolder {
            private FriendRequestRowBinding binding;
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
                    friendsAdapter = new FriendsAdapter(friendsList);
                    recyclerViewFriends.setAdapter(friendsAdapter);
                });

        Log.d("Friends", "Good so far");

        friendRequestsList = new ArrayList<>();
        db.collection("Users").document(currentUserId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        friendRequestsList = (ArrayList<String>) documentSnapshot.get("friendRequests");
                        System.out.println(friendRequestsList.get(0));
                        Log.d("Friends", "Still good");
                        if (friendRequestsList.isEmpty()) {
                            // If empty, set the height of recyclerViewFriendRequests to wrap_content
                            Log.d("Friends", "Hmmm");
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

        binding.addFriendButton.setOnClickListener(v -> showAddFriendDialog());
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
    private void showAddFriendDialog() {
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

                            // Add friend to the request list of the target user
                            DocumentReference targetUserRef = db.collection("Users").document(userId);
                            
                            targetUserRef.update("friendRequests", FieldValue.arrayUnion(getCurrentUserId()))
                                    .addOnSuccessListener(aVoid -> {
                                        // Successful
                                        Toast.makeText(FriendsActivity.this, "Friend request sent to user: " + userId, Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        // Error
                                        Toast.makeText(FriendsActivity.this, "Failed to send friend request. Please try again.", Toast.LENGTH_SHORT).show();
                                    });
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