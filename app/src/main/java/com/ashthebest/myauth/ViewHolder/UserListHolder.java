package com.ashthebest.myauth.ViewHolder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ashthebest.myauth.R;
import com.ashthebest.myauth.UserDetailsActivity;
import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Ashraful on 5/24/2018.
 */

public class UserListHolder extends RecyclerView.ViewHolder {
    private CircleImageView profilePic;
    private TextView userNameField;
    private View clickableLayer;

    private String userId;
    private String universityKey;

    public UserListHolder(View itemView) {
        super(itemView);

        profilePic = itemView.findViewById(R.id.user_pro_pic);
        userNameField = itemView.findViewById(R.id.user_name);
        clickableLayer = itemView.findViewById(R.id.clickable_layer);
    }

    public void setProfilePic(Context context, String url) {
        Glide.with(context)
                .load(url)
                .into(profilePic);
    }

    public void setUserName(String userName) {
        userNameField.setText(userName);
    }

    public void setUserId(String userId, String universityKey) {
        this.userId = userId;
        this.universityKey = universityKey;
    }

    public void setActionListener(final Activity activity) {
        userNameField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, UserDetailsActivity.class);
                intent.putExtra("USER_KEY", userId);
                intent.putExtra("UNIVERSITY_KEY", universityKey);

                activity.startActivity(intent);

            }
        });
    }
}
