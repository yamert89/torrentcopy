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

public class Downloader {
    private String bb_session = "";
    private String bb_t = "";

    public Downloader(String bb_session, String bb_t) {
        this.bb_session = bb_session;
        this.bb_t = bb_t;
    }

    public boolean download(String id){
        String URL = "https://rutracker.org/forum/viewtopic.php?t=" + id;

        Document document = null;
        Elements elements = null;
        String name = "";
        String body = "";
        String nameFolder = "K:/save/";
        Path dir;





        try {



            document = Jsoup.connect(URL).userAgent("Mozilla").timeout(40000).referrer(URL).get();
            name = document.getElementsByClass("topic-title-" + id).first().text();

            elements = document.getElementsByClass("post_body");

            body = elements.first().html();

            String urlIMG = elements.first().getElementsByTag("var").first().attr("title");

            elements = document.getElementsByAttributeValue("class", "nav w100 pad_2").first().children();



            StringBuilder sb = new StringBuilder();

            for (Element element :
                    elements) {
                sb.append(element.text());
            }

            String categoryPath = sb.toString();

            String contentPath = categoryPath.replace('»','/');
            if (Files.exists(Paths.get(nameFolder + contentPath + "/" + id))) {
                System.out.println("torrent already exist:" + name + " "+ id);

                return false;
            }



            Connection.Response response = Jsoup.connect("https://rutracker.org/forum/dl.php?t=" + id).header("Content-Type", "text/*")
                    .cookie("bb_ssl", "1")
                    .cookie("bb_session", bb_session)
                    //.cookie("bb_t", bb_t)
                    .cookie("bb_dev", "1-3")
                    .cookie("SLG_GWPT_Show_Hide_tmp", "1")
                    .cookie("SLG_wptGlobTipTmp", "1")
                    .userAgent("Mozilla").timeout(20000).referrer("https://rutracker.org/forum/viewtopic.php?t=" + id).method(Connection.Method.POST).ignoreContentType(true)
                    .execute();
            if (!response.body().startsWith("d")) {
                //System.out.println("!!!! Торент невалидный !!!!");
                throw new NullPointerException("!!!! Торент невалидный !!!!");
            }

            if (response.bodyAsBytes().length < 2000){

                throw new NullPointerException("Торрент не найден");
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
