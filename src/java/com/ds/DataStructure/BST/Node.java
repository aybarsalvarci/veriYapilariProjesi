package com.ds.DataStructure.BST;

import com.ds.Entities.BaseEntity;

public class Node<T extends BaseEntity> {

    public Node parent;
    public Node left;
    public Node right;

    public T value;

    public Node(Node parent, Node left, Node right, T value) {
        this.parent = parent;
        this.left = left;
        this.right = right;
        this.value = value;
    }


}
