package com.example.finalproject.apitest.dto.common;

public class Company {
    private Headquarters hq;

    public Company() {}
    public Company(Headquarters hq) { this.hq = hq; }

    public Headquarters getHq() { return hq; }
    public void setHq(Headquarters hq) { this.hq = hq; }
}
