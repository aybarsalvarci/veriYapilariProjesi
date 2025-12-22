package com.ds.Managers;

import com.ds.DataStructure.BST.BinarySearchTree;
import com.ds.DataStructure.BST.Node;
import com.ds.DataStructure.FileManager;
import com.ds.DataStructure.Mappers.ImageMapper;
import com.ds.Entities.Image;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

public class ImageManager implements IManager<Image> {

    private final BinarySearchTree<Image> tree;
    private final FileManager<Image> fileManager;

    private static final String IMAGE_DIR = "images/";

    public ImageManager() {
        File directory = new File(IMAGE_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        tree = new BinarySearchTree<>();
        fileManager = new FileManager<>("DatabaseFiles/images.txt", new ImageMapper());
        fileManager.readFile();

        for (Image image : fileManager.entities) {
            tree.add(image);
        }
    }

    public String savePhysicalImage(File sourceFile) throws IOException {
        if (sourceFile == null) return null;

        String fileName = sourceFile.getName();
        String extension = "";
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i);
        }

        String uniqueName = UUID.randomUUID().toString() + extension;

        Path targetPath = Paths.get(IMAGE_DIR + uniqueName);

        Files.copy(sourceFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        return uniqueName;
    }

    public String getFullPath(String imageName) {
        File file = new File(IMAGE_DIR + imageName);
        return file.toURI().toString();
    }


    @Override
    public Image get(int id) {
        Node node = tree.search(id);

        if (node == null)
            return null;

        return (Image) node.value;
    }

    @Override
    public List<Image> getAll() {
        return tree.treeToList();
    }

    @Override
    public IManager create(Image entity) {
        tree.add(entity);
        return this;
    }

    @Override
    public IManager update(Image entity) {
        Image existingImage = this.get(entity.getId());

        if (existingImage != null) {
            existingImage.setRealEstateId(entity.getRealEstateId());
            existingImage.setPath(entity.getPath());
        }
        return this;
    }

    @Override
    public IManager delete(int id) {
        Image imageToDelete = get(id);

        if (imageToDelete != null) {
            try {
                Path path = Paths.get(IMAGE_DIR + imageToDelete.getPath());
                Files.deleteIfExists(path);
            } catch (IOException e) {
                System.err.println("Fiziksel resim silinemedi: " + e.getMessage());
            }

            tree.delete(id);
        }
        return this;
    }

    @Override
    public void save() {
        fileManager.entities = tree.treeToList();
        fileManager.saveFile();
    }

    public List<Image> getImagesByRealEstateId(int realEstateId) {
        return getAll().stream()
                .filter(img -> img.getRealEstateId() == realEstateId)
                .collect(java.util.stream.Collectors.toList());
    }

}