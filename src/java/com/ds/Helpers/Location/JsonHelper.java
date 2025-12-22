package com.ds.Helpers.Location;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class JsonHelper {

    public static List<Il> getDataAsList() {
        List<Il> tumIller = new ArrayList<>();

        try {
            InputStream inputStream = JsonHelper.class.getResourceAsStream("/ililcesemtmahalle.txt");
            if (inputStream == null) {
                throw new RuntimeException("Dosya bulunamadı!");
            }

            Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name());
            String jsonContent = scanner.useDelimiter("\\A").next();
            scanner.close();

            JSONArray jsonIlListesi = new JSONArray(jsonContent);

            for (int i = 0; i < jsonIlListesi.length(); i++) {
                JSONObject jsonIl = jsonIlListesi.getJSONObject(i);
                Il yeniIl = new Il(jsonIl.getString("il"));

                JSONArray jsonIlceler = jsonIl.getJSONArray("ilceler");
                for (int j = 0; j < jsonIlceler.length(); j++) {
                    JSONObject jsonIlce = jsonIlceler.getJSONObject(j);
                    Ilce yeniIlce = new Ilce(jsonIlce.getString("ilce"));

                    JSONArray jsonSemtler = jsonIlce.getJSONArray("semtler");
                    for (int k = 0; k < jsonSemtler.length(); k++) {
                        JSONObject jsonSemt = jsonSemtler.getJSONObject(k);
                        Semt yeniSemt = new Semt(jsonSemt.getString("semt"));

                        JSONArray jsonMahalleler = jsonSemt.getJSONArray("mahalleler");
                        for (int m = 0; m < jsonMahalleler.length(); m++) {
                            yeniSemt.addMahalle(jsonMahalleler.getString(m));
                        }
                        yeniIlce.addSemt(yeniSemt);
                    }
                    yeniIl.addIlce(yeniIlce);
                }

                tumIller.add(yeniIl);
            }

        } catch (Exception e) {
            System.err.println("Parse hatası: " + e.getMessage());
            e.printStackTrace();
        }

        return tumIller;
    }
}