import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Created by Пендальф Синий on 24.06.2018.
 */
public class Free_torrents extends Downloader{
    private String URL = "http://free-torrents.org/forum/viewtopic1.php?t=";
    private String downloadAddress = "http://dl.free-torrents.org/forum/dl.php?id=";
    //TODO Error code

    public Free_torrents(String nameFolder, Map<String, String> cookies, String id) {
        super(nameFolder, cookies, id);
        URL = URL + id;
    }

    @Override
    public int download() {
        try {
            document = Jsoup.connect(URL).userAgent("Mozilla").timeout(40000).referrer(URL).cookies(cookies).get();

            getName();
            getBody();
            downloadAddress += getDownloadId();

            Elements elements2 = elements.first().getElementsByTag("var");
            for (Element e :
                    elements2) {
                if (e.attr("class").equals("postImg postImgAligned img-right")){
                    urlIMG = e.attr("title");
                    break;
                }
            }

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

                return 0;
            }

            downloadTorrent();

            dir = Files.createDirectories(Paths.get(nameFolder + "/" + contentPath + "/" + id));

            fileSystemElementsCreate();

        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        } catch (NullPointerException e){
            e.printStackTrace();
            System.out.println("Данных нет " + id + " - " + e.getMessage());
            return 0;

        }
        return 0;
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
        response = Jsoup.connect(downloadAddress)
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

        if (!response.body().startsWith("d")) {
            throw new NullPointerException("!!!! Торент невалидный !!!!");
        }

    }
    
    private String getDownloadId(){
        String html = document.html();
        int start_idx = html.indexOf(downloadAddress);
        final int offset = 44;
        start_idx += offset;
        return html.substring(start_idx, start_idx + 6);
    }
}
