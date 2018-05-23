package com.ashthebest.myauth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.ashthebest.myauth.Model.UserModel;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserDetailsActivity extends AppCompatActivity {


    private CircleImageView userProfilePic;
    private TextView userNameField;
    private TextView uvaHandleField;
    private TextView cfHandleField;
    private TextView codechefHandleField;

    private String userId;
    private String universityKey;

    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        initView();

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    UserModel userInfo = dataSnapshot.getValue(UserModel.class);
                    setUserInfo(userInfo);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void setUserInfo(UserModel model) {
        Glide.with(this)
                .load(model.getProfilePic())
                .into(userProfilePic);

        userNameField.setText(model.getUserName());
        uvaHandleField.setText(model.getUvaHandle());
        cfHandleField.setText(model.getCfHandle());
        codechefHandleField.setText(model.getCodechefHandle());
    }

    private void initView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userId = bundle.getString("USER_KEY");
            universityKey = bundle.getString("UNIVERSITY_KEY");
        }

        userProfilePic = findViewById(R.id.user_pro_pic);
        userNameField = findViewById(R.id.user_name);
        uvaHandleField = findViewById(R.id.uva_handle_field);
        cfHandleField = findViewById(R.id.cf_handle_field);
        codechefHandleField = findViewById(R.id.codechef_handle_field);

        userRef = FirebaseDatabase.getInstance().getReference().child("universityWiseUserList").child(universityKey).child(userId);
    }
}
