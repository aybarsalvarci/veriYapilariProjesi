package com.ds.Controllers;

import com.ds.Entities.*;
import com.ds.Enums.Role;
import com.ds.Helpers.UserSession;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class MainController {

    @FXML private BorderPane mainPane;
    @FXML private HBox msgBar;
    @FXML private Label lblMsg;
    @FXML private Label lblUserName;
    @FXML private Label lblUserRole;
    @FXML private Label lblPageTitle;

    @FXML private Button btnAnasayfa;
    @FXML private Button btnBireysel;
    @FXML private Button btnKurumsal;
    @FXML private Button btnKonut;
    @FXML private Button btnArsa;
    @FXML private Button btnSistemKullanicilari;

    private Button currentActiveButton;
    private static MainController instance;

    private final String IDLE_STYLE = "-fx-background-color: transparent; -fx-text-fill: #D1D5DB; -fx-cursor: hand;";
    private final String ACTIVE_STYLE = "-fx-background-color: #374151; -fx-text-fill: #FFFFFF; -fx-cursor: hand; -fx-background-radius: 8;";

    @FXML
    public void initialize() {
        instance = this;
        SystemUser currentUser = UserSession.getInstance().getCurrentUser();

        if (currentUser != null) {
            lblUserName.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
            lblUserRole.setText(currentUser.getRole().toString());

            boolean isAdmin = (currentUser.getRole() == Role.ADMIN);
            if (btnSistemKullanicilari != null) {
                btnSistemKullanicilari.setVisible(isAdmin);
                btnSistemKullanicilari.setManaged(isAdmin);
            }
        }

        handleAnasayfa();
    }

    public static MainController getInstance() {
        return instance;
    }

    private void setActiveButtonStyle(Button clickedButton, String pageTitle) {
        Button[] navButtons = {btnAnasayfa, btnBireysel, btnKurumsal, btnKonut, btnArsa, btnSistemKullanicilari};

        for (Button btn : navButtons) {
            if (btn != null) btn.setStyle(IDLE_STYLE);
        }

        if (clickedButton != null) {
            clickedButton.setStyle(ACTIVE_STYLE);
            currentActiveButton = clickedButton;
        }

        if (lblPageTitle != null) {
            lblPageTitle.setText(pageTitle);
        }
    }


    @FXML
    public void handleAnasayfa() {
        setActiveButtonStyle(btnAnasayfa, "Dashboard / Genel Bakış");
        sayfaYukle("anasayfa");
    }

    @FXML
    public void handleBireyselKullanicilar() {
        setActiveButtonStyle(btnBireysel, "Müşteri Yönetimi / Bireysel");
        sayfaYukleWithMainController("/com/ds/bireyselKullanicilarAnasayfa.fxml", "IndividualCustomerController");
    }

    @FXML
    public void handleKurumsalKullanicilar() {
        setActiveButtonStyle(btnKurumsal, "Müşteri Yönetimi / Kurumsal");
        sayfaYukleWithMainController("/com/ds/kurumsalKullanicilarAnasayfa.fxml", "CorporateCustomerController");
    }

    @FXML
    public void handleKonut() {
        setActiveButtonStyle(btnKonut, "Gayrimenkul / Konut Listesi");
        sayfaYukleWithMainController("/com/ds/konutAnasayfa.fxml", "BuildingPropertyController");
    }

    @FXML
    public void handleArsa() {
        setActiveButtonStyle(btnArsa, "Gayrimenkul / Arazi Listesi");
        sayfaYukleWithMainController("/com/ds/arsaAnasayfa.fxml", "PlotController");
    }

    @FXML
    public void handleSistemKullanicilari() {
        setActiveButtonStyle(btnSistemKullanicilari, "Sistem Ayarları / Kullanıcılar");
        sayfaYukleWithMainController("/com/ds/systemUsers.fxml", "SystemUsersController");
    }


    @FXML
    public void handleCreateBuildingProperty() {
        sayfaYukleWithMainController("/com/ds/createBuildingProperty.fxml", "BuildingPropertyAddController");
    }

    @FXML
    public void handleCreatePlot() {
        sayfaYukleWithMainController("/com/ds/createPlot.fxml", "PlotAddController");
    }

    @FXML
    public void handleCreateIndividualCustomer() {
        sayfaYukleWithMainController("/com/ds/createIndividualCustomer.fxml", "IndividualCustomerAddController");
    }

    @FXML
    public void handleCreateCorporateCustomer() {
        sayfaYukleWithMainController("/com/ds/createCorporateCustomer.fxml", "CorporateCustomerAddController");
    }

    public void handleUpdateIndividualCustomer(IndividualCustomer customer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ds/updateIndividualCustomer.fxml"));
            Parent view = loader.load();
            IndividualCustomerUpdateController controller = loader.getController();
            controller.setMainController(this);
            controller.initPage(customer);
            mainPane.setCenter(view);
        } catch (IOException e) { e.printStackTrace(); }
    }

    public void handleUpdateCorporateCustomer(CorporateCustomer customer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ds/updateCorporateCustomer.fxml"));
            Parent view = loader.load();
            CorporateCustomerUpdateController controller = loader.getController();
            controller.setMainController(this);
            controller.initPage(customer);
            mainPane.setCenter(view);
        } catch (IOException e) { e.printStackTrace(); }
    }

    public void handleUpdateBuildingProperty(BuildingProperty property) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ds/updateBuildingProperty.fxml"));
            Parent view = loader.load();
            BuildingPropertyUpdateController controller = loader.getController();
            controller.setMainController(this);
            controller.initPage(property);
            mainPane.setCenter(view);
        } catch (IOException e) { e.printStackTrace(); }
    }

    public void handleUpdatePlot(Plot property) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ds/updatePlot.fxml"));
            Parent view = loader.load();
            PlotUpdateController controller = loader.getController();
            controller.setMainController(this);
            controller.initPage(property);
            mainPane.setCenter(view);
        } catch (IOException e) { e.printStackTrace(); }
    }


    private void sayfaYukle(String fxmlDosyaAdi) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ds/" + fxmlDosyaAdi + ".fxml"));
            mainPane.setCenter(loader.load());
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void sayfaYukleWithMainController(String path, String controllerType) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Parent view = loader.load();

            Object controller = loader.getController();

            if (controller instanceof IndividualCustomerController) ((IndividualCustomerController)controller).setMainController(this);
            else if (controller instanceof CorporateCustomerController) ((CorporateCustomerController)controller).setMainController(this);
            else if (controller instanceof BuildingPropertyController) ((BuildingPropertyController)controller).setMainController(this);
            else if (controller instanceof PlotController) ((PlotController)controller).setMainController(this);
            else if (controller instanceof SystemUsersController) ((SystemUsersController)controller).setMainController(this);
            else if (controller instanceof BuildingPropertyAddController) ((BuildingPropertyAddController)controller).setMainController(this);
            else if (controller instanceof PlotAddController) ((PlotAddController)controller).setMainController(this);
            else if (controller instanceof IndividualCustomerAddController) ((IndividualCustomerAddController)controller).setMainController(this);
            else if (controller instanceof CorporateCustomerAddController) ((CorporateCustomerAddController)controller).setMainController(this);

            mainPane.setCenter(view);
        } catch (IOException e) { e.printStackTrace(); }
    }


    public void showMessage(String message, boolean isError) {
        lblMsg.setText(message);
        msgBar.setStyle("-fx-background-color: " + (isError ? "#EF4444" : "#10B981") + "; -fx-background-radius: 8;");
        msgBar.setManaged(true);
        msgBar.setVisible(true);
        msgBar.setOpacity(0);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), msgBar);
        fadeIn.setToValue(1);
        PauseTransition stay = new PauseTransition(Duration.seconds(3));
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), msgBar);
        fadeOut.setToValue(0);

        SequentialTransition sequence = new SequentialTransition(fadeIn, stay, fadeOut);
        sequence.setOnFinished(e -> { msgBar.setVisible(false); msgBar.setManaged(false); });
        sequence.play();
    }

    @FXML
    public void handleLogout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/ds/Login.fxml"));
            Stage stage = (Stage) mainPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
        } catch (IOException e) { e.printStackTrace(); }
    }
}