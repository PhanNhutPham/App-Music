package com.example.demoprojectmusic.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.demoprojectmusic.Model.Playlist;
import com.example.demoprojectmusic.Model.User;
import com.example.demoprojectmusic.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {
    TextView tv_email, tv_name;
    ImageView im_Logout;
    private String userID;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        tv_email = findViewById(R.id.tv_email);
        tv_name = findViewById(R.id.tv_Name);
        im_Logout = findViewById(R.id.im_logout);

        int userId = getIntent().getIntExtra("user_id", -1);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(String.valueOf(userId));


        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        User user = document.toObject(User.class);
                        Log.e("User", user.toString());
                        if (user != null){
                            tv_email.setText(user.getEmail());
                            tv_name.setText(user.getName());
                        }

                        // Sử dụng thông tin người dùng ở đây
                    } else {
                        // Không có dữ liệu cho ID cụ thể trong Firestore
                        // Xử lý theo ý của bạn khi không tìm thấy dữ liệu
                    }
                } else {
                    // Xảy ra lỗi khi truy vấn dữ liệu từ Firestore
                    Exception e = task.getException();
                    // Xử lý lỗi nếu cần thiết
                }
            }
        });


        im_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}