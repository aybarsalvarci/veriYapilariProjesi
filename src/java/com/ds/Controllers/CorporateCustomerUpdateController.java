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

public class CorporateCustomerUpdateController {

    @FXML private TextField txtFirstname;
    @FXML private TextField txtLastname;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTaxNumber;

    private CorporateCustomer currentCustomer;
    private MainController mainController;
    private CorporateCustomerManager corporateCustomerManager;

    public void initPage(CorporateCustomer customer)
    {
        this.currentCustomer = customer;
        txtFirstname.setText(customer.getFirstName());
        txtLastname.setText(customer.getLastName());
        txtEmail.setText(customer.getEmail());
        txtTaxNumber.setText(customer.getTaxNumber());

    }

    public CorporateCustomerUpdateController() {
        this.corporateCustomerManager = new CorporateCustomerManager();
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void handleSave()
    {
        this.currentCustomer.setFirstName(txtFirstname.getText());
        this.currentCustomer.setLastName(txtLastname.getText());
        this.currentCustomer.setEmail(txtEmail.getText());
        this.currentCustomer.setTaxNumber(txtTaxNumber.getText());

        if (this.currentCustomer.getFirstName().isEmpty() || this.currentCustomer.getLastName().isEmpty()) {
            mainController.showMessage("İsim ve soyisim alanı zorunluduur.", true);
            return;
        }

        if(this.currentCustomer.getFirstName().length()<3) {
            mainController.showMessage("İsim alanı en az 3 karakter olmalıdır.", true);
            return;
        }

        if(this.currentCustomer.getLastName().length()<3) {
            mainController.showMessage("Soyad alanı en az 3 karakter olmalıdır.", true);
            return;
        }

        if(this.currentCustomer.getTaxNumber().length() != 11) {
            mainController.showMessage("Vergi numarası 11 karakter uzunluğunda olmalıdır.", true);
            return;
        }

        if(!this.currentCustomer.getTaxNumber().matches("[0-9]*")) {
            mainController.showMessage("Vergi numarası sadece numerik karakterlerden oluşabilir.", true);
            return;
        }

        if(!this.currentCustomer.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"))
        {
            mainController.showMessage("Geçerli bir email adresi giriniz.", true);
            return;
        }

        UserManager userManager = new UserManager();

        User currentUser = userManager.get(currentCustomer.getId());
        List<User> users = userManager.getAll();

        for(User user : users) {
            if(user.getEmail().equals(this.currentCustomer.getEmail()) && !user.equals(currentUser)) {
                mainController.showMessage("Bu email adresi zaten bir kullanıcıya ait", true);
                return;
            }

        }

        List<CorporateCustomer> customers = corporateCustomerManager.getAll();
        for(CorporateCustomer customer : customers) {
            if(customer.getTaxNumber().equals(this.currentCustomer.getTaxNumber()) && !this.currentCustomer.getId().equals(customer.getId())) {
                mainController.showMessage("Bu vergi numarası zaten bir kullanıcıya ait", true);
                return;
            }
        }



        corporateCustomerManager.update(this.currentCustomer).save();

        if(mainController!=null)
        {
            mainController.handleKurumsalKullanicilar();
            mainController.showMessage("Kullanıcı güncellendi.",false);
        }
    }

    public void handleIptal()
    {
        if(mainController != null)
        {
            mainController.handleBireyselKullanicilar();
        }
    }
}