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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
    public static int count;
    public static int countVisit;
    public static int countPoint;
    public static int countText;
    private PrintStream errorStream;

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
                        if (!cookieName1.getText().isEmpty()) cookies.put(cookieName1.getText(), cookieVal1.getText());
                        if (!cookieName2.getText().isEmpty()) cookies.put(cookieName2.getText(), cookieVal2.getText());
                        if (!cookieName3.getText().isEmpty()) cookies.put(cookieName3.getText(), cookieVal3.getText());

                        Main.execute(cookies);
                        return null;
                    }
                };
                new Thread(task).start();

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
        TextArea text = new TextArea();
        text.setMinHeight(200);
        text.setMinWidth(600);
        text.setEditable(false);

        cookieName1 = new TextField();
        cookieVal1 = new TextField();
        cookieName2 = new TextField();
        cookieVal2 = new TextField();
        cookieName3 = new TextField();
        cookieVal3 = new TextField();

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







        vBox.getChildren().addAll(firstCookie, secondCookie, thirdCookie, loaded, visited, text, buttons);
        anchorPane.getChildren().add(vBox);
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setScene(new Scene(anchorPane, 600, 400));


        stage.show();


    }



    public static void updCountVisitLinks(int start){
        if (start > 0) countVisit = start;
        else countVisit++;
        countVisited.setText(String.valueOf(countVisit));
    }
    public static void updText(){
        count ++;
        countPoint++;
        countLoaded.setText(String.valueOf(count));
        if (countText > 1048576) text.setText("............... page .............");
        if (countPoint > 200) {
            text.setText(text.getText() + "\n");
            countPoint = 0;
        }


        text.setText(text.getText() + ".");
    }

    public static void updTextarea(String s){
        try {
            text.setText(text.getText() + "\n" + s);
        }catch (NullPointerException e){
            text.setText(s);
        }
    }
}
