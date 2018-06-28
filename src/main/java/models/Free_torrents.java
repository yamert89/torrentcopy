package models;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Created by Пендальф Синий on 24.06.2018.
 */
public class Free_torrents extends Downloader {
    private String URL = "http://free-torrents.org/forum/viewtopic1.php?t=";
    private String downloadAddress = "http://dl.free-torrents.org/forum/dl.php?id=";


    public Free_torrents(String nameFolder, Map<String, String> cookies, String id) {
        super(nameFolder, cookies, id);
        URL = URL + id;
    }

    @Override
    public int download() {
        try {
            document = Jsoup.connect(URL).userAgent("Mozilla").timeout(40000).referrer(URL).cookies(cookies).get();

            getName();

            downloadAddress += getDownloadId();

            Element previous = null;
            elements = document.getElementsByClass("post_wrap");


            Elements elements2 = elements.first().getElementsByTag("var");
            for (Element e :
                    elements2) {
                if (e.attr("class").equals("postImg postImgAligned img-right")){
                    urlIMG = e.attr("title");
                    previous = e;
                    break;
                }
            }
            getBody(previous);


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

                return STATUS_EXIST;
            }

            downloadTorrent();

            //prepare nameFolder
            String nameCurrentFolder = name.replaceAll("[<>?\\\\/*;:]","");

            dir = Files.createDirectories(Paths.get(nameFolder + "/" + contentPath + "/" + nameCurrentFolder));

            fileSystemElementsCreate();

        }catch (NoSuchFileException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            return STATUS_FATAL;
        }
        catch (IOException e) {
            e.printStackTrace();

        } catch (NullPointerException e){
            e.printStackTrace();
            System.out.println("Данных нет " + id + " - " + e.getMessage());
            return STATUS_NULL;

        }
        return STATUS_OK;
    }

    @Override
    public void getName() {
        name = document.getElementsByClass("maintitle").get(0).text();
    }

    @Override
    public void getBody(Element previous) {

        //body = elements.first().html();
        body = elements.first().getElementsByTag("span").first().html();
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
            throw new NoSuchFileException("!!!! Торент невалидный !!!!");
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
