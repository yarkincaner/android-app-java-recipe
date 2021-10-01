package com.example.myrecipes.dto;

public class User {
    private String userName;
    private String email;
    private String name;
    private String surname;

    public User(String userName, String email, String name, String surname) {
        this.userName = userName;
        this.email = email;
        this.name = name;
        this.surname = surname;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
