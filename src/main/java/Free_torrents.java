import javafx.application.Platform;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Created by Пендальф Синий on 24.06.2018.
 */
public class Free_torrents {
    private String _ga = "";
    private String bbe_data = "";
    private String _gid = "";


    public Free_torrents(String _ga, String bbe_t, String _gid) {
        this._ga = _ga;
        this.bbe_data = bbe_t;
        this._gid = _gid;
    }

    public boolean download(String id){
        String URL = "http://free-torrents.org/forum/viewtopic1.php?t=" + id;

        Document document = null;
        Elements elements = null;
        String name = "";
        String body = "";
        String nameFolder = "K:/save/";
        Path dir;





        try {



            document = Jsoup.connect(URL).userAgent("Mozilla").timeout(40000).referrer(URL).get();
            name = document.getElementsByClass("maintitle").get(0).text();

            elements = document.getElementsByClass("post_wrap");

            body = elements.first().html();

            //String urlIMG = elements.first().getElementsByClass("nav").text();
            String urlIMG = "";

            elements = document.getElementsByClass("nav").first().getAllElements();



            StringBuilder sb = new StringBuilder();

            for (int i = 5; i < elements.size(); i++) {
                Element element = elements.get(i);
                sb.append(element.text());
            }

            String categoryPath = sb.toString();

            String contentPath = categoryPath.replace('»','/');
            if (Files.exists(Paths.get(nameFolder + contentPath + "/" + id))) {
                System.out.println("torrent already exist:" + name + " "+ id);

                return false;
            }



            Connection.Response response = Jsoup.connect("http://dl.free-torrents.org/forum/dl.php?id=" + id).header("Content-Type", "text/html")
                    .cookie("_ga", _ga)
                    .cookie("_gid", _gid)
                    .cookie("bbe_t", bbe_data)
                    .userAgent("Mozilla").timeout(20000).referrer("http://free-torrents.org/forum/viewtopic1.php?t=" + id).method(Connection.Method.GET).ignoreContentType(true)
                    .execute();


            if (response.bodyAsBytes().length < 2000){

                throw new NullPointerException("Торрент не найден");
            }

            if (!response.body().startsWith("d")) {
                //System.out.println("!!!! Торент невалидный !!!!");
                throw new NullPointerException("!!!! Торент невалидный !!!!");
            }

            dir = Files.createDirectories(Paths.get(nameFolder + contentPath + "/" + id));








            Path fileTor = dir.resolve(id + ".torrent");
            //Paths.get(dir.toAbsolutePath().toString() + id + ".torrent");

            Path fileBody = dir.resolve(id + ".html");
            //Paths.get(dir.toAbsolutePath().toString() + id + ".html");
            Path fileName = dir.resolve(id + ".txt");
            //Paths.get(dir.toAbsolutePath().toString() + id + ".txt");

            Properties prop = new Properties();
            prop.setProperty("fileName", name);
            prop.setProperty("urlIMG", urlIMG);
            prop.setProperty("categoryPath", categoryPath);
            ;
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream( fileName.toFile()), "UTF-8");
            prop.store(writer,null);



            Files.write(fileTor, response.bodyAsBytes());
            Files.write(fileBody, body.getBytes());
            writer.close();


            System.out.println("Downloaded: " + fileTor + "\n" + URL);
            /*int finalSum = Main.sum();*/
            Platform.runLater(GuiStart::updText);








        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (NullPointerException e){
            //e.printStackTrace();
            System.out.println("Данных нет " + id + " - " + e.getMessage());
            return false;


        }
        return true;

    }
}
