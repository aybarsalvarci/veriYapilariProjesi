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

    public CustomerManager(UserManager userManager) {
        this.fileManager = new FileManager<>("DatabaseFiles/customers.txt", new CustomerMapper());
        this.tree = new BinarySearchTree<>();
        this.userManager = userManager;

        fileManager.readFile();
        for(Customer customer : fileManager.entities)
        {
            tree.add(customer);
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
//        ArrayList<Customer> customers = new ArrayList<>();
        return null;
    }

    // Kayıt İşleminde kolon tekrarı çözülecek.
    @Override
    public IManager create(Customer entity) {
        User user = userManager.get(entity.getId());

        if(user != null)
        {
            user = new User(entity.getId(), entity.getFirstName(), entity.getLastName(), entity.getEmail());
            userManager.create(user).save();
        }

        tree.add(entity);

        return this;
    }

    @Override
    public IManager update(Customer entity) {
        return null;
    }

    @Override
    public IManager delete(int id) {
        return null;
    }

    @Override
    public void save() {

    }
}
