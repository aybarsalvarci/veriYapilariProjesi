package com.ds.Controllers;

import com.ds.Entities.CorporateCustomer;
import com.ds.Entities.IndividualCustomer;
import com.ds.Managers.CorporateCustomerManager;
import com.ds.Managers.IndividualCustomerManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
    private TableColumn<CorporateCustomer, String> colIsApproved;
    @FXML
    private TableColumn<CorporateCustomer, String> colTaxNumber;
    @FXML
    private TableColumn<CorporateCustomer, Void> colAction;


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
        if (kullanicilar != null) {
            observableList = FXCollections.observableArrayList(kullanicilar);
        } else {
            observableList = FXCollections.observableArrayList();
        }

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFirstname.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastname.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colIsApproved.setCellValueFactory(new PropertyValueFactory<>("isApproved"));
        colTaxNumber.setCellValueFactory(new PropertyValueFactory<>("taxNumber"));

        aksiyonButonlariniEkle();
        kullaniciTablosu.setItems(observableList);
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
                final TableCell<CorporateCustomer, Void> cell = new TableCell<>() {

                    private final Button btnDuzenle = new Button("Düzenle");
                    private final Button btnSil = new Button("Sil");
                    private final HBox pane = new HBox(10, btnDuzenle, btnSil);

                    {
                        btnDuzenle.setOnAction((event) -> {
                            if (getTableView().getItems().size() > 0) {
                                CorporateCustomer data = getTableView().getItems().get(getIndex());
                                handleDuzenle(data);
                            }
                        });

                        btnSil.setOnAction((event) -> {
                            if (getTableView().getItems().size() > 0) {
                                CorporateCustomer data = getTableView().getItems().get(getIndex());
                                handleSil(data);
                            }
                        });

                        btnDuzenle.getStyleClass().add("btn-edit");
                        btnSil.getStyleClass().add("btn-delete");
                        pane.setAlignment(Pos.CENTER);
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
                return cell;
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