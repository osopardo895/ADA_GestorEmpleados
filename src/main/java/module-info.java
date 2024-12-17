module com.gestor_empleados.ada_gestorempleados {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires com.google.gson;
    requires org.apache.commons.lang3;
    requires java.xml;

    opens com.gestor_empleados.ada_gestorempleados to javafx.fxml;
    opens com.gestor_empleados.ada_gestorempleados.controlador to javafx.fxml;
    opens com.gestor_empleados.ada_gestorempleados.modelo to javafx.base;
    exports com.gestor_empleados.ada_gestorempleados;
}