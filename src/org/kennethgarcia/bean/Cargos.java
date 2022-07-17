/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kennethgarcia.bean;

/**
 *
 * @author Kenneth Gerardo Garcia Lemus
 * @date 4/06/2021
 * @time 18:54:14
 */
public class Cargos {

    private int id;
    private String nombre;

    public Cargos() {

    }

    public Cargos(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombreCargo(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }

}
