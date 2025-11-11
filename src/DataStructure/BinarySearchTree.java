package DataStructure;

import Entities.BaseEntity;

import java.util.LinkedList;
import java.util.Queue;

public class BinarySearchTree<T extends BaseEntity> {

    public Node root;

    public BinarySearchTree() {
    }

    public void add(T entity) {

        Node newNode = new Node(null, null, null, entity);

        if (root == null) {
            root = newNode;
        } else {
            addRecursive(root, newNode);
        }
    }

    private void addRecursive(Node currentNode, Node nodeToAdd) {

        if (nodeToAdd.value.getId() < currentNode.value.getId()) {
            if (currentNode.left == null) {
                currentNode.left = nodeToAdd;
                nodeToAdd.parent = currentNode;
            } else {
                addRecursive(currentNode.left, nodeToAdd);
            }


        } else if (nodeToAdd.value.getId() > currentNode.value.getId()) {
            if (currentNode.right == null) {
                currentNode.right = nodeToAdd;
                nodeToAdd.parent = currentNode;
            } else {
                addRecursive(currentNode.right, nodeToAdd);
            }
        }
    }

    public Node search(int id) {
        return searchRecursive(root, id);
    }

    private Node searchRecursive(Node currentNode, int id) {
        if (currentNode == null)
            return null;

        if (currentNode.value.getId() == id) {
            return currentNode;
        } else if (id < currentNode.value.getId()) {
            return searchRecursive(currentNode.left, id);
        } else if (id > currentNode.value.getId()) {
            return searchRecursive(currentNode.right, id);
        }

        return null;
    }

    public void printTree() {
        printTreeRecursive(root);
    }

    private void printTreeRecursive(Node currentNode) {
        if (currentNode == null) {
            return;
        }

        System.out.println(currentNode.value.getId());

        if (currentNode.left != null) {
            printTreeRecursive(currentNode.left);
        }

        if (currentNode.right != null) {
            printTreeRecursive(currentNode.right);
        }

    }

    public void printByLevel() {
        System.out.println("\nAğacın Seviye-Sıralı Görünümü (Kök en üstte):");
        if (root == null) {
            System.out.println("Ağaç boş.");
            System.out.println("--------------------------------------\n");
            return;
        }

        // Düğümleri seviye seviye işlemek için bir Kuyruk (Queue) kullanırız
        Queue<Node> queue = new LinkedList<>();
        queue.add(root); // Kök ile başla

        // Kuyruk boşalana kadar devam et
        while (!queue.isEmpty()) {

            // O anki seviyede kaç düğüm olduğunu say
            int levelSize = queue.size();

            // O seviyedeki tüm düğümler için döngü
            for (int i = 0; i < levelSize; i++) {
                // Düğümü kuyruktan çıkar
                Node currentNode = queue.poll();

                // Düğümün ID'sini yazdır (yanına boşluk bırakarak)
                System.out.print(currentNode.value.getId() + "   ");

                // ÖNEMLİ: Bu düğümün çocuklarını
                // BİR SONRAKİ SEVİYEDE işlenmek üzere kuyruğa ekle.
                if (currentNode.left != null) {
                    queue.add(currentNode.left);
                }
                if (currentNode.right != null) {
                    queue.add(currentNode.right);
                }
            }

            // 'for' döngüsü bittiğinde, o seviyedeki tüm düğümler
            // yazdırılmış demektir. Şimdi yeni bir satıra geç.
            System.out.println();
        }
        System.out.println("--------------------------------------\n");
    }

    public void delete(int id) {
        Node nodeToDelete = search(id);

        // bulunamazsa
        if (nodeToDelete == null)
            return;

        // leaf node
        if (nodeToDelete.left == null && nodeToDelete.right == null) {
            if (nodeToDelete.value.getId() > id) {
                nodeToDelete.parent.right = null;
            } else {
                nodeToDelete.parent.left = null;
            }
        }

        // single child

        if (nodeToDelete.left == null && nodeToDelete.right != null) {
            if (nodeToDelete.value.getId() > id)
                nodeToDelete.parent.right = nodeToDelete.right;
            else
                nodeToDelete.parent.left = nodeToDelete.right;
        } else if (nodeToDelete.left != null && nodeToDelete.right == null) {
            if (nodeToDelete.value.getId() > id)
                nodeToDelete.parent.right = nodeToDelete.left;
            else
                nodeToDelete.parent.left = nodeToDelete.left;
        }

        // two child


    }

    public Node findMinNode(Node node) {

        while (node.left != null) {
            node = node.left;
        }

        return node;

    }


}
