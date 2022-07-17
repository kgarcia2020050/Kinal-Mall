/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kennethgarcia.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import org.kennethgarcia.system.Principal;
import java.sql.SQLException;
import org.kennethgarcia.db.Conexion;
import java.sql.ResultSet;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import org.kennethgarcia.bean.Administracion;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.kennethgarcia.report.GenerarReporte;

/**
 *
 * @author Kenneth Gerardo Garcia Lemus
 * @date 13/05/2021
 * @time 10:30:41
 */
public class AdministracionController implements Initializable {

    @FXML
    private ImageView imgNuevo;
    @FXML
    private ImageView imgEditar;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private ImageView imgReporte;
    @FXML
    private Button btnDepartamentos;
    @FXML
    private Button btnCargos;
    @FXML
    private Button btnTipoClientes;
    @FXML
    private Button btnLocales;
    @FXML
    private ImageView imgDepartamentos;
    @FXML
    private ImageView imgTipoCliente;
    @FXML
    private ImageView imgCargos;
    @FXML
    private ImageView imgLocales;
    @FXML
    private ImageView imgRegresar;

    @FXML
    private void mostrarVistaDepartamentos(ActionEvent event) {
        escenarioPrincipal.escenaDepartamentos();
    }

    @FXML
    private void mostrarVistaCargos(ActionEvent event) {
        escenarioPrincipal.escenaCargos();
    }

    @FXML
    private void mostrarVistaTipoClientes(ActionEvent event) {
        escenarioPrincipal.escenaTipoCliente();
    }

    @FXML
    private void mostrarVistaLocales(ActionEvent event) {
        escenarioPrincipal.escenaLocales();
    }

    private enum Operaciones {
        NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO

    }

    public boolean existeElementoSeleccionado() {
        return tblAdministracion.getSelectionModel().getSelectedItem() != null;
    }

    private Operaciones operacion = Operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<Administracion> listaAdministracion;

    @FXML
    private Button btnNuevo;

    @FXML
    private Button btnModificar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnReporte;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtDireccion;

    @FXML
    private TextField txtTelefono;

    @FXML
    private TableView tblAdministracion;

    @FXML
    private TableColumn colId;

    @FXML
    private TableColumn colDireccion;

    @FXML
    private TableColumn colTelefono;

    private Button btnGuardar;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        cargarDatos();
    }
    private final String direccion = "/org/kennethgarcia/resources/image/";

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    @FXML
    public void regresar(MouseEvent event) {
        escenarioPrincipal.menuPrincipal();
    }

    public ObservableList<Administracion> getAdministracion() {
        ArrayList<Administracion> listado = new ArrayList<>();
        try {
            PreparedStatement stmt;
            //CallableStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarAdministracion()}");
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                listado.add(new Administracion(
                        resultado.getInt("id"),
                        resultado.getString("direccion"),
                        resultado.getString("telefono")));
            }
            /* listado.forEach(elemento->{
                    System.out.println(elemento);
        });*/
            resultado.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        listaAdministracion = FXCollections.observableArrayList(listado);
        return listaAdministracion;
    }

    public void eliminarAdministracion() {
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarAdministracion(?)}");
            stmt.setInt(1, Integer.parseInt(txtId.getText()));
            stmt.execute();
            cargarDatos();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1451) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("KINAL MALL");
                alert.setHeaderText(null);
                alert.setContentText("Antes de eliminar esta administracion debe de eliminar todos sus registros relacionados."
                        + "\n Cuentas por cobrar, Cuentas por pagar o Empleados.");
                alert.show();
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editarAdministracion() {
        Administracion registro = (Administracion) tblAdministracion.getSelectionModel().getSelectedItem();
        registro.setId(Integer.parseInt(txtId.getText()));
        registro.setDireccion(txtDireccion.getText());
        registro.setTelefono(txtTelefono.getText());

        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarAdministracion(?,?,?)}");
            stmt.setInt(1, registro.getId());
            stmt.setString(2, registro.getDireccion());
            stmt.setString(3, registro.getTelefono());
            //stmt.executeUpdate();
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void cargarDatos() {
        tblAdministracion.setItems(getAdministracion());
        colId.setCellValueFactory(new PropertyValueFactory<Administracion, Integer>("id"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Administracion, String>("direccion"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Administracion, String>("telefono"));
    }

    @FXML
    public void seleccionarElemento() {
        if (existeElementoSeleccionado()) {
            txtId.setText(String.valueOf(((Administracion) tblAdministracion.getSelectionModel().getSelectedItem()).getId()));
            txtDireccion.setText(((Administracion) tblAdministracion.getSelectionModel().getSelectedItem()).getDireccion());
            txtTelefono.setText(((Administracion) tblAdministracion.getSelectionModel().getSelectedItem()).getTelefono());
        } else {
        }
    }

    private void habilitarDatos() {
        txtDireccion.setEditable(true);
        txtTelefono.setEditable(true);
    }

    private void agregarAdministracion() {
        Administracion registro = new Administracion();
        registro.setDireccion(txtDireccion.getText());
        registro.setTelefono(txtTelefono.getText());

        try {
//   CallableStatement stmt;
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarAdministracion(?,?)}");
            stmt.setString(1, registro.getDireccion());
            stmt.setString(2, registro.getTelefono());
            // stmt.executeUpdate();
            stmt.execute();
            cargarDatos();
            limpiarTexto();
            deshabilitarTexto();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void limpiarTexto() {
        txtId.clear();
        txtDireccion.clear();
        txtTelefono.clear();

    }

    public void deshabilitarTexto() {
        txtDireccion.setEditable(false);
        txtTelefono.setEditable(false);
    }

    @FXML
    void nuevo(ActionEvent event) {
        switch (operacion) {
            case NINGUNO:
                habilitarDatos();
                limpiarTexto();
                btnNuevo.setText("Guardar");
                imgNuevo.setImage(new Image(direccion + "salvar.png"));
                btnModificar.setText("Cancelar");
                imgEditar.setImage(new Image(direccion + "boton-x.png"));
                btnEliminar.setDisable(true);
                imgEliminar.setOpacity(0.15);
                btnReporte.setDisable(true);
                imgReporte.setOpacity(0.15);
                btnCargos.setDisable(true);
                imgCargos.setOpacity(0.15);
                btnDepartamentos.setDisable(true);
                imgDepartamentos.setOpacity(0.15);
                btnLocales.setDisable(true);
                imgLocales.setOpacity(0.15);
                btnTipoClientes.setDisable(true);
                imgTipoCliente.setOpacity(0.15);
                tblAdministracion.setDisable(true);
                imgRegresar.setDisable(true);
                imgRegresar.setOpacity(0.15);
                operacion = Operaciones.GUARDAR;
                break;
            case GUARDAR:
                ArrayList<TextField> listaCampos = new ArrayList<>();
                listaCampos.add(txtDireccion);
                listaCampos.add(txtTelefono);
                ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                if (escenarioPrincipal.validar(listaCampos, listaComboBox)) {
                    if (escenarioPrincipal.validarTelefono(txtTelefono.getText())) {
                        agregarAdministracion();
                        cargarDatos();
                        limpiarTexto();
                        deshabilitarTexto();
                        btnNuevo.setText("Nuevo");
                        imgNuevo.setImage(new Image(direccion + "agregar-usuario (1).png"));
                        btnModificar.setText("Modificar");
                        imgEditar.setImage(new Image(direccion + "editar.png"));
                        btnEliminar.setDisable(false);
                        imgEliminar.setOpacity(1);
                        btnReporte.setDisable(false);
                        imgReporte.setOpacity(1);
                        btnCargos.setDisable(false);
                        imgCargos.setOpacity(1);
                        btnDepartamentos.setDisable(false);
                        imgDepartamentos.setOpacity(1);
                        btnLocales.setDisable(false);
                        imgLocales.setOpacity(1);
                        btnTipoClientes.setDisable(false);
                        imgTipoCliente.setOpacity(1);
                        tblAdministracion.setDisable(false);
                        imgRegresar.setDisable(false);
                        imgRegresar.setOpacity(1);
                        operacion = Operaciones.NINGUNO;
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("KINAL MALL");
                        alert.setHeaderText(null);
                        alert.setContentText("Numero de telefono invalido."
                                + "\n Solo se deben ingresar 8 numeros en un formato de 12345678.");
                        alert.show();
                    }

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("KINAL MALL");
                    alert.setHeaderText(null);
                    alert.setContentText("Uno o varios campos se encuentran vacios.");
                    alert.show();
                }
                break;
        }
    }

    @FXML
    void reporte(ActionEvent event) {
        Map parametro = new HashMap();
        if (existeElementoSeleccionado()) {
            int codigoAdministracion = ((Administracion) tblAdministracion.getSelectionModel().getSelectedItem()).getId();
            parametro.put("id", codigoAdministracion);
            GenerarReporte.getInstance().mostrarReporte("ReporteIndividualAdministracion.jasper", parametro, "Reporte de Administracion Individual.");
        } else {
            GenerarReporte.getInstance().mostrarReporte("ReporteAdministracion.jasper", parametro, "Reporte de Administracion.");
        }
    }

    @FXML
    void modificar(ActionEvent event) {
        switch (operacion) {
            case NINGUNO:
                if (tblAdministracion.getSelectionModel().getSelectedItem() == null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("KINAL MALL");
                    alert.setHeaderText(null);
                    alert.setContentText("Seleccione un registro para continuar.");
                    alert.show();

                } else {
                    habilitarDatos();
                    btnModificar.setText("Actualizar");
                    imgEditar.setImage(new Image(direccion + "salvar.png"));
                    btnEliminar.setText("Cancelar");
                    imgEliminar.setImage(new Image(direccion + "boton-x.png"));
                    imgNuevo.setOpacity(0.15);
                    imgReporte.setOpacity(0.15);
                    btnNuevo.setDisable(true);
                    btnReporte.setDisable(true);
                    btnCargos.setDisable(true);
                    imgCargos.setOpacity(0.15);
                    btnDepartamentos.setDisable(true);
                    imgDepartamentos.setOpacity(0.15);
                    btnLocales.setDisable(true);
                    imgLocales.setOpacity(0.15);
                    btnTipoClientes.setDisable(true);
                    imgTipoCliente.setOpacity(0.15);
                    tblAdministracion.setDisable(true);
                    imgRegresar.setDisable(true);
                    imgRegresar.setOpacity(0.15);
                    operacion = Operaciones.ACTUALIZAR;
                }
                break;
            case ACTUALIZAR:
                ArrayList<TextField> listaCampos = new ArrayList<>();
                listaCampos.add(txtId);
                listaCampos.add(txtDireccion);
                listaCampos.add(txtTelefono);
                ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                if (escenarioPrincipal.validar(listaCampos, listaComboBox)) {
                    if (escenarioPrincipal.validarTelefono(txtTelefono.getText())) {
                        editarAdministracion();
                        limpiarTexto();
                        cargarDatos();
                        deshabilitarTexto();
                        btnModificar.setText("Modificar");
                        imgEditar.setImage(new Image(direccion + "editar.png"));
                        btnEliminar.setText("Eliminar");
                        imgEliminar.setImage(new Image(direccion + "borrar-usuario.png"));
                        btnNuevo.setDisable(false);
                        imgNuevo.setOpacity(1);
                        btnReporte.setDisable(false);
                        imgReporte.setOpacity(1);
                        btnCargos.setDisable(false);
                        imgCargos.setOpacity(1);
                        btnDepartamentos.setDisable(false);
                        imgDepartamentos.setOpacity(1);
                        btnLocales.setDisable(false);
                        imgLocales.setOpacity(1);
                        btnTipoClientes.setDisable(false);
                        imgTipoCliente.setOpacity(1);
                        tblAdministracion.setDisable(false);
                        imgRegresar.setDisable(false);
                        imgRegresar.setOpacity(1);
                        operacion = Operaciones.NINGUNO;
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("KINAL MALL");
                        alert.setHeaderText(null);
                        alert.setContentText("Numero de telefono invalido."
                                + "\n Solo se deben ingresar 8 numeros en un formato de 12345678.");
                        alert.show();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("KINAL MALL");
                    alert.setHeaderText(null);
                    alert.setContentText("Uno o varios campos se encuentran vacios.");
                    alert.show();
                }
                break;

            case GUARDAR:
                limpiarTexto();
                deshabilitarTexto();
                btnNuevo.setText("Nuevo");
                imgNuevo.setImage(new Image(direccion + "agregar-usuario (1).png"));
                btnModificar.setText("Modificar");
                imgEditar.setImage(new Image(direccion + "editar.png"));
                btnEliminar.setDisable(false);
                imgEliminar.setOpacity(1);
                btnReporte.setDisable(false);
                imgReporte.setOpacity(1);
                btnCargos.setDisable(false);
                imgCargos.setOpacity(1);
                btnDepartamentos.setDisable(false);
                imgDepartamentos.setOpacity(1);
                btnLocales.setDisable(false);
                imgLocales.setOpacity(1);
                btnTipoClientes.setDisable(false);
                imgTipoCliente.setOpacity(1);
                tblAdministracion.setDisable(false);
                imgRegresar.setDisable(false);
                imgRegresar.setOpacity(1);
                operacion = Operaciones.NINGUNO;
                break;

        }

    }

    @FXML
    void eliminar(ActionEvent event) {
        switch (operacion) {
            case ACTUALIZAR:
                limpiarTexto();
                deshabilitarTexto();
                btnNuevo.setDisable(false);
                imgNuevo.setOpacity(1);
                btnReporte.setDisable(false);
                imgReporte.setOpacity(1);
                btnCargos.setDisable(false);
                imgCargos.setOpacity(1);
                btnDepartamentos.setDisable(false);
                imgDepartamentos.setOpacity(1);
                btnLocales.setDisable(false);
                imgLocales.setOpacity(1);
                btnTipoClientes.setDisable(false);
                imgTipoCliente.setOpacity(1);
                btnModificar.setText("Modificar");
                imgEditar.setImage(new Image(direccion + "editar.png"));
                btnEliminar.setText("Eliminar");
                imgEliminar.setImage(new Image(direccion + "borrar-usuario.png"));
                tblAdministracion.setDisable(false);
                imgRegresar.setDisable(false);
                imgRegresar.setOpacity(1);
                operacion = Operaciones.NINGUNO;
                break;
            case NINGUNO:
                if (tblAdministracion.getSelectionModel().getSelectedItem() == null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("KINAL MALL");
                    alert.setHeaderText(null);
                    alert.setContentText("Seleccione un registro para continuar.");
                    alert.show();
                } else {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Advertencia");
                    alert.setHeaderText(null);
                    alert.setContentText("¿Está seguro de eliminar?");

                    Optional<ButtonType> respuesta = alert.showAndWait();
                    if (respuesta.get() == ButtonType.OK) {
                        eliminarAdministracion();
                        limpiarTexto();
                        cargarDatos();
                        break;
                    }

                }
                break;
        }
    }
}
