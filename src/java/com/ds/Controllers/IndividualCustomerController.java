package com.ds.Controllers;

import com.ds.Entities.IndividualCustomer;
import com.ds.Managers.IndividualCustomerManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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

    private IndividualCustomerManager manager;
    private ObservableList<IndividualCustomer> observableList;

    public IndividualCustomerController() {
        manager = new IndividualCustomerManager();
    }

    @FXML
    public void initialize()
    {
        List<IndividualCustomer> kullanicilar = manager.getAll();
        observableList = FXCollections.observableList(kullanicilar);

//        int id, String firstName, String lastName, String email, boolean isApproved, String tc
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFirstname.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastname.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colIsApproved.setCellValueFactory(new PropertyValueFactory<>("isApproved"));
        colTC.setCellValueFactory(new PropertyValueFactory<>("tc"));

        kullaniciTablosu.setItems(observableList);
    }
}
