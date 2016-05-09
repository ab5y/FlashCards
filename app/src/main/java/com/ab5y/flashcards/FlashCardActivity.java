package com.ab5y.flashcards;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab5y.flashcards.R;
import com.ab5y.flashcards.helper.DatabaseHelper;
import com.ab5y.flashcards.model.Flashcard;

import java.util.List;

import static android.view.MotionEvent.*;


public class FlashCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_card);

        MenuItem nextCard = (MenuItem) findViewById(R.id.action_next_card);
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        List<Integer> fcIDs = db.getAllFlashcardIDs();
        if(fcIDs.isEmpty()){
            nextCard.setVisible(false);
        }

        TextView tvCardTopic = (TextView) findViewById(R.id.tvCardTopic);
        tvCardTopic.setVisibility(View.VISIBLE);
        TextView tvCardContent = (TextView) findViewById(R.id.tvCardContent);
        TextView tvCardID = (TextView) findViewById(R.id.tvCardID);

        Intent intent = getIntent();
        if(intent != null){
//            String topic = intent.getStringExtra(NewCardActivity.TOPIC_MSG);
//            String content = intent.getStringExtra(NewCardActivity.CONTENT_MSG);
            long id = intent.getLongExtra(NewCardActivity.CARD_ID, -1);
            if (id >= 0) {
                Flashcard fc = db.getFlashcard(id);
                String topic = fc.getName();
                String content = fc.getContent();
                tvCardTopic.setText(topic);
                tvCardContent.setText(content);
                tvCardID.setText(String.valueOf(id));
            }
        }

        RelativeLayout rlMainFlashCard = (RelativeLayout) findViewById(R.id.rlFlashCard);
        rlMainFlashCard.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                v.getParent().requestDisallowInterceptTouchEvent(true);
                TextView tvCardTopic = (TextView) findViewById(R.id.tvCardTopic);
                TextView tvCardContent = (TextView) findViewById(R.id.tvCardContent);

                switch (event.getAction() & ACTION_MASK) {
                    case ACTION_UP:
                        tvCardContent.setVisibility(View.INVISIBLE);
                        tvCardTopic.setVisibility(View.VISIBLE);
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                    case ACTION_DOWN:
                        tvCardTopic.setVisibility(View.INVISIBLE);
                        tvCardContent.setVisibility(View.VISIBLE);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_flash_card, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent event){
//
//    }

    public void newCard(MenuItem menuItem) {
        Intent intent = new Intent(this, NewCardActivity.class);
        startActivity(intent);
    }

    public void nextCard(MenuItem menuItem) {
        TextView tvID = (TextView) findViewById(R.id.tvCardID);
        int currID = Integer.valueOf(tvID.getText().toString());
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        List<Integer> fcIDs = db.getAllFlashcardIDs();
        db.closeDB();
        int currIndex = fcIDs.indexOf(currID);
        int len = fcIDs.size();
        if (currIndex < (len - 1)) {
            int id = fcIDs.get(currIndex + 1);
            TextView tvCardTopic = (TextView) findViewById(R.id.tvCardTopic);
//            tvCardTopic.setVisibility(View.VISIBLE);
            TextView tvCardContent = (TextView) findViewById(R.id.tvCardContent);
            TextView tvCardID = (TextView) findViewById(R.id.tvCardID);
            if (id >= 0) {
                Flashcard fc = db.getFlashcard(id);
                String topic = fc.getName();
                String content = fc.getContent();
                tvCardTopic.setText(topic);
                tvCardContent.setText(content);
                tvCardID.setText(String.valueOf(id));
            }
        }
    }
}
