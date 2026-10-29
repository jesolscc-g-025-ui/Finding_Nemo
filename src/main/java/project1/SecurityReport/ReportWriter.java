package project1.SecurityReport;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ReportWriter {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public void writeReport(ScanReport report , Path filepath)throws IOException {

        //check path if path doesnt exist create a directory

        Path parent = filepath.getParent();

        if(parent != null){
            Files.createDirectories(parent);
        }
        String json = gson.toJson(report);

        Files.writeString(filepath,json);
    }
}
