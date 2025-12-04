package com.ds.DataStructure.BST;

import com.ds.Entities.BaseEntity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class BinarySearchTree<T extends BaseEntity> {

    public Node root;
    private int lastId;

    public BinarySearchTree() {
        lastId = 0;
    }

    public void add(T entity) {

        Node newNode = new Node<T>(null, null, null, entity);

        if((entity.getId()) != null){
            lastId = entity.getId();
        }
        else
        {
            lastId++;
            entity.setId(lastId);
        }

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

    public ArrayList<T> treeToList() {
        ArrayList<T> list = new ArrayList<>();
        treeToListRecursive(root, list);

        return list;
    }

    private void treeToListRecursive(Node currentNode, ArrayList<T> list) {
        if (currentNode == null) {
            return;
        }

        list.add((T)currentNode.value);

        if (currentNode.left != null) {
            treeToListRecursive(currentNode.left, list);
        }

        if (currentNode.right != null) {
            treeToListRecursive(currentNode.right, list);
        }

    }

    public void delete(int id) {

        Node nodeToDelete = search(id);

        if (nodeToDelete == null) return;

        // two child case
        if (nodeToDelete.left != null && nodeToDelete.right != null) {
            Node minNode = findMinNode(nodeToDelete);
            nodeToDelete.value = minNode.value;
            nodeToDelete = minNode;
        }

        Node child = nodeToDelete.right != null ? nodeToDelete.right : nodeToDelete.left;

        if (nodeToDelete == root) {
            root = child;
            if (root != null) {
                root.parent = null;
                return;
            }
        }

        if (nodeToDelete == nodeToDelete.parent.left) {
            nodeToDelete.parent.left = child;
        } else {
            nodeToDelete.parent.right = child;
        }

        if(child != null)
            child.parent = nodeToDelete.parent;

    }

    public Node findMinNode(Node node) {

        while (node.left != null) {
            node = node.left;
        }

        return node;

    }


}
