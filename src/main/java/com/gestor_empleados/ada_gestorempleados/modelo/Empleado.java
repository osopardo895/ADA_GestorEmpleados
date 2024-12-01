package com.gestor_empleados.ada_gestorempleados.modelo;

/**
 *
 * @author Alejandro Fernández Martínez
 */
public class Empleado {
    private Integer id;
    private String nombre;
    private String apellidos;
    private String departamento;
    private double sueldo;
    public Empleado(Integer id, String nombre, String apellidos, String departamento, double sueldo){
        this.id=id;
        this.nombre=nombre;
        this.apellidos=apellidos;
        this.departamento=departamento;
        this.sueldo=sueldo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getDepartamento() {
        return departamento;
    }

    public double getSueldo() {
        return sueldo;
    }


}
