package Managers;

import DataStructure.BST.BinarySearchTree;
import DataStructure.BST.Node;
import DataStructure.FileManager;
import DataStructure.Mappers.CorporateCustomerMapper;
import Entities.CorporateCustomer;
import Entities.Customer;
import Entities.User;

import java.util.List;

public class CorporateCustomerManager implements IManager<CorporateCustomer> {

    private BinarySearchTree<CorporateCustomer> tree;
    private FileManager<CorporateCustomer> fileManager;
    private CustomerManager customerManager;

    public CorporateCustomerManager()
    {
        tree = new  BinarySearchTree<>();
        customerManager = new CustomerManager(new UserManager());
        fileManager = new FileManager<>("DatabaseFiles/corporateCustomers", new CorporateCustomerMapper());
        for(CorporateCustomer c : fileManager.entities)
        {
            tree.add(c);
        }
    }

    @Override
    public CorporateCustomer get(int id) {
        Node node = tree.search(id);
        if(node == null)
            throw new RuntimeException("CorporateCustomer not found");

        return (CorporateCustomer) node.value;
    }

    @Override
    public List<CorporateCustomer> getAll() {
        return tree.treeToList();
    }

    @Override
    public IManager create(CorporateCustomer entity) {
        if(entity.getId() != null)
        {
            Customer customer = customerManager.get(entity.getId());
            if(customer == null)
            {
                throw new RuntimeException("Customer with id " + entity.getId() + " doesn't exists");
            }
        }

        tree.add(entity);

        return this;
    }

    @Override
    public IManager update(CorporateCustomer entity) {
        Customer customer = customerManager.get(entity.getId());
        if(customer == null)
        {
            throw  new RuntimeException("Customer with id " + entity.getId() + " doesn't exists");
        }

        customer.setFirstName(entity.getFirstName());
        customer.setLastName(entity.getLastName());
        customer.setEmail(entity.getEmail());
        customer.setApproved(entity.getIsApproved());

        customerManager.update(customer);

        CorporateCustomer corporateCustomer = fileManager.entities.get(entity.getId());
        corporateCustomer.setTaxNumber(entity.getTaxNumber());

        return this;
    }

    @Override
    public IManager delete(int id) {
        tree.delete(id);
        return this;
    }

    @Override
    public void save() {
        fileManager.entities = tree.treeToList();
        fileManager.saveFile();
    }
}
