package org.maggdadev.forestpixel.structure;

import java.util.UUID;

public class ProjectNodeModel {
    private final String id;
    private String name;

    public ProjectNodeModel() {
        id = nextNodeId();
        name = id;
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
