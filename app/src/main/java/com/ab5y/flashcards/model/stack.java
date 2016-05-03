package com.ab5y.flashcards.model;

/**
 * Created by Abhay on 2/5/2016.
 */
public class Stack {
    int id;
    String name;
    String created_at;

    public Stack(){}

    public Stack(String name) {
        this.name = name;
    }

    public Stack(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return name;
    }

    public String getCreated_at() {
        return created_at;
    }
}
