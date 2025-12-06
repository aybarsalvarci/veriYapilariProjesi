package com.ds.Controllers;

import com.ds.Entities.CorporateCustomer;
import com.ds.Entities.IndividualCustomer;
import com.ds.Entities.User;
import com.ds.Managers.CorporateCustomerManager;
import com.ds.Managers.IndividualCustomerManager;
import com.ds.Managers.UserManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.List;

public class CorporateCustomerAddController {

    @FXML private TextField txtFirstname;
    @FXML private TextField txtLastname;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTaxNumber;

    private MainController mainController;
    private CorporateCustomerManager corporateCustomerManager;

    public CorporateCustomerAddController() {
        this.corporateCustomerManager = new CorporateCustomerManager();
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void handleCreate() {
        String ad = txtFirstname.getText();
        String soyad = txtLastname.getText();
        String email = txtEmail.getText();
        String tax = txtTaxNumber.getText();

        if (ad.isEmpty() || soyad.isEmpty()) {
            mainController.showMessage("İsim ve soyisim alanı zorunluduur.", true);
            return;
        }

        if(ad.length()<3) {
            mainController.showMessage("İsim alanı en az 3 karakter olmalıdır.", true);
            return;
        }

        if(soyad.length()<3) {
            mainController.showMessage("Soyad alanı en az 3 karakter olmalıdır.", true);
            return;
        }

        if(tax.length() != 11) {
            mainController.showMessage("TC kimlik numarası 11 karakter uzunluğunda olmalıdır.", true);
            return;
        }

        if(!tax.matches("[0-9]*")) {
            mainController.showMessage("TC kimlik numarası sadece numerik karakterlerden oluşabilir.", true);
            return;
        }

        if(!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"))
        {
            mainController.showMessage("Geçerli bir email adresi giriniz.", true);
            return;
        }

        UserManager userManager = new UserManager();

        List<User> users = userManager.getAll();

        for(User user : users) {
            if(user.getEmail().equals(email)) {
                mainController.showMessage("Bu email adresi zaten bir kullanıcıya ait", true);
                return;
            }

        }

        List<CorporateCustomer> customers = corporateCustomerManager.getAll();
        for(CorporateCustomer customer : customers) {
            if(customer.getTaxNumber().equals(tax)) {
                mainController.showMessage("Bu vergi numarası zaten bir kullanıcıya ait", true);
                return;
            }
        }

        CorporateCustomer yeniMusteri = new CorporateCustomer();
        yeniMusteri.setFirstName(ad);
        yeniMusteri.setLastName(soyad);
        yeniMusteri.setEmail(email);
        yeniMusteri.setTaxNumber(tax);
        yeniMusteri.setApproved(true);

        corporateCustomerManager.create(yeniMusteri).save();

        if (mainController != null) {
            mainController.handleKurumsalKullanicilar();
            mainController.showMessage("Kurumsal kullanıcı oluşturuldu.", false);

        }
    }

    public void handleIptal()
    {
        if(mainController != null)
        {
            mainController.handleKurumsalKullanicilar();
        }
    }
}