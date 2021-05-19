package com.example.flashcardaudiotracks;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class FlashCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);

        Intent intent = getIntent();
        //String value = intent.getStringExtra("key"); //if it's a string you stored.
    }

}