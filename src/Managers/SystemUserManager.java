package Managers;

import DataStructure.BST.BinarySearchTree;
import DataStructure.BST.Node;
import DataStructure.FileManager;
import DataStructure.Mappers.SystemUserMapper;
import Entities.SystemUser;
import Entities.User;

import java.util.List;

public class SystemUserManager implements IManager<SystemUser>{

    private final UserManager userManager;
    private final BinarySearchTree<SystemUser> tree;
    private final FileManager<SystemUser> fileManager;

    public SystemUserManager(){
        userManager = new UserManager();
        tree = new BinarySearchTree<>();
        fileManager = new FileManager<>("DatabaseFiles/systemUsers.txt", new SystemUserMapper());
        fileManager.readFile();

        for(SystemUser u : fileManager.entities)
        {
            User user = userManager.get(u.getId());
            if(user != null)
                u.setFirstName(user.getFirstName());
                u.setLastName(user.getLastName());
                u.setEmail(user.getEmail());

                tree.add(u);
        }
    }
    @Override
    public SystemUser get(int id) {
        Node node = tree.search(id);

        if(node == null)
            throw new RuntimeException("SystemUser not found");

        return (SystemUser) node.value;
    }

    @Override
    public List<SystemUser> getAll() {
        return tree.treeToList();
    }

    @Override
    public IManager create(SystemUser entity) {
        User newUser = new User(entity.getFirstName(), entity.getLastName(), entity.getEmail());
        userManager.create(newUser).save();

        entity.setId(newUser.getId());
        tree.add(entity);
        return this;
    }

    @Override
    public IManager update(SystemUser entity) {
        User user = userManager.get(entity.getId());

        user.setFirstName(entity.getFirstName());
        user.setLastName(entity.getLastName());
        user.setEmail(entity.getEmail());
        userManager.update(user).save();

        SystemUser sys = get(entity.getId());
        sys.setPasswordHash(entity.getPasswordHash());
        sys.setRole(entity.getRole());

        return this;
    }

    @Override
    public IManager delete(int id) {
        userManager.delete(id).save();
        tree.delete(id);
        return this;
    }

    @Override
    public void save() {
        fileManager.entities = tree.treeToList();
        fileManager.saveFile();
    }
}
