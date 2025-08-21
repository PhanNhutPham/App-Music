package com.example.demoprojectmusic.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demoprojectmusic.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText edtEmail, edtPassword;
    Button btnLogin;
    CheckBox cb_Remember;
    SharedPreferences mPrefs;

    private static final String PREFS_NAME = "PrefsFile";
    FirebaseAuth mAuth;
    TextView tvRegisterNow, tvForget_password;
    ProgressBar progressBar;

    ImageView imv_show_password;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent intent = getIntent();
        addControls();
        addEvents();
        mAuth = FirebaseAuth.getInstance();
        mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        //getReferensData();


    }

    private void addEvents(){
        String email = getIntent().getStringExtra("email");
        String password = getIntent().getStringExtra("password");

        // Điền email và mật khẩu vào các trường đăng nhập
        edtEmail.setText(email);
        edtPassword.setText(password);
        getReferensData();
        imv_show_password.setImageResource(R.drawable.baseline_visibility_off_24);
        imv_show_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    imv_show_password.setImageResource(R.drawable.baseline_visibility_off_24);
                }else {
                    edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imv_show_password.setImageResource(R.drawable.baseline_visibility_24);
                }

            }
        });

        tvRegisterNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });

        tvForget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String email, password;
                email = edtEmail.getText().toString().trim();
                password = edtPassword.getText().toString();

                // Validate email and password
                if (TextUtils.isEmpty(email) || !isValidEmail(email)) {
                    // Show appropriate error messages or toast and return
                    return;
                }

                // Check network connectivity
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo == null || !networkInfo.isConnected()) {
                    // Show error message for network issue and return
                    return;
                }

                // Validate password length
                if (TextUtils.isEmpty(password) || password.length() < 5) {
                    // Show appropriate error messages or toast and return
                    return;
                }

                // Proceed with Firebase authentication
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    // Firebase sign-in success, handle navigation to the next activity
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        int userId = user.getUid().hashCode();

                                        Intent intent = new Intent(getApplicationContext(), DashBoardActivity.class);
                                        intent.putExtra("user_id", userId);
                                        Log.e("useridd", String.valueOf(userId));
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // Handle the case when user information couldn't be retrieved
                                    }
                                } else {
                                    // Handle different authentication failure cases
                                    Exception exception = task.getException();
                                    if (exception instanceof FirebaseAuthInvalidUserException || exception instanceof FirebaseAuthInvalidCredentialsException) {
                                        // Invalid credentials
                                    } else if (exception instanceof FirebaseNetworkException) {
                                        // Network error
                                    } else {
                                        // Other errors
                                    }
                                    // Clear the input fields
                                    edtPassword.getText().clear();
                                    edtEmail.getText().clear();
                                }
                            }
                        });
            }
        });
    }



    private void addControls(){
        edtEmail = (TextInputEditText) findViewById(R.id.edtEmail);
        edtPassword = (TextInputEditText) findViewById(R.id.edtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tvRegisterNow = (TextView)findViewById(R.id.tvRegisterNow) ;
        imv_show_password = (ImageView) findViewById(R.id.imv_show_password);
        tvForget_password = (TextView) findViewById(R.id.tvForget_Password);
        cb_Remember = (CheckBox) findViewById(R.id.checkbox_Remember);
    }


    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@gmail.com";
        return email.matches(emailPattern);
    }

    private void getReferensData(){
        SharedPreferences sp = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if(sp.contains("pref_email")){
            String e  = sp.getString("pref_email", "not found");
            edtEmail.setText(e.toString());
        }
        if (sp.contains("pref_password")){
            String p = sp.getString("pref_password", "not found");
            edtPassword.setText(p.toString());
        }
        if(sp.contains("pref_check")){
            Boolean b = sp.getBoolean("pref_check", false);
            cb_Remember.setChecked(b);
        }
    }
}
