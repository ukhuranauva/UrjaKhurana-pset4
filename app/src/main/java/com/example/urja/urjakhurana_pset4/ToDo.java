package com.example.urja.urjakhurana_pset4;

/* This is the object constructor for a task, where an object consists of the task itself
 * and the status of the task: tbd means to be done, done means that the task has been done. */
public class ToDo {
    String status;
    String task;

    // This is the constructor
    public ToDo(String status, String task){
        this.status = status;
        this.task = task;
    }
}
