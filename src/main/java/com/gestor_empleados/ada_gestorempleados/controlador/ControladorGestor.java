package com.gestor_empleados.ada_gestorempleados.controlador;

import com.gestor_empleados.ada_gestorempleados.modelo.ArchivoBinario;
import com.gestor_empleados.ada_gestorempleados.modelo.Configuracion;
import com.gestor_empleados.ada_gestorempleados.modelo.Empleado;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class ControladorGestor implements Initializable{

    private Configuracion configuracion = new Configuracion();
    // Ruta al archivo config.properties
    String filePath = "src/resources/config.properties";
    private int id = 0;
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
        System.out.println("Vista FXML Cargada");

        ObservableList<Empleado> empleados = FXCollections.observableArrayList();
        ArchivoBinario archivoBinario = new ArchivoBinario();
        empleados.addAll(archivoBinario.leerEmpleados(configuracion.getFicheroBinario()));

        c1.setCellValueFactory(new PropertyValueFactory<>("id"));
        c2.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        c3.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        c4.setCellValueFactory(new PropertyValueFactory<>("departamento"));
        c5.setCellValueFactory(new PropertyValueFactory<>("sueldo"));
        tableView.setItems(lista);

        tableView.setItems(empleados);
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                txfNombre.setText(newValue.getNombre());
                txfApellidos.setText(newValue.getApellidos());
                txfDepartamento.setText(newValue.getDepartamento());
                txfSueldo.setText(String.valueOf(newValue.getSueldo()));
            }
        });
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
        if (!correcto){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("Datos erroneos:");
            alert.setContentText(info);
            alert.showAndWait();
        }
        return correcto;
    }


    @FXML
    void insertar(ActionEvent event) {
        if (comprobarCampos()){
            int ultimoId = configuracion.getIdEmpleado();
            Empleado nuevoEmpleado = new Empleado(ultimoId + 1, this.txfNombre.getText(), this.txfApellidos.getText(), this.txfDepartamento.getText(), Double.parseDouble(this.txfSueldo.getText()));
            tableView.getItems().add(nuevoEmpleado);

            // Actualizar el último ID en la configuración
            configuracion.setIdEmpleado(ultimoId + 1);
            actualizarUltimoId(ultimoId + 1);

            // Limpiar campos
            txfNombre.clear();
            txfApellidos.clear();
            txfDepartamento.clear();
            txfSueldo.clear();
        }
    }

    @FXML
    void borrar(ActionEvent event) {
        Empleado empleadoSeleccionado = tableView.getSelectionModel().getSelectedItem();

        if (empleadoSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "Por favor, selecciona un empleado para borrar.");
            return;
        }

        int index = tableView.getItems().indexOf(empleadoSeleccionado);
        tableView.getItems().remove(index);

        // Guardar cambios en el archivo binario
        guardarEmpleados();

        // Limpiar campos
        txfNombre.clear();
        txfApellidos.clear();
        txfDepartamento.clear();
        txfSueldo.clear();

        mostrarAlerta(Alert.AlertType.INFORMATION, "Borrado Exitoso", "El empleado ha sido eliminado correctamente.");
    }

    @FXML
    void actualizar(ActionEvent event) {
        Empleado empleadoSeleccionado = tableView.getSelectionModel().getSelectedItem();

        if (empleadoSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "Por favor, selecciona un empleado para actualizar.");
            return;
        }
        if (comprobarCampos()){
            mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "Completa todos los campos e inténtalo de nuevo.");
            return;
        }

        try {
            String nombre = txfNombre.getText();
            String apellidos = txfApellidos.getText();
            String departamento = txfDepartamento.getText();
            Double sueldo = Double.parseDouble(txfSueldo.getText());

            // Crear un nuevo empleado con los datos actualizados
            Empleado nuevoEmpleado = new Empleado(empleadoSeleccionado.getId(), nombre, apellidos, departamento, sueldo);
            int index = tableView.getItems().indexOf(empleadoSeleccionado);
            tableView.getItems().set(index, nuevoEmpleado);

            // Guardar cambios en el archivo binario
            guardarEmpleados();

            // Limpiar campos
            txfNombre.clear();
            txfApellidos.clear();
            txfDepartamento.clear();
            txfSueldo.clear();

            mostrarAlerta(Alert.AlertType.INFORMATION, "Actualización Exitosa", "El empleado ha sido actualizado correctamente.");

        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "Sueldo debe ser un número válido.");
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

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void actualizarUltimoId(int nuevoId) {
        configuracion.setIdEmpleado(nuevoId);

        Properties properties = new Properties();
        properties.setProperty("fichero_binario", configuracion.getFicheroBinario());
        properties.setProperty("fichero_xml", configuracion.getFicheroXml());
        properties.setProperty("fichero_json", configuracion.getFicheroJson());
        properties.setProperty("id_empleado", String.valueOf(nuevoId));

        String rutaRelativa = System.getProperty("user.dir") + "/config.properties";
        try (OutputStream output = new FileOutputStream(rutaRelativa)) {
            properties.store(output, null);
        } catch (IOException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "Error al actualizar el archivo de configuración: " + e.getMessage());
        }
    }

    public void guardarEmpleados() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("empleados.dat"))) {
            // Obtén la lista de empleados de la TableView
            List<Empleado> empleados = new ArrayList<>(tableView.getItems());
            out.writeObject(empleados);
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "No se pudo guardar los empleados.");
        }
    }
}
