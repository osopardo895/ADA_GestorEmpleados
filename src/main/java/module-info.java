module com.gestor_empleados.ada_gestorempleados {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;

    opens com.gestor_empleados.ada_gestorempleados to javafx.fxml;
    opens com.gestor_empleados.ada_gestorempleados.controlador to javafx.fxml;
    exports com.gestor_empleados.ada_gestorempleados;
}