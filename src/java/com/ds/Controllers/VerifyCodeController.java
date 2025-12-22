package com.ds.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class VerifyCodeController {

    @FXML private TextField txtCode;
    @FXML private Label lblMessage;

    private String targetEmail;
    private String generatedCode;

    public void initData(String email, String code) {
        this.targetEmail = email;
        this.generatedCode = code;
        System.out.println("Debug: Beklenen Kod: " + code);
    }

    @FXML
    void handleVerify(ActionEvent event) {
        String inputCode = txtCode.getText().trim();

        if (inputCode.isEmpty()) {
            setMessage("Lütfen kodu giriniz.", true);
            return;
        }

        if (inputCode.equals(generatedCode)) {
            setMessage("Kod doğrulandı! Yönlendiriliyorsunuz...", false);
            openResetPasswordPage(event);
        } else {
            setMessage("Hatalı kod! Lütfen tekrar kontrol ediniz.", true);
        }
    }

    private void openResetPasswordPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ds/resetPassword.fxml"));
            Parent root = loader.load();

            ResetPasswordController controller = loader.getController();
            controller.initData(targetEmail);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Yeni Şifre Belirle");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            setMessage("Sayfa yüklenirken hata oluştu.", true);
        }
    }

    private void setMessage(String text, boolean isError) {
        lblMessage.setTextFill(isError ? Color.web("#e63946") : Color.web("#2a9d8f"));
        lblMessage.setText(text);
    }
}