package com.personio.demo.domain.usecases;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private String name;
    private Node parent;
    private List<Node> children;

    public Node(String name) {
        this.name = name;
        children = new ArrayList<>();
    }

    public void addChild(Node child) {
        children.add(child);
        child.setParent(this);
    }

    public boolean isNameSame(String name) {
        return name.equals(this.name);
    }

    public boolean isRoot() {
        return parent == null;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public Node getParent() {
        return parent;
    }

    public List<Node> getChildren() {
        return children;
    }
}
