import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zip {

    public static void zip(File file) throws IOException {

        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(file.getPath() + ".zip"));
        zipOutputStream.setLevel(1);
        for (File f :
                file.listFiles()) {
            String fName = f.getName();
            String shortFName = fName.substring(fName.length() - 8, fName.length());
            int idxpoint = shortFName.indexOf(".");
            zipOutputStream.putNextEntry(new ZipEntry(file.getName() + shortFName.substring(idxpoint, shortFName.length())));
            InputStream inputStream = new FileInputStream(f);
            byte[] arr = new byte[1024];
            while (inputStream.available() > 0){
                int len = inputStream.read(arr);
                zipOutputStream.write(arr, 0 , len);
            }
            inputStream.close();
        }
        zipOutputStream.close();
    }
}