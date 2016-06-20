package com.ab5y.flashcards;

import android.app.LauncherActivity;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.ab5y.flashcards.custom.adapter.StackAdapter;
import com.ab5y.flashcards.helper.DatabaseHelper;
import com.ab5y.flashcards.model.Flashcard;
import com.ab5y.flashcards.model.Stack;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class NewCardActivity extends AppCompatActivity {

    public final static String TOPIC_MSG = "com.ab5y.flashcards.TOPIC";
    public final static String CONTENT_MSG = "com.ab5y.flashcards.CONTENT";
    public final static String CARD_ID = "com.ab5y.flashcards.CARD_ID";
    public final static String LOG = "NewCardActivity";
    private boolean isSelected = false;
    private ArrayList<String> selectedStacks = new ArrayList<String>();

    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            isSelected = parent.isSelected();
            Log.e(LOG, "Value of item view.isSelected() is "+view.isSelected());
            String item = (String) parent.getItemAtPosition(position);
            isSelected = !isSelected;
            if (!isSelected) {
                Log.e(LOG, "OMG ITEM UNCLICKED");
                view.setBackgroundColor(Color.BLACK);
                parent.setSelected(false);
                view.setSelected(false);
                selectedStacks.remove(parent.getItemAtPosition(position).toString());
            }
            else if(isSelected) {
                Log.e(LOG, "OMG ITEM CLICKED");
                view.setBackgroundColor(Color.MAGENTA);
                parent.setSelected(true);
                view.setSelected(true);
                selectedStacks.add(parent.getItemAtPosition(position).toString());
            }
            else {
                Log.e(LOG, "OMG ITEM UNCLICKED");
                view.setBackgroundColor(Color.WHITE);
                view.setSelected(false);
                parent.setSelected(false);
                selectedStacks.remove(parent.getItemAtPosition(position).toString());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card);

//        ListView lvStacks = (ListView) findViewById(R.id.lvStacks);
//        lvStacks.setBackgroundColor(Color.BLACK);

        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.actvStacks);
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        List<Stack> stackList = db.getAllStacks();
//        List<String> stackNames = new ArrayList<String>();
//        for(Stack stack : stackList) {
//            stackNames.add(stack.getName());
//            String stackInfo = "Stack Name: " +stack.getName()+" Stack ID: "+stack.getId();
//            Log.e(LOG, stackInfo);
//        }
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
//                getApplicationContext(),
//                android.R.layout.simple_list_item_1,
//                stackNames
//        );
        StackAdapter stackAdapter = new StackAdapter(getApplicationContext(), R.layout.simple_text_view, stackList);
        autoCompleteTextView.setAdapter(stackAdapter);
        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//        lvStacks.setAdapter(stackAdapter);
//        lvStacks.setOnItemClickListener(listener);
    }

    public void btnCreate_onClick(View view){

//        ListView lvStacks = (ListView) findViewById(R.id.lvStacks);
//        ArrayAdapter arrayAdapter = (ArrayAdapter) lvStacks.getAdapter();

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
        ListView lvStacks = (ListView) findViewById(R.id.lvStacks);
        if(!selectedStacks.isEmpty()){
            Log.e(LOG, "storing stacks works... I think");
            Log.e(LOG, selectedStacks.toString());
            Log.e(LOG, "There.");
        }
//        intent.putExtra(TOPIC_MSG, topic);
//        intent.putExtra(CONTENT_MSG, content);
        intent.putExtra(CARD_ID, id);
        startActivity(intent);
    }
}
