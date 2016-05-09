package com.ab5y.flashcards;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.ab5y.flashcards.helper.DatabaseHelper;
import com.ab5y.flashcards.model.Stack;

public class NewStackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_stack);
    }

    public void btnCreate_onClick(View view) {
        EditText etStackName = (EditText) findViewById(R.id.etStackName);
        String stackName = etStackName.getText().toString();
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        Stack s = new Stack(stackName);
        db.createStack(s);
    }
}
