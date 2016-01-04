package com.example.song.whattoeat2.database;

public class Restaurant {

    private long id;
    private String name, number;

    public Restaurant(long id, String name, String number) {
        this(name, number);
        this.id = id;
    }
    public Restaurant(String name, String number) {
        this.name = name;
        this.number = number;
    }
    public String getName() {
        return name;
    }
    public String getNumber() {
        return number;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public long getId() {
        return id;
    }
}
