package com.example.petme;

/*
    사용자 계정 정보 모델
 */

public class UserAccount {

    private String idTokken;    // firebase UID 고유 유저번호
    private String name;        // 닉네임
    private String emailID;
    private String password;

    public UserAccount() { } // firebase database 사용시 빈 생성자 필요 !

    public String getName() {
        return name;
    }

    public String getEmailID() {
        return emailID;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setIdTokken(String idTokken) {
        this.idTokken = idTokken;
    }

    public String getIdTokken() {
        return idTokken;
    }
}
