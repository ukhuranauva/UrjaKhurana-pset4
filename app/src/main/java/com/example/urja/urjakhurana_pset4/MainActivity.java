package com.example.urja.urjakhurana_pset4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/* This activity is the main activity, where the tasks are shown and the CRUD methods are called*/
public class MainActivity extends AppCompatActivity {

    DBhelper DBhelper;

    ListView toDoList;
    ListView doneList;
    EditText taskBar;
    ArrayList<String> tasks;
    ArrayList<String> toDoTasks;
    ArrayList<String> doneTasks;
    ArrayAdapter adapter;
    ArrayAdapter adapterDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // get all variables
        toDoList = (ListView) findViewById(R.id.listView);
        doneList = (ListView) findViewById(R.id.doneView);
        taskBar = (EditText) findViewById(R.id.toDo);
        DBhelper = new DBhelper(this);
        tasks = DBhelper.read();
        // divides the tasks between the done tasks and the tasks yet to be executed
        setLists();
        // adapter for the to do list
        adapter = new ArrayAdapter(this, R.layout.listview_layout, R.id.taskView, toDoTasks);
        toDoList.setAdapter(adapter);
        // adapter for the done list
        adapterDone = new ArrayAdapter(this, android.R.layout.simple_list_item_1, doneTasks);
        doneList.setAdapter(adapterDone);
        // listeners for both lists
        setListeners();
    }

    // divides the tasks between done and to do and puts this in lists
    public void setLists() {
        toDoTasks = new ArrayList<>();
        doneTasks = new ArrayList<>();
        // get each task
        for (int i=0; i < tasks.size(); i++) {
            String task = tasks.get(i);
            // get status of task
            String status = getTaskStatus(task);
            // if task is done
            if (status.equals("done")) {
                doneTasks.add(task);
            } else {
                // if task is to be done
                toDoTasks.add(task);
            }
        }
    }

    // adds the given task by user
    public void addTask(View view) {
        String task = taskBar.getText().toString();
        // creat task obect where status is to be done on default
        ToDo toDo = new ToDo("tbd", task);
        DBhelper.create(toDo);
        // update adapter
        adapter.add(toDo.task);
        adapter.notifyDataSetChanged();
    }

    // delete task from to do list
    public void deleteTaskToDo(String task) {
        int id = getTaskId(task);
        DBhelper.delete(id);
        // update adapter
        adapter.remove(task);
        adapter.notifyDataSetChanged();
    }

    // delete task from done list
    public void deleteTaskDone(String task) {
        int id = getTaskId(task);
        DBhelper.delete(id);
        // update adapter
        adapterDone.remove(task);
        adapterDone.notifyDataSetChanged();
    }

    // set on click listeners on both list views, delete item when it is long clicked on
    public void setListeners() {
        // for to do list
        toDoList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long viewId) {
                String task = toDoList.getItemAtPosition(position).toString();
                deleteTaskToDo(task);
                return true;
            }
        });
        // for done list
        doneList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long viewId) {
                String task = doneList.getItemAtPosition(position).toString();
                deleteTaskDone(task);
                return true;
            }
        });
    }

    // given a task, this returns the id of the task in the database
    public int getTaskId(String task) {
        // initialize int id
        int id = 0;
        // get all the tasks and their respectful id
        ArrayList<HashMap<String, String>> idHashMap = DBhelper.getId();
        for (int i = 0; i < idHashMap.size(); i++) {
            HashMap tempMap = idHashMap.get(i);
            String taskName = (String) tempMap.get("task");
            // if this item is the given task
            if (taskName.equals(task)) {
                // get the id of the task
                String tempid = (String) tempMap.get("id");
                id = Integer.parseInt(tempid);
                break;
            }
        }
        return id;
    }

    // given the task, get the status
    public String getTaskStatus(String task) {
        // initialize variable
        String status = null;
        // get the status of all the tasks
        ArrayList<HashMap<String, String>> idHashMap = DBhelper.getStatus();
        for (int i = 0; i < idHashMap.size(); i++) {
            HashMap tempMap = idHashMap.get(i);
            String taskName = (String) tempMap.get("task");
            // if this is the given task
            if (taskName.equals(task)) {
                // get status
                status = (String) tempMap.get("status");
                break;
            }
        }
        return status;
    }

    // when a user indicates a task is done by clicking on the checkbox
    public void updateStatus(View view) {
        // credit to tirza soute for helping with the first two lines, find the proper task of box
        RelativeLayout layout = (RelativeLayout) view.getParent();
        // get the task
        TextView taskView = (TextView) layout.getChildAt(0);
        String task = taskView.getText().toString();
        CheckBox checkbox = (CheckBox) view;
        // get id of task
        int id = getTaskId(task);
        // update status of task to done
        DBhelper.update(id, "done");
        // update adaptors
        adapter.remove(task);
        adapterDone.add(task);
        adapter.notifyDataSetChanged();
        adapterDone.notifyDataSetChanged();
        // set check of the checkbox back to false
        checkbox.setChecked(false);
    }
}
