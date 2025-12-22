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
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class ResetPasswordController {

    @FXML private PasswordField txtNewPass;
    @FXML private PasswordField txtConfirmPass;
    @FXML private Label lblMessage;

    private String userEmail;
    private SystemUserManager userManager;

    public ResetPasswordController() {
        userManager = new SystemUserManager();
    }

    public void initData(String email) {
        this.userEmail = email;
    }

    @FXML
    void handleSavePassword(ActionEvent event) {
        String newPass = txtNewPass.getText();
        String confirmPass = txtConfirmPass.getText();

        if (newPass.isEmpty() || confirmPass.isEmpty()) {
            setMessage("Alanlar boş bırakılamaz.", true);
            return;
        }

        if (!newPass.equals(confirmPass)) {
            setMessage("Şifreler uyuşmuyor!", true);
            return;
        }

        if (newPass.length() < 4) {
            setMessage("Şifre en az 4 karakter olmalıdır.", true);
            return;
        }

        SystemUser user = userManager.getUserByEmail(userEmail);
        if (user != null) {
            user.setPasswordHash(newPass);
            userManager.update(user).save();

            setMessage("Şifreniz başarıyla güncellendi! Girişe yönlendiriliyorsunuz...", false);

            goToLogin(event);
        } else {
            setMessage("Kullanıcı bulunamadı, hata oluştu.", true);
        }
    }

    private void goToLogin(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/ds/Login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Giriş Yap");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setMessage(String text, boolean isError) {
        lblMessage.setTextFill(isError ? Color.web("#e63946") : Color.web("#2a9d8f"));
        lblMessage.setText(text);
    }
}