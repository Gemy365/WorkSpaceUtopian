package com.example.android.trial5daysworkspace.Model;
// Keys For Firebase Database.
public class DrinksDay {

    private int totalDay;
    private int totalDrink;
    private String noteOtherCash;


    public DrinksDay() {
    }

    // For Drinks & Total Of Day Of Users.
    public DrinksDay(int totalDay, int totalDrink) {
        this.totalDay = totalDay;
        this.totalDrink = totalDrink;
    }

    // For Notes & Total Of Day Of MANAGER.
    public DrinksDay(int totalDay, String noteOtherCash) {
        this.totalDay = totalDay;
        this.noteOtherCash = noteOtherCash;
    }

    public int getTotalDay() {
        return totalDay;
    }

    public void setTotalDay(int totalDay) {
        this.totalDay = totalDay;
    }

    public int getTotalDrink() {
        return totalDrink;
    }

    public void setTotalDrink(int totalDrink) {
        this.totalDrink = totalDrink;
    }

    public String getNoteOtherCash() {
        return noteOtherCash;
    }

    public void setNoteOtherCash(String noteOtherCash) {
        this.noteOtherCash = noteOtherCash;
    }
}
