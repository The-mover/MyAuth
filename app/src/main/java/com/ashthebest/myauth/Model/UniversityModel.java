package com.ashthebest.myauth.Model;

/**
 * Created by Ashraful on 5/23/2018.
 */

public class UniversityModel {
    private String universityName;
    private String userListKey;

    public UniversityModel() {
    }

    public UniversityModel(String universityName, String userListKey) {
        this.universityName = universityName;
        this.userListKey = userListKey;
    }

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    public String getUserListKey() {
        return userListKey;
    }

    public void setUserListKey(String userListKey) {
        this.userListKey = userListKey;
    }
}
