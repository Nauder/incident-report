package com.utfpr.distributed.controller;

import com.utfpr.distributed.util.socket.ClientSocketConnectionHandler;
import com.utfpr.distributed.validation.UserValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterController extends BaseController {

    @FXML
    private TextField tNome;
    @FXML
    private TextField tEmail;
    @FXML
    private TextField tSenha;
    @FXML
    private Label lErro;

    @FXML
    protected void onRegistrarSubmit(ActionEvent event) {

        if (areTextFieldsPopulated(tNome, tEmail, tSenha)) {
            if (validateFields()) {
                final Map<String, Object> inputData = new HashMap<>();

                inputData.put("operacao", 1);
                inputData.put("nome", tNome.getText());
                inputData.put("email", tEmail.getText());
                inputData.put("senha", tSenha.getText());

                JSONObject response = ClientSocketConnectionHandler.run(inputData);

                if (response.query("/status") != "OK") {
                    lErro.setText((String) response.query("/status"));
                } else {
                    openNewWindow(event, "login-view.fxml", "login");
                }
            }
        } else {
            lErro.setText("Campo(s) obrigatóio(s) em branco");
        }
    }

    @FXML
    protected void onVoltarButtonClick(ActionEvent event) {
        openNewWindow(event, "login-view.fxml", "login");
    }

    private boolean validateFields() {

        if (!UserValidator.checkEmail(tEmail.getText())) {
            lErro.setText("Formato de e-mail inválido");
        } else if (!UserValidator.checkName(tNome.getText())) {
            lErro.setText("Formato de nome inválido");
        } else if (!UserValidator.checkPassword(tSenha.getText())) {
            lErro.setText("Formato de senha inválido");
        } else {
            return true;
        }

        return false;
    }
}
