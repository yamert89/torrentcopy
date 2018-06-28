package tasks;

import gui.GuiStart;
import javafx.application.Platform;
import models.Free_torrents;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Пендальф Синий on 27.06.2018.
 */
public class Free_torrentsTask extends AbstractTask {

    public Free_torrentsTask(int index, Map<String, String> cookies) {
        super(index, cookies);
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().toString() + " started");

        /*boolean downloaded = new RutrackerTask(bb_session, bb_t).download(String.valueOf(index));
        if (downloaded) downloadedCounter.getAndIncrement();*/ //TODO uncomment
        Free_torrents freeTorrents = new Free_torrents("K:\\save\\FreeTorents", cookies, String.valueOf(index));

        try {
            Jsoup.connect("http://login.free-torrents.org/forum/login.php")
                    .referrer("http://free-torrents.org/forum/indexer1.php")
                    .data("login_username", "shurup7777")
                    .data("login_password", "yamert89")
                    .timeout(40000)
                    .method(Connection.Method.POST)
                    .execute();

        } catch (IOException e) {
            e.printStackTrace();
        }
        int status = freeTorrents.download();
        Platform.runLater(() -> GuiStart.updCountVisitLinks(0));
        Platform.runLater(()->GuiStart.updText(status));
        System.out.println(Thread.currentThread().toString() + " finished");
    }
}
