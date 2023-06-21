package com.utfpr.distributed.controller;

import com.utfpr.distributed.database.UserDataAccess;
import com.utfpr.distributed.model.User;
import com.utfpr.distributed.util.socket.ServerSocketConnectionHandler;
import com.utfpr.distributed.database.DBConnection;
import com.utfpr.distributed.util.StringUtil;
import com.utfpr.distributed.util.token.TokenManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ServerController extends BaseController implements Initializable {
    @FXML
    private TextField tPorta;
    @FXML
    private Accordion aLog;
    @FXML
    private TableView<User> tLogins;
    private static Accordion aLogS;
    private static TableView<User> tLoginsS;

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
        this.initializeTable();
        aLogS = this.aLog;
        tLoginsS = this.tLogins;
    }

    private void initializeTable() {
        TableColumn<User, Integer> idCol = new TableColumn<>("#");
        TableColumn<User, String> nomeCol = new TableColumn<>("Nome");
        TableColumn<User, String> emailCol = new TableColumn<>("Email");

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomeCol.setCellValueFactory(new PropertyValueFactory<>("nome"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        this.tLogins.getColumns().setAll(idCol, nomeCol, emailCol);
    }

    private static ObservableList<User> getList() {
        List<User> users = new ArrayList<>();

        TokenManager.getTokens().forEach((id, token) ->
            users.add(UserDataAccess.getById(id))
        );

        return FXCollections.observableArrayList(users);
    }

    public static void refreshLogins() {
        Platform.runLater(() -> tLoginsS.setItems(getList()));
    }
}
