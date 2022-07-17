/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kennethgarcia.bean;

import java.sql.Date;
import java.math.BigDecimal;

/**
 *
 * @author Kenneth Gerardo Garcia Lemus
 * @date 6/07/2021
 * @time 16:28:17
 */
public class CuentasPorPagar {

    private int id;

    private String numeroFactura;
    private Date fechaLimitePago;
    private String estadoPago;
    private BigDecimal valorNetoPago;

    private int codigoAdministracion;
    private int codigoProveedor;

    public CuentasPorPagar() {
    }

    public CuentasPorPagar(int id, String numeroFactura, Date fechaLimitePago, String estadoPago, BigDecimal valorNetoPago, int codigoAdministracion, int codigoProveedor) {
        this.id = id;
        this.numeroFactura = numeroFactura;
        this.fechaLimitePago = fechaLimitePago;
        this.estadoPago = estadoPago;
        this.valorNetoPago = valorNetoPago;
        this.codigoAdministracion = codigoAdministracion;
        this.codigoProveedor = codigoProveedor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public Date getFechaLimitePago() {
        return fechaLimitePago;
    }

    public void setFechaLimitePago(Date fechaLimitePago) {
        this.fechaLimitePago = fechaLimitePago;
    }

    public String getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }

    public BigDecimal getValorNetoPago() {
        return valorNetoPago;
    }

    public void setValorNetoPago(BigDecimal valorNetoPago) {
        this.valorNetoPago = valorNetoPago;
    }

    public int getCodigoAdministracion() {
        return codigoAdministracion;
    }

    public void setCodigoAdministracion(int codigoAdministracion) {
        this.codigoAdministracion = codigoAdministracion;
    }

    public int getCodigoProveedor() {
        return codigoProveedor;
    }

    public void setCodigoProveedor(int codigoProveedor) {
        this.codigoProveedor = codigoProveedor;
    }

    @Override
    public String toString() {
        return "ID: "+id + " | " + estadoPago;
    }

}
