package com.example.demoprojectmusic.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.demoprojectmusic.R;

public class Search_Library extends AppCompatActivity {
    ImageView back_IvAlbum;
    EditText ed_Search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_library);
        addController();
        addEvent();
    }

    private void addController(){
        back_IvAlbum = findViewById(R.id.Iv_BackAlbum);
        ed_Search =findViewById(R.id.ED_search);
    }
    private void addEvent(){
        back_IvAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ed_Search.requestFocus();
        showSoftInput();
    }
    private void showSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(ed_Search, InputMethodManager.SHOW_IMPLICIT);
    }
}