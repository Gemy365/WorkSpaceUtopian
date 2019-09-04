package com.example.android.trial5daysworkspace.Model;

/**
 * Adapter Class [Setter & Getter Methods].
 * Setting Data From Me To Store Data InTo Firebase Need [Variables & Constructors & Set Methods].
 * Getting Data From Firebase To Me Need [Variables & Get Methods].
 */
public class InfoOfCustomers {
    private String name;
    private String phone;
    private String college;
    private String place;
    private String startTime;
    private String endTime;
    private String note;
    private int discount;
    private int totalCash;
    private int kitchen;
    private int totalHours;

    //    private String custIn;
    private String custOut;

    public InfoOfCustomers() {
    }

    // Constructor For SignUp Customers.
    public InfoOfCustomers(String name, String phone, String college, String place, String startTime,
                           String custOut) {
        this.name = name;
        this.phone = phone;
        this.college = college;
        this.place = place;
        this.startTime = startTime;
        this.custOut = custOut;
    }

    // Constructor For End Customers.
    public InfoOfCustomers(String name, String phone, String college, String place, String startTime,
                           String endTime, String note, int discount, int totalCash, int kitchen,
                           int totalHours, String custOut) {
        this.name = name;
        this.phone = phone;
        this.college = college;
        this.place = place;
        this.startTime = startTime;
        this.endTime = endTime;
        // If Note Is NOt Empty Appear Note Else "No Note!"
        this.note = (!note.equals("")) ? note : "No Note!";
        this.discount = discount;
        this.totalCash = totalCash;
        this.kitchen = kitchen;
        this.totalHours = totalHours;
        this.custOut = custOut;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getTotalCash() {
        return totalCash;
    }

    public void setTotalCash(int totalCash) {
        this.totalCash = totalCash;
    }

    public int getKitchen() {
        return kitchen;
    }

    public void setKitchen(int kitchen) {
        this.kitchen = kitchen;
    }

    public int getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(int totalHours) {
        this.totalHours = totalHours;
    }

    public String getCustOut() {
        return custOut;
    }

    public void setCustOut(String custOut) {
        this.custOut = custOut;
    }
}
