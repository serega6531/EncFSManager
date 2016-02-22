package ru.serega6531.encfsmanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.io.File;
import java.util.prefs.Preferences;

public class Main extends Application {

    public static Stage stage;
    public static Scene scene;

    public static File mountDir, encodeDir;

    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("design.fxml"));
        primaryStage.setTitle("EncFC Manager");
        scene = new Scene(root, 465, 290);

        Preferences prefs = Preferences.userNodeForPackage(getClass());

        String mountDirPath = prefs.get("mountDir", null);
        if(mountDirPath != null){
            mountDir = new File(mountDirPath);
            Label label = (Label) Main.scene.lookup("#mountLabel");
            label.setText("Mount point: " + mountDirPath);
        }

        String encodeDirPath = prefs.get("encodeDir", null);
        if(encodeDirPath != null){
            encodeDir = new File(encodeDirPath);
            Label label = (Label) scene.lookup("#encodeLabel");
            label.setText("Encode dir: " + encodeDirPath);
        }

        String password = prefs.get("password", null);
        if(password != null){
            PasswordField passwordField = (PasswordField) scene.lookup("#passwordEdit");
            passwordField.setText(password);
        }

        primaryStage.setScene(scene);
        stage.setResizable(false);
        stage.setFullScreen(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
