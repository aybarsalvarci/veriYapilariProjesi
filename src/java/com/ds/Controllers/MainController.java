package com.ds.Controllers;

import com.ds.Entities.IndividualCustomer;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import java.io.IOException;

public class MainController {

    @FXML
    private BorderPane mainPane;
    @FXML
    private HBox msgBar;
    @FXML
    private Label lblMsg;

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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ds/bireyselKullanicilarAnasayfa.fxml"));
            Parent view = loader.load();

            IndividualCustomerController controller = loader.getController();
            controller.setMainController(this);

            mainPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void handleUpdateIndividualCustomer(IndividualCustomer customer)
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ds/updateIndividualCustomer.fxml"));
            Parent view = loader.load();

            IndividualCustomerUpdateController controller = loader.getController();
            controller.setMainController(this);
            controller.initPage(customer);

            mainPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleCreateIndividualCustomer() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ds/createIndividualCustomer.fxml"));
            Parent view = loader.load();

            IndividualCustomerAddController controller = loader.getController();
            controller.setMainController(this);

            mainPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showMessage(String mesaj, boolean isError) {
        // 1. Mesajı ve Rengi Ayarla
        lblMsg.setText(mesaj);

        if (isError) {
            msgBar.setStyle("-fx-background-color: #EF4444;");
        } else {
            msgBar.setStyle("-fx-background-color: #10B981;");
        }

        msgBar.setVisible(true);
        msgBar.setManaged(true);

        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(event -> {
            msgBar.setVisible(false);
            msgBar.setManaged(false);
        });
        delay.play();
    }
}
