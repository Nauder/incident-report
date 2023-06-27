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
import org.json.JSONException;
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
            updateLog("Servidor ouvindo em: " + InetAddress.getLocalHost() + ":" + tPorta.getText(), "servidor iniciado:");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onParar(ActionEvent event) {
        DBConnection.close();
        System.exit(1);
    }

    private static void updateLog(String data, String title) {

        Platform.runLater(() -> {
            TitledPane pane = new TitledPane();
            HBox box = new HBox();
            Label content = new Label();
            box.getChildren().add(content);
            box.setAlignment(Pos.CENTER_LEFT);
            content.setAlignment(Pos.CENTER_LEFT);
            content.setText(data);
            pane.setText(title);
            pane.setContent(box);
            pane.setAlignment(Pos.CENTER_LEFT);

            aLogS.getPanes().add(pane);
        });
    }

    public static void appendReceived(String message) {

        if(message != null) {
            try {
                final JSONObject json = new JSONObject(message);
                updateLog(json.toString(2),
                        json.has("id") ? "recebido de " + json.getInt("id") + ":" : "recebido:");
            } catch (Exception e) {
                System.out.println("Erro ao montar log: " + e.getMessage());
            }
        }
    }

    public static void appendSent(String message, Integer id) {

        if(message != null) {
            try {
                updateLog(new JSONObject(message).toString(2),
                        id != -1 ? "enviado para " + id + ":" : "enviado:");
            } catch (Exception e) {
                System.out.println("Erro ao montar log: " + e.getMessage());
            }
        }
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
