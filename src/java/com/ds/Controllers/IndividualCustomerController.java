package com.ds.Controllers;

import com.ds.Entities.IndividualCustomer;
import com.ds.Managers.IndividualCustomerManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.util.List;

public class IndividualCustomerController {

    @FXML
    private TableView<IndividualCustomer> kullaniciTablosu;
    @FXML
    private TextField txtSearch;
    @FXML
    private TableColumn<IndividualCustomer, String> colId;
    @FXML
    private TableColumn<IndividualCustomer, String> colFirstname;
    @FXML
    private TableColumn<IndividualCustomer, String> colLastname;
    @FXML
    private TableColumn<IndividualCustomer, String> colEmail;
    @FXML
    private TableColumn<IndividualCustomer, String> colTC;
    @FXML
    private TableColumn<IndividualCustomer, Void> colAction;


    private IndividualCustomerManager manager;
    private ObservableList<IndividualCustomer> observableList;

    private MainController mainController;

    public IndividualCustomerController() {
        manager = new IndividualCustomerManager();
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        List<IndividualCustomer> kullanicilar = manager.getAll();
        observableList = FXCollections.observableArrayList(kullanicilar != null ? kullanicilar : FXCollections.emptyObservableList());

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFirstname.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastname.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTC.setCellValueFactory(new PropertyValueFactory<>("tc"));

        colId.setStyle("-fx-alignment: CENTER;");
        colTC.setStyle("-fx-alignment: CENTER;");

        colEmail.prefWidthProperty().bind(
                kullaniciTablosu.widthProperty()
                        .subtract(colId.widthProperty())
                        .subtract(colFirstname.widthProperty())
                        .subtract(colLastname.widthProperty())
                        .subtract(colTC.widthProperty())
                        .subtract(colAction.widthProperty())
                        .subtract(2)
        );

        FilteredList<IndividualCustomer> filteredData = new FilteredList<>(observableList, p -> true);
        if (txtSearch != null) {
            txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(customer -> {
                    if (newValue == null || newValue.isEmpty()) return true;
                    String lower = newValue.toLowerCase();
                    return (customer.getFirstName() != null && customer.getFirstName().toLowerCase().contains(lower)) ||
                            (customer.getLastName() != null && customer.getLastName().toLowerCase().contains(lower)) ||
                            (customer.getEmail() != null && customer.getEmail().toLowerCase().contains(lower)) ||
                            (customer.getTc() != null && customer.getTc().contains(lower));
                });
            });
        }

        SortedList<IndividualCustomer> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(kullaniciTablosu.comparatorProperty());

        aksiyonButonlariniEkle();
        kullaniciTablosu.setItems(sortedData);
    }

    @FXML
    public void handleYeniMusteri(ActionEvent event) {
        if (mainController != null) {
            mainController.handleCreateIndividualCustomer();
        } else {
            System.err.println("HATA: MainController referansı atanmamış!");
        }
    }

    private void aksiyonButonlariniEkle() {
        Callback<TableColumn<IndividualCustomer, Void>, TableCell<IndividualCustomer, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<IndividualCustomer, Void> call(final TableColumn<IndividualCustomer, Void> param) {
                return new TableCell<>() {

                    private final Button btnDuzenle = new Button("✏ Düzenle");
                    private final Button btnSil = new Button("🗑 Sil");
                    private final HBox pane = new HBox(10, btnDuzenle, btnSil);

                    {
                        btnDuzenle.getStyleClass().addAll("action-button", "btn-edit");
                        btnSil.getStyleClass().addAll("action-button", "btn-delete");

                        btnDuzenle.setOnAction((event) -> {
                            IndividualCustomer data = getTableView().getItems().get(getIndex());
                            handleDuzenle(data);
                        });

                        btnSil.setOnAction((event) -> {
                            IndividualCustomer data = getTableView().getItems().get(getIndex());

                            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Müşteri Sil");
                            alert.setHeaderText(null);
                            alert.setContentText(data.getFirstName() + " " + data.getLastName() + " adlı müşteriyi silmek istediğinize emin misiniz?");

                            alert.showAndWait().ifPresent(response -> {
                                if (response == javafx.scene.control.ButtonType.OK) {
                                    handleSil(data);
                                }
                            });
                        });

                        pane.setAlignment(Pos.CENTER);
                        pane.setPadding(new Insets(2, 0, 2, 0));
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

    private void handleDuzenle(IndividualCustomer user) {

        mainController.handleUpdateIndividualCustomer(user);
    }

    private void handleSil(IndividualCustomer user) {
        observableList.remove(user);
        manager.delete(user.getId()).save();
        mainController.showMessage("Müşteri silindi.", false);
    }

    @FXML
    private void handleCreate(IndividualCustomer customer) {
        System.out.println("create" + customer.getFirstName());
    }
}