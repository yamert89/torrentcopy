package utils;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Пендальф Синий on 30.06.2018.
 */
public class EmptyCleaner {
    public static void main(String[] args) {
        try {
            System.out.println();
            Files.walk(Paths.get("K:\\save\\FreeTorents"), FileVisitOption.FOLLOW_LINKS).forEach(path -> {
                String s = path.getFileName().toString();
                if ((path.toFile().listFiles() == null || path.toFile().listFiles().length == 0) && !s.endsWith(".zip")) {
                    try {
                        Files.delete(path);
                        System.out.println("delete " + path.getFileName());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
