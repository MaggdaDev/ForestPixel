package org.maggdadev.forestpixel.structure;

import com.google.gson.Gson;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.io.*;
import java.nio.charset.StandardCharsets;
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

    public static ProjectModel loadProjectModelFrom(File file) {
        try (ZipFile zip = new ZipFile(file)) {
            ZipEntry metaEntry = zip.getEntry("project_meta.json");
            try (InputStream is = zip.getInputStream(metaEntry)) {
                Reader r = new InputStreamReader(is, StandardCharsets.UTF_8);
                Gson gson = new Gson();
                return gson.fromJson(r, ProjectModel.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void saveModelTo(File file, ProjectNodeModel model) {
        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            ZipOutputStream zipOut = new ZipOutputStream(fileOut);
            zipOut.putNextEntry(new ZipEntry("project_meta.json"));
            Gson gson = new Gson();
            String json = gson.toJson(model);
            zipOut.write(json.getBytes());
            zipOut.closeEntry();
            zipOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
