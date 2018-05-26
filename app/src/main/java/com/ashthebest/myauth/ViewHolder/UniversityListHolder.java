package com.ashthebest.myauth.ViewHolder;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ashthebest.myauth.R;
import com.ashthebest.myauth.UserListActivity;

/**
 * Created by Ashraful on 5/23/2018.
 */

public class UniversityListHolder extends RecyclerView.ViewHolder {
    private TextView universityNameField;
    private String universityKey;

    public UniversityListHolder(View itemView) {
        super(itemView);

        universityNameField = itemView.findViewById(R.id.university_name);
    }

    public void setUniversityName(final Activity activity, final String universityName) {
        universityNameField.setText(universityName);

        universityNameField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (universityKey != null) {
                    Intent intent = new Intent(activity, UserListActivity.class);
                    intent.putExtra("UNIVERSITY_KEY", universityKey);
                    activity.startActivity(intent);
                }
            }
        });
    }

    public void setUniversityKey(String key){
        universityKey = key;
    }
}
