/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kennethgarcia.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import org.kennethgarcia.system.Principal;

/**
 *
 * @author Kenneth Gerardo Garcia Lemus
 * @date 12/05/2021
 * @time 09:01:33
 */
public class AutorController implements Initializable {

    private Principal escenarioPrincipal;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    @FXML
    public void regresar(MouseEvent event) {
        if (escenarioPrincipal.getUsuario().getRol() == 1) {
            escenarioPrincipal.menuPrincipal();
        } else {
            escenarioPrincipal.menuUsuario();
        }
    }

}
