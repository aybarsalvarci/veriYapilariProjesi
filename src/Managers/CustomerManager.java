package Managers;

import DataStructure.BST.BinarySearchTree;
import DataStructure.BST.Node;
import DataStructure.FileManager;
import DataStructure.Mappers.CustomerMapper;
import Entities.Customer;
import Entities.User;

import java.util.ArrayList;
import java.util.List;

public class CustomerManager implements IManager<Customer> {

    private FileManager<Customer> fileManager;
    private BinarySearchTree<Customer> tree;
    private UserManager userManager;

    public CustomerManager() {
        this.fileManager = new FileManager<>("DatabaseFiles/customers.txt", new CustomerMapper());
        this.tree = new BinarySearchTree<>();
        this.userManager = new UserManager();

        fileManager.readFile();
        for(Customer customer : fileManager.entities)
        {
            User user = userManager.get(customer.getId());
            if(user != null)
            {
                customer.setFirstName(user.getFirstName());
                customer.setLastName(user.getLastName());
                customer.setEmail(user.getEmail());
                tree.add(customer);
            }
        }
    }

    @Override
    public Customer get(int id) {
        Node<Customer> node = tree.search(id);

        if(node != null)
        {
            return node.value;
        }

        throw new RuntimeException("Customer with id " + id + " not found");
    }

    @Override
    public List<Customer> getAll() {
        return tree.treeToList();
    }


    @Override
    public IManager create(Customer entity) {

        if(entity.getId() != null)
        {
            User user = userManager.get(entity.getId());
            if(user == null)
            {
                throw  new RuntimeException("User with id " + entity.getId() + " doesn't exists");
            }
        }

        User user = new User(entity.getFirstName(), entity.getLastName(), entity.getEmail());
        userManager.create(user).save();

        entity.setId(user.getId());

        tree.add(entity);
        return this;
    }

    @Override
    public IManager update(Customer entity) {
        User user = userManager.get(entity.getId());
        if(user == null)
        {
            throw  new RuntimeException("User with id " + entity.getId() + " doesn't exists");
        }

        user.setFirstName(entity.getFirstName());
        user.setLastName(entity.getLastName());
        user.setEmail(entity.getEmail());

        userManager.update(user).save();

        Customer customer = get(entity.getId());
        customer.setApproved(entity.getIsApproved());

        return this;
    }

    @Override
    public IManager delete(int id) {
        tree.delete(id);
        userManager.delete(id).save();
        return this;
    }

    @Override
    public void save() {
        fileManager.entities = tree.treeToList();
        fileManager.saveFile();
    }
}
