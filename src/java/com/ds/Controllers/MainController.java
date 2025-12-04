package com.ds.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MainController {

    @FXML
    private BorderPane mainPane;

    @FXML
    public void initialize() {
        sayfaYukle("anasayfa");
    }

    @FXML
    public void handleAnasayfa() {
        sayfaYukle("anasayfa");
        System.out.println("Anasayfa");
    }

    @FXML
    public void handleBireyselKullanicilar() {
        sayfaYukle("bireyselKullanicilarAnasayfa");
    }

    @FXML
    public void handleKurumsalKullanicilar()
    {
        System.out.println("Kurumsal Kullanicilar");
    }

    @FXML
    public void handleKonut() {
        System.out.println("Konut");
    }

    @FXML
    public void handleArsa() {
        System.out.println("Arsa");
    }

    private void sayfaYukle(String fxmlDosyaAdi) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ds/" + fxmlDosyaAdi + ".fxml"));
            Parent yeniSayfa = loader.load();

            mainPane.setCenter(yeniSayfa);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
