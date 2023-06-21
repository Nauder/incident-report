package com.utfpr.distributed.controller;

import com.utfpr.distributed.model.Incidente;
import com.utfpr.distributed.model.IncidenteRow;
import com.utfpr.distributed.util.TipoIncidente;
import com.utfpr.distributed.util.socket.ClientSocketConnectionHandler;
import com.utfpr.distributed.validation.IncidenteValidator;
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
        this.openNewWindow(event, "incidente-create-view.fxml", "criar");
    }

    @FXML
    protected void onEditarButtonClick(ActionEvent event) {
        IncidenteRow row = this.tIncidentes.getSelectionModel().getSelectedItem();
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
        this.openNewWindow(event, "incidente-edit-view.fxml", "alterar");
    }

    @FXML
    protected void onBuscarButtonClick(ActionEvent event) {
        final Map<String, Object> inputData = new HashMap<>();

        if (this.areTextFieldsPopulated(tEstado, tCidade) && this.isDatePickerPopulated(dData)) {
            if (this.validateSearchFields()) {
                inputData.put("operacao", 4);
                inputData.put("data", this.dData.getValue().toString());
                inputData.put("estado", this.tEstado.getText().toUpperCase());
                inputData.put("cidade", this.tCidade.getText().toUpperCase());

                JSONObject response = ClientSocketConnectionHandler.run(inputData);

                if (response.query("/status") != "OK") {
                    this.lErro.setText((String) response.query("/status"));
                    this.lInfo.setText("");
                } else {
                    this.tIncidentes.setItems(this.getList(response));
                    this.bEditar.setDisable(true);
                    this.bRemover.setDisable(true);
                    this.lInfo.setText("Incidentes buscados com sucesso");
                    this.lErro.setText("");
                }
            }
        } else {
            this.lErro.setText("Campo(s) obrigatóio(s) em branco");
            this.lInfo.setText("");
        }
    }

    @FXML
    protected void onBuscarMeusButtonClick(ActionEvent event) {
        final Map<String, Object> inputData = new HashMap<>();

        inputData.put("operacao", 5);

        JSONObject response = ClientSocketConnectionHandler.run(inputData);

        if (response.query("/status") != "OK") {
            this.lErro.setText((String) response.query("/status"));
            this.lInfo.setText("");
        } else {
            this.tIncidentes.setItems(getList(response));
            this.bEditar.setDisable(false);
            this.bRemover.setDisable(false);
            this.lInfo.setText("Meus incidentes buscados com sucesso");
            this.lErro.setText("");
        }
    }

    @FXML
    protected void onRemoverButtonClick(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Remoção");
        alert.setHeaderText("Isto permanentemente removera o incidente.");
        alert.setContentText("Tem certeza que deseja continuar?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {

            final Map<String, Object> inputData = new HashMap<>();

            IncidenteRow row = this.tIncidentes.getSelectionModel().getSelectedItem();

            inputData.put("operacao", 6);
            inputData.put("id_incidente", row.getId_incidente());

            JSONObject response = ClientSocketConnectionHandler.run(inputData);

            if (response.query("/status") != "OK") {
                this.lErro.setText((String) response.query("/status"));
                this.lInfo.setText("");
            } else {
                this.onBuscarMeusButtonClick(event);
            }
        }
    }

    @FXML
    protected void onSubmit(ActionEvent event) {
        if (this.areTextFieldsPopulated(this.tHora, this.tEstado, this.tCidade, this.tBairro, this.tRua)
                && isChoicePopulated(this.cTipo) && isDatePickerPopulated(this.dData)) {
            if (this.validateSubmitFields()) {

                final Map<String, Object> inputData = new HashMap<>();
                inputData.put("operacao", 7);
                inputData.put("data", this.dData.getValue().toString());
                inputData.put("hora", this.tHora.getText());
                inputData.put("estado", this.tEstado.getText().toUpperCase());
                inputData.put("cidade", this.tCidade.getText().toUpperCase());
                inputData.put("bairro", this.tBairro.getText().toUpperCase());
                inputData.put("rua", this.tRua.getText().toUpperCase());
                inputData.put("tipo", this.cTipo.getValue().getCodigo());

                JSONObject response = ClientSocketConnectionHandler.run(inputData);
                if (response.query("/status") != "OK") {
                    this.lErro.setText((String) response.query("/status"));
                    this.lInfo.setText("");
                } else {
                    this.lInfo.setText("Incidente cadastrado com sucesso");
                    this.lErro.setText("");
                }
            }
        } else {
            this.lErro.setText("Campo(s) obrigatóio(s) em branco");
            this.lInfo.setText("");
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

    private boolean validateSubmitFields() {

        if (!IncidenteValidator.checkData(dData.getValue().toString())) {
            this.lErro.setText("Formato de data inválido");
        } else if (!IncidenteValidator.checkEstado(tEstado.getText())) {
            this.lErro.setText("Formato de estado inválido");
        } else if (!IncidenteValidator.checkHora(tHora.getText())) {
            this.lErro.setText("Formato de hora inválido");
        } else if (!IncidenteValidator.checkLocal(tBairro.getText())) {
            this.lErro.setText("Formato de bairro inválido");
        } else if (!IncidenteValidator.checkLocal(tRua.getText())) {
            this.lErro.setText("Formato de rua inválido");
        } else if (!IncidenteValidator.checkLocal(tCidade.getText())) {
            this.lErro.setText("Formato de cidade inválido");
        } else {
            return true;
        }

        return false;
    }

    private boolean validateSearchFields() {

        if (!IncidenteValidator.checkData(this.dData.getValue().toString())) {
            this.lErro.setText("Formato de data inválido");
        } else if (!IncidenteValidator.checkEstado(this.tEstado.getText())) {
            this.lErro.setText("Formato de estado inválido");
        } else if (!IncidenteValidator.checkLocal(this.tCidade.getText())) {
            this.lErro.setText("Formato de cidade inválido");
        } else {
            return true;
        }

        return false;
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

        this.tIncidentes.getColumns().setAll(
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
        this.cTipo.setItems(FXCollections.observableList(Arrays.stream(TipoIncidente.values()).toList()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (this.tIncidentes != null) {
            this.initializeTable();
        }
        if (this.cTipo != null) {
            this.initializeChoiceBox();
        }
    }
}
