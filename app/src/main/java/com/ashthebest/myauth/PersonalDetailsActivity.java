package com.ashthebest.myauth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ashthebest.myauth.Model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalDetailsActivity extends AppCompatActivity {

    private CircleImageView profilePic;
    private EditText userNameField;
    private EditText universityNameField;
    private EditText uvaHandleField;
    private EditText cfHandleField;
    private EditText codechefHandleField;

    private Button skipBtn;
    private Button saveBtn;

    private DatabaseReference mDatabaseRef;
    private String userId;

    public static final int PICK_IMAGE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);

        initView();
        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PersonalDetailsActivity.this, MainActivity.class));
                finish();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String universityName = universityNameField.getText().toString();
                String userName = userNameField.getText().toString();
                String uvaHandle = uvaHandleField.getText().toString();
                String cfHandle = cfHandleField.getText().toString();
                String codechefHandle = codechefHandleField.getText().toString();



               /* Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
// you have to get the uri of selected image
                StorageReference storage = FirebaseStorage.getInstance().getReference().child("profilepics");
                storage.putFile(uri);*/
                String uri = "https://firebasestorage.googleapis.com/v0/b/myauth-7b36a.appspot.com/o/profilepics%2F1527030513860.jpg?alt=media&token=25a30681-195f-494f-8fff-29ce21d00ef7";


                mDatabaseRef.child(universityName).child(userId).setValue(new UserModel(
                        userName,
                        userId,
                        uri,
                        uvaHandle,
                        cfHandle,
                        codechefHandle
                ));
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE) {
            //TODO: action
            // f or now iam just using a string you can do it later
        }
    }

    private void initView() {
        profilePic = findViewById(R.id.user_pro_pic);
        userNameField = findViewById(R.id.user_name);
        universityNameField = findViewById(R.id.university_name);
        uvaHandleField = findViewById(R.id.uva_handle_field);
        cfHandleField = findViewById(R.id.cf_handle_field);
        codechefHandleField = findViewById(R.id.codechef_handle_field);

        skipBtn = findViewById(R.id.skip_btn);
        saveBtn = findViewById(R.id.save_btn);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("universityWiseUserList");

    }
}
