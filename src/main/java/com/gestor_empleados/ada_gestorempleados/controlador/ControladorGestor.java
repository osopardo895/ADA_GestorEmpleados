package com.gestor_empleados.ada_gestorempleados.controlador;

import com.gestor_empleados.ada_gestorempleados.modelo.Empleado;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class ControladorGestor implements Initializable{
    private Integer id = 0;
    private ObservableList<Empleado> lista = FXCollections.observableArrayList();

    @FXML
    private TableColumn<Empleado, Integer> c1;

    @FXML
    private TableColumn<Empleado, String> c2;

    @FXML
    private TableColumn<Empleado, String> c3;

    @FXML
    private TableColumn<Empleado, String> c4;

    @FXML
    private TableColumn<Empleado, Double> c5;

    @FXML
    private TableView<Empleado> tableView;

    @FXML
    private TextField txfApellidos;

    @FXML
    private TextField txfDepartamento;

    @FXML
    private TextField txfNombre;

    @FXML
    private TextField txfSueldo;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Límites caracteres TextFields
        int maxLengthNombre = 30;
        int maxLengthApellidos = 60;
        int maxLengthDepartamento = 30;
        double maxLengthSueldo = 99999.99;

        // Configurar los TextFormatter que limite el número de caracteres
        txfNombre.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= maxLengthNombre ? change : null
        ));
        txfApellidos.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= maxLengthApellidos ? change : null
        ));
        txfDepartamento.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= maxLengthDepartamento ? change : null
        ));
        // Configurar el TextFormatter para validar el sueldo
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            // Validar si cumple con la expresión regular
            if (newText.matches("([0-9]{0,5}(\\.[0-9]{0,2})?)?")) {
                return change;
            }
            return null;
        };
        // Establecer el TextFormatter con el filtro y valor inicial
        TextFormatter<String> formatter = new TextFormatter<>(filter);
        txfSueldo.setTextFormatter(formatter);
    }


    @FXML
    void insertar(ActionEvent event) {
        Empleado e = new Empleado(this.id, this.txfNombre.getText(), this.txfApellidos.getText(), this.txfDepartamento.getText(), Double.parseDouble(this.txfSueldo.getText()));
        lista.add(e);
        this.id++;
    }

    @FXML
    void borrar(ActionEvent event) {

    }

    @FXML
    void actualizar(ActionEvent event) {

    }



    @FXML
    void exportarJSON(ActionEvent event) {

    }

    @FXML
    void exportarXML(ActionEvent event) {

    }

    @FXML
    void importarJSON(ActionEvent event) {

    }

    @FXML
    void importarXML(ActionEvent event) {

    }



    @FXML
    void exportarFormularioEmpleados(ActionEvent event) {

    }

    @FXML
    void importarFormularioEmpleados(ActionEvent event) {

    }
}
