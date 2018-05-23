package com.ashthebest.myauth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.ashthebest.myauth.Model.UniversityModel;
import com.ashthebest.myauth.Model.UserModel;
import com.ashthebest.myauth.ViewHolder.UniversityListHolder;
import com.ashthebest.myauth.ViewHolder.UserListHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserListActivity extends AppCompatActivity {

    private RecyclerView mUserListRecyclerView;
    private DatabaseReference mUserListRef;

    private String universityKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        initView();

        FirebaseRecyclerAdapter<UserModel, UserListHolder> adapter = new FirebaseRecyclerAdapter<UserModel, UserListHolder>(
                UserModel.class,
                R.layout.item_user_list,
                UserListHolder.class,
                mUserListRef
        ) {
            @Override
            protected void populateViewHolder(UserListHolder viewHolder, UserModel model, int position) {
                String name = model.getUserName();
                viewHolder.setUserName(model.getUserName());
                viewHolder.setUserId(model.getUserId(), universityKey);
                viewHolder.setProfilePic(getApplicationContext(), model.getProfilePic());
                viewHolder.setActionListener(UserListActivity.this);
            }
        };

        mUserListRecyclerView.setAdapter(adapter);
    }

    private void initView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            universityKey = bundle.getString("UNIVERSITY_KEY");
        }
        mUserListRecyclerView = findViewById(R.id.user_list_recyclerview);
        mUserListRef = FirebaseDatabase.getInstance().getReference().child("universityWiseUserList").child(universityKey);
        mUserListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
