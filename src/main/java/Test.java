import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by porohin on 26.06.2018.
 */
public class Test {
    public static void main(String[] args) {
        Map<String, String> cookies = new HashMap<>();
        cookies.put("bbe_data", "a%3A3%3A%7Bs%3A2%3A%22uk%22%3BN%3Bs%3A3%3A%22uid%22%3Bi%3A3497771%3Bs%3A3%3A%22sid%22%3Bs%3A20%3A%22P1MLKc5qrSYqmJX1LBMs%22%3B%7D");

        Free_torrents freeTorrents = new Free_torrents("K:\\save\\FreeTorents", cookies, "218409");
        Connection.Response response1 = null;
        try {
            response1 = Jsoup.connect("http://login.free-torrents.org/forum/login.php")
                    .method(Connection.Method.GET).timeout(40000)
                    .execute();

            Jsoup.connect("http://login.free-torrents.org/forum/login.php")
                    .referrer("http://free-torrents.org/forum/indexer1.php")
                    .data("login_username", "shurup7777")
                    .data("login_password", "yamert89")
                    .timeout(40000)
                    .cookies(response1.cookies())
                    .method(Connection.Method.POST)
                    .execute();

        } catch (IOException e) {
            e.printStackTrace();
        }
        freeTorrents.download();
    }
}
