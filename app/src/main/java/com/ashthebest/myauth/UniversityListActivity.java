package com.ashthebest.myauth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.ashthebest.myauth.Model.UniversityModel;
import com.ashthebest.myauth.ViewHolder.UniversityListHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UniversityListActivity extends AppCompatActivity {

    private RecyclerView mUniversityRecyclerView;
    private DatabaseReference mUniversityListRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_university_list);

        initView();

        FirebaseRecyclerAdapter<UniversityModel, UniversityListHolder> adapter = new FirebaseRecyclerAdapter<UniversityModel, UniversityListHolder>(
                UniversityModel.class,
                R.layout.item_university_list,
                UniversityListHolder.class,
                mUniversityListRef
        ) {
            @Override
            protected void populateViewHolder(UniversityListHolder viewHolder, UniversityModel model, int position) {
                Log.v("DatabaseResponse", model.getUniversityName());
                String name = model.getUniversityName();
                viewHolder.setUniversityName(UniversityListActivity.this, model.getUniversityName());
                viewHolder.setUniversityKey(model.getUserListKey());
            }
        };

        mUniversityRecyclerView.setAdapter(adapter);
    }

    private void initView() {

        mUniversityRecyclerView = findViewById(R.id.university_recycleview);
        mUniversityListRef = FirebaseDatabase.getInstance().getReference().child("universityList");
        mUniversityRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
