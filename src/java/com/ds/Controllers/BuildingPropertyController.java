package com.ds.Controllers;

import com.ds.Entities.BuildingProperty;
import com.ds.Entities.CorporateCustomer;
import com.ds.Entities.IndividualCustomer;
import com.ds.Enums.ContractType;
import com.ds.Managers.BuildingPropertyManager;
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

public class BuildingPropertyController {

    @FXML
    private TableView<BuildingProperty> emlakTablosu;

    @FXML
    private TableColumn<BuildingProperty, Integer> colId;
    @FXML
    private TableColumn<BuildingProperty, Integer> colCustomerId;
    @FXML
    private TableColumn<BuildingProperty, String> colTitle;
    @FXML
    private TableColumn<BuildingProperty, String> colDescription;
    @FXML
    private TableColumn<BuildingProperty, Double> colSize;
    @FXML
    private TableColumn<BuildingProperty, String> colLocation;
    @FXML
    private TableColumn<BuildingProperty, Double> colPrice;
    @FXML
    private TableColumn<BuildingProperty, String> colIndependenceType;
    @FXML
    private TableColumn<BuildingProperty, ContractType> colContractType;
    @FXML
    private TableColumn<BuildingProperty, Void> colAction;


    private BuildingPropertyManager manager;
    private ObservableList<BuildingProperty> observableList;

    private MainController mainController;

    public BuildingPropertyController() {
        manager = new BuildingPropertyManager();
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        List<BuildingProperty> emlaklar = manager.getAll();
        if (emlaklar != null) {
            observableList = FXCollections.observableArrayList(emlaklar);
        } else {
            observableList = FXCollections.observableArrayList();
        }

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colIndependenceType.setCellValueFactory(new PropertyValueFactory<>("independenceType"));
        colContractType.setCellValueFactory(new PropertyValueFactory<>("contractType"));

        aksiyonButonlariniEkle();
        emlakTablosu.setItems(observableList);
    }

    @FXML
    public void handleYeniKonut(ActionEvent event) {
        if (mainController != null) {
            mainController.handleCreateBuildingProperty();
        } else {
            System.err.println("HATA: MainController referansı atanmamış!");
        }
    }

    private void aksiyonButonlariniEkle() {
        Callback<TableColumn<BuildingProperty, Void>, TableCell<BuildingProperty, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<BuildingProperty, Void> call(final TableColumn<BuildingProperty, Void> param) {
                final TableCell<BuildingProperty, Void> cell = new TableCell<>() {

                    private final Button btnDuzenle = new Button("Düzenle");
                    private final Button btnSil = new Button("Sil");
                    private final HBox pane = new HBox(10, btnDuzenle, btnSil);

                    {
                        btnDuzenle.setOnAction((event) -> {
                            if (getTableView().getItems().size() > 0) {
                                BuildingProperty data = getTableView().getItems().get(getIndex());
                                handleDuzenle(data);
                            }
                        });

                        btnSil.setOnAction((event) -> {
                            if (getTableView().getItems().size() > 0) {
                                BuildingProperty data = getTableView().getItems().get(getIndex());
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

    private void handleDuzenle(BuildingProperty property) {

//        mainController.handleUpdateBuildingProperty(property);
    }

    private void handleSil(BuildingProperty property) {
        observableList.remove(property);
        manager.delete(property.getId()).save();
        mainController.showMessage("Konut silindi.", false);
    }

}