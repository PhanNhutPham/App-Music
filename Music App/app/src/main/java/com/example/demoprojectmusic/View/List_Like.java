package com.example.demoprojectmusic.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.demoprojectmusic.R;

public class List_Like extends AppCompatActivity {
    ImageView btnHome, btnSearch, btnLibrary, btnProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_like);
        customActionbar();
        addControls();
        addEvent();
    }

    private void addEvent() {
        int userId = getIntent().getIntExtra("user_id", -1);
        Log.e("userid", String.valueOf(userId));
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(List_Like.this, DashBoardActivity.class);
                startActivity(intent);
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(List_Like.this, SearchTheLoai.class);
                startActivity(intent);
            }
        });
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(List_Like.this, ProfileActivity.class);
                intent.putExtra("user_id", userId);
                startActivity(intent);
            }
        });
    }

    private void addControls() {
        btnHome = findViewById(R.id.btnHome);
        btnLibrary = findViewById(R.id.btnLibrary);
        btnProfile = findViewById(R.id.btnProfile);
        btnSearch = findViewById(R.id.btnSearch);
    }

    private  void customActionbar(){
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Thư viện");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#282f32")));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_like, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.Add_list) {
            showCreateDialog();
            return true;
        } else if (itemId == R.id.Search_List) {
            Intent intent = new Intent(List_Like.this, Search_Library.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void showCreateDialog() {
        // Tạo một AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Sử dụng LayoutInflater để tải layout custom_dialog.xml
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_album, null);

        // Ánh xạ các thành phần trong layout custom_dialog.xml
        EditText edtName = dialogView.findViewById(R.id.Ed_nameplaylist);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button btnCreate = dialogView.findViewById(R.id.btnCreate);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button btnClose = dialogView.findViewById(R.id.btnClose);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đóng AlertDialog khi nút "Đóng" được nhấn
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

}