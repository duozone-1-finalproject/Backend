package com.example.apitest.dto;

public class Headquarters {
    private String address;
    private String phone;
    private String website;

    public Headquarters() {}
    public Headquarters(String address, String phone, String website) {
        this.address = address;
        this.phone = phone;
        this.website = website;
    }

    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public String getWebsite() { return website; }

    public void setAddress(String address) { this.address = address; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setWebsite(String website) { this.website = website; }
}
