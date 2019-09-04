package com.example.android.trial5daysworkspace.Model;

public class ExpiredTrial {
    String isBegan;
    int Day;
    int month;
    int year;

    public ExpiredTrial() {
    }

    public ExpiredTrial(String isBegan, int day, int month, int year) {
        this.isBegan = isBegan;
        Day = day;
        this.month = month;
        this.year = year;
    }

    public String getIsBegan() {
        return isBegan;
    }

    public void setIsBegan(String isBegan) {
        this.isBegan = isBegan;
    }

    public int getDay() {
        return Day;
    }

    public void setDay(int day) {
        Day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
