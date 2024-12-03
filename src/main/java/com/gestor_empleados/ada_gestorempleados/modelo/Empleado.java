package com.gestor_empleados.ada_gestorempleados.modelo;

/**
 *
 * @author Alejandro Fernández Martínez
 */
public class Empleado {
    private int id;
    private String nombre;
    private String apellidos;
    private String departamento;
    private double sueldo;
    public Empleado(int id, String nombre, String apellidos, String departamento, double sueldo){
        this.id=id;
        this.nombre=nombre;
        this.apellidos=apellidos;
        this.departamento=departamento;
        this.sueldo=sueldo;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public double getSueldo() {
        return sueldo;
    }

    public void setSueldo(double sueldo) {
        this.sueldo = sueldo;
    }
}
