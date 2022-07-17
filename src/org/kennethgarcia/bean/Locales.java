/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.kennethgarcia.bean;

import java.math.BigDecimal;

/**
 *
 * @author Kenneth Gerardo Garcia Lemus
 * @date 4/06/2021
 * @time 18:54:53
 */
public class Locales {
    
    private int id;
    private BigDecimal saldoFavor;
    private BigDecimal saldoContra;
    private int mesesPendientes;
    private boolean disponibilidad;
    private BigDecimal valorLocal;
    private BigDecimal valorAdministracion;
  //  private BigDecimal saldoLiquido;
    
    
    public Locales(){
    
    }

    public Locales(int id, BigDecimal saldoFavor, BigDecimal saldoContra, int mesesPendientes, boolean disponibilidad, BigDecimal valorLocal, BigDecimal valorAdministracion/*,BigDecimal saldoLiquido*/) {
        this.id = id;
        this.saldoFavor = saldoFavor;
        this.saldoContra = saldoContra;
        this.mesesPendientes = mesesPendientes;
        this.disponibilidad = disponibilidad;
        this.valorLocal = valorLocal;
        this.valorAdministracion = valorAdministracion;
      //  this.saldoLiquido = saldoLiquido;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getSaldoFavor() {
        return saldoFavor;
    }

    public void setSaldoFavor(BigDecimal saldoFavor) {
        this.saldoFavor = saldoFavor;
    }

    public BigDecimal getSaldoContra() {
        return saldoContra;
    }

    public void setSaldoContra(BigDecimal saldoContra) {
        this.saldoContra = saldoContra;
    }

    public int getMesesPendientes() {
        return mesesPendientes;
    }

    public void setMesesPendientes(int mesesPendientes) {
        this.mesesPendientes = mesesPendientes;
    }

    public boolean isDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(boolean disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public BigDecimal getValorLocal() {
        return valorLocal;
    }

    public void setValorLocal(BigDecimal valorLocal) {
        this.valorLocal = valorLocal;
    }

    public BigDecimal getValorAdministracion() {
        return valorAdministracion;
    }

    public void setValorAdministracion(BigDecimal valorAdministracion) {
        this.valorAdministracion = valorAdministracion;
    }

  /*  public BigDecimal getSaldoLiquido() {
        return saldoLiquido;
    }

    public void setSaldoLiquido(BigDecimal saldoLiquido) {
        this.saldoLiquido = saldoLiquido;
    }
    */
    
    
    @Override
    public String toString() {
        String strDisponibilidad;
        if(disponibilidad==true){
        strDisponibilidad="disponible";
        }else{
        strDisponibilidad="no disponible";
        }
        
return "ID: "+id+"|"+"Local "+strDisponibilidad+" | "+"Valor local: Q"+valorLocal
        +" | "+"Valor administracion: Q"+valorAdministracion+" | "+"Meses pendientes: "+mesesPendientes;       
        
    }


    
    
}
