package Managers;

import DataStructure.BST.BinarySearchTree;
import DataStructure.BST.Node;
import DataStructure.FileManager;
import DataStructure.Mappers.UserMapper;
import Entities.User;

import java.util.List;

public class UserManager implements IManager<User> {

    private final BinarySearchTree<User> tree;
    private final FileManager<User> fileManager;

    public UserManager() {
        fileManager =new FileManager("databaseFiles/users.txt", new UserMapper());
        fileManager.readFile();
        tree = new BinarySearchTree<>();
        for(User user : fileManager.entities)
        {
            tree.add(user);
        }
    }

    @Override
    public User get(int id) {
        Node<User> node = tree.search(id);
        if(node != null)
        {
            return node.value;
        }
        return null;
    }

    @Override
    public List<User> getAll() {
        return tree.treeToList();
    }

    @Override
    public UserManager update(User entity) {
        User user = get(entity.getId());
        user.setFirstName(entity.getFirstName());
        user.setLastName(entity.getLastName());
        user.setEmail(entity.getEmail());

        return this;
    }

    @Override
    public UserManager delete(int id) {
        tree.delete(id);
        return this;
    }

    @Override
    public UserManager create(User user)
    {
        tree.add(user);
        return this;
    }

    @Override
    public void save() {
        fileManager.entities = getAll();
        fileManager.saveFile();
    }
}
