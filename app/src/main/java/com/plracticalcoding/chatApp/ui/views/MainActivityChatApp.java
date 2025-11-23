package com.plracticalcoding.chatApp.ui.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.plracticalcoding.chatApp.adapters.UsersAdapter;
import com.plracticalcoding.chatApp.data.model.User;
import com.plracticalcoding.myapplication.R;
import com.plracticalcoding.myapplication.databinding.ActivityMainchatappBinding;

import java.util.ArrayList;


public class MainActivityChatApp extends AppCompatActivity {

    ActivityMainchatappBinding mainBinding;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    UsersAdapter usersAdapter;
    ArrayList<User> usersList = new ArrayList<>();

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference().child("Users");
    ValueEventListener usersValueEventListener;

    String currentUserName;
    String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = com.plracticalcoding.myapplication.databinding.ActivityMainchatappBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        mainBinding.toolbarMain.setOverflowIcon(AppCompatResources.getDrawable(this,R.drawable.more_vert));

        mainBinding.toolbarMain.setOnMenuItemClickListener(item -> {

            if (item.getItemId() == R.id.editProfileItem){
                Intent intent = new Intent(MainActivityChatApp.this, UpdateProfileActivity.class);
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.signOutItem) {
                if (usersValueEventListener != null){
                    databaseReference.removeEventListener(usersValueEventListener);
                }
                auth.signOut();
                Intent intent = new Intent(MainActivityChatApp.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            }else {
                return false;
            }

        });

        mainBinding.usersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        usersAdapter = new UsersAdapter(usersList,this::openMessagingActivityOnUserClicked);
        mainBinding.usersRecyclerView.setAdapter(usersAdapter);
        retrieveUsersFromDatabase();
    }

    public void openMessagingActivityOnUserClicked(User user){

        Intent intent = new Intent(MainActivityChatApp.this, MessagingActivity.class);
        intent.putExtra("targetUserName",user.getUserName());
        intent.putExtra("targetUserId",user.getUserId());
        intent.putExtra("targetUserImageUrl",user.getImageUrl());
        intent.putExtra("currentUserName",currentUserName);
        intent.putExtra("currentUserId",currentUserId);
        startActivity(intent);

    }

    public void retrieveUsersFromDatabase(){

        FirebaseUser currentUser = auth.getCurrentUser();

        usersValueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                usersList.clear();
                for (DataSnapshot eachUser : snapshot.getChildren()){
                    User user = eachUser.getValue(User.class);
                    if (user != null && currentUser != null){

                        if (!user.getUserId().equals(currentUser.getUid())){
                            usersList.add(user);
                        }else {
                            currentUserName = user.getUserName();
                            currentUserId = user.getUserId();
                        }

                    }

                }

                usersAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivityChatApp.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
