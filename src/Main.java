import DataStructure.BinarySearchTree;
import Entities.Image;

public class Main {

    public static void main(String[] args)
    {

        System.out.println("Application started!");

        Image image1 = new Image(1, 1, "path1");
        Image image2 = new Image(2, 1, "path2");
        Image image3 = new Image(3, 1, "path3");
        Image image4 = new Image(4, 1, "path4");
        Image image5 = new Image(5, 1, "path5");
        Image image6 = new Image(8, 1, "path6");
        Image image7 = new Image(11, 1, "path6");
        Image image8 = new Image(15, 1, "path6");
        Image image9 = new Image(41, 1, "path6");
        Image image10 = new Image(63, 1, "path6");
        Image image11 = new Image(74, 1, "path6");
        Image image12 = new Image(44, 1, "path6");
        Image image13 = new Image(235, 1, "path6");

        BinarySearchTree<Image> bst = new BinarySearchTree<>();
        bst.add(image1);
        bst.add(image3);
        bst.add(image6);
        bst.add(image4);
        bst.add(image5);
        bst.add(image6);
        bst.add(image7);
        bst.add(image8);
        bst.add(image9);
        bst.add(image10);
        bst.add(image11);
        bst.add(image12);
        bst.add(image13);

        bst.printByLevel();
    }
}


