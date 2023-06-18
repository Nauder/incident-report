package com.utfpr.distributed.controller;

import com.utfpr.distributed.util.socket.ServerSocketConnectionHandler;
import com.utfpr.distributed.database.DBConnection;
import com.utfpr.distributed.util.StringUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

public class ServerController extends BaseController implements Initializable {
    @FXML
    private TextField tPorta;
    @FXML
    private Accordion aLog;
    private static Accordion aLogS;

    @FXML
    protected void onIniciar(ActionEvent event) {
        new Thread(new ServerSocketConnectionHandler(Integer.parseInt(tPorta.getText()))).start();
        try {
            updateLog("Servidor ouvindo em: " + InetAddress.getLocalHost() + ":" + tPorta.getText());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onParar(ActionEvent event) {
        DBConnection.close();
        System.exit(1);
    }

    private static void updateLog(String data) {

        Platform.runLater(() -> {
            TitledPane pane = new TitledPane();
            HBox box = new HBox();
            Label content = new Label();
            box.getChildren().add(content);
            box.setAlignment(Pos.CENTER_LEFT);
            content.setAlignment(Pos.CENTER_LEFT);
            content.setText(data);
            pane.setText(StringUtil.ellipsize(data, 11));
            pane.setContent(box);
            pane.setAlignment(Pos.CENTER_LEFT);

            aLogS.getPanes().add(pane);
        });
    }

    public static void appendReceived(String message) {

        updateLog("Recebido:\n" + new JSONObject(message).toString(2));
    }

    public static void appendSent(String message) {

        updateLog("Enviado:\n" + new JSONObject(message).toString(2));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DBConnection.connect();
        aLogS = this.aLog;
    }
}
