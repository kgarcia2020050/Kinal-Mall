/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kennethgarcia.controller;

import com.jfoenix.controls.JFXTimePicker;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.kennethgarcia.bean.Departamentos;
import org.kennethgarcia.system.Principal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import org.kennethgarcia.db.Conexion;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.kennethgarcia.report.GenerarReporte;

/**
 *
 * @author Kenneth Gerardo Garcia Lemus
 * @date 16/06/2021
 * @time 16:16:56
 */
public class DepartamentosController implements Initializable {

    private final String direccion = "/org/kennethgarcia/resources/image/";
    @FXML
    private TextField txtNombre;
    @FXML
    private ImageView imgNuevo;
    @FXML
    private ImageView imgEditar;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private ImageView imgReporte;
    @FXML
    private ImageView imgRegresar;

    private enum Operaciones {
        NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO

    }

    private Operaciones operacion = Operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<Departamentos> listaDepartamentos;

    public boolean existeElementoSeleccionado() {
        return tblDepartamentos.getSelectionModel().getSelectedItem() != null;
    }
    @FXML
    private TextField txtId;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colNombre;
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnModificar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnReporte;
    @FXML
    private TableView tblDepartamentos;

    public ObservableList<Departamentos> getDepartamentos() {
        ArrayList<Departamentos> lista = new ArrayList<>();

        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarDepartamentos()}");
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                lista.add(new Departamentos(
                        resultado.getInt("id"),
                        resultado.getString("nombre")));
            }

            resultado.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        listaDepartamentos = FXCollections.observableArrayList(lista);
        return listaDepartamentos;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        cargarDatos();
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    @FXML
    public void regresar(MouseEvent event) {
        escenarioPrincipal.escenaAdministracion();
    }

    @FXML
    private void nuevo(ActionEvent event) {
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
                tblDepartamentos.setDisable(true);
                imgRegresar.setDisable(true);
                imgRegresar.setOpacity(0.15);
                operacion = Operaciones.GUARDAR;
                break;
            case GUARDAR:
                ArrayList<TextField> listaCampos = new ArrayList<>();
                listaCampos.add(txtNombre);
                ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                if (escenarioPrincipal.validar(listaCampos, listaComboBox)) {
                    if (escenarioPrincipal.validarString(txtNombre.getText())) {
                        agregarDepartamentos();
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
                        tblDepartamentos.setDisable(false);
                        imgRegresar.setDisable(false);
                        imgRegresar.setOpacity(1);
                        operacion = Operaciones.NINGUNO;
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("KINAL MALL");
                        alert.setHeaderText(null);
                        alert.setContentText("Ingresar solo letras en campo Nombre.");
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
    private void modificar(ActionEvent event) {
        switch (operacion) {
            case NINGUNO:
                if (tblDepartamentos.getSelectionModel().getSelectedItem() == null) {

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
                    tblDepartamentos.setDisable(true);
                    imgRegresar.setDisable(true);
                    imgRegresar.setOpacity(0.15);
                    operacion = Operaciones.ACTUALIZAR;
                }
                break;
            case ACTUALIZAR:
                ArrayList<TextField> listaCampos = new ArrayList<>();
                listaCampos.add(txtId);
                listaCampos.add(txtNombre);
                ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                if (escenarioPrincipal.validar(listaCampos, listaComboBox)) {
                    if (escenarioPrincipal.validarString(txtNombre.getText())) {
                        editarDepartamentos();
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
                        tblDepartamentos.setDisable(false);
                        imgRegresar.setDisable(false);
                        imgRegresar.setOpacity(1);
                        operacion = Operaciones.NINGUNO;
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("KINAL MALL");
                        alert.setHeaderText(null);
                        alert.setContentText("Ingresar solo letras en campo Nombre.");
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
                tblDepartamentos.setDisable(false);
                imgRegresar.setDisable(false);
                imgRegresar.setOpacity(1);
                operacion = Operaciones.NINGUNO;
        }
    }

    @FXML
    private void eliminar(ActionEvent event) {
        switch (operacion) {
            case ACTUALIZAR:
                limpiarTexto();
                deshabilitarTexto();
                btnNuevo.setDisable(false);
                imgNuevo.setOpacity(1);
                btnReporte.setDisable(false);
                imgReporte.setOpacity(1);
                btnModificar.setText("Modificar");
                imgEditar.setImage(new Image(direccion + "editar.png"));
                btnEliminar.setText("Eliminar");
                imgEliminar.setImage(new Image(direccion + "borrar-usuario.png"));
                tblDepartamentos.setDisable(false);
                imgRegresar.setDisable(false);
                imgRegresar.setOpacity(1);
                operacion = Operaciones.NINGUNO;
                break;
            case NINGUNO:
                if (tblDepartamentos.getSelectionModel().getSelectedItem() == null) {
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
                        eliminarDepartamentos();
                        limpiarTexto();
                        cargarDatos();
                        break;
                    }

                }
                break;
        }
    }

    @FXML
    private void reporte(ActionEvent event) {
        Map parametro = new HashMap();
        GenerarReporte.getInstance().mostrarReporte("ReporteDepartamentos.jasper", parametro, "Reporte de Departamentos.");

    }

    public boolean validar() {
        return tblDepartamentos.getSelectionModel().getSelectedItem() != null;

    }

    @FXML
    public void seleccionarElemento() {
        if (validar()) {
            txtId.setText(String.valueOf(((Departamentos) tblDepartamentos.getSelectionModel().getSelectedItem()).getId()));
            txtNombre.setText(((Departamentos) tblDepartamentos.getSelectionModel().getSelectedItem()).getNombre());
        } else {
        }
    }

    public void cargarDatos() {
        tblDepartamentos.setItems(getDepartamentos());
        colId.setCellValueFactory(new PropertyValueFactory<Departamentos, Integer>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Departamentos, String>("nombre"));
    }

    public void limpiarTexto() {
        txtId.clear();
        txtNombre.clear();

    }

    public void deshabilitarTexto() {
        txtNombre.setEditable(false);
    }

    private void habilitarDatos() {
        txtNombre.setEditable(true);
    }

    private void agregarDepartamentos() {
        Departamentos registro = new Departamentos();
        registro.setNombre(txtNombre.getText());
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarDepartamentos(?)}");
            stmt.setString(1, registro.getNombre());
            stmt.execute();
            cargarDatos();
            limpiarTexto();
            deshabilitarTexto();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void editarDepartamentos() {
        Departamentos registro = (Departamentos) tblDepartamentos.getSelectionModel().getSelectedItem();
        registro.setId(Integer.parseInt(txtId.getText()));
        registro.setNombre(txtNombre.getText());
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarDepartamentos(?,?)}");
            stmt.setInt(1, registro.getId());
            stmt.setString(2, registro.getNombre());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminarDepartamentos() {
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarDepartamentos(?)}");
            stmt.setInt(1, Integer.parseInt(txtId.getText()));
            stmt.execute();
            cargarDatos();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1451) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("KINAL MALL");
                alert.setHeaderText(null);
                alert.setContentText("Antes de eliminar este departamento debe de eliminar sus empleados relacionados.");
                alert.show();
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
