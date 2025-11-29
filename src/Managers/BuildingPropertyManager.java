package Managers;

import DataStructure.BST.BinarySearchTree;
import DataStructure.BST.Node;
import DataStructure.FileManager;
import DataStructure.Mappers.BuildingPropertyMapper;
import DataStructure.Mappers.CustomerMapper;
import Entities.BuildingProperty;
import Entities.RealEstate;

import java.util.List;

public class BuildingPropertyManager implements IManager<BuildingProperty> {

    RealEstateManager realEstateManager;
    FileManager<BuildingProperty> fileManager;
    BinarySearchTree<BuildingProperty> tree;

    public BuildingPropertyManager()
    {
        realEstateManager = new RealEstateManager();
        fileManager = new FileManager<>("DatabaseFiles/buildingProperties.txt", new BuildingPropertyMapper());
        tree = new BinarySearchTree<>();

        for(BuildingProperty entity : fileManager.entities)
        {
            tree.add(entity);
        }
    }

    @Override
    public BuildingProperty get(int id) {
        Node node = tree.search(id);
        if(node != null)
        {
            RealEstate re =  realEstateManager.get(id);
            BuildingProperty bp = (BuildingProperty) node.value;
            bp.setCustomerId(re.getCustomerId());
            bp.setTitle(re.getTitle());
            bp.setDescription(re.getDescription());
            bp.setSize(re.getSize());
            bp.setLocation(re.getLocation());
            bp.setPrice(re.getPrice());

            return bp;

        }

        throw new RuntimeException("BuildingProperty not found");
    }

    @Override
    public List<BuildingProperty> getAll() {
        List<BuildingProperty> list = tree.treeToList().stream().map(bp -> {
            RealEstate re = realEstateManager.get(bp.getId());
            bp.setCustomerId(re.getCustomerId());
            bp.setTitle(re.getTitle());
            bp.setDescription(re.getDescription());
            bp.setSize(re.getSize());
            bp.setLocation(re.getLocation());
            bp.setPrice(re.getPrice());
            return bp;
        })
                .toList();

        return list;
    }

    @Override
    public IManager create(BuildingProperty entity) {

        RealEstate re = realEstateManager.get(entity.getId());

        if(re != null)
        {
            throw new RuntimeException("Already exists");
        }

//        int customerId, String title, String description, double size, String location, double price
        re = new RealEstate(entity.getCustomerId(), entity.getTitle(), entity.getDescription(), entity.getSize(), entity.getLocation(), entity.getPrice());
        realEstateManager.create(re).save();

        entity.setId(re.getId());
        tree.add(entity);

        return this;
    }

    @Override
    public IManager update(BuildingProperty entity) {
        RealEstate re = realEstateManager.get(entity.getId());

        if(re == null)
        {
            throw new RuntimeException("Not found.");
        }

//        int customerId, String title, String description, double size, String location, double price
        re.setCustomerId(entity.getCustomerId());
        re.setTitle(entity.getTitle());
        re.setDescription(entity.getDescription());
        re.setSize(entity.getSize());
        re.setLocation(entity.getLocation());
        re.setPrice(entity.getPrice());

        realEstateManager.update(re).save();

        BuildingProperty bp = get(entity.getId());
        if(re == null)
        {
            throw new RuntimeException("Not found.");
        }

        bp.setIndependenceType(entity.getIndependenceType());
        bp.setContractType(entity.getContractType());

        return this;
    }

    @Override
    public IManager delete(int id) {
        tree.delete(id);

        realEstateManager.delete(id).save();

        return this;
    }

    @Override
    public void save() {
        fileManager.saveFile();
    }
}
