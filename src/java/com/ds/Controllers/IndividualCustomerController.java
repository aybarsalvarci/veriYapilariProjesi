package com.ds.Controllers;

import com.ds.Entities.IndividualCustomer;
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

public class IndividualCustomerController {

    @FXML
    private TableView<IndividualCustomer> kullaniciTablosu;

    @FXML
    private TableColumn<IndividualCustomer, String> colId;
    @FXML
    private TableColumn<IndividualCustomer, String> colFirstname;
    @FXML
    private TableColumn<IndividualCustomer, String> colLastname;
    @FXML
    private TableColumn<IndividualCustomer, String> colEmail;
    @FXML
    private TableColumn<IndividualCustomer, String> colIsApproved;
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
        colTC.setCellValueFactory(new PropertyValueFactory<>("tc"));

        aksiyonButonlariniEkle();
        kullaniciTablosu.setItems(observableList);
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
                final TableCell<IndividualCustomer, Void> cell = new TableCell<>() {

                    private final Button btnDuzenle = new Button("Düzenle");
                    private final Button btnSil = new Button("Sil");
                    private final HBox pane = new HBox(10, btnDuzenle, btnSil);

                    {
                        btnDuzenle.setOnAction((event) -> {
                            if (getTableView().getItems().size() > 0) {
                                IndividualCustomer data = getTableView().getItems().get(getIndex());
                                handleDuzenle(data);
                            }
                        });

                        btnSil.setOnAction((event) -> {
                            if (getTableView().getItems().size() > 0) {
                                IndividualCustomer data = getTableView().getItems().get(getIndex());
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