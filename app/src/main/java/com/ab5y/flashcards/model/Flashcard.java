package com.ab5y.flashcards.model;

/**
 * Created by Abhay on 2/5/2016.
 */
public class Flashcard {
    int id;
    String name;
    String content;
    String created_at;

    // constructors

    public Flashcard(){}

    public Flashcard(String name, String content){
        this.name = name;
        this.content = content;
    }

    public Flashcard(int id, String name, String content) {
        this.id = id;
        this.name = name;
        this.content = content;
    }

    // setters

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setContent(String content){
        this.content = content;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    // getters

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getContent() {
        return this.content;
    }

    public String getCreated_at() {
        return this.created_at;
    }
}
