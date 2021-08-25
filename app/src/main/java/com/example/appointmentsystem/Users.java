package com.example.appointmentsystem;

public class Users {
    private String id,username,doctorId,imagelink,accounttype,email,gender,phone,department,age,patient,preference;
    public Users(){}
    public Users(String id, String username, String doctorId, String imagelink, String accounttype, String phone,
    String email, String department,String age, String patient,String gender,String preference) {
        this.id = id;
        this.username = username;
        this.doctorId = doctorId;
        this.imagelink = imagelink;
        this.accounttype = accounttype;
        this.phone = phone;
        this.email = email;
        this.department = department;
        this.age = age;
        this.patient = patient;
        this.gender = gender;
        this.preference = preference;
    }

    public String getPreference() {
        return preference;
    }

    public void setPreference(String preference) {
        this.preference = preference;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getAccounttype() {
        return accounttype;
    }

    public void setAccounttype(String accounttype) {
        this.accounttype = accounttype;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public String getImagelink() {
        return imagelink;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public void setImagelink(String imagelink) {
        this.imagelink = imagelink;
    }
}
