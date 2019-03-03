package org.jastka4.knapsackgui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Locale.setDefault(new Locale("en", "US"));
        ResourceBundle resources = ResourceBundle.getBundle("bundles.base");
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/home.fxml"), resources);
        primaryStage.setTitle(resources.getString("application.title"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
