package com.ds.Controllers;

import com.ds.Entities.CorporateCustomer;
import com.ds.Managers.CorporateCustomerManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.util.List;

public class CorporateCustomerController {

    @FXML
    private TableView<CorporateCustomer> kullaniciTablosu;

    @FXML
    private TableColumn<CorporateCustomer, String> colId;
    @FXML
    private TableColumn<CorporateCustomer, String> colFirstname;
    @FXML
    private TableColumn<CorporateCustomer, String> colLastname;
    @FXML
    private TableColumn<CorporateCustomer, String> colEmail;
    @FXML
    private TableColumn<CorporateCustomer, String> colTaxNumber;
    @FXML
    private TableColumn<CorporateCustomer, Void> colAction;
    @FXML
    private TextField txtSearch;

    private CorporateCustomerManager manager;
    private ObservableList<CorporateCustomer> observableList;

    private MainController mainController;

    public CorporateCustomerController() {
        manager = new CorporateCustomerManager();
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        List<CorporateCustomer> kullanicilar = manager.getAll();
        observableList = FXCollections.observableArrayList(kullanicilar != null ? kullanicilar : FXCollections.emptyObservableList());

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFirstname.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastname.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTaxNumber.setCellValueFactory(new PropertyValueFactory<>("taxNumber"));

        colId.setStyle("-fx-alignment: CENTER;");
        colTaxNumber.setStyle("-fx-alignment: CENTER;");

        colFirstname.prefWidthProperty().bind(
                kullaniciTablosu.widthProperty()
                        .subtract(colId.widthProperty())
                        .subtract(colLastname.widthProperty())
                        .subtract(colEmail.widthProperty())
                        .subtract(colTaxNumber.widthProperty())
                        .subtract(colAction.widthProperty())
                        .subtract(2)
        );

        FilteredList<CorporateCustomer> filteredData = new FilteredList<>(observableList, p -> true);
        if (txtSearch != null) {
            txtSearch.textProperty().addListener((obs, old, newValue) -> {
                filteredData.setPredicate(customer -> {
                    if (newValue == null || newValue.isEmpty()) return true;
                    String lower = newValue.toLowerCase();
                    return (customer.getFirstName() != null && customer.getFirstName().toLowerCase().contains(lower)) ||
                            (customer.getTaxNumber() != null && customer.getTaxNumber().contains(lower)) ||
                            (customer.getEmail() != null && customer.getEmail().toLowerCase().contains(lower)) ||
                            (customer.getLastName() != null && customer.getLastName().toLowerCase().contains(lower));
                });
            });
        }

        SortedList<CorporateCustomer> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(kullaniciTablosu.comparatorProperty());

        aksiyonButonlariniEkle();
        kullaniciTablosu.setItems(sortedData);
    }

    @FXML
    public void handleYeniMusteri(ActionEvent event) {
        if (mainController != null) {
            mainController.handleCreateCorporateCustomer();
        } else {
            System.err.println("HATA: MainController referansı atanmamış!");
        }
    }

    private void aksiyonButonlariniEkle() {
        Callback<TableColumn<CorporateCustomer, Void>, TableCell<CorporateCustomer, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<CorporateCustomer, Void> call(final TableColumn<CorporateCustomer, Void> param) {
                return new TableCell<>() {

                    private final Button btnDuzenle = new Button("✏ Düzenle");
                    private final Button btnSil = new Button("🗑 Sil");
                    private final HBox pane = new HBox(10, btnDuzenle, btnSil);

                    {
                        btnDuzenle.getStyleClass().addAll("action-button", "btn-edit");
                        btnSil.getStyleClass().addAll("action-button", "btn-delete");

                        btnDuzenle.setOnAction((event) -> {
                            CorporateCustomer data = getTableView().getItems().get(getIndex());
                            handleDuzenle(data);
                        });

                        btnSil.setOnAction((event) -> {
                            CorporateCustomer data = getTableView().getItems().get(getIndex());
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Silme Onayı");
                            alert.setHeaderText(null);
                            alert.setContentText(data.getFirstName() + " adlı kurumsal müşteriyi silmek istediğinize emin misiniz?");

                            alert.showAndWait().ifPresent(response -> {
                                if (response == ButtonType.OK) {
                                    handleSil(data);
                                }
                            });
                        });

                        pane.setAlignment(Pos.CENTER);
                        pane.setPadding(new javafx.geometry.Insets(2, 0, 2, 0));
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(pane);
                        }
                    }
                };
            }
        };

        colAction.setCellFactory(cellFactory);
    }

    private void handleDuzenle(CorporateCustomer user) {

        mainController.handleUpdateCorporateCustomer(user);
    }

    private void handleSil(CorporateCustomer user) {
        observableList.remove(user);
        manager.delete(user.getId()).save();
        mainController.showMessage("Müşteri silindi.", false);
    }

    @FXML
    private void handleCreate(CorporateCustomer customer) {
        System.out.println("create" + customer.getFirstName());
    }
}