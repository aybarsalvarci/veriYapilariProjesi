package com.ds.Controllers;

import com.ds.Entities.Customer;
import com.ds.Entities.Image;
import com.ds.Entities.Plot;
import com.ds.Managers.CustomerManager;
import com.ds.Managers.PlotManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

public class PlotController {

    @FXML
    private TableView<Plot> emlakTablosu;
    @FXML
    private TextField txtSearch;
    @FXML
    private TableColumn<Plot, Integer> colId;
    @FXML
    private TableColumn<Plot, Integer> colCustomerId;
    @FXML
    private TableColumn<Plot, String> colTitle;
    @FXML
    private TableColumn<Plot, String> colDescription;
    @FXML
    private TableColumn<Plot, Double> colSize;
    @FXML
    private TableColumn<Plot, String> colLocation;
    @FXML
    private TableColumn<Plot, Double> colPrice;
    @FXML
    private TableColumn<Plot, String> colZoningStatus;
    @FXML
    private TableColumn<Plot, Void> colAction;

    private PlotManager manager;
    private ObservableList<Plot> observableList;
    private Map<Integer, String> customerMap;
    private MainController mainController;

    public PlotController() {
        manager = new PlotManager();
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

        List<Plot> araziler = manager.getAll();
        observableList = FXCollections.observableArrayList(araziler != null ? araziler : FXCollections.emptyObservableList());

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colZoningStatus.setCellValueFactory(new PropertyValueFactory<>("zoningStatus"));

        colId.setStyle("-fx-alignment: CENTER;");
        colSize.setStyle("-fx-alignment: CENTER;");
        colPrice.setStyle("-fx-alignment: CENTER-RIGHT; -fx-padding: 0 10 0 0;");
        colZoningStatus.setStyle("-fx-alignment: CENTER;");

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
                        .subtract(colZoningStatus.widthProperty())
                        .subtract(colAction.widthProperty())
                        .subtract(2) // Kenarlık payı
        );

        FilteredList<Plot> filteredData = new FilteredList<>(observableList, p -> true);
        if (txtSearch != null) {
            txtSearch.textProperty().addListener((obs, old, newValue) -> {
                filteredData.setPredicate(plot -> {
                    if (newValue == null || newValue.isEmpty()) return true;
                    String low = newValue.toLowerCase();
                    return (plot.getTitle() != null && plot.getTitle().toLowerCase().contains(low)) ||
                            (plot.getLocation() != null && plot.getLocation().toLowerCase().contains(low)) ||
                            (plot.getZoningStatus() != null && plot.getZoningStatus().toLowerCase().contains(low));
                });
            });
        }

        SortedList<Plot> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(emlakTablosu.comparatorProperty());

        aksiyonButonlariniEkle();
        emlakTablosu.setItems(sortedData);
    }

    @FXML
    public void handleYeniArazi(ActionEvent event) {
        if (mainController != null) mainController.handleCreatePlot();
    }

    private void aksiyonButonlariniEkle() {
        Callback<TableColumn<Plot, Void>, TableCell<Plot, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Plot, Void> call(final TableColumn<Plot, Void> param) {
                return new TableCell<>() {

                    private final Button btnGorsel = new Button("🖼 Görseller");
                    private final Button btnDuzenle = new Button("✏ Düzenle");
                    private final Button btnSil = new Button("🗑 Sil");
                    private final HBox pane = new HBox(10, btnGorsel, btnDuzenle, btnSil);

                    {
                        btnGorsel.getStyleClass().addAll("action-button", "btn-view");
                        btnDuzenle.getStyleClass().addAll("action-button", "btn-edit");
                        btnSil.getStyleClass().addAll("action-button", "btn-delete");

                        btnGorsel.setOnAction((event) -> {
                            Plot data = getTableView().getItems().get(getIndex());
                            showImageModal(data);
                        });

                        btnDuzenle.setOnAction((event) -> {
                            Plot data = getTableView().getItems().get(getIndex());
                            handleDuzenle(data);
                        });

                        btnSil.setOnAction((event) -> {
                            Plot data = getTableView().getItems().get(getIndex());

                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Arazi Kaydını Sil");
                            alert.setHeaderText(null);
                            alert.setContentText(data.getTitle() + " başlıklı araziyi ve tüm görsellerini silmek istediğinize emin misiniz?");

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

    private void showImageModal(Plot plot) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ds/imageViewer.fxml"));
            Parent root = loader.load();

            ImageViewerController controller = loader.getController();
            controller.loadImages(plot.getId());

            Stage stage = new Stage();
            stage.setTitle("Görseller: " + plot.getTitle());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Görsel görüntüleyici açılamadı.");
        }
    }

    private void handleDuzenle(Plot property) {
        mainController.handleUpdatePlot(property);
    }

    private void handleSil(Plot property) {
        com.ds.Managers.ImageManager imgManager = new com.ds.Managers.ImageManager();

        List<Image> plotImages = imgManager.getImagesByRealEstateId(property.getId());

        if (plotImages != null && !plotImages.isEmpty()) {
            for (Image img : plotImages) {
                imgManager.delete(img.getId());
            }
            imgManager.save();
        }

        observableList.remove(property);
        manager.delete(property.getId()).save();

        mainController.showMessage("Arazi ve ilişkili tüm görseller silindi.", false);
    }
}