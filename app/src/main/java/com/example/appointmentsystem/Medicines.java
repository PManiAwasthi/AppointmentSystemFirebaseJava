package com.example.appointmentsystem;

public class Medicines {
    private String medicinename, medicinedosage, date;
    public Medicines(){}

    public Medicines(String medicinename, String medicinedosage, String date) {
        this.medicinename = medicinename;
        this.medicinedosage = medicinedosage;
        this.date = date;
    }

    public String getMedicinename() {
        return medicinename;
    }

    public void setMedicinename(String medicinename) {
        this.medicinename = medicinename;
    }

    public String getMedicinedosage() {
        return medicinedosage;
    }

    public void setMedicinedosage(String medicinedosage) {
        this.medicinedosage = medicinedosage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
