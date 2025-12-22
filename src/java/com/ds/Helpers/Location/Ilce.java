package com.ds.Helpers.Location;

import java.util.ArrayList;
import java.util.List;

public class Ilce {
    private String ad;
    private List<Semt> semtler = new ArrayList<>();

    public Ilce(String ad) { this.ad = ad; }

    public String getAd() { return ad; }
    public List<Semt> getSemtler() { return semtler; }
    public void addSemt(Semt semt) { this.semtler.add(semt); }
}