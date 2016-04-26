package com.ab5y.flashcards;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }


    public void btnNewCard_onClick(View view){
        Intent intent = new Intent(this, NewCardActivity.class);
        startActivity(intent);
    }

    public void btnDisplay_onClick(View view) {
        Intent intent = new Intent(this, FlashCardActivity.class);
        startActivity(intent);
    }
}
