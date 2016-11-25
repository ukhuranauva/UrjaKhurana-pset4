package com.example.urja.urjakhurana_pset4;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/* This class is a database helper, where the database is opened, updated and the CRUD method is
* used to edit the database and add tasks etc. */

public class DBhelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "toDoDB.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE = "todo";

    private String task_id = "task";
    private String status = "status";

    // Constructor of the helper
    public DBhelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creates database if not created yet
        String CREATE_TABLE = "CREATE TABLE " + TABLE +  " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + status + " TEXT, "  + task_id + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Upgrade database
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    // When a task is added, this adds the task to the database
    public void create(ToDo task) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        // put task and status in database
        values.put(task_id, task.task);
        values.put(status, task.status);
        db.insert(TABLE, null, values);
        db.close();
    }

    // Gives back all the tasks in the database
    public ArrayList<String> read(){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT _id , " + task_id + " FROM " + TABLE;
        ArrayList<String> toDoList = new ArrayList<>();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                toDoList.add(cursor.getString(cursor.getColumnIndex(task_id)));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return toDoList;
    }

    // When the status is changed, this function is called
    public void update(int id, String statusTask) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(status, statusTask);
        db.update(TABLE, values, "_id = ? ", new String[] {String.valueOf(id)});
        db.close();
    }

    // When a task gets deleted, the task in the database gets deleted here
    public void delete(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE, " _id = ? ", new String[] {String.valueOf(id)});
    }

    // With this function, the ids of all the tasks in the database is given
    public ArrayList<HashMap<String, String>> getId(){
        SQLiteDatabase db = getReadableDatabase();
        // get all ids and tasks
        String query = "SELECT _id , " + task_id + " FROM " + TABLE;
        ArrayList<HashMap<String, String>> toDoList = new ArrayList<>();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                // put id and task in one hashmap
                HashMap<String, String> taskData = new HashMap<>();
                taskData.put("id", cursor.getString(cursor.getColumnIndex("_id")));
                taskData.put("task", cursor.getString(cursor.getColumnIndex(task_id)));
                toDoList.add(taskData);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return toDoList;
    }

    // Here, the status of all the tasks is given with this function
    public ArrayList<HashMap<String, String>> getStatus(){
        SQLiteDatabase db = getReadableDatabase();
        // get tasks and statusses
        String query = "SELECT _id , " + task_id + ", " + status + " FROM " + TABLE;
        ArrayList<HashMap<String, String>> toDoList = new ArrayList<>();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                // add status and task to hashmap
                HashMap<String, String> taskData = new HashMap<>();
                taskData.put("status", cursor.getString(cursor.getColumnIndex(status)));
                taskData.put("task", cursor.getString(cursor.getColumnIndex(task_id)));
                toDoList.add(taskData);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return toDoList;
    }


}
