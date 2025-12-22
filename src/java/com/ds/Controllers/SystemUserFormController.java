package com.ds.Controllers;

import com.ds.Entities.SystemUser;
import com.ds.Enums.Role;
import com.ds.Managers.SystemUserManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class SystemUserFormController {

    @FXML private Label lblTitle;
    @FXML private TextField txtFirstName;
    @FXML private TextField txtLastName;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private ComboBox<Role> cmbRole;

    private SystemUserManager manager;
    private SystemUser currentUser;
    private SystemUsersController parentController;

    public SystemUserFormController() {
        manager = new SystemUserManager();
    }

    public void setParentController(SystemUsersController parentController) {
        this.parentController = parentController;
    }

    @FXML
    public void initialize() {
        cmbRole.getItems().setAll(Role.values());
        cmbRole.setValue(Role.STANDARD_USER);
    }

    public void initData(SystemUser user) {
        this.currentUser = user;
        lblTitle.setText("Kullanıcı Düzenle");
        txtFirstName.setText(user.getFirstName());
        txtLastName.setText(user.getLastName());
        txtEmail.setText(user.getEmail());
        txtPassword.setText(user.getPasswordHash());
        cmbRole.setValue(user.getRole());
    }

    @FXML
    private void handleSave() {
        String name = txtFirstName.getText();
        String surname = txtLastName.getText();
        String email = txtEmail.getText();
        String pass = txtPassword.getText();
        Role role = cmbRole.getValue();

        if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || pass.isEmpty() || role == null) {
            MainController.getInstance().showMessage("Lütfen tüm alanları doldurunuz.", true);
            return;
        }

        SystemUser existingUser = manager.getUserByEmail(email);

        if (existingUser != null) {
            if (currentUser == null) {
                showAlert("Hata", "Bu e-posta adresi (" + email + ") zaten sistemde kayıtlı!");
                return;
            }
            else if (existingUser.getId() != currentUser.getId()) {
                MainController.getInstance().showMessage("Bu e-posta adresi başka bir kullanıcı tarafından kullanılıyor!", true);
                return;
            }
        }

        if (currentUser == null) {
            SystemUser newUser = new SystemUser(name, surname, email, pass, role);
            manager.create(newUser).save();
        } else {
            currentUser.setFirstName(name);
            currentUser.setLastName(surname);
            currentUser.setEmail(email);
            currentUser.setPasswordHash(pass);
            currentUser.setRole(role);
            manager.update(currentUser).save();
        }

        if (parentController != null) {
            parentController.refreshTable();
        }

        Stage stage = (Stage) txtFirstName.getScene().getWindow();
        stage.close();
        MainController.getInstance().showMessage("Kullanıcı oluşturuldu", false);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}