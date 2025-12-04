package com.ds.Managers;

import com.ds.DataStructure.BST.BinarySearchTree;
import com.ds.DataStructure.BST.Node;
import com.ds.DataStructure.FileManager;
import com.ds.DataStructure.Mappers.IndividualCustomerMapper;
import com.ds.Entities.Customer;
import com.ds.Entities.IndividualCustomer;

import java.util.List;

public class IndividualCustomerManager implements IManager<IndividualCustomer> {

    private BinarySearchTree<IndividualCustomer> tree;
    private FileManager<IndividualCustomer> fileManager;
    private CustomerManager customerManager;

    public IndividualCustomerManager()
    {
        tree = new  BinarySearchTree<>();
        customerManager = new CustomerManager();
        fileManager = new FileManager<>("DatabaseFiles/individualCustomers.txt", new IndividualCustomerMapper());
        fileManager.readFile();
        for(IndividualCustomer c : fileManager.entities)
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
    public IndividualCustomer get(int id) {
        Node node = tree.search(id);
        if(node == null)
            throw new RuntimeException("IndividualCustomer not found");

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

        Customer customer = new Customer(entity.getFirstName(), entity.getLastName(), entity.getEmail(), entity.getIsApproved());
        customerManager.create(customer).save();

        entity.setId(customer.getId());
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

        customerManager.update(customer).save();

        IndividualCustomer corporateCustomer = get(entity.getId());
        corporateCustomer.setTc(entity.getTc());

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
