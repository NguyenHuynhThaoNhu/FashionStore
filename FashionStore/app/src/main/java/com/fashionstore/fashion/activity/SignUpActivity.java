package com.fashionstore.fashion.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.fashionstore.fashion.R;
import com.fashionstore.fashion.model.User;
import com.fashionstore.fashion.prefs.DataStoreManager;
import com.fashionstore.fashion.utils.GlobalFuntion;
import com.fashionstore.fashion.utils.StringUtil;

public class SignUpActivity extends BaseActivity {
    private EditText edtEmail, edtPassword;
    private Button btnSignUp;
    private TextView imageBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initUi();
        initListener();
    }
    private void initUi(){

        edtEmail = findViewById(R.id.inputEmailAddress);
        edtPassword = findViewById(R.id.inputPassword);
        btnSignUp = findViewById(R.id.btnsignup);
        imageBack = findViewById(R.id.alreadyHaveAccount);

    }

    private void initListener(){
        imageBack.setOnClickListener(v -> onBackPressed());
        btnSignUp.setOnClickListener(v -> onClickValidateSignUp());
    }
    private void onClickValidateSignUp() {
        String strEmail = edtEmail.getText().toString().trim();
        String strPassword = edtPassword.getText().toString().trim();

        if (StringUtil.isEmpty(strEmail)) {
            Toast.makeText(SignUpActivity.this, getString(R.string.msg_email_require), Toast.LENGTH_SHORT).show();
        } else if (StringUtil.isEmpty(strPassword)) {
            Toast.makeText(SignUpActivity.this, getString(R.string.msg_password_require), Toast.LENGTH_SHORT).show();
        }
        else if (!StringUtil.isValidEmail(strEmail)) {
            Toast.makeText(SignUpActivity.this, getString(R.string.msg_email_invalid), Toast.LENGTH_SHORT).show();
        } else {
            signUpUser(strEmail, strPassword);
        }
    }

    private void signUpUser(String email, String password) {
        showProgressDialog(true);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    showProgressDialog(false);
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            User userObject = new User(user.getEmail(), password);
                            DataStoreManager.setUser(userObject);
                            GlobalFuntion.startActivity(SignUpActivity.this, MainActivity.class);
                            finishAffinity();
                        }
                    } else {
                        Toast.makeText(SignUpActivity.this, getString(R.string.msg_sign_up_error),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}