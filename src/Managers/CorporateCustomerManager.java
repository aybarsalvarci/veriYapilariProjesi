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
        customerManager = new CustomerManager();
        fileManager = new FileManager<>("DatabaseFiles/corporateCustomers.txt", new CorporateCustomerMapper());
        fileManager.readFile();

        for(CorporateCustomer c : fileManager.entities)
        {
            Customer customer = customerManager.get(c.getId());
            c.setFirstName(customer.getFirstName());
            c.setLastName(customer.getLastName());
            c.setEmail(customer.getEmail());
            c.setApproved(customer.getIsApproved());

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

        Customer customer = new Customer(entity.getFirstName(), entity.getLastName(), entity.getEmail(), entity.getIsApproved());
        customerManager.create(customer).save();

        entity.setId(customer.getId());
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

        customerManager.update(customer).save();

        CorporateCustomer corporateCustomer = get(entity.getId());
        corporateCustomer.setTaxNumber(entity.getTaxNumber());

        return this;
    }

    @Override
    public IManager delete(int id) {
        tree.delete(id);
        customerManager.delete(id).save();
        return this;
    }

    @Override
    public void save() {
        fileManager.entities = tree.treeToList();
        fileManager.saveFile();
    }
}
