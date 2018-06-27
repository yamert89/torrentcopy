import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Created by Пендальф Синий on 24.06.2018.
 */
public class Free_torrents extends Downloader{
    private String URL = "http://free-torrents.org/forum/viewtopic1.php?t=";
    private String downloadAddress;

    public Free_torrents(String nameFolder, Map<String, String> cookies, String id) {
        super(nameFolder, cookies, id);
        URL = URL + id;
    }




    /*public boolean download(String id){
        String URL = "http://free-torrents.org/forum/viewtopic1.php?t=" + id;



    }*/

    @Override
    public boolean download() {
        try {
            document = Jsoup.connect(URL).userAgent("Mozilla").timeout(40000).referrer(URL).cookies(cookies).get();

            getName();
            getBody();
            getDownloadLink();




            //String urlIMG = elements.first().getElementsByClass("nav").text();
            String urlIMG = "";

            elements = document.getElementsByClass("nav").first().getAllElements();

            StringBuilder sb = new StringBuilder();

            for (int i = 5; i < elements.size(); i++) {
                Element element = elements.get(i);
                sb.append(element.text());
            }

            categoryPath = sb.toString();

            contentPath = categoryPath.replace('»','/');
            if (Files.exists(Paths.get(nameFolder + contentPath + "/" + id))) {
                System.out.println("torrent already exist:" + name + " "+ id);

                return false;
            }

            downloadTorrent();

            dir = Files.createDirectories(Paths.get(nameFolder + "/" + contentPath + "/" + id));

            fileSystemElementsCreate();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (NullPointerException e){
            e.printStackTrace();
            System.out.println("Данных нет " + id + " - " + e.getMessage());
            return false;

        }
        return true;
    }

    @Override
    public void getName() {
        name = document.getElementsByClass("maintitle").get(0).text();
    }

    @Override
    public void getBody() {
        elements = document.getElementsByClass("post_wrap");
        body = elements.first().html();
    }

    @Override
    public void downloadTorrent() throws IOException {
        response = Jsoup.connect("http://dl.free-torrents.org/forum/dl.php?id=" + "160783")
                .header("Content-Type", "text/html")
                .header("Accept-Encoding", "gzip, deflate")
                .header("Connection", "keep-alive")
                .cookies(cookies)
                .userAgent("Mozilla").timeout(20000)
                .referrer("http://free-torrents.org/forum/viewtopic1.php?t=" + id)
                .method(Connection.Method.GET).ignoreContentType(true)
                .execute();

        if (response.bodyAsBytes().length < 2000){
            throw new NullPointerException("Торрент не найден");
        }

        /*if (!response.body().startsWith("d")) {
            //System.out.println("!!!! Торент невалидный !!!!");
            throw new NullPointerException("!!!! Торент невалидный !!!!");
        }*/

    }
    
    private String getDownloadLink(){
        String html = document.html();
        System.out.println(html);
        int end_idx = html.lastIndexOf("http://dl.free-torrents.org/forum/dl.php?id=");
        int start_idx = html.indexOf("http://dl.free-torrents.org/forum/dl.php?id=");
        int ch = 44;
       /* for (Element el :
                elements) {
            int end_idx = el.html().lastIndexOf("window.location = 'http://dl.free-torrents.org/forum/dl.php?id=");
            int start_idx = el.html().indexOf("window.location = 'http://dl.free-torrents.org/forum/dl.php?id=");
            if (end_idx == -1) continue;
        }*/
        return "";
    }
}
