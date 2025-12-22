package com.ds.Controllers;

import com.ds.Entities.SystemUser;
import com.ds.Helpers.MailHelper;
import com.ds.Managers.SystemUserManager;
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

public class ForgotPasswordController {

    @FXML private TextField txtEmail;
    @FXML private Label lblMessage;

    private SystemUserManager systemUserManager;

    public ForgotPasswordController() {
        this.systemUserManager = new SystemUserManager();
    }

    @FXML
    void handleSendResetLink(ActionEvent event) {
        String email = txtEmail.getText();

        if (email == null || email.trim().isEmpty()) {
            setMessage("Lütfen e-posta adresinizi giriniz.", true);
            return;
        }

        SystemUser user = systemUserManager.getUserByEmail(email);

        if (user != null) {

            String kod = MailHelper.generateVerificationCode();

            String konu = "Şifre Sıfırlama Kodu - Emlak Otomasyonu";
            String icerik = "Merhaba " + user.getFirstName() + ",\n\n" +
                    "Şifre sıfırlama talebiniz alınmıştır.\n" +
                    "Doğrulama Kodunuz: " + kod + "\n\n" +
                    "Bu kodu kimseyle paylaşmayınız.";

            boolean mailGonderildi = MailHelper.sendMail(email, konu, icerik);

            if (mailGonderildi) {

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ds/verifyCode.fxml"));
                    Parent root = loader.load();


                    VerifyCodeController controller = loader.getController();
                    controller.initData(email, kod);

                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setTitle("Kodu Doğrula");
                    stage.setScene(new Scene(root));
                    stage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                    setMessage("Sayfa geçişinde hata oluştu.", true);
                }

            } else {
                setMessage("Mail gönderilemedi! İnternet bağlantınızı kontrol edin.", true);
            }

        } else {
            setMessage("Bu e-posta adresi ile kayıtlı bir kullanıcı bulunamadı.", true);
        }
    }

    @FXML
    void handleBackToLogin(ActionEvent event) throws IOException {
        switchScene(event, "/com/ds/Login.fxml", "Giriş Yap");
    }


    private void setMessage(String text, boolean isError) {
        if (isError) {
            lblMessage.setTextFill(Color.web("#e63946"));
        } else {
            lblMessage.setTextFill(Color.web("#2a9d8f"));
        }
        lblMessage.setText(text);
    }

    private void switchScene(ActionEvent event, String fxmlPath, String title) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}