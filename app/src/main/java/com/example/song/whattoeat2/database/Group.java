package com.example.song.whattoeat2.database;

public class Group {

    private long id;
    private String name;

    public Group(long id, String name) {
        this(name);
        this.id = id;
    }
    public Group(String name) {
        this.name =  name;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name= name;
    }
}
