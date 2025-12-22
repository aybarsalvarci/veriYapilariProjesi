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

public class PlotAddController {

    @FXML
    private TextField txtTitle;
    @FXML
    private TextField txtDescription;
    @FXML
    private TextField txtSize;

    @FXML
    private ComboBox<String> cmbCity;
    @FXML
    private ComboBox<String> cmbDistrict;
    @FXML
    private ComboBox<String> cmbNeighborhood;
    @FXML
    private ComboBox<String> cmbQuarter;
    @FXML
    private TextField txtStreet;

    @FXML
    private TextField txtPrice;
    @FXML
    private TextField txtZoningStatus;

    @FXML
    private ComboBox<Customer> cmbCustomer;

    @FXML
    private ListView<String> lstSelectedImages;
    @FXML
    private Label lblSelectedImageCount;

    private MainController mainController;
    private PlotManager plotManager;
    private ImageManager imageManager;
    private List<Il> turkiyeVerisi;

    private List<File> selectedFiles = new ArrayList<>();

    public PlotAddController() {
        this.plotManager = new PlotManager();
        this.imageManager = new ImageManager();
    }

    @FXML
    public void initialize() {
        loadCustomers();

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
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Resim Dosyaları", "*.png", "*.jpg", "*.jpeg")
        );

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
        String zoningStatus = txtZoningStatus.getText();
        Customer customer = cmbCustomer.getValue();

        if (title.isEmpty() || description.isEmpty() || sizeInput.isEmpty() || priceInput.isEmpty() || zoningStatus.isEmpty()) {
            mainController.showMessage("Lütfen tüm alanları doldurunuz.", true);
            return;
        }
        if (city == null || district == null || neighborhood == null || quarter == null || street.isEmpty()) {
            mainController.showMessage("Adres bilgilerini eksiksiz giriniz.", true);
            return;
        }
        if (customer == null) {
            mainController.showMessage("Müşteri seçiniz.", true);
            return;
        }

        BigDecimal size, price;
        try {
            size = new BigDecimal(sizeInput);
            price = new BigDecimal(priceInput);
        } catch (NumberFormatException e) {
            mainController.showMessage("Boyut ve Fiyat sayısal olmalıdır.", true);
            return;
        }

        String fullLocation = city + " / " + district + " / " + neighborhood + " / " + quarter + " / " + street;

        Plot plot = new Plot();
        plot.setTitle(title);
        plot.setDescription(description);
        plot.setSize(size);
        plot.setLocation(fullLocation);
        plot.setPrice(price);
        plot.setZoningStatus(zoningStatus);
        plot.setCustomerId(customer.getId());

        plotManager.create(plot).save();

        int newPlotId = plot.getId();

        if (newPlotId == 0) {
            newPlotId = plotManager.getAll().stream()
                    .mapToInt(Plot::getId)
                    .max()
                    .orElse(0);
        }

        try {
            for (File file : selectedFiles) {
                String savedFileName = imageManager.savePhysicalImage(file);

                if (savedFileName != null) {
                    Image imageEntity = new Image();
                    imageEntity.setRealEstateId(newPlotId);
                    imageEntity.setPath(savedFileName);

                    imageManager.create(imageEntity);
                }
            }
            imageManager.save();

        } catch (Exception e) {
            e.printStackTrace();
            mainController.showMessage("Resimler kaydedilirken hata oluştu: " + e.getMessage(), true);
        }

        if (mainController != null) {
            mainController.handleArsa();
            mainController.showMessage("Arsa ve resimler başarıyla kaydedildi.", false);
        }
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void handleIptal() {
        if (mainController != null) {
            mainController.handleArsa();
        }
    }

    private void loadCustomers() {
        CustomerManager customerManager = new CustomerManager();
        List<Customer> customers = customerManager.getAll();
        cmbCustomer.getItems().addAll(customers);
        cmbCustomer.setConverter(new StringConverter<Customer>() {
            @Override
            public String toString(Customer customer) {
                if (customer == null) return null;
                return customer.getFirstName() + " " + customer.getLastName();
            }

            @Override
            public Customer fromString(String s) {
                return null;
            }
        });
    }

    private void setupLocationListeners() {
        cmbCity.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                cmbDistrict.getItems().clear();
                cmbNeighborhood.getItems().clear();
                cmbQuarter.getItems().clear();
                cmbDistrict.setDisable(false);
                cmbNeighborhood.setDisable(true);
                cmbQuarter.setDisable(true);
                Il secilenIl = ilBul(newVal);
                if (secilenIl != null) secilenIl.getIlceler().forEach(ilce -> cmbDistrict.getItems().add(ilce.getAd()));
            }
        });
        cmbDistrict.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                cmbNeighborhood.getItems().clear();
                cmbQuarter.getItems().clear();
                cmbNeighborhood.setDisable(false);
                cmbQuarter.setDisable(true);
                Il secilenIl = ilBul(cmbCity.getValue());
                if (secilenIl != null) {
                    Ilce secilenIlce = ilceBul(secilenIl, newVal);
                    if (secilenIlce != null)
                        secilenIlce.getSemtler().forEach(semt -> cmbNeighborhood.getItems().add(semt.getAd()));
                }
            }
        });
        cmbNeighborhood.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                cmbQuarter.getItems().clear();
                cmbQuarter.setDisable(false);
                Il secilenIl = ilBul(cmbCity.getValue());
                if (secilenIl != null) {
                    Ilce secilenIlce = ilceBul(secilenIl, cmbDistrict.getValue());
                    if (secilenIlce != null) {
                        Semt secilenSemt = semtBul(secilenIlce, newVal);
                        if (secilenSemt != null)
                            cmbQuarter.setItems(FXCollections.observableArrayList(secilenSemt.getMahalleler()));
                    }
                }
            }
        });
    }

    private Il ilBul(String ilAdi) {
        if (turkiyeVerisi == null) return null;
        return turkiyeVerisi.stream().filter(il -> il.getAd().equals(ilAdi)).findFirst().orElse(null);
    }

    private Ilce ilceBul(Il il, String ilceAdi) {
        return il.getIlceler().stream().filter(ilce -> ilce.getAd().equals(ilceAdi)).findFirst().orElse(null);
    }

    private Semt semtBul(Ilce ilce, String semtAdi) {
        return ilce.getSemtler().stream().filter(semt -> semt.getAd().equals(semtAdi)).findFirst().orElse(null);
    }
}