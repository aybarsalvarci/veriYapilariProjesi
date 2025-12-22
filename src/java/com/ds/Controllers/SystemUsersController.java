package com.ds.Controllers;

import com.ds.Entities.SystemUser;
import com.ds.Enums.Role;
import com.ds.Helpers.UserSession;
import com.ds.Managers.SystemUserManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class SystemUsersController {

    @FXML private TableView<SystemUser> tableUsers;
    @FXML private TableColumn<SystemUser, Integer> colId;
    @FXML private TableColumn<SystemUser, String> colFirstName;
    @FXML private TableColumn<SystemUser, String> colLastName;
    @FXML private TableColumn<SystemUser, String> colEmail;
    @FXML private TableColumn<SystemUser, Role> colRole;
    @FXML private TableColumn<SystemUser, Void> colAction;
    @FXML private TextField txtSearch;

    private SystemUserManager manager;
    private ObservableList<SystemUser> observableList;
    private MainController mainController;

    public SystemUsersController() {
        manager = new SystemUserManager();
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        refreshTable();
    }

    public void refreshTable() {
        this.manager = new SystemUserManager();
        List<SystemUser> users = manager.getAll();

        int currentUserId = UserSession.getInstance().getCurrentUser().getId();
        if (users != null) {
            users = users.stream()
                    .filter(user -> !user.getId().equals(currentUserId))
                    .collect(Collectors.toList());
        }

        observableList = FXCollections.observableArrayList(users != null ? users : FXCollections.emptyObservableList());

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        colId.setStyle("-fx-alignment: CENTER;");
        colRole.setStyle("-fx-alignment: CENTER;");

        colRole.setCellFactory(column -> new TableCell<SystemUser, Role>() {
            @Override
            protected void updateItem(Role item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Label badge = new Label();
                    badge.setPadding(new Insets(3, 10, 3, 10));
                    badge.setStyle("-fx-background-radius: 12; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 11px;");

                    if (item == Role.ADMIN) {
                        badge.setText("YÖNETİCİ");
                        badge.setStyle(badge.getStyle() + "-fx-background-color: #6366F1;"); // İndigo
                    } else {
                        badge.setText("PERSONEL");
                        badge.setStyle(badge.getStyle() + "-fx-background-color: #6B7280;"); // Gri
                    }
                    setGraphic(badge);
                    setAlignment(Pos.CENTER);
                }
            }
        });

        colEmail.prefWidthProperty().bind(
                tableUsers.widthProperty()
                        .subtract(colId.widthProperty())
                        .subtract(colFirstName.widthProperty())
                        .subtract(colLastName.widthProperty())
                        .subtract(colRole.widthProperty())
                        .subtract(colAction.widthProperty())
                        .subtract(2)
        );

        FilteredList<SystemUser> filteredData = new FilteredList<>(observableList, p -> true);
        if (txtSearch != null) {
            txtSearch.textProperty().addListener((obs, oldVal, newVal) -> {
                filteredData.setPredicate(user -> {
                    if (newVal == null || newVal.isEmpty()) return true;
                    String filter = newVal.toLowerCase();
                    return (user.getFirstName() != null && user.getFirstName().toLowerCase().contains(filter)) ||
                            (user.getLastName() != null && user.getLastName().toLowerCase().contains(filter)) ||
                            (user.getEmail() != null && user.getEmail().toLowerCase().contains(filter));
                });
            });
        }

        SortedList<SystemUser> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableUsers.comparatorProperty());

        setupActionButtons();
        tableUsers.setItems(sortedData);
    }

    @FXML
    private void handleAddNew() {
        showFormModal(null);
    }

    private void setupActionButtons() {
        colAction.setCellFactory(param -> new TableCell<>() {
            private final Button btnEdit = new Button("✏ Düzenle");
            private final Button btnDelete = new Button("🗑 Sil");
            private final HBox pane = new HBox(10, btnEdit, btnDelete);

            {
                btnEdit.getStyleClass().addAll("action-button", "btn-edit");
                btnDelete.getStyleClass().addAll("action-button", "btn-delete");
                pane.setAlignment(Pos.CENTER);
                pane.setPadding(new Insets(2, 0, 2, 0));

                btnEdit.setOnAction(e -> {
                    SystemUser user = getTableView().getItems().get(getIndex());
                    showFormModal(user);
                });

                btnDelete.setOnAction(e -> {
                    SystemUser user = getTableView().getItems().get(getIndex());
                    handleDelete(user);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    private void handleDelete(SystemUser user) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Kullanıcı Sil");
        alert.setHeaderText(null);
        alert.setContentText(user.getFirstName() + " " + user.getLastName() + " adlı kullanıcıyı silmek istediğinize emin misiniz?");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            manager.delete(user.getId()).save();
            refreshTable();
            if (mainController != null) mainController.showMessage("Kullanıcı silindi.", false);
        }
    }

    private void showFormModal(SystemUser user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ds/systemUserForm.fxml"));
            Parent root = loader.load();

            SystemUserFormController controller = loader.getController();
            controller.setParentController(this);
            if (user != null) controller.initData(user);

            Stage stage = new Stage();
            stage.setTitle(user == null ? "Yeni Kullanıcı" : "Kullanıcı Düzenle");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}