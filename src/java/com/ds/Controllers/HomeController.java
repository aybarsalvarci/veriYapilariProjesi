package com.ds.Controllers;

import com.ds.Managers.BuildingPropertyManager;
import com.ds.Managers.CorporateCustomerManager;
import com.ds.Managers.IndividualCustomerManager;
import com.ds.Managers.PlotManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HomeController {

    @FXML
    private Label lblKonutSayisi;
    @FXML
    private Label lblAraziSayisi;
    @FXML
    private Label lblBireyselSayisi;
    @FXML
    private Label lblKurumsalSayisi;

    private BuildingPropertyManager buildingPropertyManager;
    private PlotManager plotManager;
    private IndividualCustomerManager individualCustomerManager;
    private CorporateCustomerManager corporateCustomerManager;

    public HomeController(){
        buildingPropertyManager = new BuildingPropertyManager();
        plotManager = new PlotManager();
        individualCustomerManager = new IndividualCustomerManager();
        corporateCustomerManager = new CorporateCustomerManager();
    }

    public void initialize(){
        int konutSayisi = buildingPropertyManager.getAll().size();
        int araziSayisi = plotManager.getAll().size();
        int bireyselSayisi = individualCustomerManager.getAll().size();
        int kurumsalSayisi = corporateCustomerManager.getAll().size();

        lblKonutSayisi.setText(String.valueOf(konutSayisi));
        lblAraziSayisi.setText(String.valueOf(araziSayisi));
        lblBireyselSayisi.setText(String.valueOf(bireyselSayisi));
        lblKurumsalSayisi.setText(String.valueOf(kurumsalSayisi));
    }
}
