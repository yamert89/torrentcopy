package tasks;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Map;

import static executor.Main.downloadedCounter;

/**
 * Created by Пендальф Синий on 27.06.2018.
 */
public class RutrackerTask extends AbstractTask {

    public RutrackerTask(int index, Map<String, String> cookies) {
        super(index, cookies);
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().toString() + " started");

        /*boolean downloaded = new RutrackerTask(bb_session, bb_t).download(String.valueOf(index));
        if (downloaded) downloadedCounter.getAndIncrement();*/ //TODO uncomment
        if (downloadedCounter.get() > 900){
            Connection.Response response1 = null;
            try {
                response1 = Jsoup.connect("https://rutracker.org/forum/login.php")
                        .method(Connection.Method.GET).timeout(40000)
                        .execute();

                Jsoup.connect("https://rutracker.org/forum/login.php")
                        .referrer("https://rutracker.org/forum/login.php")
                        .data("login_username", "shurup7777")
                        .data("login_password", "yamert89")
                        .data("login", "")
                        .timeout(40000)
                        .cookies(response1.cookies())
                        .method(Connection.Method.POST)
                        .execute();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().toString() + " finished");
    }
}
