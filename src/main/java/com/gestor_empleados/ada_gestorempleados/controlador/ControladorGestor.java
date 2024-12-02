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
import javafx.scene.control.*;

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

    @FXML
    private Label txtInfo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    private boolean comprobarCampos(){
        // Límites caracteres TextFields
        int maxLengthNombre = 30;
        int maxLengthApellidos = 60;
        int maxLengthDepartamento = 30;
        double maxLengthSueldo = 99999.99;

        String info = "Info: ";
        boolean correcto = true;
        if (txfNombre.getLength() >= maxLengthNombre){
            info+= "longitud nombre debe ser inferior a "+maxLengthNombre+"carácteres, ";
            correcto=false;
        } else if (txfNombre.getLength() == 0) {
            info+= "campo nombre está vacío, ";
            correcto=false;
        }
        if (txfApellidos.getLength() >= maxLengthApellidos){
            info+= "longitud apellidos ser inferior a "+maxLengthApellidos+"carácteres, ";
            correcto=false;
        } else if (txfApellidos.getLength() == 0) {
            info+= "campo apellidos está vacío, ";
            correcto=false;
        }
        if (txfDepartamento.getLength() >= maxLengthDepartamento){
            info+= "longitud departamento ser inferior a "+maxLengthDepartamento+"carácteres, ";
            correcto=false;
        } else if (txfDepartamento.getLength() == 0) {
            info+= "campo departamento está vacío, ";
            correcto=false;
        }
        if (txfSueldo.getLength() == 0){
            info+= "campo sueldo está vacío ";
            correcto=false;
        }else {
            try {
                if (Double.parseDouble(txfSueldo.getText()) >= maxLengthSueldo || Double.parseDouble(txfSueldo.getText()) <= 0){
                    info+= "sueldo debe ser entre 0 y "+maxLengthSueldo+" euros";
                    correcto=false;
                }
            }catch (NumberFormatException e){
                System.out.println("Error: "+e.getMessage());
                info+= "sueldo no puede tener letras";
                correcto=false;
            }
        }

        txtInfo.setText(info);
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText("Datos erroneos:");
        alert.setContentText(info);
        alert.showAndWait();
        return correcto;
    }


    @FXML
    void insertar(ActionEvent event) {
        if (comprobarCampos()){
            Empleado e = new Empleado(this.id, this.txfNombre.getText(), this.txfApellidos.getText(), this.txfDepartamento.getText(), Double.parseDouble(this.txfSueldo.getText()));
            lista.add(e);
            this.id++;
        }
    }

    @FXML
    void borrar(ActionEvent event) {

    }

    @FXML
    void actualizar(ActionEvent event) {
        if (comprobarCampos()){

        }
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
