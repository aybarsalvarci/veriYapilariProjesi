package com.ds.Helpers.Location;

import java.util.ArrayList;
import java.util.List;

public class Il {
    private String ad;
    private List<Ilce> ilceler = new ArrayList<>();

    public Il(String ad) { this.ad = ad; }

    public String getAd() { return ad; }
    public List<Ilce> getIlceler() { return ilceler; }
    public void addIlce(Ilce ilce) { this.ilceler.add(ilce); }
}