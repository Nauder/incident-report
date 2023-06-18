package com.utfpr.distributed.controller;

import com.utfpr.distributed.util.ClientSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ConnectionController extends BaseController {
    @FXML
    private TextField tEndereco;
    @FXML
    private TextField tPorta;

    @FXML
    protected void onConectarSubmit(ActionEvent event) {

        ClientSession.setEndereco(tEndereco.getText());
        ClientSession.setPorta(Integer.parseInt(tPorta.getText()));

        openNewWindow(event, "login-view.fxml", "Operação");
    }
}
