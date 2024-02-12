package com.example.mybackend.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileService {

    public static void writeToFile(List<String> lines, String filename) throws IOException {
        try (FileWriter writer = new FileWriter(filename)) {
            for (String line : lines) {
                writer.write(line + System.lineSeparator());
            }
        }
    }
}
