package com.example.urja.urjakhurana_pset4;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase toDoDB;

    ListView toDoList;
    EditText taskBar;
    ArrayList<String> activities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toDoList = (ListView) findViewById(R.id.listView);
        taskBar = (EditText) findViewById(R.id.toDo);

        toDoDB = this.openOrCreateDatabase("ToDoList", MODE_PRIVATE, null);
    }

    public void addTask(View view) {
        String task = taskBar.getText().toString();
        activities.add(task);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, activities);
        toDoList.setAdapter(adapter);

    }
}
