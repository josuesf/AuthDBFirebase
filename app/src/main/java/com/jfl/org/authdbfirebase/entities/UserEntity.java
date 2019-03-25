package com.jfl.org.authdbfirebase.entities;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserEntity {

    public String name;
    public String lastName;
    public String age;
    public String birthday;

    public UserEntity() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public UserEntity(String name, String lastName, String age, String birthday) {
        this.name = name;
        this.lastName = lastName;
        this.age = age;
        this.birthday = birthday;
    }
}
