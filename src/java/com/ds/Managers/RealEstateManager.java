package com.ds.Managers;

import com.ds.DataStructure.BST.BinarySearchTree;
import com.ds.DataStructure.BST.Node;
import com.ds.DataStructure.FileManager;
import com.ds.DataStructure.Mappers.RealEstateMapper;
import com.ds.Entities.RealEstate;

import java.util.List;

public class RealEstateManager implements IManager<RealEstate> {
    private BinarySearchTree<RealEstate> tree;
    private FileManager<RealEstate> fileManager;

    public RealEstateManager() {
        fileManager = new FileManager<>("DatabaseFiles/realEstates.txt", new RealEstateMapper());
        tree = new BinarySearchTree<>();

        fileManager.readFile();
        for(RealEstate re : fileManager.entities)
        {
            tree.add(re);
        }
    }

    @Override
    public RealEstate get(int id) {
        Node<RealEstate> node = tree.search(id);
        if(node != null)
            return node.value;

        throw new RuntimeException("No such RealEstate");
    }

    @Override
    public List<RealEstate> getAll() {
        return tree.treeToList();
    }

    @Override
    public IManager create(RealEstate entity) {
        tree.add(entity);
        return this;
    }

    @Override
    public IManager update(RealEstate entity) {
        Node<RealEstate> node = tree.search(entity.getId());

        if(node == null)
            throw new RuntimeException("No such RealEstate");

        RealEstate re = node.value;
        re.setCustomerId(entity.getCustomerId());
        re.setTitle(entity.getTitle());
        re.setDescription(entity.getDescription());
        re.setSize(entity.getSize());
        re.setLocation(entity.getLocation());
        re.setPrice(entity.getPrice());

        return this;
    }

    @Override
    public IManager delete(int id) {
        tree.delete(id);
        return this;
    }

    @Override
    public void save() {
        fileManager.entities = getAll();
        fileManager.saveFile();
    }
}
