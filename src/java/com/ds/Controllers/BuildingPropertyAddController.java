package com.ds.Controllers;

import com.ds.Entities.BuildingProperty;
import com.ds.Entities.CorporateCustomer;
import com.ds.Entities.Customer;
import com.ds.Entities.User;
import com.ds.Enums.ContractType;
import com.ds.Managers.BuildingPropertyManager;
import com.ds.Managers.CorporateCustomerManager;
import com.ds.Managers.CustomerManager;
import com.ds.Managers.UserManager;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.List;

public class BuildingPropertyAddController {

    @FXML private TextField txtTitle;
    @FXML private TextField txtDescription;
    @FXML private TextField txtSize;
    @FXML private TextField txtLocation;
    @FXML private TextField txtPrice;
    @FXML private TextField txtIndependenceType;

    @FXML private ComboBox<Customer> cmbCustomer;
    @FXML private ComboBox<ContractType> cmbContractType;

    private MainController mainController;
    private BuildingPropertyManager buildingPropertyManager;

    public BuildingPropertyAddController() {
        this.buildingPropertyManager = new BuildingPropertyManager();
    }

    public void initialize()
    {


        CustomerManager customerManager = new CustomerManager();
        List<Customer> customers = customerManager.getAll();
        cmbCustomer.getItems().addAll(customers);

        cmbCustomer.setConverter(new javafx.util.StringConverter<Customer>() {
            @Override
            public String toString(Customer customer) {
                if(customer == null)
                {
                    return null;
                }

                return customer.getFirstName() + " " + customer.getLastName();
            }

            @Override
            public Customer fromString(String s) {
                return null;
            }
        });

        cmbContractType.getItems().addAll(ContractType.values());
        cmbContractType.setConverter(new javafx.util.StringConverter<ContractType>() {
            @Override
            public String toString(ContractType contractType) {
                if(contractType == null)
                    return null;

                return contractType.getLabel();
            }

            @Override
            public ContractType fromString(String s) {
                return null;
            }
        });
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }



    @FXML
    private void handleCreate() {

        String title = txtTitle.getText();
        String description = txtDescription.getText();
        String sizeInput = txtSize.getText();
        String location = txtLocation.getText();
        String priceInput = txtPrice.getText();
        String independenceType = txtIndependenceType.getText();
        ContractType contractType = cmbContractType.getValue();
        Customer customer = cmbCustomer.getValue();

        if(title.isEmpty())
        {
            mainController.showMessage("Başlık boş bırakılamaz.", true);
            return;
        }

        if(title.length() < 5)
        {
            mainController.showMessage("Başlık alanı en az 5 karakter olmalıdır.", true);
            return;
        }

        if(description.isEmpty())
        {
            mainController.showMessage("Açıklama alanı boş olamaz.", true);
            return;
        }

        if(description.length() < 20)
        {
            mainController.showMessage("Açıklama alanı en az 20 karakter olmalıdır.", true);
            return;
        }

        if(sizeInput.isEmpty())
        {
            mainController.showMessage("Boyut alanı boş bırakılamaz.", true);
            return;
        }

        if(!sizeInput.matches("[0-9]*"))
        {
            mainController.showMessage("Boyut alanı yalnızca numerik karakterler içermelidir.", true);
            return;
        }

        double size = Integer.parseInt(sizeInput);

        if(size <= 0)
        {
            mainController.showMessage("Boyut 0'dan büyük olmalıdır.", true);
            return;
        }

        if(location.isEmpty())
        {
            mainController.showMessage("Konum alanı boş bırakılamaz.", true);
            return;
        }

        if(location.length() < 10)
        {
            mainController.showMessage("Konum alanı 10 karakterden daha az olamaz.", true);
            return;
        }

        if(priceInput.isEmpty())
        {
            mainController.showMessage("Fiyat alanı boş bırakılamaz.", true);
            return;
        }

        if(!priceInput.matches("[0-9]*"))
        {
            mainController.showMessage("Fiyat alanı sadece numerik karakterler içerebilir.", true);
            return;
        }

        double price = Double.parseDouble(priceInput);

        if(price <= 0)
        {
            mainController.showMessage("Fiyat 0'dan büyük olmalıdır.", true);
            return;
        }

        if(independenceType.isEmpty())
        {
            mainController.showMessage("Bağımlılık türü alanı boş bırakılamaz.", true);
            return;
        }

        if(contractType == null)
        {
            mainController.showMessage("Kontrat türü seçiniz.", true);
            return;
        }

        if(customer == null)
        {
            mainController.showMessage("Müşteri seçiniz.", true);
            return;
        }

        BuildingProperty buildingProperty = new BuildingProperty();
        buildingProperty.setTitle(title);
        buildingProperty.setDescription(description);
        buildingProperty.setSize(size);
        buildingProperty.setLocation(location);
        buildingProperty.setPrice(price);
        buildingProperty.setIndependenceType(independenceType);
        buildingProperty.setContractType(contractType);
        buildingProperty.setCustomerId(customer.getId());

        buildingPropertyManager.create(buildingProperty).save();

        if (mainController != null) {
            mainController.handleKonut();
            mainController.showMessage("Konut oluşturuldu.", false);

        }
    }

    public void handleIptal()
    {
        if(mainController != null)
        {
            mainController.handleKonut();
        }
    }
}