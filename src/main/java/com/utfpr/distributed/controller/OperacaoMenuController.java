package com.utfpr.distributed.controller;

import com.utfpr.distributed.util.socket.ClientSocketConnectionHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class OperacaoMenuController extends BaseController {
    @FXML
    private Label lErro;

    @FXML
    protected void onAtualizarButtonClick(ActionEvent event) {
        openNewWindow(event, "alterar-usuario-view.fxml", "alterar");
    }

    @FXML
    protected void onIncidentesButtonClick(ActionEvent event) {
        openNewWindow(event, "incidente-list-view.fxml", "incidentes");
    }

    @FXML
    protected void onLogoutButtonClick(ActionEvent event) {

        final Map<String, Object> inputData = new HashMap<>();

        inputData.put("operacao", 9);

        JSONObject response = ClientSocketConnectionHandler.run(inputData);

        if(response.query("/status") != "OK") {
            lErro.setText((String) response.query("/status"));
        } else {
            openNewWindow(event, "login-view.fxml", "login");
        }
    }

    @FXML
    protected void onRemoverButtonClick(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Remoção");
        alert.setHeaderText("Isto permanentemente removera o seu cadastro.");
        alert.setContentText("Tem certeza que deseja continuar?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            final Map<String, Object> inputData = new HashMap<>();

            inputData.put("operacao", 8);

            JSONObject response = ClientSocketConnectionHandler.run(inputData);

            if(response.query("/status") != "OK") {
                lErro.setText((String) response.query("/status"));
            } else {
                openNewWindow(event, "login-view.fxml", "login");
            }
        }
    }
}
