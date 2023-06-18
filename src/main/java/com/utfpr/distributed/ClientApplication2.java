package com.utfpr.distributed;

import com.utfpr.distributed.database.DBConnection;
import com.utfpr.distributed.util.ClientSession;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientApplication2 extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        DBConnection.connect();
        ClientSession.setId(-1);
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication2.class.getResource("connect-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        scene.getStylesheets().add(getClass().getResource("css/dark-theme.css").toExternalForm());
        stage.setTitle("Conectar");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}