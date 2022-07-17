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
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.kennethgarcia.system.Principal;
import java.sql.*;
import javafx.scene.control.Alert;
import org.kennethgarcia.db.Conexion;
import java.util.Base64;

/**
 * FXML Controller class
 *
 * @author Kenneth Gerardo Garcia Lemus
 */
public class LoginController implements Initializable {

    @FXML
    private TextField txtUsuario;
    @FXML
    private Button btnEntrar;
    @FXML
    private PasswordField txtContrasenia;

    private String pass2;
    private String user2;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    private Principal escenarioPrincipal;

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    @FXML
    private void validarUsuario(ActionEvent event) {

        if (!(txtUsuario.getText().isEmpty() || txtUsuario.getText().isEmpty())) {

            if (escenarioPrincipal.getUsuario() != null) {

                if (txtContrasenia.getText().equals(getPassword(txtUsuario.getText().trim()))) {
                    if (escenarioPrincipal.getUsuario().getRol() == 1) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("ERROR");
                        alert.setHeaderText(null);
                        alert.setContentText("Bienvenido" + " " + "Administrador:"
                                + " " + escenarioPrincipal.getUsuario().getNombre());
                        alert.showAndWait();
                        escenarioPrincipal.menuPrincipal();
                    } else {
                        if (txtContrasenia.getText().equals(getPassword(txtUsuario.getText().trim()))) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("ERROR");
                            alert.setHeaderText(null);
                            alert.setContentText("Bienvenido" + " " + "Empleado:"
                                    + " " + escenarioPrincipal.getUsuario().getNombre());
                            alert.showAndWait();
                            escenarioPrincipal.menuUsuario();
                        }

                    }

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setHeaderText(null);
                    alert.setContentText("El usuario o la contraseña son incorrectos.");
                    alert.show();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText("Algunos campos se encuentran vacios.");
            alert.show();
        }
    }

    private String getPassword(String user) {
        String passDesc = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_BuscarUsuario(?)}");
            stmt.setString(1, user);
            rs = stmt.executeQuery();

            while (rs.next()) {
                escenarioPrincipal.getUsuario().setNombre(rs.getString("usuario"));
                escenarioPrincipal.getUsuario().setContrasenia(rs.getString("contrasenia"));
                escenarioPrincipal.getUsuario().setNombre(rs.getString("nombre"));
                escenarioPrincipal.getUsuario().setRol(rs.getInt("rol"));

                passDesc = new String(Base64.getDecoder().decode(escenarioPrincipal.getUsuario().getContrasenia()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return passDesc;
    }

}
