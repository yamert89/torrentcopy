import javafx.application.Platform;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
   // private static CopyOnWriteArrayList<Boolean> collection = new CopyOnWriteArrayList<Boolean>();
    private static ExecutorService service;
    private static boolean stopped;
    private static int startIndex = 100000;
    private static int startVal;
    private static AtomicInteger downloadedCounter = new AtomicInteger();
    //public static BlockingDeque<String> set = new LinkedBlockingDeque<>();
    //public static ConcurrentMap<String, Integer> counter = new ConcurrentHashMap<>();
    //public static IntegerProperty integerProperty = new SimpleIntegerProperty(0);

    public static void execute(String bb_session, String bb_t) {

        if (Files.exists(Paths.get("E:/saveCollection"))) collectInit();


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

        service = Executors.newFixedThreadPool(20);
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) service;

        for (int j = startIndex; j < 6000000;) {
            for (int i = j; i < j + 1000; i++) {
                service.submit(new MyTask(i, bb_session, bb_t));
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
                Platform.runLater(()->{GuiStart.updTextarea("Stopped........");});
                //System.exit(0);
                return;
            }

        }






    }

    public static int sum(){
        int sum = 0;
        /*for (Integer val :
                counter.values()) {
            sum += val;

        }*/
        return sum;

    }
    

    private static void collectInit(){
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("E:/saveCollection"));

            startIndex = in.readInt();
            startVal = startIndex;

        } catch (IOException e) {
            stackTrace(e);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        //super.finalize();
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
            stackTrace(e);
        }

    }

    public static void stackTrace(Exception e){
        /*StackTraceElement[] trace = e.getStackTrace();
        for (StackTraceElement element :
                trace) {
            try {
                set.putFirst(element.toString());
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }*/
    }



    public static int rnd(int min, int max) {
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }

    static class MyTask implements Runnable{
        private String bb_session;
        private String bb_t;

        private int index;

        public MyTask(int index, String sess, String t) {

            this.index = index;
            bb_session = sess;
            bb_t = t;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().toString() + " started");
            Platform.runLater(() -> GuiStart.updCountVisitLinks(startVal));
            startVal = 0;
            boolean downloaded = new Rutracker(bb_session, bb_t).download(String.valueOf(index));
            if (downloaded) downloadedCounter.getAndIncrement();
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




}
