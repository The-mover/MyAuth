package com.ashthebest.myauth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ashthebest.myauth.Model.UniversityModel;
import com.ashthebest.myauth.ViewHolder.UniversityListHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ShowUniversityActivity extends AppCompatActivity {

    private RecyclerView mUniversityRecyclerView;
    private DatabaseReference mUniversityListRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_university);

        initView();

        FirebaseRecyclerAdapter<UniversityModel, UniversityListHolder> adapter = new FirebaseRecyclerAdapter<UniversityModel, UniversityListHolder>(
                UniversityModel.class,
                R.layout.item_university_list,
                UniversityListHolder.class,
                mUniversityListRef
        ) {
            @Override
            protected void populateViewHolder(UniversityListHolder viewHolder, UniversityModel model, int position) {
                viewHolder.setUniversityName(ShowUniversityActivity.this, model.getUniversityName());
                viewHolder.setUniversityKey(model.getUniversityKey());
            }
        };

        mUniversityRecyclerView.setAdapter(adapter);
    }

    private void initView() {

        mUniversityRecyclerView = findViewById(R.id.university_recycleview);
        mUniversityRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUniversityListRef = FirebaseDatabase.getInstance().getReference().child("universityList");

    }
}
