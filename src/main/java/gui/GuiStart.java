package gui; /**
 * Created by Пендальф Синий on 06.06.2018.
 */

import executor.Main;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class GuiStart extends Application {

    public static Label countLoaded;
    public static Label countVisited;
    public static TextArea text;
    private TextField cookieName1;
    private TextField cookieName2;
    private TextField cookieName3;
    private TextField cookieVal1;
    private TextField cookieVal2;
    private TextField cookieVal3;
    private CheckBox checkBox;
    public static AtomicInteger count = new AtomicInteger();;
    public static AtomicInteger countVisit = new AtomicInteger();;
    public static AtomicInteger countPoint = new AtomicInteger();
    public static int countText;
    public static int countFatal;
    private PrintStream errorStream;
    private static Thread generalThread;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        try {
            File out = new File("E:/log.txt");
            out.createNewFile();
            errorStream = new PrintStream(new FileOutputStream("E:/log.txt"));
            System.setErr(errorStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        primaryStage.setOnCloseRequest(event -> closeGenThread());

        AnchorPane anchorPane = new AnchorPane();
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        Button btnStart = new Button("Start");
        btnStart.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                Task<Void> task = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        Map<String, String> cookies = new HashMap<>();
                        if (!checkBox.isSelected()) {
                            if (!cookieName1.getText().isEmpty())
                                cookies.put(cookieName1.getText(), cookieVal1.getText());
                            if (!cookieName2.getText().isEmpty())
                                cookies.put(cookieName2.getText(), cookieVal2.getText());
                            if (!cookieName3.getText().isEmpty())
                                cookies.put(cookieName3.getText(), cookieVal3.getText());
                        } else {
                            cookies.put("bbe_data", "a%3A3%3A%7Bs%3A2%3A%22uk%22%3Bs%3A12%3A%22O1EKcz8nI0G1%22%3Bs%" +
                                    "3A3%3A%22uid%22%3Bi%3A3497771%3Bs%3A3%3A%22sid%22%3Bs%3A20%3A%226BGM1c8HtXd9D9tOPdCk%22%3B%7D");
                            cookies.put("PHPSESSID","0e5bc106i30q4mb3ovbn3pldk3");
                            cookies.put("SLG_GWPT_Show_Hide_tmp","1");
                            cookies.put("SLG_wptGlobTipTmp","1");
                            cookies.put("host131023","5fn5/f63oqLg6Onk7KO8vb3q5Ors7+T5v6P9+qI=");
                            cookies.put("utm131023","DosvQQmv3hkE");

                        }

                        Main.execute(cookies);
                        return null;
                    }
                };
                generalThread = new Thread(task);
                generalThread.start();



            }
        });

        Button btnStop = new Button("Stop");

        btnStop.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                Main.stop();
                errorStream.flush();
                errorStream.close();
            }
        });

        countLoaded = new Label("");
        countLoaded.setMinWidth(80);
        countLoaded.setStyle("-fx-border-style: solid; -fx-border-width: 2px; -fx-border-radius: 3px; -fx-background-color: antiquewhite");
        countLoaded.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);





        Label label = new Label("Загружено ссылок:   ");
        Label label2 = new Label("Проверено ссылок:  ");

        countVisited = new Label();
        countVisited.setStyle("-fx-border-style: solid; -fx-border-width: 2px; -fx-border-radius: 3px; -fx-background-color: antiquewhite");
        countVisited.setMinWidth(80);
        countVisited.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        text = new TextArea();
        text.setMinHeight(200);
        text.setMinWidth(600);
        text.setEditable(false);

        cookieName1 = new TextField();
        cookieVal1 = new TextField();
        cookieName2 = new TextField();
        cookieVal2 = new TextField();
        cookieName3 = new TextField();
        cookieVal3 = new TextField();

        checkBox = new CheckBox("default cookie");


        HBox firstCookie = new HBox(cookieName1, cookieVal1);
        HBox secondCookie = new HBox(cookieName2, cookieVal2);
        HBox thirdCookie = new HBox(cookieName3, cookieVal3);
        HBox loaded = new HBox(label, countLoaded);
        HBox visited = new HBox(label2, countVisited);
        HBox buttons = new HBox(btnStart, btnStop);

        firstCookie.setAlignment(Pos.CENTER);
        secondCookie.setAlignment(Pos.CENTER);
        thirdCookie.setAlignment(Pos.CENTER);
        loaded.setAlignment(Pos.CENTER);
        visited.setAlignment(Pos.CENTER);
        buttons.setAlignment(Pos.CENTER);







        vBox.getChildren().addAll(firstCookie, secondCookie, thirdCookie, checkBox, loaded, visited, text, buttons);
        anchorPane.getChildren().add(vBox);
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setScene(new Scene(anchorPane, 600, 400));


        stage.show();


    }



    public static void updCountVisitLinks(int start){
        if (start > 0) countVisit.set(start);
        else countVisit.getAndIncrement();
        countVisited.setText(String.valueOf(countVisit));
    }
    public static void updCountLoadedLinks(){
        count.getAndIncrement();
        //countPoint.getAndIncrement();
        countLoaded.setText(String.valueOf(count.get()));

    }

    public static void updText(int status, int index){
        /*STATUS_OK = 1;
        STATUS_NULL = 0;
        STATUS_EXIST = 2;
        STATUS_FATAL = -1;*/
        String res = "";

        switch (status){
            case 1:
                res = "...DOWNLOADED";
                break;
            case 0:
                res = "...NULL";
                break;
            case 2:
                res = "...ALREADY EXIST";
                break;
            case -1:
                res = "...FATAL";
                countFatal++;
                break;


        }
        res += " - ";
        res += String.valueOf(index);
        text.setText(text.getText() + "\n" + res);
        if (countFatal > 100) closeGenThread();
        if (text.getText().length() > 10000) text.setText("............... page .............");
        /*if (countPoint.get() > 200) {
            text.setText(text.getText() + "\n");
            countPoint.set(0);
        }*/


        //text.setText(text.getText() + ".");
    }

    public static void updTextarea(String s){
        try {
            text.setText(text.getText() + "\n" + s);
        }catch (NullPointerException e){
            text.setText(s);
        }
    }

    private static void closeGenThread(){
        Main.stop();
        //generalThread.interrupt();
    }
}
