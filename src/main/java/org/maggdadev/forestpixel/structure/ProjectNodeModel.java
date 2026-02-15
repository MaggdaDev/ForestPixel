package org.maggdadev.forestpixel.structure;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProjectNodeModel {
    private final String id;
    private String name;
    private List<ProjectNodeModel> children;

    public ProjectNodeModel() {
        id = nextNodeId();
        name = id;
        children = new ArrayList<>();
    }

    public List<ProjectNodeModel> getChildren() {
        return children;
    }

    public static String nextNodeId() {
        return UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
