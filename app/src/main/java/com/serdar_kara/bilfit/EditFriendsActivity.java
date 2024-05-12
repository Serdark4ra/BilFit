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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.serdar_kara.bilfit.databinding.ActivityEditFriendsBinding;
import com.serdar_kara.bilfit.databinding.FriendEditRowBinding;
import androidx.appcompat.app.AlertDialog;

import com.serdar_kara.bilfit.friends.FriendsActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class EditFriendsActivity extends AppCompatActivity {
    public class EditFriendsAdapter extends RecyclerView.Adapter<EditFriendsActivity.EditFriendsAdapter.EditFriendsViewHolder> {
        private static final String PREFS_NAME = "ButtonTimestamp";
        private static final String TIMESTAMP_KEY_PREFIX = "ButtonTimestamp_";
        private final List<String> friendsList;
        private final Context context;
        private final FirebaseFirestore db;

        public EditFriendsAdapter(Context context, List<String> friendsList) {
            this.context = context;
            this.friendsList = friendsList;
            this.db = FirebaseFirestore.getInstance();
        }

        @NonNull
        @Override
        public EditFriendsActivity.EditFriendsAdapter.EditFriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            FriendEditRowBinding binding = FriendEditRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new EditFriendsActivity.EditFriendsAdapter.EditFriendsViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull EditFriendsActivity.EditFriendsAdapter.EditFriendsViewHolder holder, int position) {
            String friendUserId = friendsList.get(position);
            loadUserProfilePhoto(holder.binding, friendUserId);

            AtomicReference<String> friendName = new AtomicReference<>();

            db.collection("Users").document(friendUserId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            friendName.set(documentSnapshot.getString("name_surname"));
                            holder.bind(friendName.get());
                        } else {
                            Toast.makeText(context, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> Log.d("Friends", "Failed to retrieve friend data"));

            holder.binding.buttonRemove.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to remove " + friendName + " from your friends list?")
                        .setPositiveButton("Remove", (dialog, which) -> {
                            removeFriend(friendUserId, position);
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> {
                            // Do nothing
                        })
                        .show();
            });
        }

        @Override
        public int getItemCount() {
            return friendsList.size();
        }

        private void removeFriend(String friendUserId, int position) {
            // Remove friend from Firestore
            db.collection("Users").document(friendUserId).update("friends", FieldValue.arrayRemove(getCurrentUserId()))
                    .addOnSuccessListener(aVoid -> {
                        friendsList.remove(position);
                        notifyItemRemoved(position);

                        notifyItemRangeChanged(position, friendsList.size());
                    })
                    .addOnFailureListener(e -> {
                        // Failed to remove friend
                        Toast.makeText(context, "Failed to remove friend. Please try again", Toast.LENGTH_SHORT).show();
                        Log.e("EditFriendsAdapter", "Error removing friend from friend", e);
                        // Add friend back to local list on failure
                        friendsList.add(position, friendUserId);
                        notifyItemInserted(position);
                    });

            db.collection("Users").document(getCurrentUserId()).update("friends", FieldValue.arrayRemove(friendUserId))
                    .addOnSuccessListener(aVoid -> {
                        // Successfully removed friend
                        Toast.makeText(context, "Friend removed successfully", Toast.LENGTH_SHORT).show();
                        // Update friend's friend list
                    })
                    .addOnFailureListener(e -> {
                        // Failed to remove friend
                        Toast.makeText(context, "Failed to remove friend. Please try again", Toast.LENGTH_SHORT).show();
                        Log.e("EditFriendsAdapter", "Error removing friend from current user", e);
                        // Add friend back to local list on failure
                        friendsList.add(position, friendUserId);
                        notifyItemInserted(position);
                    });
        }

        public class EditFriendsViewHolder extends RecyclerView.ViewHolder {
            private FriendEditRowBinding binding;

            public EditFriendsViewHolder(FriendEditRowBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }

            public void bind(String friendName) {
                binding.textEditName.setText(friendName);
                binding.textEditUsername.setText(friendName);
                binding.imageViewEdit.setImageResource(R.drawable.ic_launcher_background);
            }
        }

        private void loadUserProfilePhoto(FriendEditRowBinding binding, String userId) {
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
                                        Glide.with(EditFriendsActivity.this)
                                                .load(photoUrl)
                                                .circleCrop() // if you want to apply circle cropping to the image
                                                .into(binding.imageViewEdit);
                                    }
                                }
                            } else {
                                Toast.makeText(EditFriendsActivity.this, "Failed to load user photo.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }

    private RecyclerView recyclerViewEditFriends;

    private EditFriendsActivity.EditFriendsAdapter friendsAdapter;
    private ArrayList<String> friendsList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_friends);
        com.serdar_kara.bilfit.databinding.ActivityEditFriendsBinding binding = ActivityEditFriendsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        Toolbar toolbar = findViewById(R.id.toolbarEditFriend);
        setSupportActionBar(toolbar);

        recyclerViewEditFriends = findViewById(R.id.recyclerViewEditFriends);
        recyclerViewEditFriends.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        String currentUserId = getCurrentUserId();

        toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(EditFriendsActivity.this, FriendsActivity.class);
            startActivity(intent);
            finish();
        });

        friendsList = new ArrayList<>();
        db.collection("Users").document(currentUserId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        friendsList = (ArrayList<String>) documentSnapshot.get("friends");
                    }
                    friendsAdapter = new EditFriendsActivity.EditFriendsAdapter(this, friendsList);
                    recyclerViewEditFriends.setAdapter(friendsAdapter);
                });
    }

    private String getCurrentUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getUid();
        } else {
            Toast.makeText(EditFriendsActivity.this, "Something went wrong. Please try again",
                    Toast.LENGTH_SHORT).show();
            return "0000";
        }
    }
}