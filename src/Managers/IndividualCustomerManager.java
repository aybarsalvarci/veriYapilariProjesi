package Managers;

import DataStructure.BST.BinarySearchTree;
import DataStructure.BST.Node;
import DataStructure.FileManager;
import DataStructure.Mappers.CorporateCustomerMapper;
import DataStructure.Mappers.IndividualCustomerMapper;
import Entities.CorporateCustomer;
import Entities.Customer;
import Entities.IndividualCustomer;

import java.util.List;

public class IndividualCustomerManager implements IManager<IndividualCustomer> {

    private BinarySearchTree<IndividualCustomer> tree;
    private FileManager<IndividualCustomer> fileManager;
    private CustomerManager customerManager;

    public IndividualCustomerManager()
    {
        tree = new  BinarySearchTree<>();
        customerManager = new CustomerManager(new UserManager());
        fileManager = new FileManager<>("DatabaseFiles/individualCustomers.txt", new IndividualCustomerMapper());
        for(IndividualCustomer c : fileManager.entities)
        {
            tree.add(c);
        }
    }

    @Override
    public IndividualCustomer get(int id) {
        Node node = tree.search(id);
        if(node == null)
            throw new RuntimeException("CorporateCustomer not found");

        return (IndividualCustomer) node.value;
    }

    @Override
    public List<IndividualCustomer> getAll() {
        return tree.treeToList();
    }

    @Override
    public IManager create(IndividualCustomer entity) {
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
    public IManager update(IndividualCustomer entity) {
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

        IndividualCustomer corporateCustomer = fileManager.entities.get(entity.getId());
        corporateCustomer.setTc(entity.getTc());

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
