//package com.ds.Controllers;
//
//import com.ds.Entities.BuildingProperty;
//import com.ds.Managers.BuildingPropertyManager;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.event.Event;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Node;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.TableColumn;
//import javafx.scene.control.TableView;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.stage.Stage;
//
//import java.io.IOException;
//import java.util.List;
//
//public class BuildingPropertyController {
//
//    @FXML
//    private TableView<BuildingProperty> evTablosu;
//
//    @FXML private TableColumn<BuildingProperty, String> colId;
//    @FXML private TableColumn<BuildingProperty, String> colUser;
//    @FXML private TableColumn<BuildingProperty, String> colTitle;
//    @FXML private TableColumn<BuildingProperty, String> colDesc;
//    @FXML private TableColumn<BuildingProperty, String> colPrice;
//    @FXML private TableColumn<BuildingProperty, String> colLoc;
//    @FXML private TableColumn<BuildingProperty, String> colSize;
//    @FXML private TableColumn<BuildingProperty, String> colType;
//    @FXML private TableColumn<BuildingProperty, String> colRent;
//
//    private BuildingPropertyManager manager;
//    private ObservableList<BuildingProperty> observableList;
//    public BuildingPropertyController()
//    {
//        manager = new BuildingPropertyManager();
//    }
//
//    @FXML
//    public void initialize()
//    {
//        List<BuildingProperty> evler = manager.getAll();
//        observableList = FXCollections.observableList(evler);
//
//        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
//        colUser.setCellValueFactory(new PropertyValueFactory<>("customerId"));
//        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
//        colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
//        colSize.setCellValueFactory(new PropertyValueFactory<>("size"));
//        colLoc.setCellValueFactory(new PropertyValueFactory<>("location"));
//        colRent.setCellValueFactory(new PropertyValueFactory<>("contractType"));
//        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
//        colType.setCellValueFactory(new PropertyValueFactory<>("independenceType"));
//
//        evTablosu.setItems(observableList);
//    }
//
//    @FXML
//    public void silmeIslemiBaslat(Event evet)
//    {
//        System.out.println("silme işlemi başlat");
//    }
//
//    @FXML
//    public void backToMain(Event event) throws IOException {
//        Parent root = FXMLLoader.load(getClass().getResource("/com/ds/anasayfa.fxml"));
//        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        stage.setTitle("Anasayfa");
//        Scene scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();
//    }
//
//}
