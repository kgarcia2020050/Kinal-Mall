/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kennethgarcia.system;

import com.jfoenix.controls.JFXTimePicker;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.image.Image;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.kennethgarcia.bean.Usuario;
import org.kennethgarcia.controller.AdministracionController;
import org.kennethgarcia.controller.AutorController;
import org.kennethgarcia.controller.CargosController;
import org.kennethgarcia.controller.ClientesController;
import org.kennethgarcia.controller.CuentasPorCobrarController;
import org.kennethgarcia.controller.CuentasPorPagarController;
import org.kennethgarcia.controller.DepartamentosController;
import org.kennethgarcia.controller.EmpleadosController;
import org.kennethgarcia.controller.HorariosController;
import org.kennethgarcia.controller.LocalesController;
import org.kennethgarcia.controller.LoginController;
import org.kennethgarcia.controller.MenuPrincipalController;
import org.kennethgarcia.controller.ProveedoresController;
import org.kennethgarcia.controller.TipoClienteController;
import java.util.Base64;
/**
 *
 * @author Kenneth Gerardo Garcia Lemus
 * @date 5/05/2021
 * @time 11:27:15
 */
public class Principal extends Application {

    private final String direccion = "/org/kennethgarcia/view/";
    private final String direccion2 = "/org/kennethgarcia/resources/";
    private Stage escenarioPrincipal;
    private Scene esc;
    static Usuario usuario;
    
    
    
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.resizableProperty().setValue(Boolean.FALSE);
        usuario=new Usuario();
        this.escenarioPrincipal = stage;
        String password="12345";
        String password64=Base64.getEncoder().encodeToString(password.getBytes());
        System.out.println(password+" = "+password64);
        Login();
    }
    
    

    public static Usuario getUsuario() {
        return usuario;
    }

    public static void setUsuario(Usuario usuario) {
        Principal.usuario = usuario;
    }

    
    
    public boolean validarCorreo(String email) {
        Pattern pattern = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean validarString(String cadena) {
        Pattern pattern = Pattern.compile("([a-zA-z]{1,50})([\\s][a-zA-z]{1,50})?$");
        Matcher matcher = pattern.matcher(cadena);
        return matcher.matches();
    }

    public void menuPrincipal() {
        try {
            MenuPrincipalController menuPrincipal = null;
            menuPrincipal = (MenuPrincipalController) cambiarEscena("MenuPrincipalView.fxml", 960, 540);
            menuPrincipal.setEscenarioPrincipal(this);
        } catch (Exception ex) {
            System.out.println("Se produjo un error al cargar la vista del menú principal");
            ex.printStackTrace();
        }
    }

    public void mostrarEscenarioAutor() {
        try {
            AutorController autorController = (AutorController) cambiarEscena("AutorView.fxml", 688, 387);
            autorController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.out.println("Error al cargar la vista del autor.");
            e.printStackTrace();

        }

    }

    public boolean validarDecimal(String numero) {
        Pattern pattern1 = Pattern.compile("^[0-9]{1,8}([.][0-9]{2})?$");
        Matcher matcher1 = pattern1.matcher(numero);
        return matcher1.matches();

    }

    public boolean validarEntero(String numero) {
        Pattern pattern1 = Pattern.compile("^[0-9]{1,}$");
        Matcher matcher1 = pattern1.matcher(numero);
        return matcher1.matches();

    }

    public boolean validarTelefono(String numero) {
        Pattern pattern1 = Pattern.compile("^[0-9]{8}$");
        Matcher matcher1 = pattern1.matcher(numero);
        return matcher1.matches();

    }

    public boolean validarEstadoPago(String numero) {
        Pattern pattern1 = Pattern.compile("^[0-9]{8}$");
        Matcher matcher1 = pattern1.matcher(numero);
        return matcher1.matches();

    }

    public boolean validar(ArrayList<TextField> listaTextField, ArrayList<ComboBox> listaComboBox) {
        boolean respuesta = true;
        for (ComboBox comboBox : listaComboBox) {
            if (comboBox.getSelectionModel().getSelectedItem() == null) {
                return false;
            }
        }
        for (TextField textField : listaTextField) {
            if (textField.getText().trim().isEmpty()) {
                return false;
            }
        }
        return respuesta;
    }

    public void escenaLocales() {
        try {
            LocalesController local;
            local = (LocalesController) cambiarEscena("LocalesView.fxml", 960, 540);
            local.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.out.println("Error en vista Locales.");
            e.printStackTrace();

        }

    }

    public void escenaCargos() {
        try {
            CargosController cargo;
            cargo = (CargosController) cambiarEscena("CargosView.fxml", 960, 540);
            cargo.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.out.println("Error en vista Cargos");
            e.printStackTrace();

        }

    }

    public void escenaProveedores() {
        try {
            ProveedoresController prov;
            prov = (ProveedoresController) cambiarEscena("ProveedoresView.fxml", 960, 540);
            prov.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.out.println("Error en vista Proveedores");
            e.printStackTrace();

        }

    }

    public void escenaHorarios() {
        try {
            HorariosController horario;
            horario = (HorariosController) cambiarEscena("HorariosView.fxml", 960, 540);
            horario.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.out.println("Error en vista Horarios");
            e.printStackTrace();

        }

    }

    public void escenaCuentasPorPagar() {
        try {
            CuentasPorPagarController cuentasPagar;
            cuentasPagar = (CuentasPorPagarController) cambiarEscena("CuentasPorPagarView.fxml", 960, 540);
            cuentasPagar.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.out.println("Error en vista Cuentas por Pagar");
            e.printStackTrace();

        }

    }

    public void escenaCuentasPorCobrar() {
        try {
            CuentasPorCobrarController cuentasCobrar;
            cuentasCobrar = (CuentasPorCobrarController) cambiarEscena("CuentasPorCobrarView.fxml", 960, 540);
            cuentasCobrar.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.out.println("Error en vista Cuentas por Cobrar");
            e.printStackTrace();

        }

    }

    public void escenaDepartamentos() {
        try {
            DepartamentosController dept;
            dept = (DepartamentosController) cambiarEscena("DepartamentosView.fxml", 960, 540);
            dept.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.out.println("Error en vista Departamentos");
            e.printStackTrace();

        }

    }

    public void escenaEmpleados() {
        try {
            EmpleadosController empleado;
            empleado = (EmpleadosController) cambiarEscena("EmpleadosView.fxml", 960, 540);
            empleado.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.err.println("Error en vista Empleados.");
            e.printStackTrace();

        }

    }

    public void escenaTipoCliente() {
        try {
            TipoClienteController tipoC;
            tipoC = (TipoClienteController) cambiarEscena("TipoClienteView.fxml", 960, 540);
            tipoC.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.out.println("Error en vista Tipo Clientes.");
            e.printStackTrace();

        }

    }

    public void escenaClientes() {
        try {
            ClientesController clienteC;
            clienteC = (ClientesController) cambiarEscena("ClientesView.fxml", 960, 540);
            clienteC.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.out.println("Error en vista de Clientes.");
            e.printStackTrace();
        }

    }

    public void escenaAdministracion() {
        try {
            AdministracionController adminC;
            adminC = (AdministracionController) cambiarEscena("PlantillaView.fxml", 960, 540);
            adminC.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.out.println("Error en vista Administracion.");
            e.printStackTrace();
        }

    }

    public void Login() {
        try {
            LoginController log;
            log = (LoginController) cambiarEscena("LoginView.fxml", 960, 540);
            log.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.out.println("Error al cargar el Login.");
            e.printStackTrace();

        }

    }
    
    
    public void menuUsuario() {
        try {
            MenuPrincipalController menuPrincipal = null;
            menuPrincipal = (MenuPrincipalController) cambiarEscena("MenuUsuarioView.fxml", 960, 540);
            menuPrincipal.setEscenarioPrincipal(this);
        } catch (Exception ex) {
            System.out.println("Se produjo un error al cargar la vista del usuario");
            ex.printStackTrace();
        }
    }

    public Initializable cambiarEscena(String viewFXML, int ancho, int alto) throws IOException {
        Initializable resultado = null;

        FXMLLoader cargadorFXML = new FXMLLoader();

        InputStream archivo = Principal.class.getResourceAsStream(direccion + viewFXML);

        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(direccion + viewFXML));

        esc = new Scene((AnchorPane) cargadorFXML.load(archivo), ancho, alto);
        escenarioPrincipal.setScene(esc);
        escenarioPrincipal.sizeToScene();

        escenarioPrincipal.show();

        resultado = (Initializable) cargadorFXML.getController();

        return resultado;

    }

}
