package com.fashionstore.fashion.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fashionstore.fashion.R;
import com.fashionstore.fashion.model.UserInfo;
import com.fashionstore.fashion.prefs.DataStoreManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity {

    private EditText tv_name,tv_address,edt_phone;
    private TextView edt_email;
    private AppCompatButton btn_save;
    private ImageView img_back;
    private DatabaseReference userDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        tv_name = findViewById(R.id.tv_name);
        tv_address = findViewById(R.id.tv_address);
        edt_email = findViewById(R.id.edt_email);
        edt_phone = findViewById(R.id.edt_phone);
        btn_save = findViewById(R.id.btn_save);
        img_back = findViewById(R.id.img_back);
        edt_email.setText(DataStoreManager.getUser().getEmail());
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

        // Retrieve the user information from Firebase
        userDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
                    if (userInfo != null) {
                        // Populate the EditText fields with the existing user information

                        tv_name.setText(userInfo.getPhone());
                        edt_phone.setText(userInfo.getAddress());
                        tv_address.setText(userInfo.getFullName());


                    }
                } else {

                    Toast.makeText(EditProfileActivity.this, "User info doesn't exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Error occurred while retrieving user info
                Toast.makeText(EditProfileActivity.this, "Failed to retrieve user info: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        btn_save.setOnClickListener(v -> {

            String strName = tv_name.getText().toString().trim();
            String strPhone = edt_phone.getText().toString().trim();
            String strAddress = tv_address.getText().toString().trim();


            // Create a new instance of UserInfo with the entered values
            UserInfo userInfo = new UserInfo(strName, strPhone, strAddress);
            userDatabaseReference.setValue(userInfo, (databaseError, databaseReference) -> {
                if (databaseError == null) {
                    // Successfully saved the user info
                    Toast.makeText(EditProfileActivity.this, "User info saved", Toast.LENGTH_SHORT).show();
                } else {
                    // Error occurred while saving user info
                    Toast.makeText(EditProfileActivity.this, "Failed to save user info: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });


    }
}