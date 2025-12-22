package com.ds.Controllers;

import com.ds.Entities.BuildingProperty;
import com.ds.Entities.Customer;
import com.ds.Entities.Image;
import com.ds.Enums.ContractType;
import com.ds.Helpers.Location.Il;
import com.ds.Helpers.Location.Ilce;
import com.ds.Helpers.Location.JsonHelper;
import com.ds.Helpers.Location.Semt;
import com.ds.Managers.BuildingPropertyManager;
import com.ds.Managers.CustomerManager;
import com.ds.Managers.ImageManager;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BuildingPropertyAddController {

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

    private MainController mainController;
    private BuildingPropertyManager buildingPropertyManager;
    private ImageManager imageManager;
    private List<Il> turkiyeVerisi;

    private List<File> selectedFiles = new ArrayList<>();

    public BuildingPropertyAddController() {
        this.buildingPropertyManager = new BuildingPropertyManager();
        this.imageManager = new ImageManager();
    }

    @FXML
    public void initialize() {
        loadCustomerAndContractData();

        turkiyeVerisi = JsonHelper.getDataAsList();
        if (turkiyeVerisi != null) {
            for (Il il : turkiyeVerisi) {
                cmbCity.getItems().add(il.getAd());
            }
        }
        setupLocationListeners();
    }

    @FXML
    private void handleSelectImages() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Resimleri Seç");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Resimler", "*.png", "*.jpg", "*.jpeg"));

        List<File> files = fileChooser.showOpenMultipleDialog(txtTitle.getScene().getWindow());

        if (files != null) {
            selectedFiles.clear();
            lstSelectedImages.getItems().clear();
            selectedFiles.addAll(files);

            for (File file : files) {
                lstSelectedImages.getItems().add(file.getName());
            }
            lblSelectedImageCount.setText(files.size() + " dosya seçildi");
        }
    }

    private void loadCustomerAndContractData() {
        CustomerManager customerManager = new CustomerManager();
        List<Customer> customers = customerManager.getAll();
        cmbCustomer.getItems().addAll(customers);

        cmbCustomer.setConverter(new StringConverter<Customer>() {
            @Override
            public String toString(Customer c) { return c == null ? null : c.getFirstName() + " " + c.getLastName(); }
            @Override
            public Customer fromString(String s) { return null; }
        });

        cmbContractType.getItems().addAll(ContractType.values());
        cmbContractType.setConverter(new StringConverter<ContractType>() {
            @Override
            public String toString(ContractType c) { return c == null ? null : c.getLabel(); }
            @Override
            public ContractType fromString(String s) { return null; }
        });
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

    @FXML
    private void handleCreate() {
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
            mainController.showMessage("Lütfen zorunlu alanları doldurunuz.", true);
            return;
        }
        if (city == null || district == null || neighborhood == null || quarter == null || street.isEmpty()) {
            mainController.showMessage("Adres bilgilerini eksiksiz giriniz.", true);
            return;
        }
        if(contractType == null || customer == null) {
            mainController.showMessage("Kontrat ve Müşteri seçiniz.", true);
            return;
        }

        BigDecimal size, price;
        try {
            size = new BigDecimal(sizeInput);
            price = new BigDecimal(priceInput);
        } catch (NumberFormatException e) {
            mainController.showMessage("Sayısal alanlar hatalı.", true);
            return;
        }

        String fullLocation = city + " / " + district + " / " + neighborhood + " / " + quarter + " / " + street;

        BuildingProperty buildingProperty = new BuildingProperty();
        buildingProperty.setTitle(title);
        buildingProperty.setDescription(description);
        buildingProperty.setSize(size);
        buildingProperty.setLocation(fullLocation);
        buildingProperty.setPrice(price);
        buildingProperty.setIndependenceType(independenceType);
        buildingProperty.setContractType(contractType);
        buildingProperty.setCustomerId(customer.getId());

        buildingPropertyManager.create(buildingProperty).save();

        int newId = buildingProperty.getId();
        if (newId == 0) {
            newId = buildingPropertyManager.getAll().stream().mapToInt(BuildingProperty::getId).max().orElse(0);
        }

        try {
            for (File file : selectedFiles) {
                String savedName = imageManager.savePhysicalImage(file);
                if (savedName != null) {
                    Image img = new Image();
                    img.setRealEstateId(newId);
                    img.setPath(savedName);
                    imageManager.create(img);
                }
            }
            imageManager.save();
        } catch (Exception e) {
            e.printStackTrace();
            mainController.showMessage("Resimler kaydedilirken hata oluştu.", true);
        }

        if (mainController != null) {
            mainController.handleKonut();
            mainController.showMessage("Konut başarıyla oluşturuldu.", false);
        }
    }

    @FXML
    public void handleIptal() {
        if(mainController != null) mainController.handleKonut();
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    private Il ilBul(String ilAdi) {
        if(turkiyeVerisi == null) return null;
        return turkiyeVerisi.stream().filter(il -> il.getAd().equals(ilAdi)).findFirst().orElse(null);
    }
    private Ilce ilceBul(Il il, String ilceAdi) {
        return il.getIlceler().stream().filter(ilce -> ilce.getAd().equals(ilceAdi)).findFirst().orElse(null);
    }
    private Semt semtBul(Ilce ilce, String semtAdi) {
        return ilce.getSemtler().stream().filter(semt -> semt.getAd().equals(semtAdi)).findFirst().orElse(null);
    }
}