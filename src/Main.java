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
//        IndividualCustomer customer = new IndividualCustomer("Firstname", "lastname", "email", true, "24325243546");
//        IndividualCustomer customer1 = new IndividualCustomer("Firstname1", "lastname1", "email1", false, "24325243546");
//        IndividualCustomer customer2 = new IndividualCustomer("Firstname2", "lastname2", "email2", true, "24325243546");
//
//        IndividualCustomerManager manager = new IndividualCustomerManager();
//
//        manager.create(customer);
//        manager.create(customer1);
//        manager.create(customer2).save();

//        String firstName, String lastName, String email
//        User u1 = new User("Firstname1", "Lastname1", "email1");
//        User u2 = new User("Firstname2", "Lastname2", "email2");
//        User u3 = new User("Firstname3", "Lastname3", "email3");
//
//        UserManager um = new UserManager();

        // oluşturma
//        um.create(u1);
//        um.create(u2);
//        um.create(u3).save();

        // tekil veri çekme
//        User user = um.get(1);
//        System.out.println(user.getEmail());

        // update işlemi
//        user.setFirstName("Updated");
//        um.update(user).save();

        // delete işlemi
//        um.delete(1).save();

//        String firstName, String lastName, String email, boolean isApproved
//        Customer c1 = new Customer("cfirstName1", "cLastName1", "cemail1", true);
//        Customer c2 = new Customer("cfirstName2", "cLastName2", "cemail2", false);
//        Customer c3 = new Customer("cfirstName3", "cLastName3", "cemail3", true);
//
//        CustomerManager manager = new CustomerManager(new UserManager());

        //create işlemi
//        manager.create(c1);
//        manager.create(c2);
//        manager.create(c3).save();

        // tekil veri çekme
//        Customer customer = manager.get(1);
//        System.out.println(customer.getFirstName());

        // update işlemi
//        customer.setFirstName("updatedfn1");
//        customer.setLastName("updatedln1");
//        customer.setEmail("updatedel1");
//        customer.setApproved(false);
//
//        manager.update(customer).save();

        // delete işlemi
//        manager.delete(2).save();

//        String firstName, String lastName, String email, boolean isApproved, String tc
//        IndividualCustomer c1 = new IndividualCustomer("indCusfn1", "indCusln1", "indCusel1", true, "34523127089");
//        IndividualCustomer c2 = new IndividualCustomer("indCusfn2", "indCusln2", "indCusel2", false, "34523127089");
//        IndividualCustomer c3 = new IndividualCustomer("indCusfn3", "indCusln3", "indCusel3", true, "34523127089");
//
//        IndividualCustomerManager customerManager = new IndividualCustomerManager();

        // create işlemi
//        customerManager.create(c1);
//        customerManager.create(c2);
//        customerManager.create(c3).save();

        // tekil veri çekme işlemi

//        IndividualCustomer c = customerManager.get(4);
//        System.out.println(c.getFirstName());

        // update işlemi
//        c.setFirstName("TEst update");
//        c.setApproved(false);
//        c.setTc("test udppp");
//
//        customerManager.update(c).save();

        // silme işlemi
//        customerManager.delete(4).save();

//        String firstName, String lastName, String email, boolean isApproved, String taxNumber
        CorporateCustomer c1 = new CorporateCustomer("CorpoFirst1", "CorpoLast1", "corpoMail1", true, "corpoTaxNum1");
        CorporateCustomer c2 = new CorporateCustomer("CorpoFirst2", "CorpoLast2", "corpoMail2", false, "corpoTaxNum2");
        CorporateCustomer c3 = new CorporateCustomer("CorpoFirst3", "CorpoLast3", "corpoMail3", true, "corpoTaxNum3");


        CorporateCustomerManager manager = new CorporateCustomerManager();

        // create işlemi
//        manager.create(c1);
//        manager.create(c2);
//        manager.create(c3).save();

        // get single data
//        CorporateCustomer c4 = manager.get(8);
//        System.out.println("Corporate Customer ID: " + c4.getId() + " " + c4.getFirstName());

        // update işlem
//        c4.setFirstName("updatedFirst");
//        c4.setApproved(true);
//        c4.setTaxNumber("updatedTaxNum");
//        manager.update(c4).save();

        // delete işlem
//        manager.delete(9).save();
    }

}


