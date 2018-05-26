package com.ashthebest.myauth;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ashthebest.myauth.Model.UniversityModel;
import com.ashthebest.myauth.Model.UserModel;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalDetailsActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private ImageView profilePic;
    private EditText userNameField;
    private EditText universityNameField;
    private EditText uvaHandleField;
    private EditText cfHandleField;
    private EditText codechefHandleField;

    private Button skipBtn;
    private Button saveBtn;

    private DatabaseReference mUniversityWiseUserListRef;
    private DatabaseReference mUniversityListRef;
    private String userId;

    String universityName;
    String userName;
    String uvaHandle;
    String cfHandle;
    String codechefHandle;
    String uri;
    String savedUri;

    public static final int PICK_IMAGE = 1;
    private Uri resultUri;
    private String modifiedUniversityName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);

        initView();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            skipBtn.setVisibility(View.GONE);

            SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
            String key = sharedPref.getString("UNIVERSITY_KEY", "");
            Log.v("DATA_Ana", key);

            if (key != "") {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("universityWiseUserList").child(key).child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            UserModel model = dataSnapshot.getValue(UserModel.class);

                            Log.v("DATA_Ana", "userName: "+model.getUserName());

                            userNameField.setText(model.getUserName());
                            universityNameField.setText(model.getUniversityName());
                            uvaHandleField.setText(model.getUvaHandle());
                            cfHandleField.setText(model.getCfHandle());
                            codechefHandleField.setText(model.getCodechefHandle());

                            savedUri = model.getProfilePic();
                            uri = savedUri;

                            Glide.with(PersonalDetailsActivity.this).load(uri).into(profilePic);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }


        }


        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
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

                universityName = universityNameField.getText().toString().trim();
                userName = userNameField.getText().toString().trim();
                uvaHandle = uvaHandleField.getText().toString().trim();
                cfHandle = cfHandleField.getText().toString().trim();
                codechefHandle = codechefHandleField.getText().toString().trim();

                if (savedUri != null && uri == savedUri) {
                    saveDataToDatabase();
                } else {
                    uploadPhoto();
                }


            }
        });
    }

    private void uploadPhoto() {

        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("userPhoto").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        Bitmap bitmap = null;

        try {
            bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);

        byte[] data = byteArrayOutputStream.toByteArray();

        storageRef.putBytes(data).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    uri = task.getResult().getDownloadUrl().toString();

                    saveDataToDatabase();
                } else {
                    Toast.makeText(PersonalDetailsActivity.this, "Failed to Save\n please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void saveDataToDatabase() {
        modifiedUniversityName = universityName.toLowerCase().replace(" ", "");

        mUniversityListRef.child(modifiedUniversityName).setValue(new UniversityModel(universityName.toUpperCase(), modifiedUniversityName)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    saveUserToUserList();
                } else {
                    Toast.makeText(PersonalDetailsActivity.this, "Failed to Save\n please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveUserToUserList() {
        mUniversityWiseUserListRef.child(modifiedUniversityName).child(userId).setValue(new UserModel(
                userName,
                universityName,
                userId,
                uri,
                uvaHandle,
                cfHandle,
                codechefHandle
        )).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("UNIVERSITY_KEY", modifiedUniversityName);
                    editor.commit();

                    startActivity(new Intent(PersonalDetailsActivity.this, MainActivity.class));
                    finish();

                } else {
                    Toast.makeText(PersonalDetailsActivity.this, "Failed to Save\n please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            resultUri = data.getData();


            profilePic.setImageURI(resultUri);
        }
    }

    private void initView() {
        profilePic = findViewById(R.id.profile_pic);
        userNameField = findViewById(R.id.user_name);
        universityNameField = findViewById(R.id.university_name);
        uvaHandleField = findViewById(R.id.uva_handle_field);
        cfHandleField = findViewById(R.id.cf_handle_field);
        codechefHandleField = findViewById(R.id.codechef_handle_field);

        skipBtn = findViewById(R.id.skip_btn);
        saveBtn = findViewById(R.id.save_btn);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mUniversityWiseUserListRef = FirebaseDatabase.getInstance().getReference().child("universityWiseUserList");
        mUniversityListRef = FirebaseDatabase.getInstance().getReference().child("universityList");


    }
}
