package com.ds.Controllers;

import com.ds.Entities.Customer;
import com.ds.Entities.Image;
import com.ds.Entities.Plot;
import com.ds.Helpers.Location.Il;
import com.ds.Helpers.Location.Ilce;
import com.ds.Helpers.Location.JsonHelper;
import com.ds.Helpers.Location.Semt;
import com.ds.Managers.CustomerManager;
import com.ds.Managers.ImageManager;
import com.ds.Managers.PlotManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PlotUpdateController {

    @FXML private TextField txtTitle;
    @FXML private TextField txtDescription;
    @FXML private TextField txtSize;

    @FXML private ComboBox<String> cmbCity;
    @FXML private ComboBox<String> cmbDistrict;
    @FXML private ComboBox<String> cmbNeighborhood;
    @FXML private ComboBox<String> cmbQuarter;
    @FXML private TextField txtStreet;

    @FXML private TextField txtPrice;
    @FXML private TextField txtZoningStatus;

    @FXML private ComboBox<Customer> cmbCustomer;

    @FXML private ListView<String> lstSelectedImages;
    @FXML private Label lblSelectedImageCount;

    @FXML private ListView<Image> lstExistingImages;

    private MainController mainController;
    private PlotManager plotManager;
    private ImageManager imageManager;
    private Plot currentProperty;
    private List<Il> turkiyeVerisi;

    private List<File> selectedFiles = new ArrayList<>();

    public PlotUpdateController() {
        this.plotManager = new PlotManager();
        this.imageManager = new ImageManager();
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        turkiyeVerisi = JsonHelper.getDataAsList();
        if (turkiyeVerisi != null) {
            for (Il il : turkiyeVerisi) {
                cmbCity.getItems().add(il.getAd());
            }
        }
        setupLocationListeners();

        lstExistingImages.setCellFactory(param -> new ListCell<Image>() {
            @Override
            protected void updateItem(Image item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getPath());
                }
            }
        });
    }

    public void initPage(Plot property) {
        this.currentProperty = property;

        txtTitle.setText(property.getTitle());
        txtDescription.setText(property.getDescription());
        txtSize.setText(String.valueOf(property.getSize()));
        txtPrice.setText(String.valueOf(property.getPrice()));
        txtZoningStatus.setText(property.getZoningStatus());

        String fullLocation = property.getLocation();
        if (fullLocation != null && !fullLocation.isEmpty()) {
            String[] parts = fullLocation.split(" / ");
            try {
                if (parts.length >= 1) cmbCity.setValue(parts[0]);
                if (parts.length >= 2) cmbDistrict.setValue(parts[1]);
                if (parts.length >= 3) cmbNeighborhood.setValue(parts[2]);
                if (parts.length >= 4) cmbQuarter.setValue(parts[3]);
                if (parts.length >= 5) txtStreet.setText(parts[4]);
            } catch (Exception e) {}
        }

        CustomerManager customerManager = new CustomerManager();
        List<Customer> customers = customerManager.getAll();
        cmbCustomer.getItems().setAll(customers);
        cmbCustomer.setConverter(new StringConverter<Customer>() {
            @Override
            public String toString(Customer c) { return c == null ? null : c.getFirstName() + " " + c.getLastName(); }
            @Override
            public Customer fromString(String s) { return null; }
        });
        for(Customer customer : cmbCustomer.getItems()) {
            if(customer.getId().equals(currentProperty.getCustomerId())) {
                cmbCustomer.setValue(customer);
                break;
            }
        }

        List<Image> existingImages = imageManager.getImagesByRealEstateId(currentProperty.getId());
        lstExistingImages.getItems().setAll(existingImages);
    }

    @FXML
    private void handleDeleteExistingImage() {
        Image selectedImage = lstExistingImages.getSelectionModel().getSelectedItem();
        if (selectedImage == null) {
            mainController.showMessage("Silinecek bir resim seçiniz.", true);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Resmi Sil");
        alert.setHeaderText("Bu resmi silmek istediğinize emin misiniz?");
        alert.setContentText(selectedImage.getPath());

        if (alert.showAndWait().get() == ButtonType.OK) {
            imageManager.delete(selectedImage.getId());
            imageManager.save();

            lstExistingImages.getItems().remove(selectedImage);
            mainController.showMessage("Resim silindi.", false);
        }
    }

    @FXML
    private void handleSelectImages() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Yeni Resimleri Seç");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Resimler", "*.png", "*.jpg", "*.jpeg"));
        List<File> files = fileChooser.showOpenMultipleDialog(txtTitle.getScene().getWindow());

        if (files != null) {
            selectedFiles.clear();
            lstSelectedImages.getItems().clear();
            selectedFiles.addAll(files);
            for (File file : files) lstSelectedImages.getItems().add(file.getName());
            lblSelectedImageCount.setText(files.size() + " yeni dosya seçildi");
        }
    }

    @FXML
    private void handleSave() {
        if(txtTitle.getText().isEmpty() || txtPrice.getText().isEmpty()) {
            mainController.showMessage("Lütfen zorunlu alanları doldurunuz.", true);
            return;
        }

        String fullLocation = cmbCity.getValue() + " / " + cmbDistrict.getValue() + " / " +
                cmbNeighborhood.getValue() + " / " + cmbQuarter.getValue() + " / " + txtStreet.getText();

        this.currentProperty.setTitle(txtTitle.getText());
        this.currentProperty.setDescription(txtDescription.getText());
        this.currentProperty.setSize(new BigDecimal(txtSize.getText()));
        this.currentProperty.setLocation(fullLocation);
        this.currentProperty.setPrice(new BigDecimal(txtPrice.getText()));
        this.currentProperty.setZoningStatus(txtZoningStatus.getText());
        this.currentProperty.setCustomerId(cmbCustomer.getValue().getId());

        plotManager.update(this.currentProperty).save();

        try {
            if (!selectedFiles.isEmpty()) {
                int currentPlotId = this.currentProperty.getId();
                for (File file : selectedFiles) {
                    String savedFileName = imageManager.savePhysicalImage(file);
                    if (savedFileName != null) {
                        Image imageEntity = new Image();
                        imageEntity.setRealEstateId(currentPlotId);
                        imageEntity.setPath(savedFileName);
                        imageManager.create(imageEntity);
                    }
                }
                imageManager.save();
            }
        } catch (Exception e) {
            e.printStackTrace();
            mainController.showMessage("Resimler kaydedilirken hata oluştu.", true);
        }

        if (mainController != null) {
            mainController.handleArsa();
            mainController.showMessage("Arazi başarıyla güncellendi.", false);
        }
    }

    public void handleIptal() {
        if(mainController != null) mainController.handleArsa();
    }

    private void setupLocationListeners() {
        cmbCity.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                cmbDistrict.getItems().clear(); cmbNeighborhood.getItems().clear(); cmbQuarter.getItems().clear();
                cmbDistrict.setDisable(false); cmbNeighborhood.setDisable(true); cmbQuarter.setDisable(true);
                Il secilenIl = ilBul(newVal);
                if (secilenIl != null) secilenIl.getIlceler().forEach(ilce -> cmbDistrict.getItems().add(ilce.getAd()));
            }
        });
        cmbDistrict.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                cmbNeighborhood.getItems().clear(); cmbQuarter.getItems().clear();
                cmbNeighborhood.setDisable(false); cmbQuarter.setDisable(true);
                Il secilenIl = ilBul(cmbCity.getValue());
                if (secilenIl != null) {
                    Ilce secilenIlce = ilceBul(secilenIl, newVal);
                    if (secilenIlce != null) secilenIlce.getSemtler().forEach(semt -> cmbNeighborhood.getItems().add(semt.getAd()));
                }
            }
        });
        cmbNeighborhood.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                cmbQuarter.getItems().clear(); cmbQuarter.setDisable(false);
                Il secilenIl = ilBul(cmbCity.getValue());
                if (secilenIl != null) {
                    Ilce secilenIlce = ilceBul(secilenIl, cmbDistrict.getValue());
                    if (secilenIlce != null) {
                        Semt secilenSemt = semtBul(secilenIlce, newVal);
                        if (secilenSemt != null) cmbQuarter.setItems(FXCollections.observableArrayList(secilenSemt.getMahalleler()));
                    }
                }
            }
        });
    }

    private Il ilBul(String ilAdi) {
        if(turkiyeVerisi == null || ilAdi == null) return null;
        return turkiyeVerisi.stream().filter(il -> il.getAd().equals(ilAdi)).findFirst().orElse(null);
    }
    private Ilce ilceBul(Il il, String ilceAdi) {
        if(il == null || ilceAdi == null) return null;
        return il.getIlceler().stream().filter(ilce -> ilce.getAd().equals(ilceAdi)).findFirst().orElse(null);
    }
    private Semt semtBul(Ilce ilce, String semtAdi) {
        if(ilce == null || semtAdi == null) return null;
        return ilce.getSemtler().stream().filter(semt -> semt.getAd().equals(semtAdi)).findFirst().orElse(null);
    }
}