package com.phonebook.dao.filestorage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.*;

@Profile("fileStorage")
@Component
public class FileDataStorageImpl<T> implements FileDataStorage<T> {

    @Autowired
    Environment env;

    private String PATH;

    @Override
    public T readFromFile(String filepath, Class<T> clazz) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        PATH = env.getRequiredProperty("fileStorage.directory");
        T storage = null;
        File file = new File(PATH + filepath);

        if (file.exists()) {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader in = new BufferedReader(new FileReader(file))) {
                in.lines().forEach(sb::append);
                String jsonStr = sb.toString();
                storage = gson.fromJson(jsonStr, clazz);
            } catch (IOException e) {
                return null;
            } catch (Exception e) {
                return null;
            }
        } else {
            try {
                File directory = new File(file.getParentFile().getAbsolutePath());
                directory.mkdirs();
                file.createNewFile();
                file.setWritable(true);
                String absPath = file.getAbsolutePath();
                String canPath = file.getCanonicalPath();
                return null;

            } catch (IOException e) {
                return null;
            }
        }
        return storage;
    }

    @Override
    public void writeToFile(String obj, String filepath) {
        PATH = env.getRequiredProperty("fileStorage.directory");
        File file = new File(PATH + filepath);
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(file))) {
            writer.write(obj);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
