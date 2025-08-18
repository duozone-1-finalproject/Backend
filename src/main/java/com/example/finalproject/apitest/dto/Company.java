package com.example.apitest.dto;

import com.example.apitest.dto.Headquarters;

public class Company {
    private Headquarters hq;

    public Company() {}
    public Company(Headquarters hq) { this.hq = hq; }

    public Headquarters getHq() { return hq; }
    public void setHq(Headquarters hq) { this.hq = hq; }
}
