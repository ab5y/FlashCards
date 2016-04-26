package com.ab5y.flashcards;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

public class NewCardActivity extends AppCompatActivity {

    public final static String TOPIC_MSG = "com.ab5y.flashcards.TOPIC";
    public final static String CONTENT_MSG = "com.ab5y.flashcards.CONTENT";
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
        intent.putExtra(TOPIC_MSG, topic);
        intent.putExtra(CONTENT_MSG, content);
        startActivity(intent);
    }
}
