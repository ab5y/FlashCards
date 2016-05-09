package com.ab5y.flashcards;

import android.content.Intent;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.ab5y.flashcards.helper.DatabaseHelper;
import com.ab5y.flashcards.model.Flashcard;

public class NewCardActivity extends AppCompatActivity {

    public final static String TOPIC_MSG = "com.ab5y.flashcards.TOPIC";
    public final static String CONTENT_MSG = "com.ab5y.flashcards.CONTENT";
    public final static String CARD_ID = "com.ab5y.flashcards.CARD_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card);
    }

    public void btnCreate_onClick(View view){
        Intent intent = new Intent(this, FlashCardActivity.class);
        EditText etCardTopic = (EditText) findViewById(R.id.etCardTopic);
        EditText etCardContent = (EditText) findViewById(R.id.etCardContent);
        String topic = etCardTopic.getText().toString();
        String content = etCardContent.getText().toString();
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        Flashcard fc = new Flashcard(topic, content);
        long id = 0;
        try {
            id = db.createFlashcard(fc);
            if (id >= 0) {

            }
        } catch (SQLException e){
            throw e;
        } finally {
            db.closeDB();
        }
//        intent.putExtra(TOPIC_MSG, topic);
//        intent.putExtra(CONTENT_MSG, content);
        intent.putExtra(CARD_ID, id);
        startActivity(intent);
    }
}
