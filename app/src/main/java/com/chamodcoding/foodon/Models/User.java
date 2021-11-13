package com.chamodcoding.foodon.Models;

public class User {
    private String fname;
    private String lname;
    private String address;
    private String phone;
    private String email;
    private String password;
    private String role;

    public User() {}

    public User(String fname, String lname, String address, String phone, String email, String password, String role) {
        this.fname = fname;
        this.lname = lname;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
