package com.utfpr.distributed.controller;

import com.utfpr.distributed.util.ClientSession;
import com.utfpr.distributed.util.socket.ClientSocketConnectionHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginController extends BaseController {
    @FXML
    private TextField tEmail;
    @FXML
    private TextField tSenha;
    @FXML
    private Label lErro;

    @FXML
    protected void onRegistrarButtonClick(ActionEvent event) {
        openNewWindow(event, "registrar-view.fxml", "Registrar");
    }

    @FXML
    protected void onSubmit(ActionEvent event) {

        final Map<String, Object> inputData = new HashMap<>();

        inputData.put("operacao", 2);
        inputData.put("email", tEmail.getText());
        inputData.put("senha", tSenha.getText());

        JSONObject response = ClientSocketConnectionHandler.run(inputData);

        if(response.query("/status") != "OK") {
            lErro.setText((String) response.query("/status"));
        } else {
            ClientSession.setEmail(tEmail.getText());
            ClientSession.setSenha(tSenha.getText());
            openNewWindow(event, "operacao-menu-view.fxml", "Menu");
        }
    }
}
