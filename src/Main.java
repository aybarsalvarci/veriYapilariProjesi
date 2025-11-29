import Controllers.RealEstateController;
import DataStructure.BST.BinarySearchTree;
import DataStructure.Mappers.UserMapper;
import Entities.*;
import Enums.ContractType;
import Managers.*;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args)
    {

        System.out.println("Application started!");

//        UserManager um = new UserManager();
//        User user = um.get(4);
//        System.out.println(user.getEmail());


        // Plot sınıfı test işlemleri

//        Plot p = new Plot(2, "Plot 1", "ddesc1", 2454.34, "konum1", 24, "imarlı");
//        Plot p1 = new Plot(1, "Plot 2", "ddesc2", 11111, "konum2", 23, "iarlı");
//        Plot p2 = new Plot(13, "Plot 3", "ddesc3", 3333, "konum3", 34, "imlı");
//
//        PlotManager pm = new PlotManager();

        // tekil olarak bulma
//        p = pm.get(1);
//        System.out.println(p.getDescription());
//        p.setTitle("updated");
//        p.setZoningStatus("villa imarlı");
//
//        // güncelleme işlemi  save -> dosyaya kaydetme fonksiyonu
//        pm.update(p).save();

        // silme işlemi
//        pm.delete(2).save();

        // Tüm veri
//        List<Plot> plots = pm.getAll();
//        for (Plot plot : plots) {
//            System.out.println(plot.getTitle());
//        }


//        BuildingProperty p1 = new BuildingProperty(1, "titl1", "desc1", 23.3, "konum1", 43.4, "giriş kat", ContractType.FOR_RENT);
//        BuildingProperty p2 = new BuildingProperty(5, "titl2", "desc2", 43, "konum2", 43.4, "son kat", ContractType.FOR_RENT);
//        BuildingProperty p3 = new BuildingProperty(3, "titl3", "desc3", 234.3, "konum3", 43.4, "bodrum kat", ContractType.FOR_SALE);
//
//        BuildingPropertyManager manager = new BuildingPropertyManager();

        // oluşturma işlemleri
//        manager.create(p1);
//        manager.create(p2);
//        manager.create(p3).save();

        // tekil veri çekme
//        BuildingProperty prop = manager.get(4);
//
//        prop.setTitle("updated property");
//        prop.setIndependenceType("updated independence type");
//
//        // update işlemi save -> dosyaya kayıt etme fonksiyonu
//        manager.update(prop).save();

        // silme işlemi
//        manager.delete(4).save();

        // tüm verileri çekme
//        List<BuildingProperty> buildingProperties = manager.getAll();
//
//        for (BuildingProperty buildingProperty : buildingProperties) {
//            System.out.println(buildingProperty.getTitle());
//        }

//        String firstName, String lastName, String email, boolean isApproved, String tc
        IndividualCustomer customer = new IndividualCustomer("Firstname", "lastname", "email", true, "24325243546");
        IndividualCustomer customer1 = new IndividualCustomer("Firstname1", "lastname1", "email1", false, "24325243546");
        IndividualCustomer customer2 = new IndividualCustomer("Firstname2", "lastname2", "email2", true, "24325243546");

        IndividualCustomerManager manager = new IndividualCustomerManager();

        manager.create(customer);
        manager.create(customer1);
        manager.create(customer2).save();
    }

}


