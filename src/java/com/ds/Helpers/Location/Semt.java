package com.ds.Helpers.Location;

import java.util.ArrayList;
import java.util.List;

public class Semt {
    private String ad;
    private List<String> mahalleler = new ArrayList<>();

    public Semt(String ad) { this.ad = ad; }

    public String getAd() { return ad; }
    public List<String> getMahalleler() { return mahalleler; }
    public void addMahalle(String mahalle) { this.mahalleler.add(mahalle); }
}