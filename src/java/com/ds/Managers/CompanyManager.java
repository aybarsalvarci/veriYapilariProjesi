package com.ds.Managers;

import com.ds.DataStructure.BST.BinarySearchTree;
import com.ds.DataStructure.BST.Node;
import com.ds.DataStructure.FileManager;
import com.ds.DataStructure.Mappers.CompanyMapper;
import com.ds.Entities.Company;

import java.util.List;

public class CompanyManager implements IManager<Company> {
    private final FileManager<Company> fileManager;
    private final BinarySearchTree<Company> tree;

    public CompanyManager() {
        fileManager = new FileManager<>("DatabaseFiles/company.txt", new CompanyMapper());
        tree = new BinarySearchTree<>();
        fileManager.readFile();
        for(Company c : fileManager.entities)
        {
            tree.add(c);
        }
    }
    @Override
    public Company get(int id) {
        Node node = tree.search(id);

        if(node == null)
            throw new RuntimeException("Company not found");

        return (Company) node.value;
    }

    @Override
    public List<Company> getAll() {
        return tree.treeToList();
    }

    @Override
    public IManager create(Company entity) {
        return null;
    }

    @Override
    public IManager update(Company entity) {
        Company company = get(entity.getId());

        company.setLogo(entity.getLogo());
        company.setLocation(entity.getLocation());
        company.setTitle(entity.getTitle());

        return this;
    }

    @Override
    public IManager delete(int id) {
        return null;
    }

    @Override
    public void save() {
        fileManager.entities = tree.treeToList();
        fileManager.saveFile();
    }
}
