package ru.otus.hw08.dataprocessor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.otus.hw08.model.Measurement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class FileLoader implements Loader {

    private final File file;
    private final Gson gson;

    public FileLoader(String fileName) {
        this.file = new File(fileName);
        this.gson = new Gson();
    }

    //читает файл, парсит и возвращает результат
    @Override
    public List<Measurement> load() {
        final List<Measurement> measurements;
        try (final BufferedReader reader = new BufferedReader(new FileReader(file))) {
            measurements = gson.fromJson(reader, new TypeToken<List<Measurement>>() {
            }.getType());
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
        return measurements;
    }
}
