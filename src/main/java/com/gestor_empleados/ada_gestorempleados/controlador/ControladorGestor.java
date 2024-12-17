package com.gestor_empleados.ada_gestorempleados.controlador;

import com.gestor_empleados.ada_gestorempleados.modelo.ArchivoBinario;
import com.gestor_empleados.ada_gestorempleados.modelo.Configuracion;
import com.gestor_empleados.ada_gestorempleados.modelo.Empleado;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javafx.event.ActionEvent;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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
            Alert alert = new Alert(AlertType.WARNING);
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
            mostrarAlerta(AlertType.ERROR, "ERROR", "Por favor, selecciona un empleado para borrar.");
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

        mostrarAlerta(AlertType.INFORMATION, "Borrado Exitoso", "El empleado ha sido eliminado correctamente.");
    }

    @FXML
    void actualizar(ActionEvent event) {
        Empleado empleadoSeleccionado = tableView.getSelectionModel().getSelectedItem();

        if (empleadoSeleccionado == null) {
            mostrarAlerta(AlertType.ERROR, "ERROR", "Por favor, selecciona un empleado para actualizar.");
            return;
        }
        if (comprobarCampos()){
            mostrarAlerta(AlertType.ERROR, "ERROR", "Completa todos los campos e inténtalo de nuevo.");
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

            mostrarAlerta(AlertType.INFORMATION, "Actualización Exitosa", "El empleado ha sido actualizado correctamente.");

        } catch (NumberFormatException e) {
            mostrarAlerta(AlertType.ERROR, "ERROR", "Sueldo debe ser un número válido.");
        }
    }



    @FXML
    void exportarArchivo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exportar Archivo");
        fileChooser.setInitialFileName("archivo");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos JSON", "*.json"),
                new FileChooser.ExtensionFilter("Archivos XML", "*.xml")
        );
        File file = fileChooser.showSaveDialog(txfNombre.getScene().getWindow());
        if (file != null) {
            mostrarAlerta(AlertType.INFORMATION, "Archivo guardado", "El archivo ha sido guardado en:\n" + file.getAbsolutePath());
        }
    }

    @FXML
    void exportarJSON() {
        File file = new File(configuracion.getFicheroJson());

        try (FileWriter writer = new FileWriter(file)) {
            Gson gson = new Gson();
            String json = gson.toJson(tableView.getItems());
            writer.write(json);
            mostrarAlerta(AlertType.INFORMATION, "Éxito", "Empleados exportados a JSON correctamente.");
        } catch (IOException e) {
            mostrarAlerta(AlertType.ERROR, "ERROR", "Error al exportar a JSON: " + e.getMessage());
        }
    }



    @FXML
    void exportarXML() {
        File file = new File(configuracion.getFicheroXml());

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            org.w3c.dom.Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("Empleados");
            doc.appendChild(rootElement);

            for (Empleado e : tableView.getItems()) {
                Element empleado = doc.createElement("Empleado");
                empleado.setAttribute("id", String.valueOf(e.getId()));
                empleado.appendChild(createElement(doc, "Nombre", e.getNombre()));
                empleado.appendChild(createElement(doc, "Apellidos", e.getApellidos()));
                empleado.appendChild(createElement(doc, "Departamento", e.getDepartamento()));
                empleado.appendChild(createElement(doc, "Sueldo", String.valueOf(e.getSueldo())));
                rootElement.appendChild(empleado);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);

            mostrarAlerta(AlertType.INFORMATION, "Éxito", "Empleados exportados a XML correctamente.");
        } catch (Exception e) {
            mostrarAlerta(AlertType.ERROR, "ERROR", "Error al exportar a XML: " + e.getMessage());
        }
    }

    private org.w3c.dom.Element createElement(org.w3c.dom.Document doc, String name, String value) {
        org.w3c.dom.Element element = doc.createElement(name);
        element.appendChild(doc.createTextNode(value));
        return element;
    }


    @FXML
    void importarArchivo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Importar Archivo");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos JSON", "*.json"),
                new FileChooser.ExtensionFilter("Archivos XML", "*.xml")
        );
        File file = fileChooser.showOpenDialog(txfNombre.getScene().getWindow());
        if (file != null) {
            mostrarAlerta(AlertType.INFORMATION, "Archivo seleccionado", "El archivo seleccionado es:\n" + file.getAbsolutePath());
        }
    }

    @FXML
    void importarJSON() {
        File file = new File(configuracion.getFicheroJson());

        try (Reader reader = new FileReader(file)) {
            Gson gson = new Gson();
            Empleado[] empleadosArray = gson.fromJson(reader, Empleado[].class);
            ObservableList<Empleado> empleadosList = FXCollections.observableArrayList(empleadosArray);
            tableView.setItems(empleadosList); // Reemplaza la lista existente

            mostrarAlerta(AlertType.INFORMATION, "Éxito", "Empleados importados desde JSON correctamente.");
        } catch (IOException e) {
            mostrarAlerta(AlertType.ERROR, "ERROR", "Error al importar desde JSON: " + e.getMessage());
        }
    }



    @FXML
    void importarXML() {
        File file = new File(configuracion.getFicheroXml());

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            org.w3c.dom.Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("Empleado");
            ObservableList<Empleado> empleadosList = FXCollections.observableArrayList();

            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    int id = Integer.parseInt(element.getAttribute("id"));
                    String nombre = element.getElementsByTagName("Nombre").item(0).getTextContent();
                    String apellidos = element.getElementsByTagName("Apellidos").item(0).getTextContent();
                    String departamento = element.getElementsByTagName("Departamento").item(0).getTextContent();
                    double sueldo = Double.parseDouble(element.getElementsByTagName("Sueldo").item(0).getTextContent());
                    empleadosList.add(new Empleado(id, nombre, apellidos, departamento, sueldo));
                }
            }

            tableView.setItems(empleadosList);
            mostrarAlerta(AlertType.INFORMATION, "Éxito", "Empleados importados desde XML correctamente.");
        } catch (Exception e) {
            mostrarAlerta(AlertType.ERROR, "ERROR", "Error al importar desde XML: " + e.getMessage());
        }
    }



    @FXML
    void exportarFormularioEmpleados(ActionEvent event) {

    }

    @FXML
    void importarFormularioEmpleados(ActionEvent event) {

    }

    private void mostrarAlerta(AlertType tipo, String titulo, String mensaje) {
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
            mostrarAlerta(AlertType.ERROR, "ERROR", "Error al actualizar el archivo de configuración: " + e.getMessage());
        }
    }

    public void guardarEmpleados() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("empleados.dat"))) {
            // Obtén la lista de empleados de la TableView
            List<Empleado> empleados = new ArrayList<>(tableView.getItems());
            out.writeObject(empleados);
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(AlertType.ERROR, "ERROR", "No se pudo guardar los empleados.");
        }
    }
}
