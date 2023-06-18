package com.utfpr.distributed.controller;

import com.utfpr.distributed.ClientApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public abstract class BaseController {

    protected void openNewWindow(ActionEvent event, String newWindow, String title) {

        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(ClientApplication.class.getResource(newWindow)));
            Stage app = (Stage)((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(ClientApplication.class.getResource("css/dark-theme.css").toExternalForm());
            app.setScene(scene);
            app.setTitle(title);
            app.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
