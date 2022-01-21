package com.personio.demo.domain.entities;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a Node in a tree.
 */
@Getter
public class Node {

    private final String name;
    private Node parent;
    private final List<Node> children;

    /**
     * Constuctor
     *
     * @param name the name of the employee
     */
    public Node(String name) {
        this.name = name;
        children = new ArrayList<>();
    }

    /**
     * This method adds a child and simultaneously sets the parent to the passed child
     *
     * @param child the child node
     */
    public void addChild(Node child) {
        children.add(child);
        child.setParent(this);
    }

    /**
     * This method checks if the name is equal to the name of the object
     *
     * @param name the name to be checked
     * @return true if it matches, else false
     */
    public boolean isNameSame(String name) {
        return name.equals(this.name);
    }

    /**
     * Checks if this is the root node. The root node is determined by the parent.
     * If its null, its a root node
     *
     * @return true if it is root, else false
     */
    public boolean isRoot() {
        return parent == null;
    }

    /**
     * Setter
     *
     * @param parent the parent
     */
    public void setParent(Node parent) {
        this.parent = parent;
    }
}

