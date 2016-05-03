package com.ab5y.flashcards.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ab5y.flashcards.model.Flashcard;
import com.ab5y.flashcards.model.Stack;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Abhay on 2/5/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat Tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 3;

    // Database Name
    private static final String DATABASE_NAME = "flashcardsDB";

    // Table Names
    private static final String TABLE_FLASHCARD = "flashcards";
    private static final String TABLE_STACK = "stacks";
    private static final String TABLE_FLASHCARD_STACK = "flashcard_stacks";
    private static final String TABLE_TAG = "tags";
    private static final String TABLE_FLASHCARD_TAG = "flashcard_tags";
    private static final String TABLE_STACK_TAG = "stack_tags";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_CREATED_AT = "created_at";

    // Flashcards Table - column names
    private static final String KEY_CONTENT = "content";

    // Flashcard_Stacks Table - column names
    private static final String KEY_FLASHCARD_ID = "flashcard_id";
    private static final String KEY_STACK_ID = "stack_id";

    // Table Create Statements
    // Flashcard table create statement
    private static final String CREATE_TABLE_FLASHCARD =
            "CREATE TABLE IF NOT EXISTS " + TABLE_FLASHCARD
            + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT,"
            + KEY_CONTENT + " TEXT,"
            + KEY_CREATED_AT + " DATETIME"
            + ")";

    // Stack table create statement
    private static final String CREATE_TABLE_STACK =
            "CREATE TABLE IF NOT EXISTS " + TABLE_STACK
            + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT,"
            + KEY_CREATED_AT + " DATETIME"
            + ")";

    // Flashcard_Stack table create statement
    private static final String CREATE_TABLE_FLASHCARD_STACK =
            "CREATE TABLE IF NOT EXISTS " + TABLE_FLASHCARD_STACK
            + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_FLASHCARD_ID + " INTEGER,"
            + KEY_STACK_ID + " INTEGER,"
            + "FOREIGN KEY(" + KEY_FLASHCARD_ID + " REFERENCES " + TABLE_FLASHCARD + "(" + KEY_ID + "),"
            + "FOREIGN KEY(" + KEY_STACK_ID + " REFERENCES " + TABLE_STACK + "(" + KEY_ID + ")"
            + ")";

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_FLASHCARD);
        db.execSQL(CREATE_TABLE_STACK);
        db.execSQL(CREATE_TABLE_FLASHCARD_STACK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FLASHCARD_STACK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FLASHCARD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STACK);

        // create new tables
        onCreate(db);
    }

    /*
     * Create a Flashcard
     */
    public long createFlashcard(Flashcard flashcard) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, flashcard.getName());
        values.put(KEY_CONTENT, flashcard.getContent());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        long flashcard_id = db.insert(TABLE_FLASHCARD, null, values);

        return flashcard_id;
    }

    /*
     *  Get single flashcard
     */
    public Flashcard getFlashcard(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_FLASHCARD
                + " WHERE " + KEY_ID + " = " + id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Flashcard fc = new Flashcard();
        fc.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        fc.setName(c.getString(c.getColumnIndex(KEY_NAME)));
        fc.setContent(c.getString(c.getColumnIndex(KEY_CONTENT)));
        fc.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

        return fc;
    }

    /*
     *  Getting all flashcards
     */
    public List<Flashcard> getAllFlashcards() {
        List<Flashcard> flashcardList = new ArrayList<Flashcard>();
        String selectQuery = "SELECT * FROM " + TABLE_FLASHCARD;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if(c.moveToFirst()) {
            do {
                Flashcard fc = new Flashcard();
                fc.setId(c.getInt(c.getColumnIndex((KEY_ID))));
                fc.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                fc.setContent(c.getString(c.getColumnIndex(KEY_CONTENT)));
                fc.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                flashcardList.add(fc);
            } while (c.moveToNext());
        }
        return flashcardList;
    }

    /*
     * getting all flashcards in a stack by stack id
     */
    public List<Flashcard> getAllFlashcardsByStackID(long stack_id) {
        List<Flashcard> flashcardList = new ArrayList<Flashcard>();

        String selectQuery =
                "SELECT * FROM "
                        + TABLE_FLASHCARD + " fc, "
                        + TABLE_FLASHCARD_STACK + " fs "
                + "WHERE fs." + KEY_STACK_ID + " = " + stack_id
                + " AND fs." + KEY_FLASHCARD_ID + " = " + "fc." + KEY_ID;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Flashcard fc = new Flashcard();
                fc.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                fc.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                fc.setContent(c.getString(c.getColumnIndex(KEY_CONTENT)));
                fc.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // Add flashcard to list
                flashcardList.add(fc);
            } while(c.moveToNext());
        }
        return flashcardList;
    }

    /*
     * getting all flashcards in a stack by stack name
     */
    public List<Flashcard> getAllFlashcardsByStackName(String stack_name) {
        List<Flashcard> flashcardList = new ArrayList<Flashcard>();

        String selectQuery =
                "SELECT * FROM "
                        + TABLE_FLASHCARD + " fc, "
                        + TABLE_STACK + " st, "
                        + TABLE_FLASHCARD_STACK + " fs "
                        + "WHERE st." + KEY_NAME + " = '" + stack_name + "'"
                        + " AND fs." + KEY_STACK_ID + " = " + "st." + KEY_ID
                        + " AND fs." + KEY_FLASHCARD_ID + " = " + "fc." + KEY_ID;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Flashcard fc = new Flashcard();
                fc.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                fc.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                fc.setContent(c.getString(c.getColumnIndex(KEY_CONTENT)));
                fc.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // Add flashcard to list
                flashcardList.add(fc);
            } while(c.moveToNext());
        }
        return flashcardList;
    }

    /*
     * Updating a flashcard
     */
    public int updateFlashcard(Flashcard flashcard) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, flashcard.getName());
        values.put(KEY_CONTENT, flashcard.getContent());

        // updating row
        return db.update(TABLE_FLASHCARD, values, KEY_ID + " = ?",
                new String[] {String.valueOf(flashcard.getId())});
    }

    /*
     * Deleting a flashcard
     */
    public void deleteFlashcard(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FLASHCARD, KEY_ID + " = ?",
                new String[] {String.valueOf(id)});
    }

    /*
     * Creating a stack
     */
    public long createStack(Stack stack) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, stack.getName());
        values.put(KEY_CREATED_AT, getDateTime());

        long id = db.insert(TABLE_STACK, null, values);
        return id;
    }

    /*
     * getting all stack names
     */
    public List<Stack> getAllStacks() {
        List<Stack> stackList = new ArrayList<Stack>();
        String selectQuery = "SELECT * FROM " + TABLE_STACK;

        Log.e(LOG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all the rows and adding to list
        if (c.moveToFirst()) {
            do {
                Stack s = new Stack();
                s.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                s.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                s.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding stack to list
                stackList.add(s);
            } while(c.moveToNext());
        }
        return stackList;
    }

    /*
     * Updating a stack
     */
    public long updateStack(Stack stack){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, stack.getName());

        // updating a row
        return db.update(TABLE_STACK, values, KEY_ID + " = ?",
                new String[] {String.valueOf(stack.getId())});
    }

    /*
     *  Get single stack
     */
    public Stack getStack(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_STACK
                + " WHERE " + KEY_ID + " = " + id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Stack s = new Stack();
        s.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        s.setName(c.getString(c.getColumnIndex(KEY_NAME)));
        s.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

        return s;
    }

    /*
     * Deleting a stack
     */
    public void deleteStack(Stack stack, boolean delete_all_stack_flashcards) {
        SQLiteDatabase db = this.getWritableDatabase();
        // before deleting the stack,
        // check whether flashcards under
        // the stack should also be deleted
        if (delete_all_stack_flashcards) {
            // get all flashcards under this stack
            List<Flashcard> allStackFlashcards = getAllFlashcardsByStackID(stack.getId());

            // delete all flashcards
            for (Flashcard flashcard : allStackFlashcards) {
                deleteFlashcard(flashcard.getId());
            }
        }

        // Delete the stack
        db.delete(TABLE_STACK, KEY_ID + " = ?",
                new String[] { String.valueOf(stack.getId()) });
    }

    /*
     * Create a Flashcard-Stack
     */
    public long createFlashcardStack(long flashcard_id, long stack_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FLASHCARD_ID, flashcard_id);
        values.put(KEY_STACK_ID, stack_id);
        values.put(KEY_CREATED_AT, getDateTime());

        long id = db.insert(TABLE_FLASHCARD_STACK, null, values);

        return id;
    }

    /**
     * get datetime
     * */
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
