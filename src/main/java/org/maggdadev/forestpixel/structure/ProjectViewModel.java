package org.maggdadev.forestpixel.structure;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ProjectViewModel extends ProjectNodeViewModel {

    public ProjectViewModel(ProjectModel model) {
        super(model);

    }

    public Optional<ProjectNodeViewModel> findNodeById(String id) {
        ProjectNodeViewModel result = super.findNodeByIdRecursive(id);
        if (result == null) {
            return Optional.empty();
        } else {
            return Optional.of(result);
        }
    }

    public HashMap<String, ResourceIOAgent> getAllResourceAgents() {
        HashMap<String, ResourceIOAgent> agents = new HashMap<>();
        super.addAllResourceAgentsToMapRecursive(agents);
        return agents;
    }

    public static ProjectViewModel loadProjectModelFrom(File file) {
        try (ZipFile zip = new ZipFile(file)) {
            ZipEntry metaEntry = zip.getEntry("project_meta.json");
            ProjectModel model;
            try (InputStream is = zip.getInputStream(metaEntry)) {
                Reader r = new InputStreamReader(is, StandardCharsets.UTF_8);
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(ProjectNodeModel.class, new ProjectNodeModel.Deserializer())
                        .create();
                model = gson.fromJson(r, ProjectModel.class);
            }

            ProjectViewModel viewModel = new ProjectViewModel(model);
            viewModel.getAllResourceAgents().forEach((id, agent) -> {
                ZipEntry resourceEntry = zip.getEntry(idToResourcePath(id));
                if (resourceEntry != null) {
                    try (InputStream is = zip.getInputStream(resourceEntry)) {
                        agent.loadFrom(is);
                    } catch (IOException e) {
                        System.err.println("Could not load resource with id " + id + ": " + e.getMessage());
                    }
                } else {
                    System.err.println("No zip entry found for resource with id " + id);
                }
            });
            return viewModel;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveModelTo(File file) {
        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            ZipOutputStream zipOut = new ZipOutputStream(fileOut);
            zipOut.putNextEntry(new ZipEntry("project_meta.json"));
            Gson gson = new Gson();
            String json = gson.toJson(model);
            zipOut.write(json.getBytes());
            zipOut.closeEntry();

            HashMap<String, ResourceIOAgent> agents = getAllResourceAgents();
            agents.forEach((id, agent) -> {
                try {
                    zipOut.putNextEntry(new ZipEntry(idToResourcePath(id)));
                    agent.saveTo(zipOut);
                    zipOut.closeEntry();
                } catch (IOException e) {
                    System.err.println("Could not save resource with id " + id + ": " + e.getMessage());
                }
            });


            zipOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String idToResourcePath(String id) {
        return "resources/" + id;
    }


}
