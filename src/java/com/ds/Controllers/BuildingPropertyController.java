package com.ds.Controllers;

import com.ds.Entities.BuildingProperty;
import com.ds.Entities.Customer;
import com.ds.Entities.Image;
import com.ds.Enums.ContractType;
import com.ds.Managers.BuildingPropertyManager;
import com.ds.Managers.CustomerManager;
import com.ds.Managers.ImageManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @FXML
    private TextField txtSearch;

    private BuildingPropertyManager manager;
    private ObservableList<BuildingProperty> observableList;
    private Map<Integer, String> customerMap;
    private MainController mainController;

    public BuildingPropertyController() {
        manager = new BuildingPropertyManager();
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        CustomerManager customerManager = new CustomerManager();
        List<Customer> musteriler = customerManager.getAll();
        if (musteriler != null) {
            customerMap = musteriler.stream().collect(Collectors.toMap(Customer::getId, c -> c.getFirstName() + " " + c.getLastName()));
        }

        List<BuildingProperty> konutlar = manager.getAll();
        observableList = FXCollections.observableArrayList(konutlar != null ? konutlar : FXCollections.emptyObservableList());

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colIndependenceType.setCellValueFactory(new PropertyValueFactory<>("independenceType"));
        colContractType.setCellValueFactory(new PropertyValueFactory<>("contractType"));

        colId.setStyle("-fx-alignment: CENTER;");
        colSize.setStyle("-fx-alignment: CENTER;");
        colContractType.setStyle("-fx-alignment: CENTER;");
        colPrice.setStyle("-fx-alignment: CENTER-RIGHT; -fx-padding: 0 10 0 0;");

        colCustomerId.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Integer customerId, boolean empty) {
                super.updateItem(customerId, empty);
                if (empty || customerId == null) setText(null);
                else setText(customerMap.getOrDefault(customerId, "Bilinmiyor"));
            }
        });

        colDescription.prefWidthProperty().bind(
                emlakTablosu.widthProperty()
                        .subtract(colId.widthProperty())
                        .subtract(colCustomerId.widthProperty())
                        .subtract(colTitle.widthProperty())
                        .subtract(colSize.widthProperty())
                        .subtract(colPrice.widthProperty())
                        .subtract(colLocation.widthProperty())
                        .subtract(colIndependenceType.widthProperty())
                        .subtract(colContractType.widthProperty())
                        .subtract(colAction.widthProperty())
                        .subtract(2)
        );

        FilteredList<BuildingProperty> filteredData = new FilteredList<>(observableList, p -> true);
        if (txtSearch != null) {
            txtSearch.textProperty().addListener((obs, old, newValue) -> {
                filteredData.setPredicate(prop -> {
                    if (newValue == null || newValue.isEmpty()) return true;
                    String lower = newValue.toLowerCase();
                    return (prop.getTitle() != null && prop.getTitle().toLowerCase().contains(lower)) ||
                            (prop.getLocation() != null && prop.getLocation().toLowerCase().contains(lower)) ||
                            (prop.getDescription() != null && prop.getDescription().toLowerCase().contains(lower));
                });
            });
        }

        SortedList<BuildingProperty> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(emlakTablosu.comparatorProperty());

        aksiyonButonlariniEkle();
        emlakTablosu.setItems(sortedData);
    }

    @FXML
    public void handleYeniKonut(ActionEvent event) {
        if (mainController != null) mainController.handleCreateBuildingProperty();
    }

    private void aksiyonButonlariniEkle() {
        Callback<TableColumn<BuildingProperty, Void>, TableCell<BuildingProperty, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<BuildingProperty, Void> call(final TableColumn<BuildingProperty, Void> param) {
                return new TableCell<>() {
                    private final Button btnGorsel = new Button("🖼 Görseller");
                    private final Button btnDuzenle = new Button("✏ Düzenle");
                    private final Button btnSil = new Button("🗑 Sil");
                    private final HBox pane = new HBox(10, btnGorsel, btnDuzenle, btnSil);

                    {
                        btnGorsel.getStyleClass().addAll("action-button", "btn-view");
                        btnDuzenle.getStyleClass().addAll("action-button", "btn-edit");
                        btnSil.getStyleClass().addAll("action-button", "btn-delete");

                        btnGorsel.setOnAction(e -> showImageModal(getTableView().getItems().get(getIndex())));
                        btnDuzenle.setOnAction(e -> handleDuzenle(getTableView().getItems().get(getIndex())));
                        btnSil.setOnAction(e -> handleSil(getTableView().getItems().get(getIndex())));

                        pane.setAlignment(Pos.CENTER);
                        pane.setPadding(new Insets(2, 0, 2, 0));
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) setGraphic(null);
                        else setGraphic(pane);
                    }
                };
            }
        };
        colAction.setCellFactory(cellFactory);
    }

    private void showImageModal(BuildingProperty property) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ds/imageViewer.fxml"));
            Parent root = loader.load();

            ImageViewerController controller = loader.getController();
            controller.loadImages(property.getId());

            Stage stage = new Stage();
            stage.setTitle("Görseller: " + property.getTitle());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleDuzenle(BuildingProperty property) {
        mainController.handleUpdateBuildingProperty(property);
    }

    private void handleSil(BuildingProperty property) {
        ImageManager imgManager = new ImageManager();

        List<Image> propertyImages = imgManager.getImagesByRealEstateId(property.getId());

        if (propertyImages != null && !propertyImages.isEmpty()) {
            for (Image img : propertyImages) {
                imgManager.delete(img.getId());
            }
            imgManager.save();
        }

        observableList.remove(property);
        manager.delete(property.getId()).save();
        mainController.showMessage("Konut ve ilişkili görseller silindi.", false);
    }
}