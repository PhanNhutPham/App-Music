package com.example.demoprojectmusic.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demoprojectmusic.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity {
    private  static final String TAG = "TAG";


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextInputEditText edtEmail, edtPassword, edtName;
    Button btnRegister;
    FirebaseAuth mAuth;

    FirebaseFirestore firebaseFirestore;
    int userID;
    TextView tvLoginNow;

    //    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        addControls();
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        addEvents();
    }

    private void addControls(){
        edtEmail = (TextInputEditText) findViewById(R.id.edtEmail);
        edtPassword = (TextInputEditText) findViewById(R.id.edtPassword);
        edtName = (TextInputEditText) findViewById(R.id.edtName) ;
        btnRegister = (Button) findViewById(R.id.btnRegister);
//        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tvLoginNow = (TextView)findViewById(R.id.tvLoginNow) ;
    }

    private void addEvents(){
        tvLoginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                progressBar.setVisibility(View.VISIBLE);
                String email, password, name;
                name = edtName.getText().toString();
                email = edtEmail.getText().toString().trim();
                password = edtPassword.getText().toString();

                if (TextUtils.isEmpty(name)){
                    //Toast.makeText(getApplication(),"Name is Required", Toast.LENGTH_SHORT).show();
                    edtName.setError("Name is Required");
                    return;
                }

                if (TextUtils.isEmpty(email)){
                    //Toast.makeText(getApplication(),"Email is Required", Toast.LENGTH_SHORT ).show();
                    edtEmail.setError("Email is Required");
                    return;
                }

                if (!isValidEmail(email)) {
                    //Toast.makeText(getApplication(), "Invalid Email Format", Toast.LENGTH_SHORT).show();
                    edtEmail.setError("Invalid Email Format");
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    //Toast.makeText(getApplication(), "Password is Required", Toast.LENGTH_SHORT).show();
                    edtPassword.setError("Password is Required");
                    return;
                }

                if(password.length() < 5){
                    //Toast.makeText(getApplication(), "Password Must be >= 5 Characters", Toast.LENGTH_SHORT).show();
                    edtPassword.setError("Password Must be >= 5 Characters");
                    return;
                }


                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplication() , "Account created.", Toast.LENGTH_SHORT).show();

                                    userID = (mAuth.getCurrentUser().getUid().hashCode());
                                    Log.e("id", String.valueOf(userID));
                                    DocumentReference documentReference = firebaseFirestore.collection("users").document(String.valueOf(userID));

                                    Map<String, Object> user = new HashMap<>();
                                    user.put("id", userID);
                                    user.put("name", name);
                                    user.put("email", email);

                                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d(TAG, "onSuccess: user Profile is created for" + userID);
                                            Intent intent = new Intent(SignInActivity.this, LoginActivity.class);
                                            intent.putExtra("email", email);
                                            intent.putExtra("password", password);
                                            startActivity(intent);
                                            finish();

                                        }
                                    });

                                    edtName.setText("");
                                    edtEmail.setText("");
                                    edtPassword.setText("");
                                }

                                else {
                                    Toast.makeText(SignInActivity.this, "Authenticati", Toast.LENGTH_SHORT).show();
                                    // Nếu đăng ký thất bại, hiển thị thông báo cho người dùngon failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                String errorMessage = "";
//                                if (e instanceof FirebaseAuthException) {
//                                    // Xử lý lỗi xác thực người dùng
//                                    errorMessage = "Lỗi xác thực người dùng: " + e.getLocalizedMessage();
//                                    Toast.makeText(getApplication(), "Email đã được đăng ký, hãy nhập một email khác.", Toast.LENGTH_SHORT).show();
//
//                                } else if (e instanceof FirebaseNetworkException) {
//                                    // Xử lý lỗi mạng
//                                    errorMessage = "Lỗi truy vấn dữ liệu: " + e.getLocalizedMessage();
//                                    Toast.makeText(getApplication(), "Đã xảy ra lỗi khi truy vấn dữ liêu. Vui lòng thử lại sau.", Toast.LENGTH_SHORT).show();
//                                }
//                                else{
//                                    // Xử lý lỗi không xác định
//                                    Toast.makeText(getApplication(), "Đã xảy ra lỗi không mong đợi. Vui lòng thử lại sau.", Toast.LENGTH_SHORT).show();
//                                }
//                                Log.e("Tag", errorMessage);
//                            }
//                        });
            }
        });
    }
    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@gmail.com";
        return email.matches(emailPattern);
    }

}
