package com.utfpr.distributed.controller;

import com.utfpr.distributed.model.Incidente;
import com.utfpr.distributed.model.IncidenteRow;
import com.utfpr.distributed.util.TipoIncidente;
import com.utfpr.distributed.util.socket.ClientSocketConnectionHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.json.JSONObject;

import java.net.URL;
import java.util.*;

public class IncidenteController extends BaseController implements Initializable {
    @FXML
    private Label lErro;
    @FXML
    private Label lInfo;
    @FXML
    private TableView<IncidenteRow> tIncidentes;
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
    @FXML
    private Button bEditar;
    @FXML
    private Button bRemover;

    @FXML
    protected void onCriarButtonClick(ActionEvent event) {
        openNewWindow(event, "incidente-create-view.fxml", "criar");
    }

    @FXML
    protected void onEditarButtonClick(ActionEvent event) {
        IncidenteRow row = tIncidentes.getSelectionModel().getSelectedItem();
        IncidenteEditController.setIncidente(new Incidente(
                row.getId_incidente(),
                row.getData(),
                row.getHora(),
                row.getEstado(),
                row.getCidade(),
                row.getBairro(),
                row.getRua(),
                TipoIncidente.getCodigoFromTexto(row.getTipo_incidente())
        ));
        openNewWindow(event, "incidente-edit-view.fxml", "alterar");
    }

    @FXML
    protected void onBuscarButtonClick(ActionEvent event) {
        final Map<String, Object> inputData = new HashMap<>();

        inputData.put("operacao", 4);
        inputData.put("data", dData.getValue().toString());
        inputData.put("estado", tEstado.getText().toUpperCase());
        inputData.put("cidade", tCidade.getText().toUpperCase());

        JSONObject response = ClientSocketConnectionHandler.run(inputData);

        if(response.query("/status") != "OK") {
            lErro.setText((String) response.query("/status"));
            lInfo.setText("");
        } else {
            tIncidentes.setItems(getList(response));
            bEditar.setDisable(true);
            bRemover.setDisable(true);
            lInfo.setText("Incidentes buscados com sucesso");
            lErro.setText("");
        }
    }

    @FXML
    protected void onBuscarMeusButtonClick(ActionEvent event) {
        final Map<String, Object> inputData = new HashMap<>();

        inputData.put("operacao", 5);

        JSONObject response = ClientSocketConnectionHandler.run(inputData);

        if(response.query("/status") != "OK") {
            lErro.setText((String) response.query("/status"));
            lInfo.setText("");
        } else {
            tIncidentes.setItems(getList(response));
            bEditar.setDisable(false);
            bRemover.setDisable(false);
            lInfo.setText("Meus incidentes buscados com sucesso");
            lErro.setText("");
        }
    }

    @FXML
    protected void onRemoverButtonClick(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Remoção");
        alert.setHeaderText("Isto permanentemente removera o incidente.");
        alert.setContentText("Tem certeza que deseja continuar?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            final Map<String, Object> inputData = new HashMap<>();

            IncidenteRow row = tIncidentes.getSelectionModel().getSelectedItem();

            inputData.put("operacao", 6);
            inputData.put("id_incidente", row.getId_incidente());

            JSONObject response = ClientSocketConnectionHandler.run(inputData);

            if(response.query("/status") != "OK") {
                lErro.setText((String) response.query("/status"));
                lInfo.setText("");
            } else {
                onBuscarMeusButtonClick(event);
            }
        }
    }

    @FXML
    protected void onSubmit(ActionEvent event) {

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
            lInfo.setText("");
        } else {
            lInfo.setText("Incidente cadastrado com sucesso");
            lErro.setText("");
        }
    }

    @FXML
    protected void onReturnCreate(ActionEvent event) {
        openNewWindow(event, "incidente-list-view.fxml", "incidentes");
    }

    @FXML
    protected void onReturnList(ActionEvent event) {
        openNewWindow(event, "operacao-menu-view.fxml", "menu");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if(this.tIncidentes != null) {
            initializeTable();
        }

        if(this.cTipo != null) {
            initializeChoiceBox();
        }
    }

    private ObservableList<IncidenteRow> getList(JSONObject response) {

        List<IncidenteRow> incidentes = new ArrayList<>();

        response.getJSONArray("incidentes").toList().forEach(((entry) -> {
            Map<String, Object> map = (Map<String, Object>) entry;

            incidentes.add(new IncidenteRow(
                    (Integer) map.get("id_incidente"),
                    (String) map.get("data"),
                    (String) map.get("hora"),
                    (String) map.get("estado"),
                    (String) map.get("cidade"),
                    (String) map.get("bairro"),
                    (String) map.get("rua"),
                    TipoIncidente.getTextoFromCodigo((Integer) map.get("tipo_incidente"))
            ));
        }));

        return FXCollections.observableArrayList(incidentes);
    }

    private void initializeTable() {

        TableColumn<IncidenteRow, Integer> idCol = new TableColumn<>("#");
        TableColumn<IncidenteRow, String> dataCol = new TableColumn<>("Data");
        TableColumn<IncidenteRow, String> horaCol = new TableColumn<>("Hora");
        TableColumn<IncidenteRow, String> estadoCol = new TableColumn<>("Estado");
        TableColumn<IncidenteRow, String> cidadeCol = new TableColumn<>("Cidade");
        TableColumn<IncidenteRow, String> bairroCol = new TableColumn<>("Bairro");
        TableColumn<IncidenteRow, String> ruaCol = new TableColumn<>("Rua");
        TableColumn<IncidenteRow, String> tipoCol = new TableColumn<>("Tipo de Incidente");

        idCol.setCellValueFactory(new PropertyValueFactory<>("id_incidente"));
        dataCol.setCellValueFactory(new PropertyValueFactory<>("data"));
        horaCol.setCellValueFactory(new PropertyValueFactory<>("hora"));
        estadoCol.setCellValueFactory(new PropertyValueFactory<>("estado"));
        cidadeCol.setCellValueFactory(new PropertyValueFactory<>("cidade"));
        bairroCol.setCellValueFactory(new PropertyValueFactory<>("bairro"));
        ruaCol.setCellValueFactory(new PropertyValueFactory<>("rua"));
        tipoCol.setCellValueFactory(new PropertyValueFactory<>("tipo_incidente"));

        tIncidentes.getColumns().setAll(
                idCol,
                dataCol,
                horaCol,
                estadoCol,
                cidadeCol,
                bairroCol,
                ruaCol,
                tipoCol
        );
    }

    private void initializeChoiceBox() {
        cTipo.setItems(FXCollections.observableList(Arrays.stream(TipoIncidente.values()).toList()));
    }
}
