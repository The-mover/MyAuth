package com.ashthebest.myauth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ashthebest.myauth.Model.UniversityModel;
import com.ashthebest.myauth.ViewHolder.UniversityListHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class UniversityListActivity extends AppCompatActivity {

    private RecyclerView mUniversityRecyclerView;
    private DatabaseReference mUniversityListRef;

    private Button profileBtn;
    private Button logoutBtn;
    private Button showListBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_university_list);

        initView();

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UniversityListActivity.this, PersonalDetailsActivity.class).putExtra("DATA", 1));
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(UniversityListActivity.this, MainActivity.class));
            }
        });

        showListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UniversityListActivity.this, ShowUniversityActivity.class));
            }
        });

        FirebaseRecyclerAdapter<UniversityModel, UniversityListHolder> adapter = new FirebaseRecyclerAdapter<UniversityModel, UniversityListHolder>(
                UniversityModel.class,
                R.layout.item_university_list,
                UniversityListHolder.class,
                mUniversityListRef
        ) {
            @Override
            protected void populateViewHolder(UniversityListHolder viewHolder, UniversityModel model, int position) {
                viewHolder.setUniversityName(UniversityListActivity.this, model.getUniversityName());
                viewHolder.setUniversityKey(model.getUniversityKey());
            }
        };

        mUniversityRecyclerView.setAdapter(adapter);
    }

    private void initView() {

        mUniversityRecyclerView = findViewById(R.id.university_recycleview);
        mUniversityRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUniversityListRef = FirebaseDatabase.getInstance().getReference().child("universityList");


        profileBtn = findViewById(R.id.profile);
        logoutBtn = findViewById(R.id.logout);
        showListBtn = findViewById(R.id.showlist);
    }
}
