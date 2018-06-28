package models;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Пендальф Синий on 26.06.2018.
 */
public abstract class Downloader {

    public String URL;
    public String id;
    public Map<String, String> cookies;
    public Document document;
    public Elements elements;
    public String name;
    public String body;
    public String nameFolder = "K:/save/";
    public Path dir;
    public String urlIMG;
    public String categoryPath;
    public String contentPath;
    public Connection.Response response;
    public static final int STATUS_OK = 1;
    public static final int STATUS_NULL = 0;
    public static final int STATUS_EXIST = 2;
    public static final int STATUS_FATAL = -1;

    public Downloader(String nameFolder, Map<String, String> cookies, String id) {
        this.nameFolder = nameFolder;
        this.cookies = cookies;
        this.id = id;

    }

    abstract public int download();

    public void fileSystemElementsCreate() throws IOException {
        Path fileTor = dir.resolve(id + ".torrent");
        Path fileBody = dir.resolve(id + ".html");
        Path fileName = dir.resolve(id + ".txt");

        Properties prop = new Properties();
        prop.setProperty("fileName", name);
        prop.setProperty("urlIMG", urlIMG);
        prop.setProperty("categoryPath", categoryPath);

        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream( fileName.toFile()), "UTF-8");
        prop.store(writer,null);

        Files.write(fileTor, response.bodyAsBytes());
        Files.write(fileBody, body.getBytes());
        writer.close();


        System.out.println("Downloaded: " + fileTor + "\n" + URL);
            /*int finalSum = executor.Main.sum();*/
        //Platform.runLater(gui.GuiStart::updText); TODO uncomment
    }

    abstract public void getName();

    abstract public void getBody(Element previous);

    abstract public void downloadTorrent() throws IOException;


}
