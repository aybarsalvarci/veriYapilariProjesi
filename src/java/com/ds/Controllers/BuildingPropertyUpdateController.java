package com.ds.Controllers;

import com.ds.Entities.BuildingProperty;
import com.ds.Entities.Customer;
import com.ds.Entities.Image; // Image entity
import com.ds.Enums.ContractType;
import com.ds.Helpers.Location.Il;
import com.ds.Helpers.Location.Ilce;
import com.ds.Helpers.Location.JsonHelper;
import com.ds.Helpers.Location.Semt;
import com.ds.Managers.BuildingPropertyManager;
import com.ds.Managers.CustomerManager;
import com.ds.Managers.ImageManager; // Image Manager

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BuildingPropertyUpdateController {

    @FXML private TextField txtTitle;
    @FXML private TextField txtDescription;
    @FXML private TextField txtSize;

    @FXML private ComboBox<String> cmbCity;
    @FXML private ComboBox<String> cmbDistrict;
    @FXML private ComboBox<String> cmbNeighborhood;
    @FXML private ComboBox<String> cmbQuarter;
    @FXML private TextField txtStreet;

    @FXML private TextField txtPrice;
    @FXML private TextField txtIndependenceType;

    @FXML private ComboBox<Customer> cmbCustomer;
    @FXML private ComboBox<ContractType> cmbContractType;

    @FXML private ListView<String> lstSelectedImages;
    @FXML private Label lblSelectedImageCount;
    @FXML private ListView<Image> lstExistingImages;

    private MainController mainController;
    private BuildingPropertyManager buildingPropertyManager;
    private ImageManager imageManager;
    private BuildingProperty currentProperty;
    private List<Il> turkiyeVerisi;

    private List<File> selectedFiles = new ArrayList<>();

    public BuildingPropertyUpdateController() {
        this.buildingPropertyManager = new BuildingPropertyManager();
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
            @Override protected void updateItem(Image item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : item.getPath());
            }
        });
    }

    public void initPage(BuildingProperty property) {
        this.currentProperty = property;

        txtTitle.setText(property.getTitle());
        txtDescription.setText(property.getDescription());
        txtSize.setText(String.valueOf(property.getSize()));
        txtPrice.setText(String.valueOf(property.getPrice()));
        txtIndependenceType.setText(property.getIndependenceType());

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
            @Override public String toString(Customer c) { return c == null ? null : c.getFirstName() + " " + c.getLastName(); }
            @Override public Customer fromString(String s) { return null; }
        });
        for(Customer customer : cmbCustomer.getItems()) {
            if(customer.getId().equals(currentProperty.getCustomerId())) {
                cmbCustomer.setValue(customer); break;
            }
        }

        cmbContractType.getItems().setAll(ContractType.values());
        cmbContractType.setConverter(new StringConverter<ContractType>() {
            @Override public String toString(ContractType c) { return c == null ? null : c.getLabel(); }
            @Override public ContractType fromString(String s) { return null; }
        });
        cmbContractType.setValue(currentProperty.getContractType());

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
        String title = txtTitle.getText();
        String description = txtDescription.getText();
        String sizeInput = txtSize.getText();
        String city = cmbCity.getValue();
        String district = cmbDistrict.getValue();
        String neighborhood = cmbNeighborhood.getValue();
        String quarter = cmbQuarter.getValue();
        String street = txtStreet.getText();
        String priceInput = txtPrice.getText();
        String independenceType = txtIndependenceType.getText();
        ContractType contractType = cmbContractType.getValue();
        Customer customer = cmbCustomer.getValue();

        if(title.isEmpty() || description.isEmpty() || sizeInput.isEmpty() || priceInput.isEmpty() || independenceType.isEmpty()) {
            mainController.showMessage("Lütfen zorunlu alanları doldurunuz.", true); return;
        }
        if (city == null || district == null || neighborhood == null || quarter == null || street.isEmpty()) {
            mainController.showMessage("Adres bilgilerini eksiksiz giriniz.", true); return;
        }
        if(contractType == null || customer == null) {
            mainController.showMessage("Kontrat ve Müşteri seçiniz.", true); return;
        }

        BigDecimal size, price;
        try {
            size = new BigDecimal(sizeInput);
            price = new BigDecimal(priceInput);
        } catch (NumberFormatException e) {
            mainController.showMessage("Sayısal alanlar hatalı.", true); return;
        }

        String fullLocation = city + " / " + district + " / " + neighborhood + " / " + quarter + " / " + street;

        this.currentProperty.setTitle(title);
        this.currentProperty.setDescription(description);
        this.currentProperty.setSize(size);
        this.currentProperty.setLocation(fullLocation);
        this.currentProperty.setPrice(price);
        this.currentProperty.setIndependenceType(independenceType);
        this.currentProperty.setContractType(contractType);
        this.currentProperty.setCustomerId(customer.getId());

        buildingPropertyManager.update(this.currentProperty).save();

        try {
            if (!selectedFiles.isEmpty()) {
                int currentId = this.currentProperty.getId();
                for (File file : selectedFiles) {
                    String savedName = imageManager.savePhysicalImage(file);
                    if (savedName != null) {
                        Image img = new Image();
                        img.setRealEstateId(currentId);
                        img.setPath(savedName);
                        imageManager.create(img);
                    }
                }
                imageManager.save();
            }
        } catch (Exception e) {
            e.printStackTrace();
            mainController.showMessage("Resimler kaydedilirken hata oluştu.", true);
        }

        if (mainController != null) {
            mainController.handleKonut();
            mainController.showMessage("Konut başarıyla güncellendi.", false);
        }
    }

    public void handleIptal() {
        if(mainController != null) mainController.handleKonut();
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