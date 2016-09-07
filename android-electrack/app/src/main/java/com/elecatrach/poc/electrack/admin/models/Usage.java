package com.elecatrach.poc.electrack.admin.models;


public class Usage {

    String month;
    int totalAmount;
    int totalUnits;
    int month_value;
    int year;

    public Usage() {

    }


    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getTotalUnits() {
        return totalUnits;
    }

    public void setTotalUnits(int totalUnits) {
        this.totalUnits = totalUnits;
    }

    public int getMonth_value() {
        return month_value;
    }

    public void setMonth_value(int month_value) {
        this.month_value = month_value;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
