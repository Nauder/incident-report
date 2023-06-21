package com.utfpr.distributed.controller;

import com.utfpr.distributed.model.Incidente;
import com.utfpr.distributed.util.TipoIncidente;
import com.utfpr.distributed.util.socket.ClientSocketConnectionHandler;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.json.JSONObject;

import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class IncidenteEditController extends BaseController implements Initializable {

    @FXML
    private Label lErro;
    @FXML
    private ChoiceBox<TipoIncidente> cTipo;
    @FXML
    private DatePicker dData;
    @FXML
    private TextField tHora;
    @FXML
    private TextField tEstado;
    @FXML
    private TextField tCidade;
    @FXML
    private TextField tBairro;
    @FXML
    private TextField tRua;

    private static Incidente incidente;

    @FXML
    protected void onSubmit(ActionEvent event) {
        final Map<String, Object> deleteData = new HashMap<>();

        deleteData.put("operacao", 6);
        deleteData.put("id_incidente", incidente.getId_incidente());

        JSONObject deleteResponse = ClientSocketConnectionHandler.run(deleteData);

        if(deleteResponse.query("/status") != "OK") {
            lErro.setText((String) deleteResponse.query("/status"));
        } else {
            final Map<String, Object> inputData = new HashMap<>();

            inputData.put("operacao", 7);
            inputData.put("data", dData.getValue().toString());
            inputData.put("hora", tHora.getText());
            inputData.put("estado", tEstado.getText().toUpperCase());
            inputData.put("cidade", tCidade.getText().toUpperCase());
            inputData.put("bairro", tBairro.getText().toUpperCase());
            inputData.put("rua", tRua.getText().toUpperCase());
            inputData.put("tipo", cTipo.getValue().getCodigo());

            JSONObject response = ClientSocketConnectionHandler.run(inputData);

            if(response.query("/status") != "OK") {
                lErro.setText((String) response.query("/status"));
            }
        }
    }

    @FXML
    protected void onReturnEdit(ActionEvent event) {
        openNewWindow(event, "incidente-list-view.fxml", "incidentes");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeIncidenteFields();
        initializeChoiceBox();
    }

    private void initializeIncidenteFields() {

        this.dData.setValue(LocalDate.parse(incidente.getData()));
        this.tBairro.setText(incidente.getBairro());
        this.tCidade.setText(incidente.getCidade());
        this.tRua.setText(incidente.getRua());
        this.tEstado.setText(incidente.getEstado());
        this.tHora.setText(incidente.getHora());
        this.cTipo.setValue(TipoIncidente.getFromCodigo(incidente.getTipo_incidente()));
    }

    private void initializeChoiceBox() {
        cTipo.setItems(FXCollections.observableList(Arrays.stream(TipoIncidente.values()).toList()));
    }

    public static Incidente getIncidente() {

        return incidente;
    }

    public static void setIncidente(Incidente novoIncidente) {

        incidente = novoIncidente;
    }
}
