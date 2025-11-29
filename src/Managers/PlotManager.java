package Managers;

import DataStructure.BST.BinarySearchTree;
import DataStructure.BST.Node;
import DataStructure.FileManager;
import DataStructure.Mappers.PlotMapper;
import Entities.Plot;
import Entities.RealEstate;

import java.util.List;

public class PlotManager implements IManager<Plot> {
    private final BinarySearchTree<Plot> tree;
    private final FileManager<Plot> fileManager;
    private final RealEstateManager realEstateManager;

    public PlotManager() {
        realEstateManager = new RealEstateManager();
        tree = new BinarySearchTree<>();
        fileManager = new FileManager<>("DatabaseFiles/plots.txt", new PlotMapper());
        fileManager.readFile();
        for (Plot plot : fileManager.entities) {
            tree.add(plot);
        }

    }

    @Override
    public Plot get(int id) {
        Node node = tree.search(id);

        if(node == null) {
            throw new RuntimeException("Plot with id " + id + " not found");
        }
        Plot plot = (Plot) node.value;
        RealEstate re = realEstateManager.get(id);

        plot.setTitle(re.getTitle());
        plot.setCustomerId(re.getCustomerId());
        plot.setDescription(re.getDescription());
        plot.setPrice(re.getPrice());
        plot.setSize(re.getSize());
        plot.setLocation(re.getLocation());

        return plot;
    }

    @Override
    public List<Plot> getAll() {
        List<Plot> plots = tree.treeToList().stream().map(plot -> {
            RealEstate re = realEstateManager.get(plot.getId());
            plot.setTitle(re.getTitle());
            plot.setCustomerId(re.getCustomerId());
            plot.setDescription(re.getDescription());
            plot.setPrice(re.getPrice());
            plot.setSize(re.getSize());
            plot.setLocation(re.getLocation());
            return plot;
        }).toList();

        return plots;
    }

    @Override
    public IManager create(Plot entity) {
        RealEstate re = new RealEstate(entity.getCustomerId(), entity.getTitle(), entity.getDescription(), entity.getSize(), entity.getLocation(), entity.getPrice());
        realEstateManager.create(re).save();

        entity.setId(re.getId());
        tree.add(entity);

        return this;
    }

    @Override
    public IManager update(Plot entity) {
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

        Plot p = get(entity.getId());
        if(re == null)
        {
            throw new RuntimeException("Not found.");
        }

        p.setZoningStatus(entity.getZoningStatus());

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
        fileManager.entities = tree.treeToList();
        fileManager.saveFile();
    }
}
