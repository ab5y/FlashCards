package com.ab5y.flashcards;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.ab5y.flashcards.helper.DatabaseHelper;
import com.ab5y.flashcards.model.Flashcard;

import java.util.List;

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
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        List<Flashcard> flashcardList = db.getAllFlashcards();
        if (!flashcardList.isEmpty()) {
            Flashcard fc = db.getAllFlashcards().get(0);
            long id = fc.getId(); // db.getMaxFlashcardID();
            if (id >= 0)
                intent.putExtra(NewCardActivity.CARD_ID, id);
            startActivity(intent);
        }
    }

    public void btnNewStack_onClick(View view) {
        Intent intent = new Intent(this, NewStackActivity.class);
        startActivity(intent);
    }
}
