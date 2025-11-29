package com.plracticalcoding.androidLibraries.model;

public class Info {

    public  String str_name;
    public String str_age;
    public String str_gender;


    public Info(String str_name, String str_age, String str_gender) {
        this.str_name = str_name;
        this.str_age = str_age;
        this.str_gender = str_gender;
    }


    public String getStr_name() {
        return str_name;
    }

    public void setStr_name(String str_name) {
        this.str_name = str_name;
    }

    public String getStr_age() {
        return str_age;
    }

    public void setStr_age(String str_age) {
        this.str_age = str_age;
    }

    public String getStr_gender() {
        return str_gender;
    }

    public void setStr_gender(String str_gender) {
        this.str_gender = str_gender;
    }
}
