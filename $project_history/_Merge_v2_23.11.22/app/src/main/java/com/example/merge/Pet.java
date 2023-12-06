package com.example.merge;

import android.net.Uri;

public class Pet {
    private String name;
    private String gender;
    private String birth;
    private String age;
    private String image; //
    public Pet() {}

    public Pet(String name, String age, String gender, String birth, String image) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.birth = birth;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}