package Managers;

import DataStructure.BST.BinarySearchTree;
import DataStructure.BST.Node;
import DataStructure.FileManager;
import DataStructure.Mappers.ImageMapper;
import Entities.Image;
import Entities.RealEstate;

import java.util.List;

public class ImageManager implements IManager<Image> {

    private final FileManager<Image> fileManager;
    private final BinarySearchTree<Image> tree;

    public ImageManager()
    {
        fileManager = new FileManager<>("DatabaseFiles/images.txt", new ImageMapper());
        tree = new BinarySearchTree<>();
        fileManager.readFile();
        for(Image image : fileManager.entities)
            tree.add(image);

    }

    @Override
    public Image get(int id) {
        Node node = tree.search(id);

        if(node == null)
            throw new RuntimeException("No such image with id " + id);

        return (Image) node.value;
    }

    @Override
    public List<Image> getAll() {
        return tree.treeToList();
    }

    @Override
    public IManager create(Image entity) {
        RealEstateManager realEstateManager = new RealEstateManager();
        RealEstate re = realEstateManager.get(entity.getRealEstateId());

        if(re == null)
            throw new RuntimeException("No such realEstate with id " + entity.getRealEstateId());

        tree.add(entity);
        return this;
    }

    @Override
    public IManager update(Image entity) {
        Image image = get(entity.getId());
        if(image == null)
            throw new RuntimeException("No such image with id " + entity.getId());

        image.setRealEstateId(entity.getRealEstateId());
        image.setPath(entity.getPath());

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
