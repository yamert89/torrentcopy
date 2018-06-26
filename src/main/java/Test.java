import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

/**
 * Created by porohin on 26.06.2018.
 */
public class Test {
    public static void main(String[] args) {
        Free_torrents free_torrents = new Free_torrents("GA1.2.1448130846.1530006249","a%3A3%3A%7Bs%3A2%3A%22u" +
                "k%22%3Bs%3A12%3A%22O1EKcz8nI0G1%22%3Bs%3A3%3A%22uid%22%3Bi%3A3497771%3Bs%3A3%3A%22sid%22%3Bs%3A20%3A%22suD" +
                "Hm6Asixr1T1AKtzlH%22%3B%7D", "GA1.2.31082032.1530006249");
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
        free_torrents.download("218875");
    }
}
