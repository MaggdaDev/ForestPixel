package org.maggdadev.forestpixel.structure;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ProjectNodeModel {
    private final String id;
    private String name;
    private List<ProjectNodeModel> children;
    private final ProjectNodeType type;

    public ProjectNodeType getType() {
        return type;
    }

    public enum ProjectNodeType {
        ROOT(""), FOLDER(""), SPRITE_FILE(".sprite");
        public final String extension;
        ProjectNodeType(String extension) {
            this.extension = extension;
        }
    }



    public ProjectNodeModel(String name, ProjectNodeType type) {
        id = nextNodeId();
        this.name = name;
        children = new ArrayList<>();
        this.type = type;
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

    public String getNameWithExtension() {
        return name + type.extension;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean canHaveChildren() {
        return type == ProjectNodeType.ROOT || type == ProjectNodeType.FOLDER;
    }

    @Override
    public String toString() {
        return "ProjectNodeModel{" +
                "id='" + id + '\'' +
                ", name='" + getNameWithExtension() + '\'' +
                ", children=" + children +
                '}';
    }

    public static class Deserializer implements JsonDeserializer<ProjectNodeModel> {

        @Override
        public ProjectNodeModel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext ctx)
            throws JsonParseException {

            JsonObject obj = json.getAsJsonObject();

            ProjectNodeType type = ProjectNodeType.valueOf(obj.get("type").getAsString());

            return switch (type) {
                case SPRITE_FILE -> ctx.deserialize(obj, ProjectFileModel.class);
                default -> ctx.deserialize(obj, ProjectNodeModel.class);
            };
        }
    }
}
