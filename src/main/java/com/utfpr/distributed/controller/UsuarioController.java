package com.utfpr.distributed.controller;

import com.utfpr.distributed.util.ClientSession;
import com.utfpr.distributed.util.socket.ClientSocketConnectionHandler;
import com.utfpr.distributed.validation.UserValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class UsuarioController extends BaseController implements Initializable {

    @FXML
    private TextField tNome;
    @FXML
    private TextField tEmail;
    @FXML
    private TextField tSenha;
    @FXML
    private Label lErro;

    @FXML
    protected void onUpdateSubmit(ActionEvent event) {

        if (areTextFieldsPopulated(tNome, tEmail, tSenha)) {
            if (validateFields()) {
                final Map<String, Object> inputData = new HashMap<>();

                inputData.put("operacao", 3);
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
    protected void onCancel(ActionEvent event) {
        openNewWindow(event, "operacao-menu-view.fxml", "menu");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tNome.setText(ClientSession.getNome());
        tEmail.setText(ClientSession.getEmail());
        tSenha.setText(ClientSession.getSenha());
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
