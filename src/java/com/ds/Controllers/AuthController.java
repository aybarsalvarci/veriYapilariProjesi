package com.ds.Controllers;

import com.ds.Entities.SystemUser;
import com.ds.Managers.SystemUserManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class AuthController {

    SystemUserManager systemUserManager;

    @FXML
    private TextField txtEmail;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Label lblMessage;

    public AuthController() {
        systemUserManager = new SystemUserManager();
    }

    @FXML
    void handleLoginBtn(ActionEvent event) throws IOException {
        String email = txtEmail.getText();
        String password = txtPassword.getText();

        if (email.isEmpty() || password.isEmpty()) {
            setMessage("Lütfen tüm alanları doldurunuz.", true);
        } else {
            SystemUser usr = systemUserManager.getUserByEmail(email);

            if (usr != null && usr.getPasswordHash().equals(password)) {

                com.ds.Helpers.UserSession.getInstance().login(usr);

                setMessage("Giriş Başarılı! Hoş geldiniz.", false);

                Parent root = FXMLLoader.load(getClass().getResource("/com/ds/mainLayout.fxml"));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setTitle("Anasayfa - " + usr.getFirstName() + " " + usr.getLastName());
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

            } else {
                setMessage("Hatalı kullanıcı adı veya şifre!", true);
            }
        }
    }

    @FXML
    private void handleForgotPassword(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/ds/forgotPassword.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Şifre Sıfırlama");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void setMessage(String text, boolean isError) {
        if (isError) {
            lblMessage.setTextFill(Color.web("#e63946"));
        } else {
            lblMessage.setTextFill(Color.web("#2a9d8f"));
        }
        lblMessage.setText(text);
    }
}