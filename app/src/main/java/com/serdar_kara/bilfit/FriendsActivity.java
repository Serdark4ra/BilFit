package com.serdar_kara.bilfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
public class FriendsActivity extends AppCompatActivity {

    private class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {
        private List<FirebaseUser> friendsList;

        public FriendsAdapter(List<FirebaseUser> friendsList) {
            this.friendsList = friendsList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.item_friend, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            FirebaseUser friend = friendsList.get(position);
            holder.textViewName.setText(friend.getDisplayName());
        }

        @Override
        public int getItemCount() {
            return friendsList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView textViewName;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                textViewName = itemView.findViewById(R.id.textViewFriendName);
            }
        }
    }

    private class FriendRequestsAdapter extends RecyclerView.Adapter<FriendRequestsAdapter.ViewHolder> {
        private List<FirebaseUser> friendRequestsList;

        public FriendRequestsAdapter(List<FirebaseUser> friendRequestsList) {
            this.friendRequestsList = friendRequestsList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.item_friend_request, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            FirebaseUser friendRequest = friendRequestsList.get(position);
            holder.textViewName.setText(friendRequest.getDisplayName());
        }

        @Override
        public int getItemCount() {
            return friendRequestsList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView textViewName;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                textViewName = itemView.findViewById(R.id.textViewFriendRequestName);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

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

        // Fetch friend requests
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
        }
        private String getCurrentUserId() {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                return user.getUid();
            } else {
                // Handle the case where the user is not logged in
                // For now, return a default value or handle it as needed
                return "000000";
            }
        }
    }
}