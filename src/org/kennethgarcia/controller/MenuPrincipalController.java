/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kennethgarcia.controller;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import org.kennethgarcia.system.Principal;

/**
 *
 * @author Kenneth Gerardo Garcia Lemus
 * @date 5/05/2021
 * @time 11:42:50
 */
public class MenuPrincipalController implements Initializable {

    private Principal escenarioPrincipal;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    @FXML
    private void mostrarVistaAutor(ActionEvent event) {
        this.escenarioPrincipal.mostrarEscenarioAutor();
    }

    @FXML
    private void mostrarVistaAdministracion(ActionEvent event) {
        this.escenarioPrincipal.escenaAdministracion();
    }

    @FXML
    private void mostrarVistaClientes(ActionEvent event) {
        this.escenarioPrincipal.escenaClientes();
    }

    @FXML
    private void mostrarVistaEmpleados(ActionEvent event) {
        this.escenarioPrincipal.escenaEmpleados();
    }

    @FXML
    private void mostrarProveedores(ActionEvent event) {
        this.escenarioPrincipal.escenaProveedores();
    }

    @FXML
    private void btnRegresar(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Advertencia");
        alert.setHeaderText(null);
        alert.setContentText("¿Está seguro que desea salir?");

        Optional<ButtonType> opcion = alert.showAndWait();
        if (opcion.get() == ButtonType.OK) {
            this.escenarioPrincipal.getUsuario().setRol(0);
            this.escenarioPrincipal.Login();
        }
    }

}
