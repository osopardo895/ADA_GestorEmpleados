package com.gestor_empleados.ada_gestorempleados;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("vista.fxml"));

        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setTitle("Gestor Empleados");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
