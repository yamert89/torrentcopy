package executor;

import gui.GuiStart;
import javafx.application.Platform;
import org.jsoup.Connection;
import tasks.Free_torrentsTask;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
   // private static CopyOnWriteArrayList<Boolean> collection = new CopyOnWriteArrayList<Boolean>();
    private static ExecutorService service;
    private static boolean stopped;
    private static int startIndex = 210000;
    private static int endIndex = 230000;
    public static AtomicInteger downloadedCounter = new AtomicInteger();

    //public static BlockingDeque<String> set = new LinkedBlockingDeque<>();
    //public static ConcurrentMap<String, Integer> counter = new ConcurrentHashMap<>();
    //public static IntegerProperty integerProperty = new SimpleIntegerProperty(0);

    public static void execute(Map<String, String> cookies, boolean shutdown) {

        if (Files.exists(Paths.get("E:/saveCollection"))) collectInit();

        Platform.runLater(() -> GuiStart.updCountVisitLinks(startIndex));
        //startVal = 0;


        Connection.Response response1 = null;
       /* try {
            response1 = Jsoup.connect("https://rutracker.org/forum/login.php")
                    .method(Connection.Method.GET).timeout(40000)
                    .execute();

            Jsoup.connect("https://rutracker.org/forum/login.php")
                    .referrer("https://rutracker.org/forum/login.php")
                    .data("login_username", "***")
                    .data("login_password", "***")
                    .data("login", "***")
                    .timeout(40000)
                    .cookies(response1.cookies())
                    .method(Connection.Method.POST)
                    .execute();

        } catch (IOException e) {
            e.printStackTrace();
        }*/

        service = Executors.newFixedThreadPool(20);
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) service;



        for (int j = startIndex; j < endIndex;) {
            for (int i = j; i < j + 1000; i++) {
               // service.submit(new MyTask(i, bb_session, bb_t));
                service.submit(new Free_torrentsTask(i, cookies));
            }

            while(threadPoolExecutor.getQueue().size()!= 0){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            j += 1000;
            startIndex = j;

            Platform.runLater(() -> GuiStart.updTextarea("Блок завершен...."));
            if (stopped){
                System.out.println("Stopped!!!");
                Platform.runLater(()->{
                    GuiStart.updTextarea("Stopped........");});
                System.exit(0);
                return;
            }

        }
        stop();
        while(!stopped){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        service.shutdownNow();
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.open(new File("E:/shutdown.cmd"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);

    }



    private static void collectInit(){
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("E:/saveCollection"));

            startIndex = in.readInt();
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        stop();

    }

    public static void stop(){
        stopped = true;
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream("E:/saveCollection"));
            out.writeInt(startIndex);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }











}
