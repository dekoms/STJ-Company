package com.example.profile;

import android.net.Uri;

public class MainData {
    private Uri ivProfileUri;
    private String name;
    private String age;
    private String gender;
    private String birth;

    public MainData(Uri ivProfileUri, String name, String age, String gender,String birth) {
        this.ivProfileUri = ivProfileUri;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.birth = birth;
    }

    public Uri getIvProfileUri() {
        return ivProfileUri;
    }

    public void setIvProfileUri(Uri ivProfileUri) {
        this.ivProfileUri = ivProfileUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
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
}