package com.example.finalproject.apitest.dto;

import com.example.finalproject.apitest.dto.Headquarters;

public class Company {
    private Headquarters hq;

    public Company() {}
    public Company(Headquarters hq) { this.hq = hq; }

    public Headquarters getHq() { return hq; }
    public void setHq(Headquarters hq) { this.hq = hq; }
}
