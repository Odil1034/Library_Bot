package uz.pdp.maven.backend.utils.fileWriterAndLoader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileWriterAndLoader<M> {

    private final Path path;
    private final Gson gson;

    public FileWriterAndLoader(String path) {
        this.path = Path.of(path);
        this.gson = buildGson();
    }

    public void write(List<M> list) {
        String json = gson.toJson(list);
        try {
            Files.writeString(path, json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<M> load(Class<M> mClass) {
        try {
            String json = Files.readString(path);
            Type type = TypeToken.getParameterized(List.class, mClass).getType();
            ArrayList<M> arrayList = gson.fromJson(json, type);

            return arrayList == null ? new ArrayList<>() : arrayList;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private Gson buildGson() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .setDateFormat("dd/MM/yyyy")
                .create();
    }
}
