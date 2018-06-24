/**
 * Created by Пендальф Синий on 06.06.2018.
 */

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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class GuiStart extends Application {

    public static Label textArea;
    public static Label countLinks;
    public static TextArea text;
    TextField textFieldS;
    TextField textFieldT;
    private static int count;
    private static int countVisit;
    private static int countPoint;
    private static int countText;
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
                        Main.execute(textFieldS.getText(), textFieldT.getText());
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

        textArea = new Label("");
        textArea.setMinWidth(80);
        textArea.setStyle("-fx-border-style: solid; -fx-border-width: 2px; -fx-border-radius: 3px; -fx-background-color: antiquewhite");
        textArea.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);





        Label label = new Label("Загружено ссылок:");
        Label label2 = new Label("Проверено ссылок:");
        Label sesL = new Label("bb_session");
        Label bbL = new Label("bb_t");
        countLinks = new Label();
        countLinks.setStyle("-fx-border-style: solid; -fx-border-width: 2px; -fx-border-radius: 3px; -fx-background-color: antiquewhite");
        countLinks.setMinWidth(80);
        countLinks.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        TextArea text = new TextArea();
        text.setMinHeight(200);
        text.setMinWidth(600);
        text.setEditable(false);

        textFieldS = new TextField();
        textFieldT = new TextField();







        vBox.getChildren().addAll(sesL, textFieldS, bbL, textFieldT, label,textArea, label2, countLinks, text, btnStart, btnStop);
        anchorPane.getChildren().add(vBox);
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setScene(new Scene(anchorPane, 600, 400));


        stage.show();


    }



    public static void updCountVisitLinks(int start){
        if (start > 0) countVisit = start;
        else countVisit++;
        countLinks.setText(String.valueOf(countVisit));
    }
    public static void updText(){
        count ++;
        countPoint++;
        textArea.setText(String.valueOf(count));
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
