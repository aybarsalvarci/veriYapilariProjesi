package com.ds.Controllers;

import com.ds.Entities.IndividualCustomer;
import com.ds.Entities.User;
import com.ds.Helpers.ValidationHelper;
import com.ds.Managers.IndividualCustomerManager;
import com.ds.Managers.UserManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.List;

public class IndividualCustomerUpdateController {

    @FXML private TextField txtFirstname;
    @FXML private TextField txtLastname;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTC;

    private IndividualCustomer currentCustomer;
    private MainController mainController;
    private IndividualCustomerManager individualCustomerManager;
    private ValidationHelper validationHelper;

    public void initPage(IndividualCustomer customer)
    {
        this.currentCustomer = customer;
        txtFirstname.setText(customer.getFirstName());
        txtLastname.setText(customer.getLastName());
        txtEmail.setText(customer.getEmail());
        txtTC.setText(customer.getTc());

    }

    public IndividualCustomerUpdateController() {
        this.individualCustomerManager = new IndividualCustomerManager();
        this.validationHelper = new ValidationHelper();
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void handleSave()
    {
        this.currentCustomer.setFirstName(txtFirstname.getText());
        this.currentCustomer.setLastName(txtLastname.getText());
        this.currentCustomer.setEmail(txtEmail.getText());
        this.currentCustomer.setTc(txtTC.getText());

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

        if(!validationHelper.isTCValid(this.currentCustomer.getTc())) {
            mainController.showMessage("Geçerli bir tc kimlik numarası giriniz.", true);
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

        List<IndividualCustomer> customers = individualCustomerManager.getAll();
        for(IndividualCustomer customer : customers) {
            if(customer.getTc().equals(this.currentCustomer.getTc()) && !this.currentCustomer.getId().equals(customer.getId())) {
                mainController.showMessage("Bu TC kimlik numarası zaten bir kullanıcıya ait", true);
                return;
            }
        }



        individualCustomerManager.update(this.currentCustomer).save();

        if(mainController!=null)
        {
            mainController.handleBireyselKullanicilar();
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