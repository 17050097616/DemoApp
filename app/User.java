package com.echase.cashier.utils;

import com.google.gson.annotations.SerializedName;

/**
 * Created by chm on 2018/4/17.
 */

public class User {
    public User() {
    }

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public User(String name, int age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
    }

    public String name;
    public int age;
    @SerializedName(value = "emailAddress")
    public String email;
}
