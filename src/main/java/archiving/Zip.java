package archiving;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zip {

    public static void zip(File file) {
        try {

            ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(file.getPath() + ".zip"));
            zipOutputStream.setLevel(9);
            for (File f :
                    file.listFiles()) {
                String fName = f.getName();
                //String shortFName = fName.substring(fName.length() - 8, fName.length());
                //int idxpoint = shortFName.indexOf(".");
                zipOutputStream.putNextEntry(new ZipEntry(fName));
                InputStream inputStream = new FileInputStream(f);
                byte[] arr = new byte[1024];
                while (inputStream.available() > 0) {
                    int len = inputStream.read(arr);
                    zipOutputStream.write(arr, 0, len);
                }
                inputStream.close();
            }
            zipOutputStream.close();
            File[] list = file.listFiles();
            for (int i = 0; i < list.length; i++) {
                if(!list[i].delete()) throw new IOException("Archive not deleted");
            }
            if(!file.delete()) throw new IOException("Archive not deleted");
            System.out.println("Successfully archiving ");
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}