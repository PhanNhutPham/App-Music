package com.example.demoprojectmusic.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demoprojectmusic.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ForgetPasswordActivity extends AppCompatActivity {
    TextView tv_arrowBack;
    private EditText editTextInputEmail;
    private Button sendButton;


    FirebaseAuth mAuth;
    TextView resend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        Intent intent = getIntent();
        addControls();
        addEvents();
        mAuth = FirebaseAuth.getInstance();
    }

    private void addEvents(){

        tv_arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = editTextInputEmail.getText().toString().trim();

                if (!isValidEmail(email)) {
                    Toast.makeText(ForgetPasswordActivity.this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                CollectionReference usersCollection = firebaseFirestore.collection("users");

                usersCollection.whereEqualTo("email", email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                // Email đã tồn tại trong Firestore
                                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(ForgetPasswordActivity.this, "Email đổi mật khẩu đã được gửi!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(ForgetPasswordActivity.this, "Gửi email đổi mật khẩu thất bại!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                // Email không tồn tại trong Firestore
                                Toast.makeText(ForgetPasswordActivity.this, "Email không tồn tại!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Lỗi khi truy vấn Firestore
                            String errorMessage = task.getException().getMessage();
                            Toast.makeText(ForgetPasswordActivity.this, "Lỗi truy vấn dữ liệu: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = editTextInputEmail.getText().toString().trim();

                if (!isValidEmail(email)) {
                    Toast.makeText(ForgetPasswordActivity.this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                CollectionReference usersCollection = firebaseFirestore.collection("users");

                usersCollection.whereEqualTo("email", email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                // Email đã tồn tại trong Firestore
                                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(ForgetPasswordActivity.this, "Email đổi mật khẩu đã được gửi!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(ForgetPasswordActivity.this, "Gửi email đổi mật khẩu thất bại!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                // Email không tồn tại trong Firestore
                                Toast.makeText(ForgetPasswordActivity.this, "Email không tồn tại!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Lỗi khi truy vấn Firestore
                            String errorMessage = task.getException().getMessage();
                            Toast.makeText(ForgetPasswordActivity.this, "Lỗi truy vấn dữ liệu: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void addControls(){
        tv_arrowBack = findViewById(R.id.tv_arrowBack);
        editTextInputEmail = findViewById(R.id.editTextInputEmail);
        sendButton = findViewById(R.id.sendEmailButton);
        resend = findViewById(R.id.tvResend);
    }


}