package com.serdar_kara.bilfit.friends;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.serdar_kara.bilfit.MainActivity;
import com.serdar_kara.bilfit.R;
import com.serdar_kara.bilfit.databinding.ActivityFriendsBinding;
import com.serdar_kara.bilfit.databinding.FriendRequestRowBinding;
import com.serdar_kara.bilfit.databinding.FriendRowBinding;

import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.LinearLayout;
public class FriendsActivity extends AppCompatActivity {

    private class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder> {

        private List<FirebaseUser> friendsList;

        public FriendsAdapter(List<FirebaseUser> friendsList) {
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
            String friendUserId = String.valueOf(friendsList.get(position));
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Users").document(friendUserId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            FirebaseUser friend = documentSnapshot.toObject(FirebaseUser.class);
                            holder.binding.textFriendName.setText(friend.getDisplayName());
                            holder.binding.textFriendUsername.setText(friend.getDisplayName());
                            holder.binding.imageViewFriend.setImageResource(R.drawable.ic_launcher_background);
                        } else {
                            Toast.makeText(FriendsActivity.this, "Something went wrong. Please try again",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

            holder.binding.buttonGoTogether.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO
                }
            });

            holder.binding.buttonCheckProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO
                }
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
        private List<FirebaseUser> friendRequestsList;

        public FriendRequestsAdapter(List<FirebaseUser> friendRequestsList) {
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
            String friendUserId = String.valueOf(friendsList.get(position));
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Users").document(friendUserId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            FirebaseUser friend = documentSnapshot.toObject(FirebaseUser.class);
                            holder.binding.textRequestName.setText(friend.getDisplayName());
                            holder.binding.textRequestUsername.setText(friend.getDisplayName());
                            holder.binding.imageViewRequest.setImageResource(R.drawable.ic_launcher_background);
                        } else {
                            Toast.makeText(FriendsActivity.this, "Something went wrong. Please try again",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

            holder.binding.buttonAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO
                }
            });

            holder.binding.buttonDeny.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO
                }
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

    private String currentUserId;
    private RecyclerView recyclerViewFriends;
    private RecyclerView recyclerViewFriendRequests;
    private FriendsAdapter friendsAdapter;
    private FriendRequestsAdapter friendRequestsAdapter;
    private ArrayList<FirebaseUser> friendsList;
    private ArrayList<FirebaseUser> friendRequestsList;
    private FirebaseFirestore db;

    private ActivityFriendsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        binding = ActivityFriendsBinding.inflate(getLayoutInflater());
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
        currentUserId = getCurrentUserId();

        // Add an onClickListener to the navigation icon
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        friendsList = new ArrayList<>();
        db.collection("Users").document(currentUserId).collection("friends")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            FirebaseUser friend = document.toObject(FirebaseUser.class);
                            friendsList.add(friend);
                        }
                        // Initialize and set adapter for friends RecyclerView
                        friendsAdapter = new FriendsAdapter(friendsList);
                        recyclerViewFriends.setAdapter(friendsAdapter);
                    } else {
                        Toast.makeText(FriendsActivity.this, "Error fetching friends: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });

        friendRequestsList = new ArrayList<>();
        db.collection("Users").document(currentUserId).collection("friendRequests")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            FirebaseUser friendRequest = document.toObject(FirebaseUser.class);
                            friendRequestsList.add(friendRequest);
                        }
                        // Initialize and set adapter for friend requests RecyclerView
                        friendRequestsAdapter = new FriendRequestsAdapter(friendRequestsList);
                        recyclerViewFriendRequests.setAdapter(friendRequestsAdapter);
                    } else {
                        Toast.makeText(FriendsActivity.this, "Error fetching friend requests: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });

        binding.addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the method to show the add friend dialog
                showAddFriendDialog();
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
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String username = editTextUsername.getText().toString().trim();

                // Find the user
                db.collection("Users")
                        .whereEqualTo("username", username)
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                QueryDocumentSnapshot documentSnapshot = (QueryDocumentSnapshot) queryDocumentSnapshots.getDocuments().get(0); // Get the first document
                                String userId = documentSnapshot.getId();

                                // Add friend to the request list of the target user
                                DocumentReference targetUserRef = db.collection("Users").document(userId);
                                targetUserRef.update("friendRequests." + FirebaseAuth.getInstance().getCurrentUser().getUid(), true)
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
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}