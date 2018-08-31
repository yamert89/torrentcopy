package utils;

import archiving.Zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class WalkFileRename {
    private String dir;

    public WalkFileRename(String dir) {
        this.dir = dir;
    }

    public void rename(String s) throws IOException {
        File path = new File(s);
        boolean rename = false;
        String fileName = "";
        File fileS = null;
        for (File file :
                path.listFiles()) {
            if (file.isDirectory()) rename(file.getAbsolutePath());
            else{
                fileName = file.getName();
                if (!fileName.endsWith(".txt")) continue;
                rename = true;
                fileS = file;
                break;
            }
        }
        if (rename) {
            Properties prop = new Properties();
            InputStreamReader reader = new InputStreamReader(new FileInputStream(fileS), "windows-1251");
            prop.load(reader);
            String str = prop.getProperty("fileName");
            reader.close();
            str = str.replaceAll("[<>?\\\\/*;:]","");
           // boolean fortune = path.renameTo(new File(path.getParent() + "/" + str));
            String newName = path.getParent() + "/&" + str;
            Path path1 = new File(newName).toPath();
            Files.move(path.toPath(),path1);

            Zip.zip(path1.toFile());

        }

    }



    public static void main(String[] args) {
        try {
            new WalkFileRename("E:/test2").rename("E:/test2");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}